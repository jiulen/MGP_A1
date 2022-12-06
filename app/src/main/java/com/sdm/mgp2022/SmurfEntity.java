package com.sdm.mgp2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

import java.util.Random;

public class SmurfEntity implements EntityBase {

    private boolean isDone = false;
    private boolean isInit = false;

    float xPos, yPos = 0;

    private Bitmap bmp = null;
    //WE going to use the Sprite class we created
    private Sprite spritesheet = null;

    private boolean hasTouched = false;

    int ScreenHeight, ScreenWidth;
    int currScore = 0;

    public boolean IsDone() {
        return isDone;
    }

    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    public void Init(SurfaceView _view) {
        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.smurf_sprite);
//        // or spritesheet = new Sprite(bmp, 4, 4, 16);
//        spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(), R.drawable.smurf_sprite),
//                4, 4, 16);

        spritesheet = new Sprite(ResourceManager.Instance.GetBitmap(R.drawable.smurf_sprite), 4, 4, 16);

        Random ranGen = new Random();
        xPos = ranGen.nextFloat() * _view.getWidth();
        yPos = ranGen.nextFloat() * _view.getHeight();

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        isInit = true;
    }

    public void Update(float _dt){
        if (spritesheet == null)
            System.out.println("men2");
        spritesheet.Update(_dt);

        // Check collision using TouchManager , refer to slides for Week 6 page 7
        if (TouchManager.Instance.HasTouch())
        {
            //Check collision of finger with image
            if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0.0f,
                    xPos, yPos, spritesheet.GetWidth() * 0.5f) || hasTouched)
            {
                hasTouched = true;
                xPos = TouchManager.Instance.GetPosX();
                yPos = TouchManager.Instance.GetPosY();
            }
        }

        //Example: Lose case
        //Lose = true
        //StateManager.Instance.ChangeState("EndScreen");
    }

    public void Render(Canvas _canvas)
    {
        //spritesheet.Render(_canvas, (int)xPos, (int)yPos);

        float lifeTime = 30.f;
        float scaleFactor = 0.5f + Math.abs((float)Math.sin(lifeTime));
        //Render with your own transformation matrix
        Matrix transform = new Matrix();
        transform.postTranslate(xPos, yPos);
        transform.postScale(scaleFactor, scaleFactor);
        transform.postRotate((float)Math.toDegrees(lifeTime));
        _canvas.drawBitmap(bmp,transform,null);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE); // FILL_AND_STROKE
        _canvas.drawRect(ScreenWidth/20 +5,ScreenHeight/25 - 5,
                4 * ScreenWidth/20, 2 * ScreenHeight/25, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        _canvas.drawRect(ScreenWidth/20 +8,ScreenHeight/25,
                4 * ScreenWidth/20 + currScore, 2 * ScreenHeight/25 -5 , paint);
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

    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == "SampleEntity")
        {
            //SetIsDone (true)
            //Play an audio
        }
    }
}

