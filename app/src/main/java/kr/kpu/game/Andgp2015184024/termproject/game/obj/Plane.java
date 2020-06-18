package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.BoxCollidable;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;
import kr.kpu.game.Andgp2015184024.termproject.util.CollisionHelper;

public class Plane implements GameObject, BoxCollidable {
    public static final int BULLET_FIRE_INTERVAL_NSEC = 100_000_000;
    private static final String TAG = Plane.class.getSimpleName();
    private static Bitmap bitmap;  // final은 const 같은 느낌, 스태틱으로 해서 객체가 초기화 될때마다 바뀌지 않도록
    private final float dx;
    private final float dy;
    //private final Matrix matrix;
    private static int halfSize;
    private float x;
    private float y;
    private long lastFire;
    private Joystick joystick;
    private int SPEED = 100;

    public Plane(float x, float y, float dx, float dy){
        GameWorld gw = GameWorld.get();
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(gw.getResources(), R.mipmap.plane_240);
            halfSize = bitmap.getHeight() / 2;
        }
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
//        this.matrix = new Matrix();
//        matrix.preTranslate(x - halfSize, y - halfSize);
    }

    public void update(){
//        x += dx;
//        y += dy;
        //matrix.postRotate(5.0f, x, y);
//        GameWorld gw = GameWorld.get();
        MainWorld gw = MainWorld.get();
        long now = gw.getCurrentTimeNanos();
        long elapsed = now - lastFire;
        if(elapsed > BULLET_FIRE_INTERVAL_NSEC){
            fire();
            lastFire = now;
        }

        int xdir = joystick.getHorzDirection();
        x += xdir * SPEED * gw.getTimeDiffInSecond();
        if(x < 0){
            x = 0;
        }else if(x > gw.getRight()){
            x = gw.getRight();
        }

        ArrayList<GameObject> enemies = gw.objectsAt(MainWorld.Layer.enemy);
        for(GameObject e : enemies){
            if(! (e instanceof Enemy)){
                Log.e(TAG, "object at Layer.enemy is: " + e);
                continue;
            }
            Enemy enemy = (Enemy) e;
            if( CollisionHelper.collides(enemy, this)){
                gw.endGame();

                break;
            }
        }
    }


    private void fire() {
        Bullet bullet = Bullet.get(x, y - halfSize);
        MainWorld.get().add(MainWorld.Layer.missile, bullet);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x - halfSize, y - halfSize, null);
    }

//    public void head(float x, float y) {
//        this.x = x;
//    }

    @Override
    public void getBox(RectF rect) {
        int hw = bitmap.getWidth() / 2;
        int hh = bitmap.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }
}
