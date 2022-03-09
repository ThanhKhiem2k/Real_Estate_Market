 package com.example.real_estate_market;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.real_estate_market.Activity.LoginActivity;
import com.example.real_estate_market.Activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);

    }

     private void nextActivity() {
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         if(user == null){
             //chưa login
             Intent intent = new Intent(this, LoginActivity.class);
             startActivity(intent);
         } else {
             //Đã login
             Intent intent = new Intent(this, MainActivity.class);
             startActivity(intent);
         }
         finish();
    }
 }