package kr.kpu.game.Andgp2015184024.termproject.ui.view;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.res.bitmap.SharedBitmap;
import kr.kpu.game.Andgp2015184024.termproject.util.IndexTimer;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();
    public static final int FRAME_RATE_SECONDS = 10;
    private Rect mainRect;
    private Paint mainPaint;

    private boolean moves;
    private GameWorld gameWorld;
    private IndexTimer timer;
    private boolean paused;


    public GameView(Context context) {
        super(context);
        initResource();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initResource();
    }

    private void initResource() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        mainRect = new Rect(0, 0, size.x, size.y);

        SharedBitmap.setResources(getResources());
        mainPaint = new Paint();
        mainPaint.setColor(0xFFFFEEEE);


        gameWorld = GameWorld.get();
        gameWorld.setRect(mainRect);
        gameWorld.initResource(this);


        timer = new IndexTimer(FRAME_RATE_SECONDS, 1);
        postFrameCallback();

    }

    private void postFrameCallback() {
        if(paused){
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                update(frameTimeNanos);
                invalidate();
                postFrameCallback(); // 반복해서 함수가 불려질수 있게
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mainRect.left = getPaddingLeft();
        mainRect.top = getPaddingTop();
        mainRect.right = getWidth() - getPaddingRight();
        mainRect.bottom = getHeight() - getPaddingBottom();
        gameWorld.setRect(mainRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mainRect, mainPaint);
        gameWorld.draw(canvas);
    }

    private int count;
    public void update(long frameTimeNanos) {
        gameWorld.update(frameTimeNanos);
        count++;
        if(timer.done()){
            Log.d(TAG, "Frames per second = "+ (float)(count / FRAME_RATE_SECONDS));
            count = 0;
            timer.reset();
        }
    }

//    public void doAction(){
//        gameWorld.doAction();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameWorld.onTouchEvent(event);
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }
}
