/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.auth;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import edu.ucsb.cs.cs190i.papertown.R;

/**
 * Created by xuanwang on 4/23/17.
 *
 */

public class SSLContextHolder {

  private static SSLContextHolder mInstance;
  private SSLContext sslContext;

  private SSLContextHolder(Context context){
    try {
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      InputStream cert = context.getResources().openRawResource(R.raw.server);
      Certificate ca;
      try {
        ca = cf.generateCertificate(cert);
      } finally {
        cert.close();
      }

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
      sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, tmf.getTrustManagers(), null);
    } catch (Exception e){
      Log.e("SSL", e.toString());
    }
  }

  public static synchronized SSLContextHolder getInstance(Context context) {
    if(mInstance == null){
      mInstance = new SSLContextHolder(context.getApplicationContext());
    }
    return mInstance;
  }

  public SSLSocketFactory getSocketFactory() {
    return sslContext.getSocketFactory();
  }
}
