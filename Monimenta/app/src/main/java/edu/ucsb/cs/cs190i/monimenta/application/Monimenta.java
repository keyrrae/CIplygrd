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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuanwang on 4/21/17.
 */

public class Monimenta extends Application {

    private static Map<String, Activity> destoryMap = new HashMap<>();

     /**
     * Add to destroy map
     *
     * @param activity the activity to finish
     */

    public static void addDestoryActivity(String activityName, Activity activity) {
        destoryMap.put(activityName, activity);
    }
    /**
     *销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        destoryMap.get(activityName).finish();
        destoryMap.remove(activityName);
    }
}
