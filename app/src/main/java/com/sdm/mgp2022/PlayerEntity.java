package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class PlayerEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos = 0;
    private int width, height = 0;

    private int tileWidth = 0;

    private int column = 2; //start at third column, 0 - 5

    private Bitmap bmp, scaledBmp = null;

    private boolean moving = false;

    private final float MOVE_COOLDOWN = 0.1f;
    private float currentTime = 0;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.player_claw, bfo);

        //player take 3/9 of screen width for width and 2/9 of screen width for height
        width = tileWidth;
        height = tileWidth * 2;
        scaledBmp = Bitmap.createScaledBitmap(bmp, width, height, true);

        xPos = (column + 0.5f) * tileWidth;
        yPos = 11 * tileWidth; // wont change

        isInit = true;
    }

    public void Update(float _dt){
        if (moving)
            currentTime -= _dt;

        if (moving && currentTime <= 0)
            moving = false;
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
        return LayerConstants.PLAYER_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_PLAYER;
    }

    public static PlayerEntity Create(int _tileWidth){
        PlayerEntity result = new PlayerEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PLAYER);
        result.tileWidth = _tileWidth;
        return result;
    }

    public void MoveLeft()
    {
        if (column > 0 && !moving)
        {
            column -= 1;
            xPos -= tileWidth;
            moving = true;
            currentTime = MOVE_COOLDOWN;
        }
    }

    public void MoveRight()
    {
        if (column < 5 && !moving)
        {
            column += 1;
            xPos += tileWidth;
            moving = true;
            currentTime = MOVE_COOLDOWN;
        }
    }
}