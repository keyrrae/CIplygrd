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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.util.Calendar;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.application.AppConstants;
import edu.ucsb.cs.cs190i.papertown.auth.BasicAuthInterceptor;
import edu.ucsb.cs.cs190i.papertown.geo.GeoActivity;
import edu.ucsb.cs.cs190i.papertown.login.LoginActivity;
import edu.ucsb.cs.cs190i.papertown.models.UserToken;
import edu.ucsb.cs.cs190i.papertown.network.HttpClientSingleton;
import edu.ucsb.cs.cs190i.papertown.signup.SignupActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.CRED;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.EMAIL;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.ENDPOINT;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.JSON_BODY;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.PREF_NAME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN_LIFETIME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN_TIME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.USERID;

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

    // Facebook Login
    final LoginButton facebookLogin = (LoginButton) findViewById(R.id.login_button);
    facebookLogin.setVisibility(View.GONE);

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
        Log.d("Splash", loginResult.toString());
        loginResult.getAccessToken();

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

    final String userid = sharedPreferences.getString(USERID, "");
    if(!userid.equals("")){
      signupButton.setVisibility(View.GONE);
      loginButton.setVisibility(View.GONE);
      facebookLogin.setVisibility(View.GONE);

      Handler mHandler = new Handler();

      int tokenTime = sharedPreferences.getInt(TOKEN_TIME, 0);

      Calendar c = Calendar.getInstance();
      int now = c.get(Calendar.SECOND);

      if(now - tokenTime > TOKEN_LIFETIME){
        String email = sharedPreferences.getString(EMAIL, "");
        String cred = sharedPreferences.getString(CRED, "");
        // TODO: request token
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new BasicAuthInterceptor("admin", "admin"))
            .build();

        String jsonRequest = "{ \"email\": \"" + email + "\", \"cred\": \"" + cred + "\" }";

        Log.d("request", jsonRequest);

        RequestBody body = RequestBody.create(JSON_BODY, jsonRequest);
        Request request = new Request.Builder()
            .url(ENDPOINT + "auth")
            .post(body)
            .build();

        client.newCall(request).enqueue(new Callback() {
          @Override public void onFailure(Call call, IOException e) {
            e.printStackTrace();
          }

          @Override public void onResponse(Call call, Response response) throws IOException {
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            if (!response.isSuccessful()) {
              editor.remove(TOKEN);
              editor.remove(TOKEN_TIME);
              editor.remove(USERID);
              editor.remove(EMAIL);
              editor.remove(CRED);
              editor.commit();

              SplashScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                  signupButton.setVisibility(View.VISIBLE);
                  loginButton.setVisibility(View.VISIBLE);
                  //facebookLogin.setVisibility(View.GONE);
                }
              });
              return;
            }

            final Gson gson = new Gson();
            // Get a handler that can be used to post to the main thread
            // Parse response using gson deserializer
            // Process the data on the worker thread

            final UserToken ut = gson.fromJson(response.body().string(), UserToken.class);

            editor.putString(TOKEN, ut.getToken());
            Calendar c = Calendar.getInstance();
            int now = c.get(Calendar.SECOND);
            editor.putInt(TOKEN_TIME, now);

            editor.commit();
            HttpClientSingleton.initializeInstance(getApplicationContext(), userid, ut.getToken());
          }
        });

      } else {
        // Initialize OkHttpClient
        String token = sharedPreferences.getString(TOKEN, "");
        HttpClientSingleton.initializeInstance(getApplicationContext(), userid, token);

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
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
