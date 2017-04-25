/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.monimenta.geo;

import android.net.Proxy;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.io.IOException;

import edu.ucsb.cs.cs190i.monimenta.R;
import edu.ucsb.cs.cs190i.monimenta.network.HttpClientSingleton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static edu.ucsb.cs.cs190i.monimenta.application.AppConstants.ENDPOINT;

public class GeoActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geo);

    // loading CAs from an InputStream
    OkHttpClient client = HttpClientSingleton.getInstance(getApplicationContext()).getClient();
    Request request = new Request.Builder()
        //.header("Authorization", "token abcd")
        .url(ENDPOINT)
        .build();

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
          if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
          } else {
            Log.d("response", response.body().string());
          }
        }
      });
  }
}
