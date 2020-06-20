package kr.kpu.game.Andgp2015184024.termproject.res.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

import kr.kpu.game.Andgp2015184024.termproject.R;


public class BGSound {
    private Context context;
    private MediaPlayer mp;

    public static BGSound get(){
        if(singleton == null){
            singleton = new BGSound();
        }
        return singleton;
    }
    private static BGSound singleton;

    public void init(Context context){
        this.context = context;
        mp = MediaPlayer.create(this.context, R.raw.play_sound);
        mp.setVolume(1.f, 1.f);
    }

    public void playBGM() {
        mp.setLooping(true);
        mp.start();
    }

    public void stop() {
        mp.stop();
    }
}
