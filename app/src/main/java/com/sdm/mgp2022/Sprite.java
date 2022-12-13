package com.sdm.mgp2022; //by jiulen

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

// For sprite Animation
public class Sprite {
    private int row = 0;
    private int col = 0;
    private int width = 0;
    private int height = 0;

    private Bitmap bmp = null;

    public int currentFrame = 0;
    public int startFrame = 0;
    public int endFrame = 0;

    private float timePerFrame = 0.0f;
    private float timeAcc = 0.0f;

    public boolean canLoop = true;

    // method to call in the entity when loading a sprite for animation
    public Sprite (Bitmap _bmp, int _row, int _col, int _fps){
        //fps = how fast the animation to run equivalent to time
        bmp = _bmp;
        row = _row;
        col = _col;

        width = bmp.getWidth() / _col;
        height = bmp.getHeight() / _row; // Find equal width and height size for every frame for the sprite image

        timePerFrame = 1.0f / (float)_fps;

        endFrame = _col * _row;
    }

    public void Render (Canvas _canvas, int _x, int _y){
        int frameX = currentFrame % col;
        int frameY = currentFrame /col;
        int srcX  = frameX * width;
        int srcY = frameY * height;

        _x -= 0.5f * width;
        _y -= 0.5f * height;

        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(_x, _y, _x + width, _y + height);
        _canvas.drawBitmap(bmp, src, dst, null);

    }

    public void Update(float _dt){
        timeAcc += _dt;
        if (timeAcc > timePerFrame){
            if (canLoop)
            {
                if(currentFrame < endFrame)
                    ++currentFrame;
            }
            else
            {
                if(currentFrame < endFrame - 1)
                    ++currentFrame;
            }
            if(currentFrame >= endFrame && canLoop)
                currentFrame = startFrame;

            timeAcc = 0.0f;
        }
    }


    public void SetAnimationFrames(int _start, int _end){
        timeAcc = 0.0f;
        currentFrame = _start;
        startFrame = _start;
        endFrame = _end;
    }

    public int GetHeight(){
        return height;
    }

    public int GetWidth(){
        return width;
    }
}