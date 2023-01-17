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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class Settings extends Activity implements View.OnClickListener, View.OnTouchListener {
    //Define buttons
    private ImageButton btn_back;
    private Button btn_tutorial;

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
        btn_tutorial = findViewById(R.id.tutorialButton);
        btn_tutorial.setOnClickListener(this);

        final int volumeScale = 100;
        SeekBar soundSlider = findViewById(R.id.soundSlider);
        soundSlider.setMax((int)(AudioManager.Instance.getMaxVolume() * volumeScale));
        soundSlider.setProgress((int)(AudioManager.Instance.getCurrentVolume() * volumeScale));
        soundSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                AudioManager.Instance.ChangeVolume(i / (float)volumeScale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CheckBox checkbox_vibrate = findViewById(R.id.vibrateCheckbox);
        checkbox_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                GameSystem.Instance.SaveEditBegin();
                GameSystem.Instance.SetBoolInSave("Vibrate", b);
                GameSystem.Instance.SaveEditEnd();
            }
        });

        if (!GameSystem.Instance.CheckKeyInSave("Vibrate"))
        {
            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetBoolInSave("Vibrate", true);
            GameSystem.Instance.SaveEditEnd();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckBox checkbox_vibrate = findViewById(R.id.vibrateCheckbox);
        checkbox_vibrate.setChecked(GameSystem.Instance.GetBoolFromSave("Vibrate"));
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
        else if (v == btn_tutorial)
        {
            //Intent intent = new Intent();
            //intent.setClass(this, Tutorial.class);
            //startActivity(intent);
        }
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
