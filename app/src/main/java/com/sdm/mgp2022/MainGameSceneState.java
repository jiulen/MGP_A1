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
    private float timer = 0.0f;

    @Override
    public String GetName() {
        return "MainGame";
    }

    BoardManager board = new BoardManager();
    @Override
    public void OnEnter(SurfaceView _view)
    {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int tileWidth = displayMetrics.widthPixels / 9;

        RenderBackground.Create();
        RenderTextEntity.Create();
        PauseButtonEntity.Create();
        PlayerEntity.Create(tileWidth);

        /* To spawn a tile
            TileEntity tile = TileEntity.Create(TileEntity.TILE_TYPES.TILE_METAL, tileWidth);
            tile.SetPosX(tile.GetWidth() * (column + 0.5f));
            tile.SetPosY(tile.GetWidth() * (row + 0.5f));
        */

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



