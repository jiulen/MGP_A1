package com.sdm.mgp2022; // By Jiu Len

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.ImageButton;

// Created by TanSiewLan2021

public class Mainmenu extends Activity implements OnClickListener, StateBase{
    //Define buttons
    private ImageButton btn_play;
    private ImageButton btn_leaderboard;
    private ImageButton btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this); //Set Listener to this button --> Play Button
        btn_leaderboard = findViewById(R.id.btn_leaderboard);
        btn_leaderboard.setOnClickListener(this); //Set Listener to this button --> Leaderboard Button
        btn_settings = findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this); //Set Listener to this button --> Settings Button
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        AudioManager.Instance.PlayAudio(R.raw.titlescreen, true);
    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_play)
        {
            intent.setClass(this, GamePage.class);
            StateManager.Instance.ChangeState("MainGame");
            AudioManager.Instance.StopAudio(R.raw.titlescreen);
        }
        else if (v == btn_leaderboard)
        {
            intent.setClass(this, Leaderboard.class);
        }
        else if (v == btn_settings)
        {
            intent.setClass(this, Settings.class);
        }

        startActivity(intent);

    }

    @Override
    public String GetName() {
        return "MainMenu";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void Render(Canvas _canvas)
    {
    }

    @Override
    public void Update(float _dt)
    {
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
