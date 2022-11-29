package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class SmurfEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    int xPos = 100, yPos = 200;

    //WE going to use the Sprite class we created
    private Sprite spritesheet = null;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        // bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.smurf_sprite);
        // or spritesheet = new Sprite(bmp, 4, 4, 16);
        spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(), R.drawable.smurf_sprite),
                4, 4, 16);

        isInit = true;
        if (spritesheet == null)
            System.out.println("men1");
    }

    public void Update(float _dt){
        if (spritesheet == null)
            System.out.println("men2");
        spritesheet.Update(_dt);

        // Check collision using TouchManager , refer to slides for Week 6 page 7
    }

    public void Render(Canvas _canvas)
    {
        spritesheet.Render(_canvas, xPos, yPos);
    }

    public boolean IsInit(){
        return isInit;
    }

    public int GetRenderLayer(){
        return LayerConstants.RENDERSMURF_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_SMURF;
    }

    // New
    public static SmurfEntity Create(){
        SmurfEntity result = new SmurfEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_SMURF);
        return result;
    }

    public float GetPosX(){
        return xPos;
    }

    public float GetPosY(){
        return yPos;
    }
}

