package kr.jysso3070.game.Andgp.strikers5491.game.world;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.jysso3070.game.Andgp.strikers5491.R;
import kr.jysso3070.game.Andgp.strikers5491.game.framework.GameWorld;
import kr.jysso3070.game.Andgp.strikers5491.game.iface.GameObject;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.Boss;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.EnemyGenerator;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.Fighter;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.HpObject;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.Joystick;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.JoystickBG;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.MyPlane;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.Plane;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.ScoreObject;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.ScreenObject;
import kr.jysso3070.game.Andgp.strikers5491.game.obj.bg.ImageScrollBackground;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.BGSound;
import kr.jysso3070.game.Andgp.strikers5491.res.sound.SoundEffects;
import kr.jysso3070.game.Andgp.strikers5491.ui.activity.TitleActivity;

public class MainWorld extends GameWorld {
    private static final int BALL_COUNT = 10;
    public static final String PREF_KEY_HIGHSCORE = "highscore";
    public static final String PREFS_NAME = "Prefs";
    private static final String TAG = MainWorld.class.getSimpleName();
    private Fighter fighter;
    private EnemyGenerator enemyGenerator = new EnemyGenerator();
    private Plane plane;
    private ScoreObject scoreObject;
    private ScoreObject highScoreObject;
    private PlayState playState = PlayState.normal;
    private Joystick joystick;
    private MyPlane myPlane;
    private JoystickBG joystickback;
    private Boss boss;
    private HpObject hpObject;
    private BGSound bgSound;

    private boolean gameOverFlag;
    private int type = 0;

    public static void create() {
        if(singleton != null){
//            Log.e(TAG, "object already, created");
        }
        singleton = new MainWorld();
    }

    public static MainWorld get(){
        return (MainWorld) singleton;
    }

    private enum PlayState{
        normal, paused, gameOver
    }


    public enum Layer{
        bg, missile, enemy, enemyBoss, player, enemyMissile, effect, ui, COUNT
    }

    protected int getLayerCount(){
        return Layer.COUNT.ordinal();
    }

    public MyPlane GetPlayer() { return myPlane; }

    @Override
    public void initObjects() {
        Resources res = view.getResources();
        GameWorld gw = GameWorld.get();
        gameOverFlag = false;

        add(Layer.bg, new ImageScrollBackground(R.mipmap.stage1,
                ImageScrollBackground.Orientation.vertical, 25));
        myPlane = new MyPlane(500, rect.bottom-100);
        add(Layer.player, myPlane);
        joystickback = new JoystickBG(rect.right - 400, rect.bottom - 400, JoystickBG.Direction.normal, 100);
        add(Layer.ui, joystickback);
        joystick = new Joystick(rect.right - 400, rect.bottom - 400, Joystick.Direction.normal, 100);
        add(Layer.ui, joystick);
        myPlane.setJoystick(joystick);
        SharedPreferences gyroPrefs = view.getContext().getSharedPreferences("gyroPrefs", Context.MODE_PRIVATE);
        boolean gyroOn = gyroPrefs.getBoolean("gyroSensorOn", false);
        myPlane.setGyro(gyroOn);

        int viewRight = gw.getRight();
        int viewTop = gw.getTop();
        scoreObject = new ScoreObject(viewRight - 50, viewTop + 10, R.mipmap.num_sprite1);
        add(Layer.ui, scoreObject);
        hpObject = new HpObject(25, 25, R.mipmap.hpicon_2, myPlane.getMaxHp());
        add(Layer.ui, hpObject);

        startGame();
    }

    public void add(Layer layer, final GameObject obj){
        add(layer.ordinal(), obj);
    }
    public ArrayList<GameObject> objectsAt(Layer layer) {
        return objectsAt(layer.ordinal());
    }

    private void startGame() {
        playState = PlayState.normal;
        scoreObject.reset();

        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
//        highScoreObject.setScore(highScore);

        ArrayList<GameObject> ui = MainWorld.get().objectsAt(MainWorld.Layer.ui);
        for(GameObject temp : ui){
            if(temp instanceof ScreenObject){
                MainWorld.get().remove(temp);
            }
        }
        BGSound.get().playBGM();



    }

    public void endGame() {
        playState = PlayState.gameOver;
        int score = scoreObject.getScore();
        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highScore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
        if(score > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(PREF_KEY_HIGHSCORE, score);
            editor.commit();
            // 게임종료시 진행되야 할 것들
        }

        GameWorld gw = GameWorld.get();
        ScreenObject gameoverImg = new ScreenObject(gw.getRight() / 3, gw.getBottom() / 2, R.mipmap.gameover);
        add(Layer.ui, gameoverImg);
        BGSound.get().stop();
        SoundEffects.get().play(R.raw.enemy_bomb);
//        enemyGenerator.resetWave();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        joystick.onTouchEvent(event);
//        GameWorld gw = GameWorld.get();
//        myPlane.move(event.getX(), event.getY());
        int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
            if(playState == PlayState.gameOver){
                Context context = getContext();
                Intent intent = new Intent(context, TitleActivity.class);
                context.startActivity(intent);
//                startGame();
                return false;
            }
//            doAction();
//            plane.head(event.getX(), event.getY());
        } else if(action == MotionEvent.ACTION_MOVE){
//            plane.head(event.getX(), event.getY());
        }
        return true;
    }

    public void addScore(int score) {
        int value = scoreObject.addScore(score);
    }
    public void decreaseHpObject() {
        hpObject.decreaseHp();
    }

    public void doAction() {
        fighter.fire();
    }

    @Override
    public void update(long frameTimeNanos) {

        if(playState != PlayState.normal){
            return;
        }
        super.update(frameTimeNanos);

        enemyGenerator.update();

        // Boss Monster
        if (enemyGenerator.getIsboss()) {
            if (type == 0) {
                boss = new Boss((rect.right / 2) - 200, -500);
                add(Layer.enemyBoss, boss);
                enemyGenerator.setIsboss(false);
                type = 1;
            }
            else if (type == 1) {
                boss = new Boss((rect.right / 2) - 200, -500, 1);
                add(Layer.enemyBoss, boss);
                enemyGenerator.setIsboss(false);
                type = 0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
