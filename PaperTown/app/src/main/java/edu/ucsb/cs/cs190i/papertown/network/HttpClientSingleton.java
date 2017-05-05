/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.network;

import android.content.Context;
import android.util.Log;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;

import java.security.cert.X509Certificate;

import edu.ucsb.cs.cs190i.papertown.auth.BasicAuthInterceptor;
import edu.ucsb.cs.cs190i.papertown.auth.SSLContextHolder;
import okhttp3.OkHttpClient;

import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.HOST_NAME_HEROKU;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.HOST_NAME_LOCAL;

/**
 * Created by xuanwang on 4/24/17.
 */

public class HttpClientSingleton {
  private static HttpClientSingleton mInstance;
  private final OkHttpClient okHttpClient;

  private HttpClientSingleton(Context context, String uid, String token){

    // REF: http://stackoverflow.com/questions/19005318/implementing-x509trustmanager-passing-on-part-of-the-verification-to-existing
    X509TrustManager customTm = null;
    try {
      TrustManagerFactory tmf = TrustManagerFactory
          .getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init((KeyStore) null);

      // Get hold of the default trust manager
      X509TrustManager x509Tm = null;
      for (TrustManager tm : tmf.getTrustManagers()) {
        if (tm instanceof X509TrustManager) {
          x509Tm = (X509TrustManager) tm;
          break;
        }
      }

      // Wrap it in your own class.
      final X509TrustManager finalTm = x509Tm;
      customTm = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return finalTm.getAcceptedIssuers();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {

          finalTm.checkServerTrusted(chain, authType);
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
          finalTm.checkClientTrusted(chain, authType);
        }
      };
    }catch (Exception e){
      Log.e("TrustManager", e.toString());
    }

    if(customTm != null) {
      okHttpClient = new OkHttpClient.Builder()
          // TODO: how to distinguish localhost and remote server?
          .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
              Log.d("hostname", hostname);
              return hostname.startsWith(HOST_NAME_LOCAL) || hostname.equals(HOST_NAME_HEROKU);
            }
          }).addInterceptor(new BasicAuthInterceptor(uid, token))
          .build();

    } else {
      okHttpClient = new OkHttpClient.Builder()
          .sslSocketFactory(
              SSLContextHolder
                  .getInstance(context)
                  .getSocketFactory())
          .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
              return hostname.equals("10.0.3.2");
            }
          })//.addInterceptor(new BasicAuthInterceptor("signup", "signup"))
          .build();
    }
  }


  public static synchronized HttpClientSingleton getInstance(Context context) {
    if(mInstance == null){
      mInstance = new HttpClientSingleton(context.getApplicationContext(), "admin", "admin");
    }
    return mInstance;
  }

  public static synchronized HttpClientSingleton initializeInstance(Context context,  String uid, String token){
    if(mInstance == null){
      mInstance = new HttpClientSingleton(context.getApplicationContext(), uid, token);
    }
    return mInstance;
  }
}
