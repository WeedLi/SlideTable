package com.leo.monthtable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //点击切换日期
    public void changeDate(View view) {
        Toast.makeText(this, "切换日期", Toast.LENGTH_SHORT).show();
    }
}
