package com.sdm.mgp2022;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

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

        RenderTextEntity.Create((int)(ScreenWidth / 9 * 7.5));
        //PauseButtonEntity.Create();

        //UI Background
        rightBG = UIBackgroundEntity.Create(tileWidth * 6,0, ScreenWidth, tileWidth * 12,
                                           tileWidth * 6,0, tileWidth * 6 + 10, tileWidth * 12);
        bottomBG = UIBackgroundEntity.Create(0,tileWidth * 12, ScreenWidth, ScreenHeight,
                                            0,tileWidth * 12, ScreenWidth, tileWidth * 12 + 10);
        //UI Buttons (set initial position based on width)
        aButton = ButtonEntity.Create(R.drawable.a_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 7 - 10, ScreenHeight - ScreenWidth / 10 - 20);
        bButton = ButtonEntity.Create(R.drawable.b_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 9 - 10, ScreenHeight - ScreenWidth / 10 * 3 - 20);
        leftButton = ButtonEntity.Create(R.drawable.left_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 + 50, ScreenHeight - ScreenWidth / 10 - 20);
        rightButton = ButtonEntity.Create(R.drawable.right_button, ScreenWidth / 5, ScreenWidth / 5,
                ScreenWidth / 10 * 3 + 100, ScreenHeight - ScreenWidth / 10 - 20);

        //PauseButtonEntity.Create();

        //Set bounds for game buttons (based on bottomBG)
        aButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);
        bButton.SetBounds(0, ScreenWidth, tileWidth * 12, ScreenHeight);

        //UI Other (e.g. Text, Pictures)
        RenderTextEntity.Create((int)(ScreenWidth / 9 * 7.5));

        //UI Other

        board.fillBoard(tileWidth);

    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        //Render background behind player

        //Render all entities
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt) {

        EntityManager.Instance.Update(_dt);

        board.updateBoard(level,_dt);
        if (aButton.isDown)
        {
            //Grab or drop tile
        }
        else if (bButton.isDown)
        {
            //Swap tiles
        }

        if (leftButton.isDown)
        {
            player.MoveLeft();
        }
        else if (rightButton.isDown)
        {
            player.MoveRight();
        }
    }
}



