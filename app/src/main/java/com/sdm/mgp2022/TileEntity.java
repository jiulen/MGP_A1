package com.sdm.mgp2022; //by jiulen

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class TileEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos = 0;
    private int width = 0;

    private Bitmap bmpN, scaledBmpN = null; //for normal tile
    private Bitmap bmpA, scaledBmpA = null; //for attack tile
    private Bitmap bmpG, scaledBmpG = null; //for garbage tile

    private Bitmap bmpC, scaledBmpC = null; //for clearing tile
    private Sprite clearSpritesheet = null; //for clearing tile

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
    public boolean isClearing = false;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;

        switch (tileType)
        {
            case TILE_PAPER:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_paper_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_paper_atk, bfo);
                bmpC = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_paper_clear, bfo);
                break;
            case TILE_PLASTIC:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_plastic_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_plastic_atk, bfo);
                bmpC = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_plastic_clear, bfo);
                break;
            case TILE_METAL:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_metal_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_metal_atk, bfo);
                bmpC = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_metal_clear, bfo);
                break;
            case TILE_GLASS:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_glass_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_glass_atk, bfo);
                bmpC = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_glass_clear, bfo);
                break;
            case TILE_WOOD:
                bmpN = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_wood_normal, bfo);
                bmpA = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_wood_atk, bfo);
                bmpC = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_wood_clear, bfo);
                break;
        }

        bmpG = BitmapFactory.decodeResource(_view.getResources(), R.drawable.tile_garbage, bfo);

        scaledBmpN = Bitmap.createScaledBitmap(bmpN, width, width, true);
        scaledBmpA = Bitmap.createScaledBitmap(bmpA, width, width, true);
        scaledBmpG = Bitmap.createScaledBitmap(bmpG, width, width, true);
        scaledBmpC = Bitmap.createScaledBitmap(bmpC, width * 4, width, true); //* 4 since 4 columns in spritesheet

        clearSpritesheet = new Sprite(scaledBmpC, 1, 4, 16);
        clearSpritesheet.canLoop = false;

        isInit = true;
    }

    public void Update(float _dt){
        if (isClearing)
            clearSpritesheet.Update(_dt);
    }

    public void Render(Canvas _canvas)
    {
        if (isClearing)
        {
            if (clearSpritesheet != null)
                clearSpritesheet.Render(_canvas, (int)xPos, (int)yPos);
        }
        else if (isGarbage)
        {
            if (scaledBmpG != null) {
                _canvas.drawBitmap(scaledBmpG, xPos - scaledBmpG.getWidth() * 0.5f,
                        yPos - scaledBmpG.getHeight() * 0.5f, null);
            }
        }
        else if (isAttack)
        {
            if (scaledBmpA != null) {
                _canvas.drawBitmap(scaledBmpA, xPos - scaledBmpA.getWidth() * 0.5f,
                        yPos - scaledBmpA.getHeight() * 0.5f, null);
            }
        }
        else
        {
            if (scaledBmpN != null) {
                _canvas.drawBitmap(scaledBmpN, xPos - scaledBmpN.getWidth() * 0.5f,
                        yPos - scaledBmpN.getHeight() * 0.5f, null);
            }
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
    public static TileEntity Create(TILE_TYPES _tileType, int width){
        TileEntity result = new TileEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TILE);
        result.width = width;
        result.tileType = _tileType;
        return result;
    }
    public static TileEntity Create(TILE_TYPES _tileType, int width, float _xPos, float _yPos){
        TileEntity result = new TileEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TILE);
        result.width = width;
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

