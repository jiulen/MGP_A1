package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class EnemyEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    private float xPos, yPos;
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
    public int health = 100; //enemy die when health = 0
    private float hpPosY;
    //Attack bar
    TextEntity atkName;
    Paint atkPaint = new Paint();
    public int charge = 0; //enemy attack when charge = 100
    private float chargeTime = 0.3f; //time required to get +1 charge
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
        //For name
        enemyNameText = TextEntity.Create(xPos, yPos - 150, 0, 0, 0, 70, Paint.Align.CENTER);
        enemyNameText.text = "Home";
        //For background of bitmap
        bgPaint.setARGB(255, 0, 0, 0);
        bgPaint.setStyle(Paint.Style.FILL);
        //For bitmap
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = true;
        //Load all bmps
//        bmps[0] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.enemy1, bfo);
//        bmps[1] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.enemy2, bfo);
//        bmps[2] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.enemy3, bfo);
//        bmps[3] = BitmapFactory.decodeResource(_view.getResources(), R.drawable.enemy4, bfo);
//        scaledBmps[0] = Bitmap.createScaledBitmap(bmps[0], width, width, true);
//        scaledBmps[1] = Bitmap.createScaledBitmap(bmps[1], width, width, true);
//        scaledBmps[2] = Bitmap.createScaledBitmap(bmps[2], width, width, true);
//        scaledBmps[3] = Bitmap.createScaledBitmap(bmps[3], width, width, true);
        //For health bar
        hpName = TextEntity.Create(xPos, yPos + 200, 0, 0, 0, 70, Paint.Align.CENTER);
        hpName.text = "Health";
        hpPaint.setARGB(255, 255, 0, 0);
        hpPosY = yPos + 250;
        //For attack bar
        atkName = TextEntity.Create(xPos, yPos + 370, 0, 0, 0, 70, Paint.Align.CENTER);
        atkName.text = "Attack";
        atkPaint.setARGB(255, 0, 0, 255);
        atkPosY = yPos + 430;
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
            System.out.println("Charge: " + charge);
        }
    }
    public void Render(Canvas _canvas) {
        //Draw background
        _canvas.drawRect(xPos - width * 0.5f, yPos - width * 0.5f,
                        xPos + width * 0.5f, yPos + width * 0.5f, bgPaint);
        //Draw bitmap (if available)
        if (scaledBmps[enemyLevel] != null)
        {
            _canvas.drawBitmap(scaledBmps[enemyLevel], xPos - scaledBmps[enemyLevel].getWidth() * 0.5f,
                    yPos - scaledBmps[enemyLevel].getHeight() * 0.5f, null);
        }
        //Draw health bar
        _canvas.drawRect(xPos - width * 0.5f, hpPosY - 15,
                        xPos - width * 0.5f + width * (health / 100.f), hpPosY + 15, hpPaint);
        _canvas.drawRect(xPos - width * 0.5f - outlineWidth * 0.5f, hpPosY - 15 - outlineWidth * 0.5f,
                        xPos + width * 0.5f + outlineWidth * 0.5f, hpPosY + 15 + outlineWidth * 0.5f,
                             outlinePaint);
        //Draw attack bar
        _canvas.drawRect(xPos - width * 0.5f, atkPosY - 15,
                xPos - width * 0.5f + width * (charge / 100.f), atkPosY + 15, atkPaint);
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
        return ENTITY_TYPE.ENT_TEXT;
    }

    public static EnemyEntity Create(float _xPos, float _yPos, int _width)
    {
        EnemyEntity result = new EnemyEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TEXT);
        result.xPos = _xPos;
        result.yPos = _yPos;
        result.width = _width;
        return result;
    }

    public void ChangeLevel(int newLevel)
    {
        enemyLevel = newLevel;
        switch (newLevel) //change other things here e.g. hp, charge time
        {
            case 1:
                enemyNameText.text = "Home";
                break;
            case 2:
                enemyNameText.text = "Home";
                break;
            case 3:
                enemyNameText.text = "Home";
                break;
            case 4:
                enemyNameText.text = "Home";
                break;
        }
    }
}


