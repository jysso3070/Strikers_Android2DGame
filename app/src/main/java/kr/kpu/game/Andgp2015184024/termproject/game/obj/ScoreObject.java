package kr.kpu.game.Andgp2015184024.termproject.game.obj;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.kpu.game.Andgp2015184024.termproject.game.framework.GameWorld;
import kr.kpu.game.Andgp2015184024.termproject.game.iface.GameObject;

public class ScoreObject implements GameObject {
    private final Bitmap bitmap;
    private final int width;
    private final int height;
    private final float right, top;
    private int scoreValue;
    private ValueAnimator scoreAnimator;
    private int scoreDisplay;
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();

    public ScoreObject(float right, float top, int resID) {
        GameWorld gw = GameWorld.get();
        bitmap = BitmapFactory.decodeResource(gw.getResources(), resID);
        width = bitmap.getWidth() / 10;
        height = bitmap.getHeight();
        this.right = right;
        this.top = top;
        scoreAnimator = ObjectAnimator.ofInt(this, "scoreDisplay", 0);

        srcRect.top = 0;
        srcRect.bottom = height;
    }

    public void setScoreDisplay(int scoreDisplay) {
        this.scoreDisplay = scoreDisplay;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        dstRect.right = right;
        dstRect.top = top;
        dstRect.left = right - width;
        dstRect.bottom = top + height;
        int value = scoreDisplay;
        if(value == 0){
            srcRect.left = 0;
            srcRect.right = width;
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            return;
        }
        while (value > 0){
            int digit = value % 10;
            srcRect.left = digit * width;
            srcRect.right = srcRect.left + width;
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            value /=10;
            dstRect.left -= width;
            dstRect.right -= width;
        }
    }

    public int addScore(int score) {
        scoreValue += score;
//        oa = ObjectAnimator.ofInt(this, "scoreDisplay", scoreDisplay, scoreValue);
        scoreAnimator.setIntValues(scoreDisplay, scoreValue);
        scoreAnimator.setDuration(500);
        scoreAnimator.start();
        return scoreValue;
    }

    public void reset() {
        addScore(-scoreValue);
    }

    public int getScore() {
        return scoreValue;
    }

    public void setScore(int score) {
        addScore(score - scoreValue);
    }
}
