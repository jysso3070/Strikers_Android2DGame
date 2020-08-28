package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.BoxCollidable;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.Recyclable;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.SoundEffects;
import kr.jysso3070.game.Andgp.strikers5491.util.CollisionHelper;

public class MyMissile implements GameObject, BoxCollidable, Recyclable {
    private static final float BULLET_SPEED = 1500;
    private static final String TAG = MyMissile.class.getSimpleName();
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

        ArrayList<GameObject> enemies = gw.objectsAt(MainWorld.Layer.enemy);
        for(GameObject e : enemies){
            if(! (e instanceof Enemy)){
//                Log.e(TAG, "object at Layer.enemy is: " + e);
                continue;
            }
            Enemy enemy = (Enemy) e;
            if( CollisionHelper.collides(enemy, this)){
                enemy.decreaseLife(this.power);
                SoundEffects.get().play(R.raw.enemy_hit);
                gw.add(MainWorld.Layer.effect, new AttackSprite(enemy.getX(), enemy.getY()));
                toBeDeleted = true;
                break;
            }
        }

        // Boss와 충돌처리
        ArrayList<GameObject> bosses = gw.objectsAt(MainWorld.Layer.enemyBoss);
        for (GameObject b : bosses) {
            Boss boss = (Boss) b;
            if (CollisionHelper.collides(boss, this)) {
                boss.decreaseLife(this.power);
                gw.add(MainWorld.Layer.effect, new AttackSprite(x, y));
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

    @Override
    public void recycle() {

    }
}
