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

import java.util.Set;

public class Leaderboard extends Activity implements View.OnClickListener {
    //Define buttons
    private ImageButton btn_back;

    private TextView text_col_rank;
    private TextView text_col_name;
    private TextView text_col_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.leaderboard);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this); //Set Listener to this button --> Resume Button

        text_col_rank = findViewById(R.id.text_col_rank);
        text_col_name = findViewById(R.id.text_col_name);
        text_col_score = findViewById(R.id.text_col_score);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //Parse data
        Set<String> leaderboardInfoSet = GameSystem.Instance.GetStringSetFromSave("Leaderboard");
        String[] leaderbaordInfoArray = leaderboardInfoSet.toArray(new String[leaderboardInfoSet.size()]);

        //Sort scores

        //Fill in leaderboard text

    }

    @Override
    //Invoke a callback event in the view
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_back)
        {
            GameSystem.Instance.SetIsPaused(false);
            intent = null;
            finish();
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
