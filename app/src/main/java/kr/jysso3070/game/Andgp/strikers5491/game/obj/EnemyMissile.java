package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.BoxCollidable;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;

public class EnemyMissile implements GameObject, BoxCollidable {
    private static final String TAG = EnemyMissile.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 6;
    private final FrameAnimationBitmap fab;
    private int rand_dir;
    private int speed;
    private float x;
    private float y;
    private int type;
    private int dir = 1;
    private float targetX;
    private float targetY;
    private static Random rand;

    public EnemyMissile(float x, float y, int speed){
        fab = new FrameAnimationBitmap(R.mipmap.e_missile, FRAME_PER_SECOND, 0);
        this.x = x;
        this.y = y;
        this.speed = speed;
        if (speed != 100) this.type = 0;
        else this.type = 2;
        rand = new Random();
        rand_dir = rand.nextInt(10);
        if (rand_dir < 5) dir = 1;
        else dir = -1;
    }

    public EnemyMissile(float x, float y){
        fab = new FrameAnimationBitmap(R.mipmap.energyball, FRAME_PER_SECOND, 3);
        this.x = x;
        this.y = y;
        this.type = 1;
        targetX = MainWorld.get().GetPlayer().getX();
        if (x < targetX) dir = 1;
        else dir = -1;
    }

    public void update(){
        GameWorld gw = GameWorld.get();
        if (type == 0) {
            y += speed;
        }
        else if (type == 1) {
            if (Math.abs(x - targetX) > 10) x += 10 * dir;
            y += 5;
        }
        else if (type == 2) {
            if (x < 0) dir = 1;
            if (x > gw.getRight()) dir = -1;
            x += 10 * dir;
            y += 5;
        }

        if (y > gw.getBottom()) {
            gw.remove(this);
        }
//        Log.d(TAG, "Index = "+index);
    }
    public void draw(Canvas canvas){
        fab.draw(canvas, x, y);
    }

    @Override
    public void getBox(RectF rect) {
        int hw = fab.getWidth() / 3;
        int hh = fab.getHeight() / 3;
        rect.left = x - hw;
        rect.right = x + hw;
        rect.top = y - hh;
        rect.bottom = y + hh;
    }
}
