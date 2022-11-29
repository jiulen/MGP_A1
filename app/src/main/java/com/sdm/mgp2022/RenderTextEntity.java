package com.sdm.mgp2022;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;

public class RenderTextEntity implements EntityBase{

    private boolean isDone = false;
    private boolean isInit = false;

    int frameCount;
    long lastTime;
    long lastFPSTime;
    float fps;

    // Paint object
    Paint paint = new Paint();
    private int red = 0, green = 0, blue = 0;

    // Define how to use my own font type
    Typeface myFont;

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
        //Use own font
        myFont = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/amogus.ttf");
        isInit = true;
    }
    public void Update(float _dt)
    {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        lastTime = currentTime;
        if (currentTime - lastFPSTime > 1000) {
            fps = (frameCount * 1000) / (currentTime - lastFPSTime);
            lastFPSTime = currentTime;
            frameCount = 0;
        }
    }
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setARGB(255, red, green, blue);
        //paint.setStrokeWidth(200);
        paint.setTypeface(myFont);
        paint.setTextSize(70);
        _canvas.drawText("FPS: " + fps, 30, 80, paint);
    }

    public boolean IsInit() { return true; }

    public int GetRenderLayer()
    {
        return LayerConstants.RENDERTEXT_LAYER;
    }
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_TEXT;
    }

    public static RenderTextEntity Create()
    {
        RenderTextEntity result = new RenderTextEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TEXT);
        return result;
    }
}
