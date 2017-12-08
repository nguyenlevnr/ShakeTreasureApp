package com.example.thaonguyenlp.shaketreasureapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "com.example.thaonguyenlp.myapplication.USERNAME";
    public static final String DATA_SCORE = "com.example.thaonguyenlp.myapplication.DATASCORE";
    private Button mSignOutButton;
    private String mUsername = "anonymous", mMessages;

    public static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ArrayList<FriendlyMessage> data = new ArrayList<>();
    private String namecheck, datascore;
    private int namescore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("scores");

        // Code for Authentication
        mSignOutButton = (Button) findViewById(R.id.button_sign_out);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(MainActivity.this);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                    Toast.makeText(MainActivity.this, "You're now signed in. Welcome!",
                            Toast.LENGTH_LONG).show();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        Log.i("MainActivity tag", "now running main onCreate");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        Log.i("MainActivity tag", "now running main onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            Log.i("MainActivity tag", "now running main onPause");

        }
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = "anonymous"; // ANONYMOUS;
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);

                    // Add the new message to our growing string of messages
                    data.add(friendlyMessage);

                    //Toast.makeText(MainActivity.this, "New message: " + friendlyMessage.getText(),
                            //Toast.LENGTH_SHORT).show();
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void playGame(View view) {
        Intent intentMap = new Intent(this, MapsActivity.class);
        intentMap.putExtra(USER_NAME, mUsername);
        startActivity(intentMap);
    }

    public void scoreGame(View view) {
        Intent intentScore = new Intent(this, ScoreActivity.class);
        datascore = sourceData(data);
        intentScore.putExtra(DATA_SCORE, datascore);
        startActivity(intentScore);
        finish();
    }

    public String sourceData(ArrayList<FriendlyMessage> data){
        String str="";
        ArrayList<String> names = new ArrayList<>();
        ArrayList<FriendlyMessage> source = data;
        boolean hasName = false;
        //names.add(data.get(0).getName());
        for (int index= 0; index < data.size(); index++)
        {
            namecheck = data.get(index).getName();
            if(namecheck != null){
                for (int i= 0; i < names.size(); i++)
                {
                    if(namecheck.equals(names.get(i))){
                        hasName = true;
                    }
                }
                if(!hasName){
                    names.add(namecheck);
                }
            }
        }
        for (int i= 0; i < names.size(); i++)
        {
            namecheck = names.get(i);
            for (int index= 0; index < data.size(); index++)
            {
                if(namecheck.equals(data.get(index).getName())){
                    namescore = namescore + Integer.parseInt(data.get(index).getText());
                }
            }
            str = str +  "\n" + namecheck + ": " + namescore;
            namescore=0;
        }
        return str;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity tag", "now running main onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity tag", "now running main onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity tag", "now running main onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity tag", "now running main onDestroy");
    }

}
