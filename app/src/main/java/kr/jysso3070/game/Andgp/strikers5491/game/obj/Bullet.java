package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.BoxCollidable;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.Recyclable;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;
import kr.jysso3070.game.Andgp.strikers5491.util.CollisionHelper;

public class Bullet implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Bullet.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 6;
    public static final int FRAME_COUNT = 13;
    private static final float BULLET_SPEED = 1500;
    private FrameAnimationBitmap fab;
    private int halfSize; // final은 const 같은 느낌, 스태틱으로 해서 객체가 초기화 될때마다 바뀌지 않도록
    private float x;
    private float y;
    private int power;


    private Bullet(){

    }
    public static Bullet get(float x, float y){
//        GameWorld gw = GameWorld.get();
        MainWorld gw = MainWorld.get();
        Resources res = gw.getResources();
        Bullet b = (Bullet) gw.getRecyclePool().get(Bullet.class);
        if(b == null){
            b = new Bullet();
        }
        b.fab = new FrameAnimationBitmap(R.mipmap.metal_slug_missile, FRAME_PER_SECOND, FRAME_COUNT);
        b.halfSize = b.fab.getHeight() / 2;
        b.x = x;
        b.y = y;
        b.power = 100;
        return b;
    }

    public void update(){
//        GameWorld gw = GameWorld.get();
        MainWorld gw = MainWorld.get();
//        x += dx;
//        if(dx > 0 && x > gw.getRight() - halfSize || dx < 0 && x < gw.getLeft() + halfSize){
//            dx *= -1;
//        }
        y -= BULLET_SPEED * gw.getTimeDiffInSecond();

        boolean toBeDeleted = false;

        ArrayList<GameObject>enemies = gw.objectsAt(MainWorld.Layer.enemy);
        for(GameObject e : enemies){
            if(! (e instanceof Enemy)){
                Log.e(TAG, "object at Layer.enemy is: " + e);
                continue;
            }
            Enemy enemy = (Enemy) e;
            if( CollisionHelper.collides(enemy, this)){
                enemy.decreaseLife(this.power);
                toBeDeleted = true;
                break;
            }
        }

        if(!toBeDeleted){
            if(y < gw.getTop() - halfSize){
                toBeDeleted = true;
            }
        }
        if(toBeDeleted){
            gw.remove(this);
        }
//        Log.d(TAG, "Index = "+index);
    }
    public void draw(Canvas canvas){
        fab.draw(canvas, x, y);
    }

    @Override
    public void getBox(RectF rect) {
        int hw = fab.getWidth() / 2;
        int hh = fab.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }

    @Override
    public void recycle() {

    }
}
