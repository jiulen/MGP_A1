package com.sdm.mgp2022; //by jiulen

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class UIBackgroundEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    float left, top, right, bottom;
    float o_left, o_top, o_right, o_bottom;

    Paint bgPaint = new Paint();
    Paint bgOutlinePaint = new Paint();

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        bgPaint.setARGB(255, 82, 90, 107);
        bgPaint.setStyle(Paint.Style.FILL);

        bgOutlinePaint.setARGB(255, 255, 255, 255);
        bgOutlinePaint.setStyle(Paint.Style.FILL);

        isInit = true;
    }

    public void Update(float _dt){

    }

    public void Render(Canvas _canvas)
    {
        _canvas.drawRect(left, top, right, bottom, bgPaint);

        _canvas.drawRect(o_left, o_top, o_right, o_bottom, bgOutlinePaint);
    }

    public boolean IsInit(){
        return isInit;
    }

    public int GetRenderLayer(){
        return LayerConstants.UIBG_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_BUTTON;
    }

    public static UIBackgroundEntity Create(float _left, float _top, float _right, float _bottom,
                                      float _oLeft, float _oTop, float _oRight, float _oBottom){
        UIBackgroundEntity result = new UIBackgroundEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_BUTTON);
        result.left = _left;
        result.top = _top;
        result.right = _right;
        result.bottom = _bottom;
        result.o_left = _oLeft;
        result.o_top = _oTop;
        result.o_right = _oRight;
        result.o_bottom = _oBottom;
        return result;
    }
}
