package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.BoxCollidable;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.sensor.GyroSensor;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;
import kr.kpu.game.Andgp2015184024.termproject.util.CollisionHelper;

public class MyPlane implements GameObject, BoxCollidable {

    private static final int BULLET_FIRE_INTERVAL_NSEC = 100_000_000;
    private static final String TAG = MyPlane.class.getSimpleName();
    private final FrameAnimationBitmap fab;
    private final int halfSize;
    private final int height;
    private final int width;
    private final FrameAnimationBitmap fab_hit;
    private GyroSensor gyroSensor;
    private float x;
    private float y;
    private long lastFire;
    private int gwBottom;
    private int gwTop;
    private int gwLeft;
    private int gwRight;
    private Joystick joystick;
    private static final int M_SPEED = 500;
    private int MaxHP;
    private int currentHP;
    private boolean collisionCooltimeFlag;
    private long cooltimeStartTime;
    private final int collisionCooltime = 2;

    private boolean gyroOn = false;
    private static final int G_SPEED = 50;
    private float dx;
    private float dy;
    private Double stickAngle;
    private boolean JoystickDown;


    public MyPlane(float x, float y){
        GameWorld gw = GameWorld.get();
        this.MaxHP = 4;
        this.currentHP = MaxHP;
        fab = new FrameAnimationBitmap(R.mipmap.jet2, 3, 6);
        fab_hit = new FrameAnimationBitmap(R.mipmap.jet2_hit2, 3, 6);
        halfSize = fab.getHeight() / 2;
        this.height = fab.getHeight();
        this.width = fab.getWidth();
        this.x = x;
        this.y = y;
        gwTop = gw.getTop();
        gwBottom = gw.getBottom();
        gwLeft = gw.getLeft();
        gwRight = gw.getRight();
        collisionCooltimeFlag = false;

        if(gyroOn){
            gyroSensor = GyroSensor.get();
            gyroSensor.reset();
        }
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

    @Override
    public void update() {
        MainWorld gw = MainWorld.get();
        long now = gw.getCurrentTimeNanos();
        long elapsed = now - lastFire;
        if(elapsed > BULLET_FIRE_INTERVAL_NSEC) {
            fire();
            lastFire = now;
        }
//        Log.e(TAG, "flag: " + collisionCooltimeFlag);

        if(gyroOn){
            float seconds = GameWorld.get().getTimeDiffInSecond();
            this.dx = gyroSensor.getPitchDegree() * G_SPEED;
            this.dy = gyroSensor.getRollDegree() * G_SPEED;
            this.x += dx * seconds;
            this.y += dy * seconds;
        }
        this.stickAngle = joystick.getAngle();
        this.JoystickDown = joystick.getJoystickDown();
        moveByJoystick();

        if(y < gwTop + this.height/2){ this.y = gwTop + this.width;}
        if(y > gwBottom - height/2 ){this.y = gwBottom - height/2;}
        if(x < gwLeft + this.width/2){this.x = gwLeft + this.width/2;}
        if(x > gwRight - this.width/2){this.x = gwRight - this.width/2;}

        if(collisionCooltimeFlag == true){
            checkCollisionCooltime();
        }
        if(collisionCooltimeFlag == false) {
            ArrayList<GameObject> enemies = gw.objectsAt(MainWorld.Layer.enemy); // 적유닛 충돌처리
            for (GameObject e : enemies) {
                if (!(e instanceof Enemy)) {
                    Log.e(TAG, "object at Layer.enemy is: " + e);
                    continue;
                }
                Enemy enemy = (Enemy) e;
                if (CollisionHelper.collides(enemy, this)) {
                    decreaseMyHp();
                    gw.decreaseHpObject();
                    gw.add(MainWorld.Layer.effect, new AttackSprite(x, y));
                    cooltimeStartTime = gw.getCurrentTimeNanos() / 1_000_000_000;
                    collisionCooltimeFlag = true;
                    Log.e(TAG, "object collision with enemy: " + e);
                    break;
                }
            }
        }
        if(collisionCooltimeFlag == false) {
            ArrayList<GameObject> enemyMissiles = gw.objectsAt(MainWorld.Layer.enemyMissile); //적 미사일 충돌처리
            for (GameObject em : enemyMissiles) {
                if (!(em instanceof EnemyMissile)) {
                    Log.e(TAG, "object at Layer.enemyMissile is: " + em);
                    continue;
                }
                EnemyMissile enemyMissile = (EnemyMissile) em;
                if (CollisionHelper.collides(enemyMissile, this)) {
//                gw.endGame();
                    Log.e(TAG, "object collision with enemyMissile: " + em);
                    decreaseMyHp();
                    gw.decreaseHpObject();
                    gw.add(MainWorld.Layer.effect, new AttackSprite(x, y));
                    cooltimeStartTime = gw.getCurrentTimeNanos() / 1_000_000_000;
                    collisionCooltimeFlag = true;
                    break;
                }
            }
        }
    }

    private void decreaseMyHp() {
        this.currentHP -= 1;

        if(this.currentHP < 1){
            MainWorld.get().endGame();
        }
    }

    private void checkCollisionCooltime() {
        MainWorld mw = MainWorld.get();
        long nowTime = mw.getCurrentTimeNanos() / 1_000_000_000;
        if(nowTime - cooltimeStartTime > collisionCooltime){
            collisionCooltimeFlag = false;
        }

    }

    private void moveByJoystick() {
        if(!this.JoystickDown){return;}
        float seconds = GameWorld.get().getTimeDiffInSecond();
        if(stickAngle >= -112.5 && stickAngle < -67.5) { // up
            this.y -= M_SPEED * seconds;
        } else if(stickAngle >= -67.5 && stickAngle < -22.5){ // up right
            this.x += (M_SPEED * seconds) / Math.sqrt(2);
            this.y -= (M_SPEED * seconds) / Math.sqrt(2);
        } else if(stickAngle >= -22.5 && stickAngle < 22.5){ // right
            this.x += M_SPEED * seconds;
        } else if(stickAngle >= 22.5 && stickAngle < 67.5){ // down right
            this.x += (M_SPEED * seconds) / Math.sqrt(2);
            this.y += (M_SPEED * seconds) / Math.sqrt(2);
        } else if(stickAngle >= 67.5 && stickAngle < 112.5){ // down
            this.y += M_SPEED * seconds;
        } else if(stickAngle >= 112.5 && stickAngle< 157.5){ // down left
            this.x -= (M_SPEED * seconds) / Math.sqrt(2);
            this.y += (M_SPEED * seconds) / Math.sqrt(2);
        } else if( (stickAngle >= 157.5 && stickAngle <= 180) || (stickAngle >= -180 && stickAngle < -157.5) ){ // left
            this.x -= M_SPEED * seconds;
        } else if(stickAngle >= -157.5 && stickAngle < -112.5){ // up left
            this.x -= (M_SPEED * seconds) / Math.sqrt(2);
            this.y -= (M_SPEED * seconds) / Math.sqrt(2);
        }
    }

    private void fire() {
        MyMissile missile = MyMissile.get(x, y - halfSize);
        MainWorld.get().add(MainWorld.Layer.missile, missile);
    }


    @Override
    public void draw(Canvas canvas) {
        if(collisionCooltimeFlag){
            fab_hit.draw(canvas, x, y);
        }
        else{
            fab.draw(canvas, x, y);
        }

    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
        if(y < gwTop + this.height/2){ this.y = gwTop + this.width;}
        if(y > gwBottom - height/2 ){this.y = gwBottom - height/2;}
        if(x < gwLeft + this.width/2){this.x = gwLeft + this.width/2;}
        if(x > gwRight - this.width/2){this.x = gwRight - this.width/2;}
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    public void setGyro(boolean gyroOn) {
        if(gyroOn){
            this.gyroOn = true;
            gyroSensor = GyroSensor.get();
            gyroSensor.reset();
        }
    }

    @Override
    public void getBox(RectF rect) {
        int hw = fab.getWidth() / 6;
        int hh = fab.getHeight() / 2;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - 2*(hh/3);
        rect.bottom = y - (hh/4);
    }

    public int getMaxHp() {
        return this.MaxHP;
    }
}
