package kr.jysso3070.game.Andgp.strikers5491.res.sound;

import android.content.Context;
import android.media.MediaPlayer;

import kr.jysso3070.game.Andgp.strikers5491.R;


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
