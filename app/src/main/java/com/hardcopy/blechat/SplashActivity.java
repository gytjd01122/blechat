package com.hardcopy.blechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hardcopy.blechat.setting.UserSetting;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSetting.init(this);
        if (UserSetting.getName().equals(UserSetting.USER_SETTING_DEFAULT_VALUE)){
            launchActivity(FirstStepActivity.class);
        }else {
            launchActivity(MainActivity.class);
        }
    }

    private void launchActivity(Class targetClass){
        Intent intent = new Intent(this,targetClass);
        startActivity(intent);
        finish();
    }
}
