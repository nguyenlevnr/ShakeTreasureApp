package com.example.thaonguyenlp.shaketreasureapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    TextView scoreView;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent gameIntent = getIntent();
        data = gameIntent.getStringExtra(MainActivity.DATA_SCORE);
        scoreView = findViewById(R.id.scoreText);
        scoreView.setText(data);
        Log.i("ScoreActivity tag", "now running score onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ScoreActivity tag", "now running score onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ScoreActivity tag", "now running score onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ScoreActivity tag", "now running score onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ScoreActivity tag", "now running score onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ScoreActivity tag", "now running score onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ScoreActivity tag", "now running score onDestroy");
    }
}
