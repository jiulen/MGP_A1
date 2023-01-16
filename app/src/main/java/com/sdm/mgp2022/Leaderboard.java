package com.sdm.mgp2022; // By Jiu Len

import static java.lang.Integer.parseInt;

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

        if (leaderboardInfoSet.size() > 0)
        {
            String[] leaderbaordInfoArray = leaderboardInfoSet.toArray(new String[leaderboardInfoSet.size()]);
            PlayerLeaderboardInfo playerLeaderboardInfo[] = new PlayerLeaderboardInfo[leaderbaordInfoArray.length];

            for (int i = 0; i < leaderbaordInfoArray.length; ++i)
            {
                String[] data = leaderbaordInfoArray[i].split(",");
                playerLeaderboardInfo[i] = new PlayerLeaderboardInfo(data[0], parseInt(data[1]));
            }
            //Sort scores
            LeaderboardSorter.Instance.mergeSort(playerLeaderboardInfo, 0, playerLeaderboardInfo.length - 1);
            //Reset leaderboard text
            text_col_rank.setText("RANK");
            text_col_name.setText("NAME");
            text_col_score.setText("SCORE");
            //Fill in leaderboard text
            for (int i = 0; i < playerLeaderboardInfo.length && i < 20; ++i) // Only show top 10 entries
            {
                text_col_rank.setText(text_col_rank.getText() + "\n\n" + (i + 1));
                text_col_name.setText(text_col_name.getText() + "\n\n" + playerLeaderboardInfo[i].GetPlayerName());
                text_col_score.setText(text_col_score.getText() + "\n\n" + String.format("%09d",playerLeaderboardInfo[i].GetPlayerScore()));
            }
        }
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
