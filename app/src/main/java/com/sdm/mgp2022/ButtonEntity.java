package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.SurfaceView;

public class ButtonEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos = 0;
    private int width, height = 0;
    private int xMinBound, xMaxBound, yMinBound, yMaxBound;

    private int resourceID;
    private Bitmap bmp, scaledBmp = null;

    public boolean isDown = false;
    public boolean isUp = true;

    //Vibration
    private Vibrator vibrator;
    public long vibrateTimeMS = 100;
    public int amplitude = 5;
    public boolean canVibrate = true;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        //Display screen size
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmp = BitmapFactory.decodeResource(_view.getResources(), resourceID, bfo);
        scaledBmp = Bitmap.createScaledBitmap(bmp, width, height, true);

        //Setup vibrator
        vibrator = (Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE);

        isInit = true;
    }

    public void Update(float _dt){
        if (TouchManager.Instance.HasTouch())
        {
            if (!isDown && isUp)
            {
                if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(),
                        0.0f, xPos, yPos, width * 0.5f)) {
                    isDown = true;
                    isUp = false;
                    startVibrate();
                }
            }

        }
        else
        {
            isDown = false;
            isUp = true;
        }

    }

    public void Render(Canvas _canvas)
    {
        _canvas.drawBitmap(scaledBmp, xPos - scaledBmp.getWidth() * 0.5f,
                yPos - scaledBmp.getHeight() * 0.5f, null);
    }

    public boolean IsInit(){
        return isInit;
    }

    public int GetRenderLayer(){
        return LayerConstants.UI_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_BUTTON;
    }

    public static ButtonEntity Create(int rid, int _width, int _height, float _xPos, float _yPos){
        ButtonEntity result = new ButtonEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_BUTTON);
        result.resourceID = rid;
        result.width = _width;
        result.height = _height;
        result.xPos = _xPos;
        result.yPos = _yPos;
        return result;
    }

    public void SetBounds(int minX, int maxX, int minY, int maxY)
    {
        xMinBound = minX;
        xMaxBound = maxX;
        yMinBound = minY;
        yMaxBound = maxY;
    }

    public void SetPos(float x, float y) //maybe checking if valid should be done outside idk
    {
        //Check if within x bounds
        if (x >= (xMinBound + width * 0.5f))
        {
            if (x <= (xMaxBound - width * 0.5f))
            {
                xPos = x;
            }
            else
            {
                xPos = xMaxBound - width * 0.5f;
            }
        }
        else {
            xPos = xMinBound + width * 0.5f;
        }

        //Check if within y bounds
        if (y >= (yMinBound + height * 0.5f))
        {
            if (y <= (yMaxBound - height * 0.5f))
            {
                yPos = y;
            }
            else
            {
                yPos = yMaxBound - height * 0.5f;
            }
        }
        else {
            yPos = yMinBound + height * 0.5f;
        }
    }

    public void startVibrate()
    {
        if (canVibrate)
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateTimeMS, amplitude));
    }
    public void stopVibrate()
    {
        vibrator.cancel();
    }
}