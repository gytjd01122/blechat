package com.hardcopy.blechat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;


public class UserActivity extends Activity {
    SharedPreferences pref;
    SharedPreferences prefs;


    EditText name;
    EditText age;
    EditText tall;
    EditText kg;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        name = findViewById(R.id.editText1);
        age = findViewById(R.id.editText2);
        tall = findViewById(R.id.editText3);
        kg = findViewById(R.id.editText4);

        btn = findViewById(R.id.btn);


        pref = getSharedPreferences("name", MODE_PRIVATE);
        prefs =getSharedPreferences("name", MODE_PRIVATE);



       String user_name = prefs.getString("user_name","");

       if(!user_name.equals("")) {
            name.setText(prefs.getString("user_name", "0"));
            age.setText(prefs.getString("user_age", "0"));
            tall.setText(prefs.getString("user_tall", "0"));
            kg.setText(prefs.getString("user_kg", "0"));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();




                    editor.putString("user_name", name.getText().toString());
                    editor.putString("user_age", age.getText().toString());
                    editor.putString("user_tall", tall.getText().toString());
                    editor.putString("user_kg", kg.getText().toString());
                    editor.commit();



                name.setText(prefs.getString("user_name", "0"));
                age.setText(prefs.getString("user_age", "0"));
                tall.setText(prefs.getString("user_tall", "0"));
                kg.setText(prefs.getString("user_kg", "0"));


            }
        });

    }

}


