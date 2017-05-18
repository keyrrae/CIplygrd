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
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.application.AppConstants;
import edu.ucsb.cs.cs190i.papertown.geo.GeoActivity;
import edu.ucsb.cs.cs190i.papertown.login.LoginActivity;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;
import edu.ucsb.cs.cs190i.papertown.signup.SignupActivity;


public class SplashScreenActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  CallbackManager callbackManager;
  private final static String TAG = "Splash";

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
    final Button loginButton = (Button) findViewById(R.id.button_login);
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

    // Signup Button
    final Button signupButton = (Button) findViewById(R.id.button_signup);
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

    // ========================= Facebook Login =======================================
    // TODO

    // Facebook Login
    final LoginButton facebookLogin = (LoginButton) findViewById(R.id.login_button);
    facebookLogin.setVisibility(View.GONE);

    callbackManager = CallbackManager.Factory.create();

    facebookLogin.setReadPermissions("email");
    // If using in a fragment
    // facebookLogin.setFragment(this);
    // Other app specific specialization

    // Callback registration
    facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        // App code
        Log.d("Splash", loginResult.toString());
        loginResult.getAccessToken();

        // TODO: create credentials from login result

        Intent geoIntent = new Intent(SplashScreenActivity.this, GeoActivity.class);
        startActivity(geoIntent);
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

    // ========================= Firebase User =======================================

    mAuth = FirebaseAuth.getInstance();
    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
          Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
          // User is signed out
          Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...
      }
    };

    // Check if the user has logged in
    FirebaseUser user = mAuth.getCurrentUser();
    if (user != null) {
      // Name, email address, and profile photo Url
      String name = user.getDisplayName();
      String email = user.getEmail();
      Uri photoUrl = user.getPhotoUrl();

      // The user's ID, unique to the Firebase project. Do NOT use this value to
      // authenticate with your backend server, if you have one. Use
      // FirebaseUser.getToken() instead.
      String uid = user.getUid();

      UserSingleton curUser = UserSingleton.getInstance();
      curUser.setEmail(email);
      curUser.setName(name);
      curUser.setPhotoUrl(photoUrl);
      curUser.setUid(uid);

      loginButton.setVisibility(View.GONE);
      signupButton.setVisibility(View.GONE);
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
      mHandler.postDelayed(mLaunchTask, 1000);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mAuthListener != null) {
      mAuth.removeAuthStateListener(mAuthListener);
    }
  }
}
