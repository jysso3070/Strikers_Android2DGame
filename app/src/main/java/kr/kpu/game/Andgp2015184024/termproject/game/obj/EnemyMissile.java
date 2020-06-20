package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.BoxCollidable;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;

public class EnemyMissile implements GameObject, BoxCollidable {
    private static final String TAG = EnemyMissile.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 6;
    private final FrameAnimationBitmap fab;
    private int speed;
    private float x;
    private float y;


    public EnemyMissile(float x, float y, int speed){
        fab = new FrameAnimationBitmap(R.mipmap.e_missile, FRAME_PER_SECOND, 0);
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update(){
        GameWorld gw = GameWorld.get();
        y += speed;

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
