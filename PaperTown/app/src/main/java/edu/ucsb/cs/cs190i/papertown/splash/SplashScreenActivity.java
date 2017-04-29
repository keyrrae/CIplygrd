/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.shimmer.ShimmerFrameLayout;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.application.AppConstants;
import edu.ucsb.cs.cs190i.papertown.geo.GeoActivity;
import edu.ucsb.cs.cs190i.papertown.login.LoginActivity;
import edu.ucsb.cs.cs190i.papertown.signup.SignupActivity;

import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.EMAIL;

public class SplashScreenActivity extends AppCompatActivity {

  CallbackManager callbackManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Remove title bar
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    //Remove notification bar
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_splash_screen);

    // Shimmer
    ShimmerFrameLayout container =
        (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
    container.setDuration(1000);
    container.startShimmerAnimation();

    // Login Button
    Button loginButton = (Button) findViewById(R.id.button_login);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        intent.putExtra("finisher", new ResultReceiver(null) {
          @Override
          protected void onReceiveResult(int resultCode, Bundle resultData) {
            SplashScreenActivity.this.finish();
          }
        });
        startActivityForResult(intent,1);
      }
    });

    Button signupButton = (Button) findViewById(R.id.button_signup);
    signupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(SplashScreenActivity.this, SignupActivity.class);
        intent.putExtra("finisher", new ResultReceiver(null) {
          @Override
          protected void onReceiveResult(int resultCode, Bundle resultData) {
            SplashScreenActivity.this.finish();
          }
        });
        startActivityForResult(intent,1);
      }
    });

    // Facebook Login
    LoginButton facebookLogin = (LoginButton) findViewById(R.id.login_button);
    callbackManager = CallbackManager.Factory.create();

    facebookLogin.setReadPermissions("email");
    // If using in a fragment
    //facebookLogin.setFragment(this);
    // Other app specific specialization

    // Callback registration
    facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        // App code
        Log.d("Splash", "success");

        // TODO: create credentials from login result

        Intent geoIntent = new Intent(SplashScreenActivity.this, GeoActivity.class);
        startActivity(geoIntent);
        SharedPreferences sharedPreferences =
            getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        //sharedPreferences.edit().putBoolean(AppConstants.LOGGED_IN, true).apply();
        finish();
      }

      @Override
      public void onCancel() {
        // App code
      }

      @Override
      public void onError(FacebookException exception) {
        // App code
      }
    });

    SharedPreferences sharedPreferences =
        getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);

    String email = sharedPreferences.getString(EMAIL, "");
    if(!email.equals("")){
      signupButton.setVisibility(View.GONE);
      loginButton.setVisibility(View.GONE);
      facebookLogin.setVisibility(View.GONE);

      Handler mHandler = new Handler();

      Runnable mLaunchTask = new Runnable() {
        public void run() {
          //will launch the activity
          Intent intent = new Intent(getApplicationContext(), GeoActivity.class);
          startActivity(intent);
          finish();
        }
      };

      // do the splash
      mHandler.postDelayed(mLaunchTask, 3000);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
