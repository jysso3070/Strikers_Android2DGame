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
    private final int height;
    private final int width;
    private float x;
    private float y;
    private long lastFire;
    private int gwBottom;
    private int gwTop;
    private int gwLeft;
    private int gwRight;

    public MyPlane(float x, float y){
        GameWorld gw = GameWorld.get();
        fab = new FrameAnimationBitmap(R.mipmap.jet2, 3, 6);
        halfSize = fab.getHeight() / 2;
        this.height = fab.getHeight();
        this.width = fab.getWidth();
        this.x = x;
        this.y = y;
        gwTop = gw.getTop();
        gwBottom = gw.getBottom();
        gwLeft = gw.getLeft();
        gwRight = gw.getRight();
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

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
        if(y < gwTop + this.height/2){ this.y = gwTop + this.width;}
        if(y > gwBottom - height/2 ){this.y = gwBottom - height/2;}
        if(x < gwLeft + this.width/2){this.x = gwLeft + this.width/2;}
        if(x > gwRight - this.width/2){this.x = gwRight - this.width/2;}
    }
}
