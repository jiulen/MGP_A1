package com.sdm.mgp2022;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

// Created by TanSiewLan2021

public class MainGameSceneState implements StateBase {
    int ScreenWidth, ScreenHeight;
    int tileWidth;

    private float timer = 0.0f;

    //For ui background
    Paint bgPaint = new Paint();
    Paint bgOutlinePaint = new Paint();

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

        bgPaint.setARGB(255, 82, 90, 107);
        bgPaint.setStyle(Paint.Style.FILL);

        bgOutlinePaint.setARGB(255, 255, 255, 255);
        bgOutlinePaint.setStyle(Paint.Style.FILL);

        tileWidth = ScreenWidth / 9;

        RenderBackground.Create();
        RenderTextEntity.Create();
        PauseButtonEntity.Create();
        PlayerEntity.Create(tileWidth);

        /* To spawn a tile
            TileEntity tile = TileEntity.Create(TileEntity.TILE_TYPES.TILE_METAL, tileWidth);
            tile.SetPosX(tile.GetWidth() * (column + 0.5f));
            tile.SetPosY(tile.GetWidth() * (row + 0.5f) + TOP_BAR_HEIGHT);
        */

        //To visualise how much big the playing space is
        /*for (int row=0; row < 10; ++row)
        {
            for (int col=0; col < 6; ++col)
            {
                TileEntity tile = TileEntity.Create(TileEntity.TILE_TYPES.TILE_METAL, tileWidth);
                tile.SetPosX(tile.GetWidth() * (col + 0.5f));
                tile.SetPosY(tile.GetWidth() * (row + 0.5f));
            }
        }*/

        board.fillBoard();
    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas);

        //Draw ui background
        //Right
        _canvas.drawRect(tileWidth * 6,0, ScreenWidth, tileWidth * 12, bgPaint);
        //Left line of right box
        _canvas.drawRect(tileWidth * 6,0, tileWidth * 6 + 10, tileWidth * 12, bgOutlinePaint);
        //Bottom
        _canvas.drawRect(0,tileWidth * 12, ScreenWidth, ScreenHeight, bgPaint);
        //Top line of bottom box
        _canvas.drawRect(0,tileWidth * 12, ScreenWidth, tileWidth * 12 + 10, bgOutlinePaint);
    }

    @Override
    public void Update(float _dt) {

        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown()) {

            //Example of touch on screen in the main game to trigger back to Main menu
//            StateManager.Instance.ChangeState("Mainmenu");
        }
    }
}



