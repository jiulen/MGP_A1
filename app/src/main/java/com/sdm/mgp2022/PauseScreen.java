package com.sdm.mgp2022;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.FragmentActivity;

public class PauseScreen extends FragmentActivity implements View.OnClickListener {
    public static PauseScreen Instance = null;

    //Define buttons
    private Button btn_resume;
    private Button btn_mainmenu;
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

        setContentView(R.layout.pause);

        btn_resume = findViewById(R.id.resumeButton);
        btn_resume.setOnClickListener(this); //Set Listener to this button --> Resume Button
        btn_mainmenu = findViewById(R.id.mainmenuButton);
        btn_mainmenu.setOnClickListener(this); //Set Listener to this button --> Main Menu Button
        btn_leaderboard = findViewById(R.id.btn_leaderboard);
        btn_leaderboard.setOnClickListener(this); //Set Listener to this button --> Leaderboard Button
        btn_settings = findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this); //Set Listener to this button --> Settings Button

        Instance = this;
    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_resume)
        {
            GameSystem.Instance.SetIsPaused(false);
            intent = null;
            finish();
        }
        else if (v == btn_mainmenu)
        {
            intent = null;

            //Button got clicked show the popup dialog
            if (MainMenuConfirmDialogFragment.IsShown)
                return;

            MainMenuConfirmDialogFragment newMainMenuConfirm = new MainMenuConfirmDialogFragment();
            newMainMenuConfirm.show(PauseScreen.Instance.getSupportFragmentManager(), "Main Menu Confirm");
        }
        else if (v == btn_leaderboard)
        {
            intent.setClass(this, Leaderboard.class);
        }
        else if (v == btn_settings)
        {
            intent.setClass(this, Settings.class);
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

    public void StartMainMenu()
    {
        Intent intent = new Intent(this, Mainmenu.class);
        StateManager.Instance.ChangeState("MainMenu");
        startActivity(intent);
    }
}
