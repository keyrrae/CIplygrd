/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.monimenta.application;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuanwang on 4/21/17.
 */

public class Monimenta extends Application {
  private static Map<String, Activity> destoryMap = new HashMap<>();

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);

  }

  /**
   * Add to destroy map
   *
   * @param activity the activity to finish
   */

  public static void addDestroyActivity(String activityName, Activity activity) {
    destoryMap.put(activityName, activity);
  }
  /**
   *destroy an activity
   */
  public static void destroyActivity(String activityName) {
    destoryMap.get(activityName).finish();
    destoryMap.remove(activityName);
  }
}
