package com.sdm.mgp2022;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

import org.w3c.dom.Text;

// Created by TanSiewLan2021

public class MainGameSceneState implements StateBase {
    int ScreenWidth, ScreenHeight;
    int tileWidth;

    int level = 1;

    private float timer = 0.0f;

    //For ui background
    UIBackgroundEntity rightBG;
    UIBackgroundEntity bottomBG;

    //For ui
    ButtonEntity aButton;
    ButtonEntity bButton;
    ButtonEntity leftButton;
    ButtonEntity rightButton;

    TextEntity scoreTitleText;
    TextEntity scoreText;
    TextEntity levelText;

    EnemyEntity enemy;

    //GameObjects
    PlayerEntity player;

    @Override
    public String GetName() {
        return "MainGame";
    }

    BoardManager board = new BoardManager();
    @Override
    public void OnEnter(SurfaceView _view)
    {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        ScreenWidth = displayMetrics.widthPixels;
        ScreenHeight = displayMetrics.heightPixels;

        tileWidth = ScreenWidth / 9;

        //Create 6 backgrounds (one for each column)
        RenderBackground.Create(tileWidth / 2, tileWidth * 5, tileWidth, tileWidth * 10);
        RenderBackground.Create(tileWidth / 2 * 3, tileWidth * 5, tileWidth, tileWidth * 10);
        RenderBackground.Create(tileWidth / 2 * 5, tileWidth * 5, tileWidth, tileWidth * 10);
        RenderBackground.Create(tileWidth / 2 * 7, tileWidth * 5, tileWidth, tileWidth * 10);
        RenderBackground.Create(tileWidth / 2 * 9, tileWidth * 5, tileWidth, tileWidth * 10);
        RenderBackground.Create(tileWidth / 2 * 11, tileWidth * 5, tileWidth, tileWidth * 10);
        //Game Objects
        player = PlayerEntity.Create(tileWidth);

        //UI Background
        rightBG = UIBackgroundEntity.Create(tileWidth * 6,0, ScreenWidth, tileWidth * 12,
                                           tileWidth * 6,0, tileWidth * 6 + 10, tileWidth * 12);
        bottomBG = UIBackgroundEntity.Create(0,tileWidth * 12, ScreenWidth, ScreenHeight,
                                            0,tileWidth * 12, ScreenWidth, tileWidth * 12 + 10);
        //UI Buttons (set initial position based on width)
        aButton = ButtonEntity.Create(R.drawable.a_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 7 - 20, ScreenHeight - ScreenWidth / 10 - 20);
        bButton = ButtonEntity.Create(R.drawable.b_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 9 - 20, ScreenHeight - ScreenWidth / 10 * 3 - 20);
        leftButton = ButtonEntity.Create(R.drawable.left_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 + 50, ScreenHeight - ScreenWidth / 5 - 10);
        rightButton = ButtonEntity.Create(R.drawable.right_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 3 + 100, ScreenHeight - ScreenWidth / 5 - 10);

        //PauseButtonEntity.Create();

        //Set bounds for game buttons (based on bottomBG)
        aButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);
        bButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);

        //UI Other (e.g. Text, Pictures)
        //Text
        FPSTextEntity.Create((int)(ScreenWidth / 9 * 7.5));
        //Enemy
        enemy = EnemyEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 2.5), (int)(ScreenWidth / 9 * 2));
        //More text
        scoreTitleText = TextEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 8.5), 255, 255, 255, 50, Paint.Align.CENTER);
        scoreTitleText.text = "Score";
        scoreText = TextEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 9), 255, 255, 255, 50, Paint.Align.CENTER);
        scoreText.text = String.format("%09d", player.score);
        levelText = TextEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 11), 255, 255, 255, 50, Paint.Align.CENTER);
        levelText.text = "Level " + level;

        board.fillBoard(tileWidth);
        board.setPlayerCol(player.column);
    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        //Render all entities
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt)
    {
        EntityManager.Instance.Update(_dt);

        board.updateBoard(level, tileWidth, _dt);

        if (!board.attackSent)
        {
            if (board.clearedTilesNum > 0)
            {
                //Send attack if attack not sent yet
                System.out.println("Attack: " + board.clearedTilesNum);
                //Damage enemy
                enemy.health -= board.clearedTilesNum;
                if (enemy.health < 0)
                    enemy.health = 0;
                //Get points
                player.score += (board.clearedTilesNum * 200 - 400); //Simplified from this: board.clearedTilesNum * 100 + (board.clearedTilesNum - 4) * 100
            }

            board.attackSent = true; //Set attackSent back to true
        }

        //Update UI texts
        scoreText.text = String.format("%09d", player.score);
        levelText.text = "Level " + level;

        if (leftButton.isDown)
        {
            player.MoveLeft();
            board.setPlayerCol(player.column);
            if (board.selectedTile != null)
            {
                board.selectedTile.SetPosX(player.xPos);
            }
            leftButton.isDown = false;
        }
        else if (rightButton.isDown)
        {
            player.MoveRight();
            board.setPlayerCol(player.column);
            if (board.selectedTile != null)
            {
                board.selectedTile.SetPosX(player.xPos);
            }
            rightButton.isDown = false;
        }

        if (aButton.isDown)
        {
            //Swap tiles
            board.setButtonDownA(true);
            aButton.isDown = false;
        }
        if (bButton.isDown)
        {
            //Swap tiles
            board.setButtonDownB(true);
            bButton.isDown = false;
        }

        board.setEnemyHealth(enemy.health);
    }
}



