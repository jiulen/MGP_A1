package com.sdm.mgp2022;

// Created by TanSiewLan2022

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

import java.util.HashMap;

public class ResourceManager {
    public final static ResourceManager Instance = new ResourceManager();

    private Resources res = null;
    private HashMap<Integer, Bitmap> resMap = new HashMap<Integer, Bitmap>();

    private ResourceManager() {}

    public void Init(SurfaceView _view)
    {
        res = _view.getResources();
    }

    public Bitmap GetBitmap(int _id)
    {
        if (resMap.containsKey(_id))
            return resMap.get(_id);

        Bitmap result = BitmapFactory.decodeResource(res, _id);
        resMap.put(_id, result);
        return result;
    }
}
