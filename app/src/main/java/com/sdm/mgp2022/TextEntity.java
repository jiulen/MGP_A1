package com.sdm.mgp2022; //by jiulen

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;

public class TextEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    // Define how to use my own font type
    Typeface myFont;
    public Paint textPaint = new Paint();

    float xPos, yPos;
    public String text;
    private int red = 0, green = 0, blue = 0;
    int fontSize;
    Paint.Align alignment;

    public boolean rendered;
    private int renderLayer;

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
        myFont = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/MozartNbp-93Ey.ttf");
        textPaint.setARGB(255, red, green, blue);
        textPaint.setTypeface(myFont);
        textPaint.setTextSize(fontSize);
        textPaint.setTextAlign(alignment);

        isInit = true;
    }
    public void Update(float _dt)
    {
    }
    public void Render(Canvas _canvas) {
        if (rendered)
        {
            _canvas.drawText(text, xPos, yPos, textPaint);
        }
    }

    public boolean IsInit() { return isInit; }

    public int GetRenderLayer()
    {
        return renderLayer;
    }
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_TEXT;
    }

    public static TextEntity Create(float _xPos, float _yPos, int r, int g, int b, int _fontSize, Paint.Align align, int _renderLayer, boolean _rendered)
    {
        TextEntity result = new TextEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TEXT);
        result.xPos = _xPos;
        result.yPos = _yPos;
        result.red = r;
        result.green = g;
        result.blue = b;
        result.fontSize = _fontSize;
        result.alignment = align;
        result.renderLayer = _renderLayer;
        result.rendered = _rendered;
        return result;
    }
}

