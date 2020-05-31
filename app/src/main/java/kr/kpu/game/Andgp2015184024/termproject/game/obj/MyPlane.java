package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Canvas;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;

public class MyPlane implements GameObject {

    private static final int BULLET_FIRE_INTERVAL_NSEC = 100_000_000;
    private final FrameAnimationBitmap fab;
    private final int halfSize;
    private final float x;
    private final float y;
    private long lastFire;

    public MyPlane(float x, float y){
        GameWorld gw = GameWorld.get();
        fab = new FrameAnimationBitmap(R.mipmap.jet2, 3, 6);
        halfSize = fab.getHeight() / 2;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        MainWorld gw = MainWorld.get();
        long now = gw.getCurrentTimeNanos();
        long elapsed = now - lastFire;
        if(elapsed > BULLET_FIRE_INTERVAL_NSEC){
            fire();
            lastFire = now;
        }

    }

    private void fire() {
        MyMissile missile = MyMissile.get(x, y - halfSize);
        MainWorld.get().add(MainWorld.Layer.missile, missile);
    }


    @Override
    public void draw(Canvas canvas) {
        fab.draw(canvas, x, y);

    }
}
