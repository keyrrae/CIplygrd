/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.monimenta.geo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import edu.ucsb.cs.cs190i.monimenta.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeoActivity extends AppCompatActivity implements OnMapInitializedListener {

  AirMapView mapView;
  boolean mapReady = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geo);

    mapView = (AirMapView) findViewById(R.id.map_view);
    mapView.setOnMapInitializedListener(this);

    mapView.initialize(getSupportFragmentManager());

    LatLng airbnbLatLng = new LatLng(10.0, 10.0);
    addMarker("Airbnb HQ", airbnbLatLng, 2);


         // loading CAs from an InputStream
             try{
             CertificateFactory cf = CertificateFactory.getInstance("X.509");
             InputStream cert = getApplicationContext().getResources().openRawResource(R.raw.server);
             Certificate ca;
             try {
                 ca = cf.generateCertificate(cert);
               } finally { cert.close(); }

                 // creating a KeyStore containing our trusted CAs
                     String keyStoreType = KeyStore.getDefaultType();
             KeyStore keyStore = KeyStore.getInstance(keyStoreType);
             keyStore.load(null, null);
             keyStore.setCertificateEntry("ca", ca);

                 // creating a TrustManager that trusts the CAs in our KeyStore
                     String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
             TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
             tmf.init(keyStore);

                 // creating an SSLSocketFactory that uses our TrustManager
                     SSLContext sslContext = SSLContext.getInstance("TLS");
             sslContext.init(null, tmf.getTrustManagers(), null);

                 OkHttpClient okHttpClient = new OkHttpClient.Builder()
                     .sslSocketFactory(sslContext.getSocketFactory())
                     .hostnameVerifier(new HostnameVerifier() {
              @Override
              public boolean verify(String hostname, SSLSession session) {
                         if(hostname.startsWith("10.0.3.2")){
                             return true;
                           }
                         return false;
                       }
            })
                     .build();

                 Request request = new Request.Builder()
                     //.header("Authorization", "token abcd")
                     .url("https://10.0.3.2:5000")
                     .build();

                 okHttpClient.newCall(request).enqueue(new Callback() {
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
                              if(mapReady){
                                Log.d("Map", String.valueOf(mapReady));
                              }
                           }
                       }
        });
           }catch (Exception e) {
             Log.e("ca", e.toString());
           }


  }

  private void addMarker(String title, LatLng latLng, int id) {
    mapView.addMarker(new AirMapMarker.Builder()
        .id(id)
        .position(latLng)
        .title(title)
        .iconId(R.mipmap.ic_launcher)
        .build());
  }

  @Override
  public void onMapInitialized() {
    mapReady = true;
    LatLng airbnbLatLng = new LatLng(0.0, 0.0);
    addMarker("Airbnb HQ", airbnbLatLng, 1);
  }
}
