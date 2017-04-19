/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.lithoplay;

import android.app.Application;

import com.facebook.soloader.SoLoader;
import com.facebook.stetho.Stetho;

/**
 * Created by xuanwang on 4/18/17.
 */

public class LithoPlayApp extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        SoLoader.init(this, false);
    }
}
