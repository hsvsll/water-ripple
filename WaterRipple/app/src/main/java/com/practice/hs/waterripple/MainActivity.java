package com.practice.hs.waterripple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBt = (Button) findViewById(R.id.btWaterRippleTwo);
        initView();
    }



    private void initView() {
        mBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btWaterRippleTwo ) {
            TestActivity.navigationToActivity(MainActivity.this);
        }
    }
}
