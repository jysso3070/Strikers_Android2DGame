package kr.kpu.game.Andgp2015184024.termproject.util;

import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;

public class IndexTimer {
    private static final String TAG = IndexTimer.class.getSimpleName();
    private final int count;
    private final int fps;
    private long time;

    public IndexTimer(int count, int framesPerSecond) {
        this.count = count;
        this.fps = framesPerSecond;
        this.time = GameWorld.get().getCurrentTimeNanos();
    }

    public int getIndex(){
        long elapsed =GameWorld.get().getCurrentTimeNanos() - time;
        int index = (int)((elapsed * fps + 500000000) / 1000000000 );
//        Log.d(TAG, "e * f=" + (elapsed * fps) + "index="+index);
        return index % count;
    }

    public void reset() {
        this.time = GameWorld.get().getCurrentTimeNanos();
    }

    public boolean done() {
        long elapsed = GameWorld.get().getCurrentTimeNanos() - time;
        int index = (int)((elapsed * fps + 500000000) / 1000000000 );
        return index >= count;
    }
}
