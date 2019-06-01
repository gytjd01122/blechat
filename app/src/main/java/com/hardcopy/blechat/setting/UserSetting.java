package com.hardcopy.blechat.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSetting {

    private static final String USER_SETTING = "userSetting";
    public static final String USER_SETTING_DEFAULT_VALUE = "0";

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
        return pref.getString(USER_SETTING_KEY.NAME.toString(),USER_SETTING_DEFAULT_VALUE);
    }

    public void setName(String name) {
        editor.putString(USER_SETTING_KEY.NAME.toString(), name);
        editor.commit();
    }

    public static Integer getAge() {
        return pref.getInt(USER_SETTING_KEY.AGE.toString(),Integer.valueOf(USER_SETTING_DEFAULT_VALUE));
    }

    public void setAge(String age) {
        editor.putInt(USER_SETTING_KEY.AGE.toString(), Integer.valueOf(age));
        editor.commit();
    }

    public static Float getWeight() {
        return pref.getFloat(USER_SETTING_KEY.WEIGHT.toString(), Float.valueOf(USER_SETTING_DEFAULT_VALUE));
    }

    public void setWeight(String weight) {
        editor.putFloat(USER_SETTING_KEY.WEIGHT.toString(), Float.valueOf(weight));
        editor.commit();
    }

    public static Float getHeight() {
        return pref.getFloat(USER_SETTING_KEY.HEIGHT.toString(),  Float.valueOf(USER_SETTING_DEFAULT_VALUE));
    }

    public void setHeight(String height) {
        editor.putFloat(USER_SETTING_KEY.HEIGHT.toString(), Float.valueOf(height));
        editor.commit();
    }
}
