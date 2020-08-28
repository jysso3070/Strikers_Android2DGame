package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.AnticipateInterpolator;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.SoundEffects;

public class Fighter implements GameObject {
    private static final String TAG = Fighter.class.getSimpleName();
    private static final int FRAME_PER_SECOND = 10;
    public static final int FRAME_COUNT = 5;
    private final FrameAnimationBitmap fabIdle;
    private final FrameAnimationBitmap fabShoot;
    private final int halfSize; // final은 const 같은 느낌, 스태틱으로 해서 객체가 초기화 될때마다 바뀌지 않도록
    private final int shootOffset;
    private float x;
    private float y;

    public void setScale(float scale) {
        Log.v(TAG, "setScale: "+ scale);
        this.scale = scale;
    }

    private float scale;
//    private long firedOn;

    public void fire() {
        if (state != State.idle) {
            return;
        }
//        firedOn = GameWorld.get().getCurrentTimeNanos();
        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "scale", 1.0f, 2.0f);
        oa.setDuration(300);
        oa.setInterpolator(new AnticipateInterpolator());
        oa.start();

        state = State.shoot;
        fabShoot.reset();
        SoundEffects.get().play(R.raw.hadouken);
    }
    private void addFireBall(){
        int height = fabIdle.getHeight();
        int fx = (int) (x + height * 0.80f);
        int fy = (int) (y - height * 0.10f);

        int speed = height / 10;
//        GameWorld gw = GameWorld.get();
        MainWorld gw = MainWorld.get();
        FireBall fb = new FireBall(fx, fy, speed);
        gw.add(MainWorld.Layer.missile, fb);
    }

    private enum State{
        idle, shoot
    }
    private State state = State.idle;
    public Fighter(float x, float y){
        GameWorld gw = GameWorld.get();
        Resources res = gw.getResources();
        fabIdle = new FrameAnimationBitmap(R.mipmap.ryu, FRAME_PER_SECOND, FRAME_COUNT);
        fabShoot = new FrameAnimationBitmap(R.mipmap.ryu_1, FRAME_PER_SECOND, FRAME_COUNT);
        shootOffset = fabShoot.getHeight() * 32 / 100;
        halfSize = fabIdle.getHeight() / 2;
        this.x = x;
        this.y = y;

        Context context = gw.getContext();
    }

    @Override
    public void update(){
        if (state == State.shoot) {
            boolean done = fabShoot.done();
            if(done){
                state = State.idle;
                fabIdle.reset();
                addFireBall();
            }
        }
    }


    @Override
    public void draw(Canvas canvas){
        if(state == State.idle){
            fabIdle.draw(canvas, x, y);
        } else{
            float now = GameWorld.get().getCurrentTimeNanos();
//            float scale = (float) (1 + (now - firedOn) / 1_000_000_000.0);
            canvas.save();
            canvas.scale(scale, scale, x, y);
            fabShoot.draw(canvas, x + shootOffset, y);
            canvas.restore();
        }
    }
}
