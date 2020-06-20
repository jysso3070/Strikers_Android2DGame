package kr.kpu.game.Andgp2015184024.termproject.res.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import kr.kpu.game.Andgp2015184024.termproject.R;


public class SoundEffects {
    private Context context;
    private SoundPool soundPool;

    public static SoundEffects get(){
        if(singleton == null){
            singleton = new SoundEffects();
        }
        return singleton;
    }
    private static SoundEffects singleton;

    public void init(Context context){
        this.context = context;
        AudioAttributes audioAttribute = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioAttribute = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            this.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttribute)
                    .setMaxStreams(3)
                    .build();
        } else{
            this.soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private static final int[] SOUND_IDS = {
            R.raw.hadouken, R.raw.play_sound, R.raw.enemy_bomb,
    };
    private HashMap<Integer, Integer> soundIdMap = new HashMap<>();
    public void ladAll(){
        for(int resId : SOUND_IDS){
            int soundId = soundPool.load(context, resId, 1);
            soundIdMap.put(resId, soundId);
        }
    }
    public int play(int resId){
        int soundId = soundIdMap.get(resId);
        int streamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        return streamId;
    }
}
