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

package com.hardcopy.blechat.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hardcopy.blechat.MainActivity;
import com.hardcopy.blechat.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExampleFragment extends Fragment implements View.OnClickListener {



	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
	private boolean button_drive = false;
	private boolean button_safe = false;
	private boolean gps_load = false;
	TextView mTextChat;
	EditText mEditChat;

	public Button mBtnSend;
	public Button btnDrive;
	public Button btnSafe;


	long endTime;
	long startTime;



	public ExampleFragment(){}

	@SuppressLint("ValidFragment")
	public ExampleFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
		
		//mTextChat = (TextView) rootView.findViewById(R.id.text_chat);
	//	mTextChat.setMaxLines(1000);
	//	mTextChat.setVerticalScrollBarEnabled(true);
	//	mTextChat.setMovementMethod(new ScrollingMovementMethod());
		
	//	mEditChat = (EditText) rootView.findViewById(R.id.edit_chat);
	//	mEditChat.setOnEditorActionListener(mWriteListener);
		
		mBtnSend = (Button) rootView.findViewById(R.id.button_send);
		mBtnSend.setOnClickListener(this);
		btnDrive = (Button) rootView.findViewById((R.id.button2));
        btnDrive.setOnClickListener(this);
        btnSafe = (Button) rootView.findViewById((R.id.button3));
        btnSafe.setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		    case R.id.button_send:
		    	sendMessage("1");
		    	gps_load = true;
		    	break;

			case R.id.button2:
			    if(button_drive){
			        button_drive = false;
					endTime = System.currentTimeMillis();

					long drive_time = endTime - startTime;
					Date date = new Date(endTime);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String getTime = sdf.format(date);

                    ((MainActivity)getActivity()).button_2 = false;

			      	//((MainActivity)getActivity()).setDefaultLocation();
					//((MainActivity)getActivity()).db.gpsDao().insertAll(new Gps(getTime ,drive_time,((MainActivity)getActivity()).exr_distance));

					((MainActivity)getActivity()).DriveExit();

			        btnDrive.setText("주행모드 시작");


                }else{
			        button_drive = true;
					((MainActivity)getActivity()).button_2 = true;

					startTime = System.currentTimeMillis();

					((MainActivity)getActivity()).DriveStart();
			        btnDrive.setText("주행모드 종료");

                }

                break;

			case R.id.button3:
				if(button_safe){
					sendMessage("3");
					button_safe = false;
					btnSafe.setText("안전모드 ON");


				}else{
					button_safe = true;
					sendMessage("2");
					btnSafe.setText("안전모드 OFF");

				}
				break;
		}


	}
	
	
    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                if(message != null && message.length() > 0)
                	sendMessage(message);
            }
            return true;
        }
    };
	
    // Sends user message to remote
    private void sendMessage(String message) {
    	if(message == null || message.length() < 1)
    		return;
    	// send to remote
    	if(mFragmentListener != null)
    		mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
    	else
    		return;
    	// show on text view

    }
    
    private static final int NEW_LINE_INTERVAL = 1000;
    private long mLastReceivedTime = 0L;
    
    // Show messages from remote
    public void showMessage(String message) {
    	if(message != null && message.length() > 0) {
    		if(message.equals("g")){              //

			}

			else if(message.equals("w")){        // 안전모드 상태에서 기존 위치를 벗어나는 경우
				show("위치 이동 감지");
			}

			else if(message.equals("v")){       // 안전모드 상태에서 기준 이상의 진동이 발생 할 경우
				show("진동 감지");
			}

			else{
			    if(gps_load){
					String s[] = message.split(":");  // gps 데이터 값이면 / spilt 하여 경도 위도로 나눔

					LatLng gps_new = new LatLng(Double.parseDouble(s[0]),Double.parseDouble(s[1]));

					((MainActivity)getActivity()).setCurrentLocation(gps_new,"현재 위치","");

					gps_load = false;
				}


			}
    	}
    }

    void show(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("경고 메세지");
        builder.setMessage(text);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }










}
    

