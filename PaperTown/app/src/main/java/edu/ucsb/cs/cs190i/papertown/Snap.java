/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown;

import java.util.List;

/**
 * Created by Zhenyu on 2017-05-21.
 */

public class Snap {

    private int mGravity;
    private String mText;
    private List<App> mApps;

    public Snap(int gravity, String text, List<App> apps) {
        mGravity = gravity;
        mText = text;
        mApps = apps;
    }

    public String getText(){
        return mText;
    }

    public int getGravity(){
        return mGravity;
    }

    public List<App> getApps(){
        return mApps;
    }

}