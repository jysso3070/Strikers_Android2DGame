package kr.jysso3070.game.Andgp.strikers5491.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import kr.jysso3070.game.Andgp.strikers5491.R;

public class TitleActivity extends AppCompatActivity {
    private static final String TAG = TitleActivity.class.getSimpleName();
    private boolean gyroSenserOn = false;
    private CheckBox gyroSenserCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        gyroSenserCheckBox = (CheckBox)findViewById(R.id.gyroSensorCheck);
        Log.d(TAG, "onCreate()");
    }

    protected void onPause(){
        Log.d(TAG, "onPause()");
        super.onPause();
    }
    protected void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    public void onBtnStart(View view) {
        Log.d(TAG, "onBtnStart()");
        gyroSenserOn = gyroSenserCheckBox.isChecked();
        SharedPreferences prefs = view.getContext().getSharedPreferences("gyroPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("gyroSensorOn", gyroSenserOn);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBtnHighscore(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }
}
