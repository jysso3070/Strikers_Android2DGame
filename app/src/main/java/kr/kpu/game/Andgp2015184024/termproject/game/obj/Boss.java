package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.BoxCollidable;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;

public class Boss implements GameObject, BoxCollidable {
    private static Bitmap bitmap;
    private final FrameAnimationBitmap fab;
    private float x;
    private float y;
    private float size;
    private short dir;
    private int life;
    private int type;
    // pattern에 사용
    private int pattern_cool;
    private int energyball_cool;
    private int randatt_cool;

    public Boss(float x, float y) {
        fab = new FrameAnimationBitmap(R.mipmap.boss_b, 12, 3);
        GameWorld gw = GameWorld.get();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gw.getResources(), R.mipmap.boss_a);
        }
        this.x = x;
        this.y = y;
        this.life = 10000;
        size = bitmap.getHeight();
        dir = 0;
        type = 0;
        pattern_cool = 100;
        energyball_cool = 201;
        randatt_cool = 151;
    }

    public Boss(float x, float y, int type) {
        fab = new FrameAnimationBitmap(R.mipmap.boss_b, 12, 3);
        this.type = type;
        this.x = x;
        this.y = y + 200;
        this.life = 15000;
        size = fab.getHeight();
        dir = 0;
        pattern_cool = 100;
        energyball_cool = 201;
        randatt_cool = 151;
    }

    public void update() {
        GameWorld gw = GameWorld.get();
        // Boss의 움직임
        if (type == 0) if (y < 100) ++y;
        if (type == 1) if (y < 200) ++y;
        switch (dir) {
            case 0:
                if (type == 0) {
                    if (x < gw.getRight() - size) ++x;
                    else dir = 1;
                }
                else if (type == 1) {
                    if (x < gw.getRight() - (size / 2)) ++x;
                    else dir = 1;
                }
                break;

            case 1:
                if (type == 0) {
                    if (x > gw.getLeft()) --x;
                    else dir = 0;
                }
                else if (type == 1) {
                    if (x > gw.getLeft() + (size / 2)) --x;
                    else dir = 0;
                }
                break;

            default:
                dir = 0;
                break;
        }
        // Boss pattern 1
        if (pattern_cool < 0) fire();
        // Boss Pattern 2 - 체력이 적을 때
        if (energyball_cool % 50 == 0 && energyball_cool <= 200) EnergyBall();
        // Boss Pattern 3 - 2번째 보스만 가지고 있는 패턴
        if (randatt_cool % 2 == 0 && randatt_cool <= 150 && type != 0) randomAttack();
        --pattern_cool;
        if (life < 8000) --energyball_cool;
        if (life < 5000 && type != 0) --randatt_cool;
    }


    @Override
    public void draw(Canvas canvas) {
        if (type == 0) canvas.drawBitmap(bitmap, x, y, null);
        else if (type == 1) fab.draw(canvas, x, y);
    }

    private void fire() {
        if (type == 0) {
            for (int i = 0; i < 3; ++i) {
                EnemyMissile em = new EnemyMissile(x + (size / 2), y + size + (i * 50), 10);
                MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
            }
        }
        else {
            for (int i = 0; i < 3; ++i) {
                EnemyMissile em = new EnemyMissile(x, y + (i * 50), 10);
                MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
            }
        }
        pattern_cool = 100;
    }

    private void EnergyBall() {
        if (type == 0) {
            EnemyMissile em = new EnemyMissile(x + (size / 2), y + (size / 2));
            MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
        }
        else {
            EnemyMissile em = new EnemyMissile(x, y);
            MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
        }
        if (energyball_cool < 0) energyball_cool = 500;
    }

    private void randomAttack() {
        EnemyMissile em = new EnemyMissile(x, y, 100);
        MainWorld.get().add(MainWorld.Layer.enemyMissile, em);

        if (randatt_cool < 0) randatt_cool = 400;
    }

    public void decreaseLife(int power) {
        this.life -= power;
        if (life <= 0) {
            MainWorld gw = MainWorld.get();
            gw.remove(this);
            if (type == 0) gw.addScore(10000);
            else if (type == 1) gw.addScore(20000);
        }
    }

    @Override
    public void getBox(RectF rect) {
        if (type == 0) {
            rect.left = x + (size / 3);
            rect.right = x + (size / 2) + (size / 10);
            rect.top = y;
            rect.bottom = y + size;
        }
        else if (type == 1) {
            rect.left = x - (size / 2);
            rect.right = x + (size / 2);
            rect.top = y - (size / 2);
            rect.bottom = y + (size / 2) - (size / 10);
        }
    }

}
