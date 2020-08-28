package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.BoxCollidable;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;

public class Ball implements GameObject, BoxCollidable {
    private static final String TAG = Ball.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 6;
    private final FrameAnimationBitmap fab;
    private final int halfSize; // final은 const 같은 느낌, 스태틱으로 해서 객체가 초기화 될때마다 바뀌지 않도록
    private float dx;
    private float dy;
    private float x;
    private float y;


    public Ball(float x, float y, float dx, float dy){
        GameWorld gw = GameWorld.get();
        fab = new FrameAnimationBitmap(R.mipmap.fireball_128_24f, FRAME_PER_SECOND, 0);
        halfSize = fab.getHeight() / 2;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void update(){
        GameWorld gw = GameWorld.get();
        x += dx;
        if(dx > 0 && x > gw.getRight() - halfSize || dx < 0 && x < gw.getLeft() + halfSize){
            dx *= -1;
        }
        if(dy> 0 && y > gw.getBottom() - halfSize || dy < 0 && y < gw.getTop() + halfSize){
            dy *= -1;
        }
        y += dy;
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
