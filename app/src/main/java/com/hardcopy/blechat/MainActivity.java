/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.blechat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hardcopy.blechat.R;
import com.hardcopy.blechat.db.AppDatabase;
import com.hardcopy.blechat.fragments.ExampleFragment;
import com.hardcopy.blechat.fragments.FragmentAdapter;
import com.hardcopy.blechat.fragments.IFragmentListener;
import com.hardcopy.blechat.service.BTCTemplateService;
import com.hardcopy.blechat.utils.AppSettings;
import com.hardcopy.blechat.utils.Constants;
import com.hardcopy.blechat.utils.Logs;
import com.hardcopy.blechat.utils.RecycleUtils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, IFragmentListener, OnMapReadyCallback {



    // Debugging
    private static final String TAG = "BIPER_SYSTEM";
    public static boolean button_2 = false;


	private Thread timeThread = null;

	FragmentManager fm;

	private TextView dt;
	private TextView tt;



	//db
	public AppDatabase db;


	private GoogleMap mGoogleMap = null;
	private Marker currentMarker = null;

	private static final int GPS_ENABLE_REQUEST_CODE = 2001;
	private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
	private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

	// onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
	private static final int PERMISSIONS_REQUEST_CODE = 100;
	boolean needRequest = false;

	Location mCurrentLocatiion;
	LatLng currentPosition;
	LatLng startPosition = null;

	Polyline line;
	public float exr_distance = 0;



	private PolylineOptions polylineOptions = new PolylineOptions();


	private FusedLocationProviderClient mFusedLocationClient;
	private LocationRequest locationRequest;
	private Location location;


	private View mLayout;
   // Snackbar 사용하기 위해서는 View가 필요합니다.
	// (참고로 Toast에서는 Context가 필요했습니다.)


	String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소
	// Context, System
	private Context mContext;
	private BTCTemplateService mService;
	private ActivityHandler mActivityHandler;

	// Global
	
	// UI stuff
	private FragmentManager mFragmentManager;
	private FragmentAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	
	private ImageView mImageBT = null;
	private TextView mTextStatus = null;





	// Refresh timer
	private Timer mRefreshTimer = null;

	public LatLng GPS_Data = null;
	

	/*****************************************************
	 *	 Overrided methods
	 ******************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//----- System, Context
		mContext = this;	//.getApplicationContext();
		mActivityHandler = new ActivityHandler();
		AppSettings.initializeAppSettings(mContext);
		
		setContentView(R.layout.activity_main);

		dt = findViewById(R.id.dis);
		tt = findViewById(R.id.time);

		dt.setVisibility(View.INVISIBLE);
		tt.setVisibility(View.INVISIBLE);

		Log.d(TAG, "onCreate");



		locationRequest = new LocationRequest()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(UPDATE_INTERVAL_MS)
				.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


		LocationSettingsRequest.Builder builder =
				new LocationSettingsRequest.Builder();

		builder.addLocationRequest(locationRequest);


		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


		android.app.FragmentManager fragmentManager = getFragmentManager();
		MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		// Create the adapter that will return a fragment for each of the primary sections of the app.
		mFragmentManager = getSupportFragmentManager();
		mSectionsPagerAdapter = new FragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);


		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Setup views

		mImageBT = (ImageView) findViewById(R.id.status_title);
		mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
		mTextStatus = (TextView) findViewById(R.id.status_text);
		mTextStatus.setText(getResources().getString(R.string.bt_state_init));

		// Do data initialization after service started and binded
		doStartService();

		db = AppDatabase.getAppDatabase(this);




	}






	LocationCallback locationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			super.onLocationResult(locationResult);

			List<Location> locationList = locationResult.getLocations();

			if (locationList.size() > 0) {
				location = locationList.get(locationList.size() - 1);
				//location = locationList.get(0);

				currentPosition
						= new LatLng(location.getLatitude(), location.getLongitude());


				String markerTitle = getCurrentAddress(currentPosition);
				String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
						+ " 경도:" + String.valueOf(location.getLongitude());

				Log.d(TAG, "onLocationResult : " + markerSnippet);


				//현재 위치에 마커 생성하고 이동
				setCurrentLocation(location, markerTitle, markerSnippet);

				mCurrentLocatiion = location;
			}


		}

	};

	private void startLocationUpdates() {

		if (!checkLocationServicesStatus()) {

			Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
			showDialogForLocationServiceSetting();
		}else {

			int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION);
			int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_COARSE_LOCATION);



			if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
					hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

				Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
				return;
			}


			Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

			mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

			if (checkPermission())
				mGoogleMap.setMyLocationEnabled(true);

		}

	}






    @Override
    public void onMapReady(final GoogleMap googleMap){

		Log.d(TAG, "onMapReady :");

		mGoogleMap = googleMap;


		//런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
		//지도의 초기위치를 서울로 이동
		setDefaultLocation();



		//런타임 퍼미션 처리
		// 1. 위치 퍼미션을 가지고 있는지 체크합니다.
		int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION);
		int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION);



		if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
				hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

			// 2. 이미 퍼미션을 가지고 있다면
			// ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


			startLocationUpdates(); // 3. 위치 업데이트 시작


		}else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

			// 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

				// 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
				Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
						Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

					@Override
					public void onClick(View view) {

						// 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
						ActivityCompat.requestPermissions( MainActivity.this, REQUIRED_PERMISSIONS,
								PERMISSIONS_REQUEST_CODE);
					}
				}).show();


			} else {
				// 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
				// 요청 결과는 onRequestPermissionResult에서 수신됩니다.
				ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
						PERMISSIONS_REQUEST_CODE);
			}

		}



		mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {

				Log.d( TAG, "onMapClick :");


			}
		});



	}




	@Override
	public synchronized void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");

		if (checkPermission()) {

			Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
			mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

			if (mGoogleMap!=null)
				mGoogleMap.setMyLocationEnabled(true);

		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		// Stop the timer

		if(mRefreshTimer != null) {
			mRefreshTimer.cancel();
			mRefreshTimer = null;
		}
		super.onStop();

		if (mFusedLocationClient != null) {

			Log.d(TAG, "onStop : call stopLocationUpdates");
			mFusedLocationClient.removeLocationUpdates(locationCallback);
		}
	}

	public String getCurrentAddress(LatLng latlng) {

		//지오코더... GPS를 주소로 변환
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		List<Address> addresses;

		try {

			addresses = geocoder.getFromLocation(
					latlng.latitude,
					latlng.longitude,
					1);
		} catch (IOException ioException) {
			//네트워크 문제
			//Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
			return "지오코더 서비스 사용불가";
		} catch (IllegalArgumentException illegalArgumentException) {
			Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
			return "잘못된 GPS 좌표";

		}


		if (addresses == null || addresses.size() == 0) {
			Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
			return "주소 미발견";

		} else {
			Address address = addresses.get(0);
			return address.getAddressLine(0).toString();
		}

	}



	public boolean checkLocationServicesStatus() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}


	public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

		if(button_2) {

			dt.setVisibility(View.VISIBLE);
			tt.setVisibility(View.VISIBLE);


			if (currentMarker != null) currentMarker.remove();

			LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            if(startPosition == null){

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLatLng);
                markerOptions.title(markerTitle);
                markerOptions.snippet(markerSnippet);
                markerOptions.draggable(true);

                startPosition = currentPosition;

				polylineOptions.color(Color.RED);
				polylineOptions.width(5);
				polylineOptions.add(currentLatLng);
				mGoogleMap.addPolyline(polylineOptions);

                currentMarker = mGoogleMap.addMarker(markerOptions);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                mGoogleMap.moveCamera(cameraUpdate);

            }

			else if(currentLatLng != startPosition) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLatLng);
                markerOptions.title(markerTitle);
                markerOptions.snippet(markerSnippet);
                markerOptions.draggable(true);


				polylineOptions.color(Color.RED);
				polylineOptions.width(5);
				polylineOptions.add(currentLatLng);
			    mGoogleMap.addPolyline(polylineOptions);


                Location sp = new Location("시작 위치");
                sp.setLatitude(startPosition.latitude);
                sp.setLongitude(startPosition.longitude);

                exr_distance += sp.distanceTo(location);


					String dis = String.format("%.1f", exr_distance / 1000);
					dt.setText(dis+"km");


                startPosition = currentPosition;

                currentMarker = mGoogleMap.addMarker(markerOptions);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                mGoogleMap.moveCamera(cameraUpdate);

            }




		}

	}

	public void setCurrentLocation(LatLng location, String markerTitle, String markerSnippet) { // button_1 현재 확인 버튼

			if (currentMarker != null) currentMarker.remove();

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(location);
			markerOptions.title(markerTitle);
			markerOptions.snippet(markerSnippet);
			markerOptions.draggable(true);


			currentMarker = mGoogleMap.addMarker(markerOptions);

			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(location);
			mGoogleMap.moveCamera(cameraUpdate);



	}

	public void DriveStart(){
		timeThread = new Thread(new timeThread());
		timeThread.start();

	}
	public void DriveExit() {
		timeThread.interrupt();
		//polylineOptions = null;

		mGoogleMap.clear();
		exr_distance = 0;

		tt.setVisibility(View.INVISIBLE);
		dt.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//int mSec = msg.arg1 % 100;
			int sec = (msg.arg1 / 100) % 60;
			int min = (msg.arg1 / 100) / 60;
			int hour = (msg.arg1 / 100) / 360;
			//1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

			@SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);

			tt.setText(result);


		}
	};

	public class timeThread implements Runnable {
		@Override
		public void run() {
			int i = 0;

			while (true) {
				while (button_2) { //일시정지를 누르면 멈춤
					Message msg = new Message();
					msg.arg1 = i++;
					handler.sendMessage(msg);

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
						runOnUiThread(new Runnable(){
							@Override
							public void run() {

							}
						});
						return; // 인터럽트 받을 경우 return
					}
				}
			}
		}
	}



	public void setDefaultLocation() {


		//디폴트 위치, Seoul
		LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
		String markerTitle = "위치정보 가져올 수 없음";
		String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


		if (currentMarker != null) currentMarker.remove();

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(DEFAULT_LOCATION);
		markerOptions.title(markerTitle);
		markerOptions.snippet(markerSnippet);
		markerOptions.draggable(true);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		currentMarker = mGoogleMap.addMarker(markerOptions);

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
		mGoogleMap.moveCamera(cameraUpdate);

	}


	//여기부터는 런타임 퍼미션 처리을 위한 메소드들
	private boolean checkPermission() {

		int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION);
		int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION);



		if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
				hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
			return true;
		}

		return false;

	}

	@Override
	public void onRequestPermissionsResult(int permsRequestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grandResults) {

		if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

			// 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

			boolean check_result = true;


			// 모든 퍼미션을 허용했는지 체크합니다.

			for (int result : grandResults) {
				if (result != PackageManager.PERMISSION_GRANTED) {
					check_result = false;
					break;
				}
			}


			if ( check_result ) {

				// 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
				startLocationUpdates();
			}
			else {
				// 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

				if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
						|| ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


					// 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
					Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
							Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

						@Override
						public void onClick(View view) {

							finish();
						}
					}).show();

				}else {


					// "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
					Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
							Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

						@Override
						public void onClick(View view) {

							finish();
						}
					}).show();
				}
			}

		}
	}


	//여기부터는 GPS 활성화를 위한 메소드들
	private void showDialogForLocationServiceSetting() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("위치 서비스 비활성화");
		builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
				+ "위치 설정을 수정하실래요?");
		builder.setCancelable(true);
		builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent callGPSSettingIntent
						= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
			}
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}








	@Override
	public void onDestroy() {
		super.onDestroy();
		finalizeActivity();
	}
	
	@Override
	public void onLowMemory (){
		super.onLowMemory();
		// onDestroy is not always called when applications are finished by Android system.
		finalizeActivity();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_scan:
				// Launch the DeviceListActivity to see devices and do scan
				doScan();
				return true;

			case R.id.action_exr:
				Intent exr = new Intent(getApplicationContext(),ExerciseActivity.class);
				startActivity(exr);

				return true;
			case R.id.action_user :
				Intent user = new Intent(getApplicationContext(),UserActivity.class);
				startActivity(user);


				return true;
			case R.id.action_cofig:
				Intent config = new Intent(getApplicationContext(),CofigActivity.class);
				startActivity(config);

				return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();		// TODO: Disable this line to run below code
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		// This prevents reload after configuration changes
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * Implements TabListener
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		//mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public void OnFragmentCallback(int msgType, int arg0, int arg1, String arg2, String arg3, Object arg4) {
		switch(msgType) {
		case IFragmentListener.CALLBACK_RUN_IN_BACKGROUND:
			if(mService != null)
				mService.startServiceMonitoring();
			break;
		case IFragmentListener.CALLBACK_SEND_MESSAGE:
			if(mService != null && arg2 != null)
				mService.sendMessageToRemote(arg2);

		default:
			break;
		}
	}
	
	
	/*****************************************************
	 *	Private methods
	 ******************************************************/
	
	/**
	 * Service connection
	 */
	private ServiceConnection mServiceConn = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.d(TAG, "Activity - Service connected");
			
			mService = ((BTCTemplateService.ServiceBinder) binder).getService();
			
			// Activity couldn't work with mService until connections are made
			// So initialize parameters and settings here. Do not initialize while running onCreate()
			initialize();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
	
	/**
	 * Start service if it's not running
	 */
	private void doStartService() {
		Log.d(TAG, "# Activity - doStartService()");
		startService(new Intent(this, BTCTemplateService.class));
		bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Stop the service
	 */
	private void doStopService() {
		Log.d(TAG, "# Activity - doStopService()");
		mService.finalizeService();
		stopService(new Intent(this, BTCTemplateService.class));
	}
	
	/**
	 * Initialization / Finalization
	 */
	private void initialize() {
		Logs.d(TAG, "# Activity - initialize()");
		
		// Use this check to determine whether BLE is supported on the device. Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
		    Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
		    finish();
		}
		
		mService.setupService(mActivityHandler);
		
		// If BT is not on, request that it be enabled.
		// RetroWatchService.setupBT() will then be called during onActivityResult
		if(!mService.isBluetoothEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
		}
		
		// Load activity reports and display
		if(mRefreshTimer != null) {
			mRefreshTimer.cancel();
		}
		
		// Use below timer if you want scheduled job
		//mRefreshTimer = new Timer();
		//mRefreshTimer.schedule(new RefreshTimerTask(), 5*1000);
	}
	
	private void finalizeActivity() {
		Logs.d(TAG, "# Activity - finalizeActivity()");
		
		if(!AppSettings.getBgService()) {
			doStopService();
		} else {
		}

		// Clean used resources
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
	}
	
	/**
	 * Launch the DeviceListActivity to see devices and do scan
	 */
	private void doScan() {
		Intent intent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(intent, Constants.REQUEST_CONNECT_DEVICE);
	}
	
	/**
	 * Ensure this device is discoverable by others
	 */
	private void ensureDiscoverable() {
		if (mService.getBluetoothScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(intent);
		}
	}
	
	
	/*****************************************************
	 *	Public classes
	 ******************************************************/
	
	/**
	 * Receives result from external activity
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logs.d(TAG, "onActivityResult " + resultCode);

		switch(requestCode) {
		case Constants.REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Attempt to connect to the device
				if(address != null && mService != null)
					mService.connectDevice(address);
			}
			break;

			case GPS_ENABLE_REQUEST_CODE:

				//사용자가 GPS 활성 시켰는지 검사
				if (checkLocationServicesStatus()) {
					if (checkLocationServicesStatus()) {

						Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


						needRequest = true;

						return;
					}
				}

				break;

		case Constants.REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a BT session
				mService.setupBLE();
			} else {
				// User did not enable Bluetooth or an error occured
				Logs.e(TAG, "BT is not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
			}
			break;


		}	// End of switch(requestCode)
	}



	/*****************************************************
	 *	Handler, Callback, Sub-classes
	 ******************************************************/
	
	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) 
		{
			switch(msg.what) {
			// Receives BT state messages from service 
			// and updates BT state UI
			case Constants.MESSAGE_BT_STATE_INITIALIZED:
				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + 
						getResources().getString(R.string.bt_state_init));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
				break;
			case Constants.MESSAGE_BT_STATE_LISTENING:
				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + 
						getResources().getString(R.string.bt_state_wait));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
				break;
			case Constants.MESSAGE_BT_STATE_CONNECTING:
				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + 
						getResources().getString(R.string.bt_state_connect));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_away));
				break;
			case Constants.MESSAGE_BT_STATE_CONNECTED:
				if(mService != null) {
					String deviceName = mService.getDeviceName();
					if(deviceName != null) {
						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + 
								getResources().getString(R.string.bt_state_connected) + " " + deviceName);
						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
					} else {
						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + 
								getResources().getString(R.string.bt_state_connected) + " no name");
						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
					}
				}
				break;
			case Constants.MESSAGE_BT_STATE_ERROR:
				mTextStatus.setText(getResources().getString(R.string.bt_state_error));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
				break;
			
			// BT Command status
			case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
				mTextStatus.setText(getResources().getString(R.string.bt_cmd_sending_error));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
				break;
				
			///////////////////////////////////////////////
			// When there's incoming packets on bluetooth
			// do the UI works like below
			///////////////////////////////////////////////
			case Constants.MESSAGE_READ_CHAT_DATA:
				if(msg.obj != null) {
					ExampleFragment frg = (ExampleFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_EXAMPLE);
					frg.showMessage((String)msg.obj);
				}
				break;
			
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
	}	// End of class ActivityHandler
	
    /**
     * Auto-refresh Timer
     */
	private class RefreshTimerTask extends TimerTask {
		public RefreshTimerTask() {}
		
		public void run() {
			mActivityHandler.post(new Runnable() {
				public void run() {
					// TODO:
					mRefreshTimer = null;
				}
			});
		}
	}






}
