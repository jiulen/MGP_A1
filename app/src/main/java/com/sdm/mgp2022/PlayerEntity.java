package com.sdm.mgp2022; //by jiulen

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class PlayerEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    public float xPos, yPos = 0;
    private int width, height = 0;

    private int tileWidth = 0;

    public int column = 2; //start at third column, 0 - 5

    private Bitmap bmp, scaledBmp = null;
    public Sprite spritesheet = null;

    private Bitmap bmpRev, scaledBmpRev = null;
    public Sprite spritesheetRev = null;

    public boolean isSelect, isDrop = false;
    private boolean isReverse = false;

    public int score = 0;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.player_claw_animation, bfo);
        bmpRev = BitmapFactory.decodeResource(_view.getResources(), R.drawable.player_claw_animation_reverse, bfo);

        //player take 3/9 of screen width for width and 2/9 of screen width for height
        width = tileWidth * 3;
        height = tileWidth * 2;
        scaledBmp = Bitmap.createScaledBitmap(bmp, width * 4, height, true);
        scaledBmpRev = Bitmap.createScaledBitmap(bmpRev, width * 4, height, true);

        spritesheet = new Sprite(scaledBmp, 1, 4, 40);
        spritesheet.canLoop = false;
        spritesheetRev = new Sprite(scaledBmpRev, 1, 4, 40);
        spritesheetRev.canLoop = false;

        xPos = (column + 0.5f) * tileWidth;
        yPos = 11 * tileWidth; // wont change

        isInit = true;
    }

    public void Update(float _dt){
        if (spritesheet != null)
        {
            if (isSelect)
            {
                spritesheet.Update(_dt);
                isReverse = false;
            }
            else if (isDrop)
            {
                spritesheetRev.Update(_dt);
                isReverse = true;
            }
        }
    }

    public void Render(Canvas _canvas)
    {
        if (spritesheet != null && spritesheetRev != null)
        {
            if (!isReverse)
            {
                spritesheet.Render(_canvas, (int)xPos, (int)yPos);
            }
            else
            {
                spritesheetRev.Render(_canvas, (int)xPos, (int)yPos);
            }
        }

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
        if (column > 0)
        {
            column -= 1;
            xPos -= tileWidth;
        }
    }

    public void MoveRight()
    {
        if (column < 5)
        {
            column += 1;
            xPos += tileWidth;
        }
    }

    public void ResetPlayer()
    {
        // Reset sprite animation
        isSelect = false;
        isDrop = false;
        isReverse = false;
        spritesheet.currentFrame = spritesheet.startFrame;

        // Reset position
        column = 2;
        xPos = (column + 0.5f) * tileWidth;
    }
}