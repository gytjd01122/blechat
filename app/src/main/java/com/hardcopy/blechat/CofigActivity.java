package com.hardcopy.blechat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CofigActivity extends Activity {
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cofig);

        list = (ListView) findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        data.add("고객센터/도움말");
        data.add("블루투스 정보");
        data.add("알림 설정");
        adapter.notifyDataSetChanged();//이걸해야 저장이됨
    }
}
