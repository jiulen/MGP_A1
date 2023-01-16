package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

import androidx.fragment.app.FragmentManager;

public class PauseButtonEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private Bitmap bmpP = null;
    private Bitmap scaledbmpP = null;

    private Bitmap bmpUP = null;
    private Bitmap scaledbmpUP = null;

    private boolean Paused = false;

    float xPos, yPos;
    int width, height;
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

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmpP = BitmapFactory.decodeResource(_view.getResources(), R.drawable.pause, bfo);
        bmpUP = BitmapFactory.decodeResource(_view.getResources(), R.drawable.pause1, bfo);

        scaledbmpP = Bitmap.createScaledBitmap(bmpP, width, height, true);
        scaledbmpUP = Bitmap.createScaledBitmap(bmpUP, width, height, true);

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

                    //Button got clicked show the popup dialog
                    if (PauseConfirmDialogFragment.IsShown)
                        return;

                    PauseConfirmDialogFragment newPauseConfirm = new PauseConfirmDialogFragment();
                    newPauseConfirm.setCancelable(false);
                    newPauseConfirm.show(GamePage.Instance.getSupportFragmentManager(), "Pause Confirm");
                }
                // if not just want a pause without the popup dialog
                buttonDelay = 0;
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
        return LayerConstants.UI_LAYER;
    }

    public void SetRenderLayer(int _newLayer) {
        return;
    }

    public ENTITY_TYPE GetEntityType() {
        return ENTITY_TYPE.ENT_PAUSE;
    }

    public static PauseButtonEntity Create(int _width, int _height, float _xPos, float _yPos) {
        PauseButtonEntity result = new PauseButtonEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PAUSE);
        result.width = _width;
        result.height = _height;
        result.xPos = _xPos;
        result.yPos = _yPos;
        return result;
    }
}
