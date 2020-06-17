package com.cookandroid.p2016314024_07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TextView tv = findViewById(R.id.textView);
        Button btn = findViewById(R.id.button);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat",0);
        double lng = intent.getDoubleExtra("lng",0);
        String title = intent.getStringExtra("title");
        tv.setText(title+":"+lat+":"+lng);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
