package com.sdm.mgp2022;

// Created by TanSiewLan2021

// StateManager to deal with which state is current or next.

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.HashMap;

public class StateManager {
    // Singleton Instance
    public static final StateManager Instance = new StateManager();

    // Container to store all our states!
    private HashMap<String, StateBase> stateMap = new HashMap<String, StateBase>();
    private StateBase currState = null;
    private StateBase nextState = null;

    private SurfaceView view = null;

    // This is the protected constructor for singleton
    private StateManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    void AddState(StateBase _newState)
    {
        // Add the state into the state map
        stateMap.put(_newState.GetName(), _newState);
    }

    void ChangeState(String _nextState)
    {
        // Try to assign the next state
        nextState = stateMap.get(_nextState);

        // If no next state, we assign back to current state
        if (nextState == null)
            nextState = currState;

        // Extra to add if possible : throw some warning if next state function fails
    }

    void Update(float _dt)
    {
        if (nextState != currState)
        {
            // We need to change state
            currState.OnExit();
            nextState.OnEnter(view);
            currState = nextState;
        }

        if (currState == null)
            return;

        currState.Update(_dt);
    }

    void Render(Canvas _canvas)
    {
        currState.Render(_canvas);
    }

    String GetCurrentState()
    {
        if (currState == null)
            return "INVALID";

        return currState.GetName();
    }

    void Start(String _newCurrent)
    {
        // Make sure only can call once at the start
        if (currState != null || nextState != null)
            return;

        currState = stateMap.get(_newCurrent);
        if (currState != null)
        {
            currState.OnEnter(view);
            nextState = currState;
        }
    }

    public void Clean(){
        stateMap.clear();
    }
}
