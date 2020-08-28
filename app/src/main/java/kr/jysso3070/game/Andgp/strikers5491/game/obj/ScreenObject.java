package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;

public class ScreenObject implements GameObject {
    private final Bitmap bitmap;
    private final int width;
    private final int height;
    private final float left;
    private final float top;
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();

    public ScreenObject(float left, float top, int resID){
        GameWorld gw = GameWorld.get();
        bitmap = BitmapFactory.decodeResource(gw.getResources(), resID);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.left = left;
        this.top = top;

        srcRect.top = 0;
        srcRect.bottom = height;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.left, this.top, null);
    }

}
