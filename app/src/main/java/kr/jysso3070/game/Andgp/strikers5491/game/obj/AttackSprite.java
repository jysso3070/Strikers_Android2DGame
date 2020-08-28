package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import android.graphics.Canvas;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;
import kr.jysso3070.game.Andgp.strikers5491.res.bitmap.FrameAnimationBitmap;

public class AttackSprite implements GameObject {
    public static final int FRAMES_PER_SECOND = 12;
    private FrameAnimationBitmap fab;
    private float x;
    private float y;
    private int count = 0;

    public AttackSprite(float x, float y) {
        fab = new FrameAnimationBitmap(R.mipmap.hit, FRAMES_PER_SECOND, 6);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        MainWorld mw = MainWorld.get();
        ++count;
        if (count == FRAMES_PER_SECOND) mw.remove(this);
    }

    @Override
    public void draw(Canvas canvas) {
        fab.draw(canvas, x, y);
    }
}
