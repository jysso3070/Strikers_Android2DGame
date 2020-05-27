package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.content.res.Resources;
import android.graphics.Canvas;

import kr.kpu.game.Andgp2015184024.termproject.R;
import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.FrameAnimationBitmap;

public class FireBall implements GameObject {
    private static final String TAG = FireBall.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 10;
    public static final int FRAME_COUNT_FIRE = 2;
    public static final int FRAME_COUNT_FLY = 6;
    private final FrameAnimationBitmap fabFire;
    private final FrameAnimationBitmap fabFly;
    //private final int halfSize; // final은 const 같은 느낌, 스태틱으로 해서 객체가 초기화 될때마다 바뀌지 않도록
    private final int speed;
    private float x;
    private float y;

    public void fire(){
        if (state != State.fire){
            return;
        }
        state = State.fly;
        fabFly.reset();
    }

    private enum State{
        fire, fly
    }
    private State state = State.fire;
    public FireBall(float x, float y, int speed){
        GameWorld gw = GameWorld.get();
        Resources res = gw.getResources();
        fabFire = new FrameAnimationBitmap(R.mipmap.hadoken1, FRAME_PER_SECOND, FRAME_COUNT_FIRE);
        fabFly = new FrameAnimationBitmap(R.mipmap.hadoken2, FRAME_PER_SECOND, FRAME_COUNT_FLY);
        //halfSize = fabFire.getHeight() / 2;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    @Override
    public void update(){
        x += speed;
        GameWorld gw = GameWorld.get();
        if(x > gw.getRight()){
            gw.remove(this);
            return;
        }
        if (state == State.fire) {
            boolean done = fabFire.done();
            if(done){
                state = State.fly;
                fabFly.reset();
            }
        }
    }


    @Override
    public void draw(Canvas canvas){
        if(state == State.fire){
            fabFire.draw(canvas, x, y);
        } else{
            fabFly.draw(canvas, x, y);
        }
    }
}
