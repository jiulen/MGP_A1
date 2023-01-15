package com.sdm.mgp2022;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WinLoseScreen extends Activity implements View.OnClickListener{
    //Define buttons
    private Button btn_mainmenu;

    private TextView text_winlose;
    private TextView text_score;
    private TextView text_highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.winlose);

        btn_mainmenu = findViewById(R.id.mainmenuButton);
        btn_mainmenu.setOnClickListener(this); //Set Listener to this button --> Main Menu Button

        text_winlose = findViewById(R.id.winloseTitle);
        text_score = findViewById(R.id.scoreText);
        text_highscore = findViewById(R.id.highscoreText);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (GameSystem.Instance.GetBoolFromSave("Win") == true)
            text_winlose.setText("You Win");
        else
            text_winlose.setText("You Lose");

        text_score.setText("HI-SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Score")));
        text_highscore.setText("   SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Hi-Score")));
    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_mainmenu)
        {
            intent.setClass(this, Mainmenu.class);
            StateManager.Instance.ChangeState("MainMenu");
        }

        if (intent != null)
            startActivity(intent);
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
