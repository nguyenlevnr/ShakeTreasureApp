package com.example.thaonguyenlp.shaketreasureapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static int VIEW_NUMBER = 1;
    TextView tv1, tv2, tv3, tv4;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;
    List<Integer> intList = new ArrayList<>();
    //DBManager dbManager;
    String name, score, result;
    int time = 3000;
    int intScore =0;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent mainIntent = getIntent();
        name = mainIntent.getStringExtra(MainActivity.USER_NAME);
        score = "0";
        //dbManager = new DBManager(GameActivity.this);
        VIEW_NUMBER = 1;
        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        iv5 = findViewById(R.id.imageView5);
        iv6 = findViewById(R.id.imageView6);
        iv7 = findViewById(R.id.imageView7);
        iv8 = findViewById(R.id.imageView8);
        hideImgView(iv1);
        hideImgView(iv2);
        hideImgView(iv3);
        hideImgView(iv4);
        hideImgView(iv5);
        hideImgView(iv6);
        hideImgView(iv7);
        hideImgView(iv8);
        Random rand = new Random();
        int number = rand.nextInt(9999 - 1000) + 1000;
        result = String.valueOf(number);
        char[] charArray = result.toCharArray();
        for (int index = 0; index < 4; index++)
        {
            int num = Character.getNumericValue(charArray[index]);
            intList.add(num);
        }
        Log.i("GameActivity tag", "now running game onCreate");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("scores");
    }

    public void hideImgView(ImageView iv) {
        iv.setVisibility(View.INVISIBLE);
    }
    public void showImgView(ImageView iv) {
        iv.setVisibility(View.VISIBLE);
    }
    public void clickNumber(View view) {
        if (!(iv1.getVisibility() == View.VISIBLE && iv2.getVisibility() == View.VISIBLE && iv3.getVisibility() == View.VISIBLE && iv4.getVisibility() == View.VISIBLE)) {
            switch (VIEW_NUMBER) {
                case 1:
                    changeText(tv1, view);
                    break;
                case 2:
                    changeText(tv2, view);
                    break;
                case 3:
                    changeText(tv3, view);
                    break;
                case 4:
                    changeText(tv4, view);
                    break;
            }
        }
    }

    public void changeText(TextView tv, View view) {
        switch (view.getId()) {
            case R.id.key_1:
                tv.setText("1");
                break;
            case R.id.key_2:
                tv.setText("2");
                break;
            case R.id.key_3:
                tv.setText("3");
                break;
            case R.id.key_4:
                tv.setText("4");
                break;
            case R.id.key_5:
                tv.setText("5");
                break;
            case R.id.key_6:
                tv.setText("6");
                break;
            case R.id.key_7:
                tv.setText("7");
                break;
            case R.id.key_8:
                tv.setText("8");
                break;
            case R.id.key_9:
                tv.setText("9");
                break;
            case R.id.key_10:
                tv.setText("0");
                break;
        }
    }

    public void clickNext(View view) {
        if (!(iv1.getVisibility() == View.VISIBLE && iv2.getVisibility() == View.VISIBLE && iv3.getVisibility() == View.VISIBLE && iv4.getVisibility() == View.VISIBLE)) {
            switch (VIEW_NUMBER) {
                case 1:
                    if(checkNull(tv1)){
                        changeView();
                        tv1.setBackgroundResource(R.drawable.back);
                        tv2.setBackgroundResource(R.drawable.choose_next);
                    }else{
                        Toast.makeText(this, "Please choose a number", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    if(checkNull(tv2)){
                        changeView();
                        tv2.setBackgroundResource(R.drawable.back);
                        tv3.setBackgroundResource(R.drawable.choose_next);
                    }else{
                        Toast.makeText(this, "Please choose a number", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 3:
                    if(checkNull(tv3)){
                        changeView();
                        tv3.setBackgroundResource(R.drawable.back);
                        tv4.setBackgroundResource(R.drawable.choose_next);
                    }else{
                        Toast.makeText(this, "Please choose a number", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 4:
                    if(checkNull(tv4)){
                        changeView();
                        tv4.setBackgroundResource(R.drawable.back);
                        tv1.setBackgroundResource(R.drawable.choose_next);
                    }else{
                        Toast.makeText(this, "Please choose a number", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    public void changeView() {
        if(VIEW_NUMBER <4){
            VIEW_NUMBER += 1;
        }else{
            VIEW_NUMBER = 1;
        }
    }

    public boolean checkNull(TextView tv) {
        String text = tv.getText().toString();
        return !text.equals("");
    }

    public void clickCheck(View view) {
        if (!(iv1.getVisibility() == View.VISIBLE && iv2.getVisibility() == View.VISIBLE && iv3.getVisibility() == View.VISIBLE && iv4.getVisibility() == View.VISIBLE)) {
            if(checkNull(tv1) && checkNull(tv2) && checkNull(tv3) && checkNull(tv4)){
                List<Integer> res = checkNumber();
                if(res.get(0)==1){
                    showImgView(iv1);
                    hideImgView(iv5);
                    intScore += 100;
                }else{
                    showImgView(iv5);
                    hideImgView(iv1);
                }
                if(res.get(1)==1){
                    showImgView(iv2);
                    hideImgView(iv6);
                    intScore += 100;
                }else{
                    showImgView(iv6);
                    hideImgView(iv2);
                }
                if(res.get(2)==1){
                    showImgView(iv3);
                    hideImgView(iv7);
                    intScore += 100;
                }else{
                    showImgView(iv7);
                    hideImgView(iv3);
                }
                if(res.get(3)==1){
                    showImgView(iv4);
                    hideImgView(iv8);
                    intScore += 100;
                }else{
                    showImgView(iv8);
                    hideImgView(iv4);
                }
                if(res.get(0)==1 && res.get(1)==1 && res.get(2)==1 && res.get(3)==1){
                    intScore *= time;
                    score = String.valueOf(intScore);
                    FriendlyMessage friendlyMessage = new
                            FriendlyMessage( score, name, null);
                    mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    Toast.makeText(this, "Score: " + score, Toast.LENGTH_LONG).show();
                }else{
                    if(time > 0){
                        time -= 100;
                    }
                    score = String.valueOf(intScore);
                }
            }else {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_LONG).show();
            }
        }
    }

    public List<Integer> checkNumber() {
        List<Integer> result = new ArrayList<>();
        String text1 = tv1.getText().toString();
        String text2 = tv2.getText().toString();
        String text3 = tv3.getText().toString();
        String text4 = tv4.getText().toString();
        if( Integer.parseInt(text1) == intList.get(0)){
            result.add(1);
        }else{
            result.add(0);
        }
        if( Integer.parseInt(text2) == intList.get(1)){
            result.add(1);
        }else{
            result.add(0);
        }
        if( Integer.parseInt(text3) == intList.get(2)){
            result.add(1);
        }else{
            result.add(0);
        }
        if( Integer.parseInt(text4) == intList.get(3)){
            result.add(1);
        }else{
            result.add(0);
        }
        return result;
    }

    //public void restartGame(View view) {
        //this.recreate();
    //}

    //public void scoreGame(View view) {
        //Intent intentScore = new Intent(this, ScoreActivity.class);
        //intentScore.putExtra(MainActivity.EXTRA_MESSAGE, name);
        //startActivity(intentScore);
        //finish();
    //}

    public void resultGame(View view) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GameActivity tag", "now running game onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("GameActivity tag", "now running game onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("GameActivity tag", "now running game onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("GameActivity tag", "now running game onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("GameActivity tag", "now running game onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("GameActivity tag", "now running game onDestroy");
    }
}
