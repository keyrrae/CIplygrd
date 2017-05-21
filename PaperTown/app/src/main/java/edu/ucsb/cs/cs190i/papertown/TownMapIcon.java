/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Zhenyu on 2017-05-21.
 */

public class TownMapIcon {

    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    Bitmap iconBitmap;

    private int resourceWidth;
    private int resourceHeight;

    public TownMapIcon(Resources res, int resourceId, int padding) {

        resourceWidth = BitmapFactory.decodeResource(res, resourceId).getWidth();
        resourceHeight = BitmapFactory.decodeResource(res, resourceId).getHeight();

        iconBitmap = Bitmap.createBitmap(resourceWidth + padding, resourceHeight + padding, conf);
        Canvas canvas1 = new Canvas(iconBitmap);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        // modify canvas
        canvas1.drawColor(Color.WHITE);
        canvas1.drawBitmap(BitmapFactory.decodeResource(res,resourceId), ((canvas1.getWidth() - resourceWidth) / 2), ((canvas1.getHeight() - resourceHeight) / 2), color);
    }

    public Bitmap getIconBitmap(){
        return iconBitmap;
    }


}
