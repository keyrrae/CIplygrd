/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by xuanwang on 4/21/17.
 */

public class PaperTownApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);

  }
}
