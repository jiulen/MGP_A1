package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class PauseButtonEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private Bitmap bmpP = null;
    private Bitmap scaledbmpP = null;

    private Bitmap bmpUP = null;
    private Bitmap scaledbmpUP = null;

    private boolean Paused = false;

    int xPos, yPos;
    int ScreenWidth, ScreenHeight;

    float buttonDelay = 0;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {

        //Display screensize
        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        bmpP = ResourceManager.Instance.GetBitmap(R.drawable.pause);
        bmpUP = ResourceManager.Instance.GetBitmap(R.drawable.pause1);

        scaledbmpP = Bitmap.createScaledBitmap(bmpP, (int) ScreenWidth / 7, (int) ScreenHeight / 7, true);
        //if it still appears too big. own scale down it.
        // (int) ScreenWidth/12, (int) ScreenHeight/7,

        scaledbmpUP = Bitmap.createScaledBitmap(bmpUP, (int) ScreenWidth / 7, (int) ScreenHeight / 7, true);

        xPos = ScreenWidth - 150;
        yPos = 150;  // I using a hard number.. U can use ScreenHeight - 50

        isInit = true;
    }

    public void Update(float _dt) {
        buttonDelay += _dt;

        if (TouchManager.Instance.HasTouch()) {
            if (TouchManager.Instance.IsDown() && !Paused) {
                // Check Collision with the pause button image
                float imgRadius = scaledbmpP.getHeight() * 0.5f;

                if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(),
                        0.0f, xPos, yPos, imgRadius) && buttonDelay >= 0.25) {
                    Paused = true;
                }
                // if not just want a pause without the popup dialog
                buttonDelay = 0;
                GameSystem.Instance.SetIsPaused(!GameSystem.Instance.GetIsPaused());
            }
        } else Paused = false;
    }

    public void Render(Canvas _canvas) {
        if (Paused == false)
            _canvas.drawBitmap(scaledbmpP, xPos - scaledbmpP.getWidth() * 0.5f,
                    yPos - scaledbmpP.getHeight() * 0.5f, null);

        else
            _canvas.drawBitmap(scaledbmpUP, xPos - scaledbmpUP.getWidth() * 0.5f,
                    yPos - scaledbmpUP.getHeight() * 0.5f, null);
    }

    public boolean IsInit() {
        return isInit;
    }

    public int GetRenderLayer() {
        return LayerConstants.PAUSEBUTTON_LAYER;
    }

    public void SetRenderLayer(int _newLayer) {
        return;
    }

    public ENTITY_TYPE GetEntityType() {
        return ENTITY_TYPE.ENT_PAUSE;
    }

    public static PauseButtonEntity Create() {
        PauseButtonEntity result = new PauseButtonEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PAUSE);
        return result;
    }
}
