package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;

public class MyMissile implements GameObject {
    private static final float BULLET_SPEED = 1500;
    private FrameAnimationBitmap fab;
    private int halfSize;
    private float x;
    private float y;
    private int power;

    private MyMissile(){

    }
    public static MyMissile get(float x, float y){
        MainWorld gw = MainWorld.get();
        Resources res = gw.getResources();
        MyMissile missile = (MyMissile)gw.getRecyclePool().get(MyMissile.class);
        if(missile == null){
            missile = new MyMissile();
        }
        missile.fab = new FrameAnimationBitmap(R.mipmap.jetfire, 1, 1);
        missile.halfSize = missile.fab.getHeight() / 2;
        missile.x = x;
        missile.y = y;
        missile.power = 100;
        return missile;
    }



    @Override
    public void update() {
        MainWorld gw = MainWorld.get();
        y -= BULLET_SPEED * gw.getTimeDiffInSecond();

        boolean toBeDeleted = false;

        if(!toBeDeleted){
            if(y < gw.getTop() - halfSize){
                toBeDeleted = true;
            }
        }
        if(toBeDeleted){
            gw.remove(this);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        fab.draw(canvas, x, y);
    }

    public void getBox(RectF rect) {
        int hw = fab.getWidth() / 2;
        int hh = fab.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }
}
