package kr.kpu.game.Andgp2015184024.termproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.kpu.game.Andgp2015184024.termproject.R;

public class TitleActivity extends AppCompatActivity {

    private static final String TAG = TitleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBtnHighscore(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }
}
