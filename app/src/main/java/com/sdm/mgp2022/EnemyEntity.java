package com.sdm.mgp2022; //by jiulen

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class EnemyEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    public float xPos, yPos;
    private int width;

    //Name
    TextEntity enemyNameText;
    //Background
    Paint bgPaint = new Paint();
    //Bitmap
    private Bitmap[] bmps = new Bitmap[4];
    private Bitmap[] scaledBmps = new Bitmap[4];
    //Health bar
    TextEntity hpName;
    Paint hpPaint = new Paint();
    public int maxHealth = 50;
    public int health = maxHealth; //enemy die when health = 0
    private float hpPosY;
    //Attack bar
    TextEntity atkName;
    Paint atkPaint = new Paint();
    public int charge = 0; //enemy attack when charge = 100
    public int maxCharge = 100;
    private float chargeTime = 0.2f; //time required to get +1 charge
    private float lastCharge = 0;
    private float atkPosY;
    //Both bars
    Paint outlinePaint = new Paint();
    private final int outlineWidth = 10;

    public int enemyLevel = 1;

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
        enemyLevel = 1;

        //For name
        enemyNameText = TextEntity.Create(xPos, yPos - width * 0.6f, 255, 255, 255, 50, Paint.Align.CENTER, LayerConstants.UI_LAYER, true);
        enemyNameText.text = "Home";
        //For background of bitmap
        bgPaint.setARGB(255, 0, 0, 0);
        bgPaint.setStyle(Paint.Style.FILL);
        //For bitmap
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;
        //Load all bmps
        bmps[0] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.green_bin, bfo);
        bmps[1] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.factory, bfo);
        bmps[2] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.landfill, bfo);
        bmps[3] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.rivertrash, bfo);
        scaledBmps[0] = Bitmap.createScaledBitmap(bmps[0], width, width, true);
        scaledBmps[1] = Bitmap.createScaledBitmap(bmps[1], width, width, true);
        scaledBmps[2] = Bitmap.createScaledBitmap(bmps[2], width, width, true);
        scaledBmps[3] = Bitmap.createScaledBitmap(bmps[3], width, width, true);
        //For health bar
        hpName = TextEntity.Create(xPos, yPos + width * 0.7f, 255, 255, 255, 50, Paint.Align.CENTER, LayerConstants.UI_LAYER, true);
        hpName.text = "Health";
        hpPaint.setARGB(255, 255, 0, 0);
        hpPosY = yPos + width * 1.0f;
        //For attack bar
        atkName = TextEntity.Create(xPos, yPos + width * 1.4f, 255, 255, 255, 50, Paint.Align.CENTER, LayerConstants.UI_LAYER, true);
        atkName.text = "Attack";
        atkPaint.setARGB(255, 0, 255, 255);
        atkPosY = yPos + width * 1.7f;
        //Both bar
        outlinePaint.setARGB(255, 255, 255, 255);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(outlineWidth);

        isInit = true;
    }
    public void Update(float _dt)
    {
        if (charge < 100)
            lastCharge += _dt;
        if (lastCharge >= chargeTime)
        {
            charge += 1;
            lastCharge = 0;
        }
    }
    public void Render(Canvas _canvas) {
        //Draw background
        _canvas.drawRect(xPos - width * 0.5f, yPos - width * 0.5f,
                        xPos + width * 0.5f, yPos + width * 0.5f, bgPaint);
        //Draw bitmap (if available)
        if (scaledBmps[enemyLevel - 1] != null)
        {
            _canvas.drawBitmap(scaledBmps[enemyLevel - 1], xPos - scaledBmps[enemyLevel - 1].getWidth() * 0.5f,
                    yPos - scaledBmps[enemyLevel - 1].getHeight() * 0.5f, null);
        }
        //Draw health bar
        _canvas.drawRect(xPos - width * 0.5f, hpPosY - 15,
                        xPos - width * 0.5f + width * (health / (float)maxHealth), hpPosY + 15, hpPaint);
        _canvas.drawRect(xPos - width * 0.5f - outlineWidth * 0.5f, hpPosY - 15 - outlineWidth * 0.5f,
                        xPos + width * 0.5f + outlineWidth * 0.5f, hpPosY + 15 + outlineWidth * 0.5f,
                             outlinePaint);
        //Draw attack bar
        _canvas.drawRect(xPos - width * 0.5f, atkPosY - 15,
                xPos - width * 0.5f + width * (charge / (float)maxCharge), atkPosY + 15, atkPaint);
        _canvas.drawRect(xPos - width * 0.5f - outlineWidth * 0.5f, atkPosY - 15 - outlineWidth * 0.5f,
                xPos + width * 0.5f + outlineWidth * 0.5f, atkPosY + 15 + outlineWidth * 0.5f,
                outlinePaint);
    }

    public boolean IsInit() { return isInit; }

    public int GetRenderLayer()
    {
        return LayerConstants.UI_LAYER;
    }
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_ENEMY;
    }

    public static EnemyEntity Create(float _xPos, float _yPos, int _width)
    {
        EnemyEntity result = new EnemyEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_ENEMY);
        result.xPos = _xPos;
        result.yPos = _yPos;
        result.width = _width;
        return result;
    }

    public void ChangeLevel(int newLevel)
    {
        enemyLevel = newLevel;
        switch (newLevel) //change other things here e.g. picture
        {
            case 1:
                enemyNameText.text = "Home";
                chargeTime = 0.2f;
                break;
            case 2:
                enemyNameText.text = "Factory";
                chargeTime = 0.25f;
                break;
            case 3:
                enemyNameText.text = "Landfill";
                chargeTime = 0.3f;
                break;
            case 4:
                enemyNameText.text = "Ocean";
                chargeTime = 0.35f;
                break;
        }

        // Reset values
        health = maxHealth;
        charge = 0;
        lastCharge = 0;
    }
}


