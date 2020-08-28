package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.SharedBitmap;

public class Joystick implements GameObject {

    private static final String TAG = Joystick.class.getSimpleName();
    private SharedBitmap sbmp;
    private final float x, y;
    private final int size;
    private final Direction direction;
    private boolean down;
    private float xDown, yDown;
    private float dx, dy;
    private double angle;
    private double agl;

    public Double getAngle() {
        return this.angle;
    }

    public boolean getJoystickDown(){
        return  this.down;
    }

    public enum Direction {
        normal, horizontal, vertical
    }
    public Joystick(float x, float y, Direction dir, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.direction = dir;
        this.sbmp = SharedBitmap.load(R.mipmap.stick);
        this.down = false;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(150);
        sbmp.draw(canvas, x, y, paint);
        if (down) {
//            Log.d(TAG, "angle = " + angle + " dx=" + dx + " dy=" + dy);
//            System.out.println("anlgle" + angle);
            sbmp.draw(canvas, x + dx, y + dy, paint);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                dx = dy = 0;
                down = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!down) {
                    return;
                }

                float dx = event.getX() - xDown;
                float dy = event.getY() - yDown;
                move(dx, dy);
                break;
            default:
                down = false;
        }
    }

    private void move(float dx, float dy) {
        if (direction == Direction.vertical) {
            dx = 0;
            if (dy < -size) {
                dy = -size;
            } else if (dy > size) {
                dy = size;
            }
        } else if (direction == Direction.horizontal) {
            dy = 0;
            if (dx < -size) {
                dx = -size;
            } else if (dx > size) {
                dx = size;
            }
        } else {
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > size) {
                dx = dx * size / dist;
                dy = dy * size / dist;
            }
        }
        this.dx = dx;
        this.dy = dy;
        this.angle = Math.atan2(dy, dx) * 180 / Math.PI;
//        Log.d(TAG, "angle = " + angle + " dx=" + dx + " dy=" + dy);
    }

    public int getHorzDirection() {
        if (!down) return 0;
        if (dx == 0) return 0;
        int dir = angle < Math.PI / 2 && angle > -Math.PI / 2 ? 1 : -1;
//        Log.v(TAG, "Dir = " + dir);
        return dir;
    }
}