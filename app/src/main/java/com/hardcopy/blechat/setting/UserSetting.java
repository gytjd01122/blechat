package com.hardcopy.blechat.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSetting {

    private static final String USER_SETTING = "userSetting";

    private enum USER_SETTING_KEY {
        NAME , AGE , WEIGHT , HEIGHT
    }

    private Activity caller;
    private static SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSetting(Activity caller) {
        this.caller = caller;
        pref = caller.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static String getName() {
        return pref.getString(USER_SETTING_KEY.NAME.toString(),"");
    }

    public void setName(String name) {
        editor.putString(USER_SETTING_KEY.NAME.toString(), name);
        editor.commit();
    }

    public static Integer getAge() {
        return pref.getInt(USER_SETTING_KEY.AGE.toString(),0);
    }

    public void setAge(Integer age) {
        editor.putInt(USER_SETTING_KEY.NAME.toString(), age);
        editor.commit();
    }

    public static Float getWeight() {
        return pref.getFloat(USER_SETTING_KEY.WEIGHT.toString(),0);
    }

    public void setWeight(Float weight) {
        editor.putFloat(USER_SETTING_KEY.WEIGHT.toString(), weight);
        editor.commit();
    }

    public static Float getHeight() {
        return pref.getFloat(USER_SETTING_KEY.HEIGHT.toString(),0);
    }

    public void setHeight(Float height) {
        editor.putFloat(USER_SETTING_KEY.HEIGHT.toString(), height);
        editor.commit();
    }
}
