package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.game.world.MainWorld;

public class Boss implements GameObject {
    private static Bitmap bitmap;
    private float x;
    private float y;
    private float size;
    private short dir;
    // pattern에 사용
    private int pattern_cool;
    private int ball_num;

    public Boss(float x, float y) {
        GameWorld gw = GameWorld.get();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(gw.getResources(), R.mipmap.boss_a);
        }
        this.x = x;
        this.y = y;
        size = bitmap.getHeight();
        dir = 0;
        pattern_cool = 0;
        ball_num = 0;
    }

    public void update() {
        GameWorld gw = GameWorld.get();
        // Boss의 움직임
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
        if (pattern_cool == 0 && ball_num < 1) fire();
        --pattern_cool;
    }

    public void draw(Canvas canvas) { canvas.drawBitmap(bitmap, x, y, null); }

    private void fire() {
        EnemyMissile em = new EnemyMissile(x + (size / 2), y + size, 10, 5);
        MainWorld.get().add(MainWorld.Layer.enemyMissile, em);
        ++ball_num;
        pattern_cool = 100;
    }
}
