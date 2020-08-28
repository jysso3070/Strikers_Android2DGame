package kr.jysso3070.game.Andgp.strikers5491.game.obj;

import java.util.Random;

import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.world.MainWorld;

public class EnemyGenerator {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private static final long INITIAL_GENERATE_INTERVAL = 5_000_000_000L;
    private static final long MINIMUM_GENERATE_INTERVAL = 1_000_000_000L;
    private static final double INTERVAL_REDUCE_RATE_PER_WAVE = 0.995;
    private static final int MAX_SPEED = 1000;
    private static final int MAX_LEVEL = Enemy.RES_IDS.length;
    private long lastGenerated;
    private long generationInterval;
    private final Random rand;
    private int wave;
    private boolean is_boss;

    public void setIsboss(boolean is_bool) { is_boss = is_bool; }
    public boolean getIsboss() { return is_boss; }

    public EnemyGenerator() {
        //this.lastGenerated = GameWorld.get().getCurrentTimeNanos();
        generationInterval = INITIAL_GENERATE_INTERVAL;
        rand = new Random();
    }

    public void update() {
        GameWorld gw = GameWorld.get();
        long now = gw.getCurrentTimeNanos();
        if (lastGenerated == 0) {
            lastGenerated = now;
            return;
        }
        long elapsed = now - lastGenerated;
        if (elapsed > generationInterval) {
            generateWave();
            lastGenerated = now;
            if (wave % 10 == 0 && wave != 0) is_boss = true;
        }
    }

    private void generateWave() {
        wave++;
        String msg = "Wave " + wave + " Generated: /";
        for (int i = 0; i < 5; i++) {
            int level = generateEnemy(120 + i * 200);
            msg += level + "/";
        }
        generationInterval *= INTERVAL_REDUCE_RATE_PER_WAVE;
        if (generationInterval < MINIMUM_GENERATE_INTERVAL) {

        }
        msg += " Interval=" + (generationInterval / 1_000_000_000.0);
        //Log.d(TAG, msg);
    }

    private static int[] DIFFS = {
            -3, -3, -2, -2, -2, -2, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2
    };
    private int generateEnemy(int x) {
        int speed = 500 + 10 * wave;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        int level = wave / 10 + DIFFS[rand.nextInt(DIFFS.length)];
        //int level = (int) (rand.nextGaussian() * 11 + wave - 5) / 10 + 1;
        if (level < 1) {
            level = 1;
        }
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        MainWorld gw = MainWorld.get();
        Enemy e = Enemy.get(x, level, speed);
//        GameWorld gw = GameWorld.get();
        gw.add(MainWorld.Layer.enemy, e);
//        Log.d(TAG, "" + e);

        return level;
    }

}
