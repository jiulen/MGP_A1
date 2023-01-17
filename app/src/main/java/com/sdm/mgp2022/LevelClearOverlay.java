package com.sdm.mgp2022; // By Jiu Len

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class LevelClearOverlay implements EntityBase {
    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos = 0;
    private int width, height = 0;

    private Bitmap bmp, scaledBmp = null;

    //Level Clear Text
    TextEntity levelClearText;
    //Continue Text
    TextEntity continueText;

    public boolean rendered;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.overlay_bg, bfo);
        scaledBmp = Bitmap.createScaledBitmap(bmp, width, height, true);

        xPos = width / 2;
        yPos = height / 2;

        //For clear text
        levelClearText = TextEntity.Create(xPos, yPos - height * 0.25f, 0, 255, 0, 125, Paint.Align.CENTER, LayerConstants.OVERLAY_TEXT_LAYER, false);
        levelClearText.text = "LEVEL CLEAR TEXT";
        //For continue text
        continueText = TextEntity.Create(xPos, yPos + height * 0.25f, 255, 255, 255, 100, Paint.Align.CENTER, LayerConstants.OVERLAY_TEXT_LAYER, false);
        continueText.text = "Touch to continue";

        rendered = false;

        isInit = true;
    }

    public void Update(float _dt){
    }

    public void Render(Canvas _canvas)
    {
        if (rendered)
        {
            //Background
            _canvas.drawBitmap(scaledBmp, xPos - scaledBmp.getWidth() * 0.5f,
                    yPos - scaledBmp.getHeight() * 0.5f, null);
        }
    }

    public boolean IsInit(){
        return isInit;
    }

    public int GetRenderLayer(){
        return LayerConstants.OVERLAY_BG_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_OVERLAY;
    }

    public static LevelClearOverlay Create(int _width, int _height){
        LevelClearOverlay result = new LevelClearOverlay();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_OVERLAY);
        result.width = _width;
        result.height = _height;
        return result;
    }

    public void SetLevel(int level)
    {
        //positive for clear and negative for lose
        switch (level)
        {
            case 1:
                levelClearText.text = "LEVEL 1 CLEAR";
                break;
            case 2:
                levelClearText.text = "LEVEL 2 CLEAR";
                break;
            case 3:
                levelClearText.text = "LEVEL 3 CLEAR";
                break;
            case 4:
                levelClearText.text = "LEVEL 4 CLEAR";
                break;
            case -1:
                levelClearText.text = "LEVEL 1 LOSE";
                break;
            case -2:
                levelClearText.text = "LEVEL 2 LOSE";
                break;
            case -3:
                levelClearText.text = "LEVEL 3 LOSE";
                break;
            case -4:
                levelClearText.text = "LEVEL 4 LOSE";
                break;
        }
    }

    public void SetRender(boolean rendering)
    {
        rendered = rendering;
        levelClearText.rendered = rendering;
        continueText.rendered = rendering;
    }
}
