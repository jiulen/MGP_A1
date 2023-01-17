package com.sdm.mgp2022;

import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AudioManager {
    public final static AudioManager Instance = new AudioManager();
    public float maxVolume = 100;
    public float volume = 100;

    private SurfaceView view = null;

    private HashMap<Integer, MediaPlayer> AudioManager = new HashMap<Integer, MediaPlayer>();
    private ArrayList<MediaPlayer> pauseArray = new ArrayList<MediaPlayer>();

    AudioManager() {};

    public void Init(SurfaceView surfaceView) {
        view = surfaceView;
        Release();
        //init volume from sharedPref
        volume = GameSystem.Instance.GetIntFromSave("Volume");
    }

    private void Release() {
        for(Map.Entry<Integer, MediaPlayer> entry: AudioManager.entrySet()) {
            entry.getValue().stop();
            entry.getValue().reset();
            entry.getValue().release();
        }
        AudioManager.clear();
    }

    public void PlayAudio(int _id, boolean loop) {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            curr.seekTo(0);
            curr.setVolume(volume, volume);
            curr.start();
            curr.setLooping(loop);
        }
        else {
            MediaPlayer curr = MediaPlayer.create(view.getContext(), _id);
            AudioManager.put(_id,curr);
            curr.start();
            curr.setVolume(volume, volume);
            curr.setLooping(loop);
        }
    }

    public void PauseAudio(int _id)
    {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            if (curr.isPlaying())
                curr.pause();
        }
    }

    public void RestartAudio(int _id)
    {
        if (AudioManager.containsKey(_id))
        {
            MediaPlayer curr = AudioManager.get(_id);
            curr.start();
            curr.seekTo(0);
        }
    }

    public void ResumeAudio(int _id)
    {
        if (AudioManager.containsKey(_id))
        {
            MediaPlayer curr = AudioManager.get(_id);
            curr.start();
        }
    }


    public void StopAudio(int _id) {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            curr.stop();
            curr.reset();
            curr.release();
            AudioManager.remove(_id);
        }
    }

    public void ChangeVolume(int _volume)
    {
        volume = _volume;
        for(Map.Entry<Integer, MediaPlayer> entry: AudioManager.entrySet()) {
            entry.getValue().setVolume(_volume, _volume);
        }
    }

    public void StopAllAudio()
    {
        for(Map.Entry<Integer, MediaPlayer> entry: AudioManager.entrySet()) {
            MediaPlayer curr = entry.getValue();
            curr.stop();
            curr.reset();
            curr.release();

            AudioManager.remove(entry.getKey());
        }
    }

    public void PauseAllNoLoopAudio() //Doesnt stop looping audio (if need stop loop audio use StopAudio)
    {
        for(Map.Entry<Integer, MediaPlayer> entry: AudioManager.entrySet()) {
            MediaPlayer curr = entry.getValue();
            if (!curr.isLooping() && curr.isPlaying())
            {
                curr.pause();
                pauseArray.add(curr);
            }
        }
    }

    public void ResumeAllAudio() //Resume all audio
    {
        for (MediaPlayer mediaPlayer : pauseArray)
        {
            mediaPlayer.start();
        }

        pauseArray.clear();
    }

    public void ClearPauseArray() //Used with StopAllNoLoopAudio() when switch scenes
    {
        pauseArray.clear();
    }

    public boolean getIsPlaying(int _id)
    {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            return curr.isPlaying();
        }
        return false;
    }
}
