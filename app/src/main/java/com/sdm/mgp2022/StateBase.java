package com.sdm.mgp2022;

// Created by TanSiewLan2021

// Create an interface "StateBase". That is what a state will need.

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface StateBase {
    String GetName();

    void OnEnter(SurfaceView _view);
    void OnExit();
    void Render(Canvas _canvas);
    void Update(float _dt);
}
