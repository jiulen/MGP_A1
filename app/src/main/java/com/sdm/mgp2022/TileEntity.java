package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class TileEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private int ScreenHeight, ScreenWidth;

    private float xPos, yPos = 0;
    private int width = 0;

    private Bitmap bmpN, scaledBmpN = null; //for normal tile
    private Bitmap bmpA, scaledBmpA = null; //for attack tile
    private Bitmap bmpG, scaledBmpG = null; //for garbage tile

    enum TILE_TYPES {
        TILE_PAPER,
        TILE_PLASTIC,
        TILE_METAL,
        TILE_GLASS,
        TILE_WOOD
    }
    public TILE_TYPES tileType;
    public boolean isAttack = false;
    public boolean isGarbage = false;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        //Display screen size
        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        switch (tileType)
        {
            case TILE_PAPER:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_paper_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_paper_atk, bfo);
                break;
            case TILE_PLASTIC:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_plastic_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_plastic_atk, bfo);
                break;
            case TILE_METAL:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_metal_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_metal_atk, bfo);
                break;
            case TILE_GLASS:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_glass_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_glass_atk, bfo);
                break;
            case TILE_WOOD:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_wood_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_wood_atk, bfo);
                break;
        }

        bmpG = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_garbage, bfo);

        //each tile take 1/9 of screen width
        width = ScreenWidth / 9;

        scaledBmpN = Bitmap.createScaledBitmap(bmpN, width, width, true);
        scaledBmpA = Bitmap.createScaledBitmap(bmpA, width, width, true);
        scaledBmpG = Bitmap.createScaledBitmap(bmpG, width, width, true);

        isInit = true;
    }

    public void Update(float _dt){

    }

    public void Render(Canvas _canvas)
    {
        if (isGarbage)
        {
            _canvas.drawBitmap(scaledBmpG, xPos - scaledBmpG.getWidth() * 0.5f,
                    yPos - scaledBmpG.getHeight() * 0.5f, null);
        }
        else if (isAttack)
        {
            _canvas.drawBitmap(scaledBmpA, xPos - scaledBmpA.getWidth() * 0.5f,
                    yPos - scaledBmpA.getHeight() * 0.5f, null);
        }
        else
        {
            _canvas.drawBitmap(scaledBmpN, xPos - scaledBmpN.getWidth() * 0.5f,
                    yPos - scaledBmpN.getHeight() * 0.5f, null);
        }

    }

    public boolean IsInit(){
        return isInit;
    }

    public int GetRenderLayer(){
        return LayerConstants.GAMEOBJECTS_LAYER;
    }
    public void SetRenderLayer(int _newLayer){
        return;
    }

    public ENTITY_TYPE GetEntityType(){
        return ENTITY_TYPE.ENT_TILE;
    }

    // Both works (first one need set pos after create, second one set pos while create)
    public static TileEntity Create(TILE_TYPES _tileType){
        TileEntity result = new TileEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TILE);
        result.tileType = _tileType;
        return result;
    }
    public static TileEntity Create(TILE_TYPES _tileType, float _xPos, float _yPos){
        TileEntity result = new TileEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TILE);
        result.tileType = _tileType;
        result.SetPosX(_xPos);
        result.SetPosY(_yPos);
        return result;
    }

    public float GetPosX() { return xPos; }
    public float GetPosY(){
        return yPos;
    }
    public float GetWidth() { return width; }

    public void SetPosX(float _xPos){
        xPos = _xPos;
    }
    public void SetPosY(float _yPos){
        yPos = _yPos;
    }

    public void OnHit(Collidable _other)
    {
        //idk
    }
}

