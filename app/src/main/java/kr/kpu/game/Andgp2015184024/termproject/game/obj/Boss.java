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

public class Boss implements GameObject, BoxCollidable {
    private static Bitmap bitmap;
    private float x;
    private float y;
    private float size;
    private short dir;
    private int life;
    // pattern에 사용
    private int pattern_cool;

    public Boss(float x, float y) {
        GameWorld gw = GameWorld.get();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gw.getResources(), R.mipmap.boss_a);
        }
        this.x = x;
        this.y = y;
        this.life = 10000;
        size = bitmap.getHeight();
        dir = 0;
        pattern_cool = 0;
    }

    public void update() {
        GameWorld gw = GameWorld.get();
        // Boss의 움직임
        if (y < 100) ++y;
        switch (dir) {
            case 0:
                if (x < gw.getRight() - size) ++x;
                else dir = 1;
                break;

            case 1:
                if (x > gw.getLeft()) --x;
                else dir = 0;
                break;

            default: dir = 0;
                break;
        }
        // Boss pattern 1
        if (pattern_cool == 0) fire();
        --pattern_cool;
    }

    public void draw(Canvas canvas) { canvas.drawBitmap(bitmap, x, y, null); }

    private void fire() {
        for (int i = 0; i < 3; ++i) {
            EnemyMissile em = new EnemyMissile(x + (size / 2), y + size + (i * 50), 10);
            MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
        }
        pattern_cool = 100;
    }

    public void decreaseLife(int power){
        this.life -= power;
        if(life <= 0){
            MainWorld gw = MainWorld.get();
            gw.remove(this);
            gw.addScore(10000);
        }
    }

    @Override
    public void getBox(RectF rect) {
        rect.left = x + (size / 3);
        rect.right = x + (size / 2) + (size / 10);
        rect.top = y;
        rect.bottom = y + size;
    }
}
