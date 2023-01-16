package com.sdm.mgp2022;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

import java.util.HashSet;
import java.util.Set;

// Created by TanSiewLan2021

public class GameSystem {
    public final static GameSystem Instance = new GameSystem();
    public static final String SHARED_PREF_ID = "GameSaveFile";

    // Game stuff
    private boolean isPaused = false;
    SharedPreferences sharedPref = null;
    SharedPreferences.Editor editor = null;

    // Singleton Pattern : Blocks others from creating
    private GameSystem()
    {
    }

    public void Update(float _deltaTime)
    {
    }

    public void Init(SurfaceView _view)
    {
        // Get our shared preferences (Save file)
        sharedPref = Splashpage.Instance.getSharedPreferences(SHARED_PREF_ID,0);

        // We will add all of our states into the state manager here!
        StateManager.Instance.AddState(new Mainmenu());
        StateManager.Instance.AddState(new MainGameSceneState());
    }

    public void SetIsPaused(boolean _newIsPaused)
    {
        isPaused = _newIsPaused;
    }

    public boolean GetIsPaused()
    {
        return isPaused;
    }

    public void ResetSave()
    {
        if (editor != null)
            return;

        editor = sharedPref.edit();
        editor.clear().commit();
    }

    public void SaveEditBegin()
    {
        // Safety check, only allow if not already editing
        if (editor != null)
            return;

        // Start the editing
        editor = sharedPref.edit();
    }

    public void SaveEditEnd()
    {
        // Check if have editor
        if (editor == null)
            return;

        editor.commit();
        editor = null;
    }

    public void SetIntInSave(String _key, int _value)
    {
        if (editor == null)
            return;

        editor.putInt(_key, _value);
    }

    public void SetBoolInSave(String _key, boolean _value)
    {
        if (editor == null)
            return;

        editor.putBoolean(_key, _value);
    }

    public void SetStringSetInSave(String _key, Set<String> _value)
    {
        if (editor == null)
            return;

        editor.putStringSet(_key, _value);
    }

    public int GetIntFromSave(String _key)
    {
        return sharedPref.getInt(_key, 10);
    }

    public boolean GetBoolFromSave(String _key) { return sharedPref.getBoolean(_key, false); }

    public Set<String> GetStringSetFromSave(String _key) {
        Set<String> returnSet = new HashSet<String>(sharedPref.getStringSet(_key, new HashSet<String>()));
        return returnSet; //returns a copy of string set
    }

    public boolean CheckKeyInSave(String _key) {
        return sharedPref.contains(_key);
    }
}
