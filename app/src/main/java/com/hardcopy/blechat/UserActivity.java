package com.hardcopy.blechat;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hardcopy.blechat.setting.UserSetting;

import java.util.ArrayList;

public class UserActivity extends Activity {

    private static final String EDITTEXT_IDENTIFIER = "editText";

    public enum EDITTEXT_TYPE {
        NAME(1), AGE(2), HEIGHT(3), WEIGHT(4);
        final private int value;

        private EDITTEXT_TYPE(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public int getValueStartWith0() {
            return value - 1;
        }
    };

    ArrayList<EditText> editTexts = new ArrayList<EditText>();
    private Button saveButton;

    private UserSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //초기화
        setting = new UserSetting(this);

        // 레이아웃:activity_user 에 있는 id를 리스트 Edittexts로 담기.
        for( EDITTEXT_TYPE type : EDITTEXT_TYPE.values()){
            editTexts.add(
                    (EditText) findViewById(getResources().
                            getIdentifier(EDITTEXT_IDENTIFIER + type.getValue() , "id" , getPackageName())
                    ));
        }
        saveButton = findViewById(R.id.btn);

        //기능: 자동불러오기
        for (EDITTEXT_TYPE type : EDITTEXT_TYPE.values()){
            EditText _element =  editTexts.get(type.getValueStartWith0());
            String _i;

            switch (type){
                case NAME:
                    _i = String.valueOf(UserSetting.getName());
                    if(_i.equals(UserSetting.USER_SETTING_DEFAULT_VALUE)){ _i = ""; }
                    _element.setText(_i); break;
                case AGE:
                    _i = String.valueOf(UserSetting.getAge());
                    if(_i.equals(UserSetting.USER_SETTING_DEFAULT_VALUE)){ _i = ""; }
                    _element.setText(_i); break;

                case HEIGHT:
                    _i = UserSetting.getHeight().toString();
                    if(_i.equals("0.0")){ _i = ""; } //TODO  HARDCODED 0.0
                    _element.setText(_i); break;
                case WEIGHT:
                    _i = UserSetting.getWeight().toString();
                    if(_i.equals("0.0")){ _i = ""; } //TODO HARDCODED 0.0
                    _element.setText(_i); break;
            }
        }

        //기능: 저장하기
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (EDITTEXT_TYPE typeNumber : EDITTEXT_TYPE.values()){
                    String _element =  editTexts.get(typeNumber.getValueStartWith0()).getText().toString();

                    switch (typeNumber){
                        case NAME:
                            if (_element.equals("")) {_element = UserSetting.USER_SETTING_DEFAULT_VALUE;}
                            setting.setName(_element); break;
                        case AGE:
                            if (_element.equals("")) {_element = UserSetting.USER_SETTING_DEFAULT_VALUE;}
                            setting.setAge(_element); break;
                        case HEIGHT:
                            if (_element.equals("")) {_element = UserSetting.USER_SETTING_DEFAULT_VALUE;}
                            setting.setHeight(_element); break;
                        case WEIGHT:
                            if (_element.equals("")) {_element = UserSetting.USER_SETTING_DEFAULT_VALUE;}
                            setting.setWeight(_element); break;
                    }
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
