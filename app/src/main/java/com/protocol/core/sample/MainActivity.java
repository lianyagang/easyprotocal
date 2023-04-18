package com.protocol.core.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modulea.AModuleActivity;


public class MainActivity extends AppCompatActivity {
    boolean isExpandDescripe = false;//初始展开状态为false，即未展开；


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util textViewSpanUtil = new Util();
        findViewById(R.id.btn).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AModuleActivity.class)));
//        TextView tv = findViewById(R.id.tv1);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isExpandDescripe) {
//                    isExpandDescripe = false;
//                    tv.setMaxLines(2);
//                } else {
//                    isExpandDescripe = true;
//                    tv.setMaxLines(Integer.MAX_VALUE);
//
//                }
//                textViewSpanUtil.toggleEllipsize(getApplicationContext(), tv, 2, tv.getText().toString(), "ALL", R.color.black, isExpandDescripe);
//            }
//        });
    }
}
