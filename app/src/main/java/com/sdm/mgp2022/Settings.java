package com.sdm.mgp2022; // jiulen

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class Settings extends Activity implements View.OnClickListener, View.OnTouchListener {
    //Define buttons
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this); //Set Listener to this button --> Play Button
        btn_back.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (v == btn_back)
                {
                    btn_back.setImageResource(R.drawable.back_icon_clicked);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (v == btn_back)
                {
                    btn_back.setImageResource(R.drawable.back_icon);
                }
        }
        return false;
    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        if (v == btn_back)
            finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
