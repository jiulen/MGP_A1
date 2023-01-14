package com.sdm.mgp2022;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;
//public class AudioManager {
//    public final static AudioManager Instance = new AudioManager();
//
//    private Resources res = null;
//    private SurfaceView view = null;
//    private HashMap<Integer, MediaPlayer> audioMap = new HashMap<Integer, MediaPlayer>();
//
//    private AudioManager()
//    {
//
//    }
//
//    public void Init(SurfaceView _view)
//    {
//        view = _view;
//        res = _view.getResources();
//    }
//
//    public void PlayAudio(int _id)
//    {
//        if (audioMap.containsKey(_id))
//        {
//            audioMap.get(_id).reset();
//            audioMap.get(_id).start();
//        }
//
//        // Load the audio
//        MediaPlayer newAudio = MediaPlayer.create(view.getContext(), _id);
//        audioMap.put(_id, newAudio);
//        newAudio.start();
//    }
//
//    public boolean IsPlaying(int _id)
//    {
//        if (!audioMap.containsKey(_id))
//            return false;
//
//        return audioMap.get(_id).isPlaying();
//    }
//}

public class AudioManager {
    public final static AudioManager Instance = new AudioManager();

    private SurfaceView view = null;

    private HashMap<Integer, MediaPlayer> AudioManager = new HashMap<Integer, MediaPlayer>();
    AudioManager() {};

    public void Init(SurfaceView surfaceView) {
        view = surfaceView;
        Release();
    }

    private void Release() {
        for(Map.Entry<Integer, MediaPlayer> entry: AudioManager.entrySet()) {
            entry.getValue().stop();
            entry.getValue().reset();
            entry.getValue().release();
        }
        AudioManager.clear();
    }

    public void PlayAudio(int _id, float _volume, boolean loop) {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            curr.seekTo(0);
            curr.setVolume(_volume, _volume);
            curr.start();
            curr.setLooping(loop);
        }
        else {
            MediaPlayer curr = MediaPlayer.create(view.getContext(), _id);
            AudioManager.put(_id,curr);
            curr.start();
            curr.setLooping(loop);
        }
    }


    public void StopAudio(int _id) {
        if (AudioManager.containsKey(_id)) {
            MediaPlayer curr = AudioManager.get(_id);
            curr.setVolume(0, 0);
            curr.stop();
            curr.reset();
            curr.release();
            AudioManager.remove(_id);
        }
    }
}
