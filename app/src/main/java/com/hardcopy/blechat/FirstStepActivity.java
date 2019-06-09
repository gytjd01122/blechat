package com.hardcopy.blechat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.hardcopy.blechat.adapters.ViewPagerFragmentAdapter;
import com.hardcopy.blechat.databinding.FragmentFirststepBinding;
import com.hardcopy.blechat.fragments.FirstStepFragment;
import com.hardcopy.blechat.setting.UserSetting;

import me.relex.circleindicator.CircleIndicator3;

public class FirstStepActivity extends AppCompatActivity {

    CircleIndicator3 indicator;
    public static ViewPager2 viewpager;
    ViewPagerFragmentAdapter adapter;
    UserSetting userSetting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firststep);

        userSetting = new UserSetting(this);

        viewpager = findViewById(R.id.view_pager);
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        // add Fragments in your ViewPagerFragmentAdapter class
        adapter.addFragment(new FirstStepFragment("처음이시군요"));
        adapter.addFragment(new FirstStepFragment("당신의 이름은?", "이름을 입력하세요",true));
        adapter.addFragment(new FirstStepFragment("당신의 나이는?", "나이를 입력하세요",true));
        adapter.addFragment(new FirstStepFragment("당신의 키는?","키를 입력하세요",true));
        adapter.addFragment(new FirstStepFragment("당신의 몸무게는?", "몸무게를 입력하세요",true));
        adapter.addFragment(new FirstStepFragment("준비 완료!"));

        viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewpager.setAdapter(adapter);

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);

        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 5){
                    userSetting.setName(adapter.getItem(1).layout.textInputEditText.getText().toString());
                    userSetting.setAge(adapter.getItem(2).layout.textInputEditText.getText().toString());
                    userSetting.setHeight(adapter.getItem(3).layout.textInputEditText.getText().toString());
                    userSetting.setWeight(adapter.getItem(4).layout.textInputEditText.getText().toString());

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



        };


}

