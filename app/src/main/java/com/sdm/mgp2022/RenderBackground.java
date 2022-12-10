package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

// This is an entity
// Scrolling background

public class RenderBackground implements EntityBase{
    private boolean isDone = false;
    private boolean isInit = false;

    private Bitmap bmp = null; // Using Android API. .. Bitmap to load images
    private Bitmap scaledbmp =  null;
    private Sprite spritesheet = null;

    private float xPos = 0, yPos = 0;
    private int width, height;

    public boolean IsDone()
    {
        return isDone;
    }
    public void SetIsDone(boolean _isDone)
    {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view)
    {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.conveyor_belt, bfo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, width, height, true);

        spritesheet = new Sprite(scaledbmp, 1, 3, 12);

        isInit = true;
    }
    public void Update(float _dt)
    {
        spritesheet.Update(_dt);
    }
    public void Render(Canvas _canvas)
    {
        spritesheet.Render(_canvas, (int)xPos, (int)yPos);
    }

    public boolean IsInit()
    {
        return isInit;
    }

    public int GetRenderLayer()
    {
        return LayerConstants.BACKGROUND_LAYER;
    }
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_DEFAULT;
    }

    public static RenderBackground Create(float _xPos, float _yPos, int _width, int _height)
    {
        RenderBackground result = new RenderBackground();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_DEFAULT);
        result.xPos = _xPos;
        result.yPos = _yPos;
        result.width = _width * 3; //width * 3 since 3 columns in spritesheet
        result.height = _height;
        return result;
    }
}
