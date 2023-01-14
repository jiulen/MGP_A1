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

    TextEntity scoreText;
    TextEntity hiScoreText;
    TextEntity levelText;

    EnemyEntity enemy;
    
    PauseButtonEntity pauseButton;

    //GameObjects
    PlayerEntity player;

    @Override
    public String GetName() {
        return "MainGame";
    }

    BoardManager board;
    @Override
    public void OnEnter(SurfaceView _view) //by jiulen
    {
        board = new BoardManager();

        //Set scores in save
        GameSystem.Instance.SaveEditBegin();
        //Init score saved
        GameSystem.Instance.SetIntInSave("Score", 0);
        //Init hi-score saved (if no hi-score)
        if (!GameSystem.Instance.CheckKeyInSave("Hi-Score")) {
            GameSystem.Instance.SetIntInSave("Hi-Score", 0);
        }
        GameSystem.Instance.SaveEditEnd();

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
                ScreenWidth / 10 + 50, ScreenHeight - ScreenWidth / 5 - 20);
        rightButton = ButtonEntity.Create(R.drawable.right_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 3 + 100, ScreenHeight - ScreenWidth / 5 - 20);

        pauseButton = PauseButtonEntity.Create(ScreenWidth / 7, ScreenWidth / 7,
                (int)(ScreenWidth / 9 * 7.5), ScreenWidth / 9 * 11);

        //Set bounds for game buttons (based on bottomBG)
        aButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);
        bButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);

        //UI Other (e.g. Text, Pictures)
        //Text
        FPSTextEntity.Create((int)(ScreenWidth / 9 * 7.5));
        //Enemy
        enemy = EnemyEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 3), (int)(ScreenWidth / 9 * 2));
        //More text
        scoreText = TextEntity.Create((int)(ScreenWidth / 9 * 3.3), (int)(ScreenWidth / 9 * 12.75), 255, 255, 255, 50, Paint.Align.RIGHT);
        scoreText.text = "SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Score"));
        hiScoreText = TextEntity.Create((int)(ScreenWidth / 9 * 3.3), (int)(ScreenWidth / 9 * 13.25), 255, 255, 255, 50, Paint.Align.RIGHT);
        hiScoreText.text = "HI-SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Hi-Score"));
        levelText = TextEntity.Create((int)(ScreenWidth / 9 * 7.5), (int)(ScreenWidth / 9 * 12.75), 255, 255, 255, 50, Paint.Align.LEFT);
        levelText.text = "LEVEL " + level;

        board.fillBoard(tileWidth);
        board.setPlayerCol(player.column);

        GameSystem.Instance.SetIsPaused(false);
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
    public void Update(float _dt) //by jiulen
    {
        EntityManager.Instance.Update(_dt);

        if (!GameSystem.Instance.GetIsPaused())
        {
            if (board.lose) //If lose
            {

            }
            else if (board.win) //If win
            {

            }
            else //Only play if not won or lost
            {
                board.updateBoard(level, tileWidth, _dt);

                //check enemy attack
                if (enemy.charge == enemy.maxCharge && board.boardState == BoardManager.boardStates.READY) //enemy atk charged and board ready
                {
                    switch (enemy.enemyLevel)
                    {
                        case 1:
                            //supposed to be level 3 atk (test here for now)
                            int rowDropped = board.dropNewTilesRowEarly(tileWidth);
                            if (rowDropped == -1)
                            {
                               System.out.println("Invalid tile spawn - from level 3 attack");
                            }
                            else
                            {

                                board.ConvertGarbage3(rowDropped);

                            }
                            board.moveSpeedMultiplier = 5;
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }

                    enemy.charge = 0; //reset charge after attack done
                }

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
                        //Get score
                        player.score += (board.clearedTilesNum * 200 - 400); //Simplified from this: board.clearedTilesNum * 100 + (board.clearedTilesNum - 4) * 100
                        //Save new score
                        GameSystem.Instance.SaveEditBegin();
                        GameSystem.Instance.SetIntInSave("Score", player.score);
                        if (player.score > GameSystem.Instance.GetIntFromSave("Hi-Score"))
                        {
                            GameSystem.Instance.SetIntInSave("Hi-Score", player.score);
                        }
                        GameSystem.Instance.SaveEditEnd();

                        board.clearedTilesNum = 0;
                    }

                    board.attackSent = true; //Set attackSent back to true
                }

                //Update UI texts
                scoreText.text = "SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Score"));
                hiScoreText.text = "HI-SCORE " + String.format("%09d", GameSystem.Instance.GetIntFromSave("Hi-Score"));
                levelText.text = "LEVEL " + level;

                board.setEnemyHealth(enemy.health);

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

                //Player animation
                switch (board.playerAnim)
                {
                    case DROP:
                        player.isDrop = true;
                        player.isSelect = false;
                        player.spritesheetRev.currentFrame = player.spritesheetRev.startFrame;

                        board.playerAnim = BoardManager.playerAnimation.NONE;
                        break;
                    case SELECT:
                        player.isSelect = true;
                        player.isDrop = false;
                        player.spritesheet.currentFrame = player.spritesheet.startFrame;

                        board.playerAnim = BoardManager.playerAnimation.NONE;
                        break;
                }
            }
        }
    }
}



