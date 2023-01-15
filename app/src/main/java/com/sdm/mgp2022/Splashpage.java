package com.sdm.mgp2022; // jonathan

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

// Created by TanSiewLan2021

public class Splashpage extends Activity
{
    protected boolean _active = true;
    protected int _splashTime = 2000;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashpage);
        //AudioManager.Instance.PlayAudio(R.raw.logo, 100, false);
        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(200);
                        if(_active) {
                            waited += 200;
                        }
                    }
                } catch(InterruptedException e) {
                    //do nothing
                } finally {
                    finish();
					//Create new activity based on and intent with CurrentActivity
                    //Intent intent = new Intent(Splashpage.this, Mainmenu.class);
                    Intent intent = new Intent(Splashpage.this, Mainmenu.class);
                    startActivity(intent);
                    StateManager.Instance.ChangeState("MainMenu");
                }
            }
        };
        splashTread.start();
    }
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }

   @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    //End here with the 3 methods for the android life cycle of an activity class
}
