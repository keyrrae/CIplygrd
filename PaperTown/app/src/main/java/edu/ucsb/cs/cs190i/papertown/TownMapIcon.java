/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown;

import android.content.Context;
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

    private Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    private Bitmap iconBitmap;

    private int resourceWidth;
    private int resourceHeight;
    private Resources resources;
    private String category;
    private int padding = 5;
    private int iconDrawableId;
    private boolean ifPressed = false;
    private int backgroundColor;

    public TownMapIcon(Context contextInput, String categoryInput, boolean ifPressedInput) {
        resources = contextInput.getResources();
        category = categoryInput;
        ifPressed = ifPressedInput;
    }

    private void setIconDrawableIdAndBackgroundColor(){

        if(!ifPressed) {
            backgroundColor = Color.WHITE;
            if (category.equals("place")) {
                iconDrawableId = R.drawable.ic_place_black_18dp;
            } else if (category.equals("creature")) {
                iconDrawableId = R.drawable.ic_traffic_black_18dp;
            } else if (category.equals("event")) {
                iconDrawableId = R.drawable.ic_chat_black_18dp;
            }
        }
        else{
            backgroundColor = resources.getColor(R.color.Medium_Green);
            if (category.equals("place")) {
                iconDrawableId = R.drawable.ic_place_white_18dp;
            } else if (category.equals("creature")) {
                iconDrawableId = R.drawable.ic_traffic_white_18dp;
            } else if (category.equals("event")) {
                iconDrawableId = R.drawable.ic_chat_white_18dp;
            }
        }
    }

    public Bitmap getIconBitmap(){
        setIconDrawableIdAndBackgroundColor();
        resourceWidth = BitmapFactory.decodeResource(resources,iconDrawableId).getWidth();
        resourceHeight = BitmapFactory.decodeResource(resources,iconDrawableId).getHeight();

        iconBitmap = Bitmap.createBitmap(resourceWidth + padding, resourceHeight + padding, conf);
        Canvas canvas1 = new Canvas(iconBitmap);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        // modify canvas
        canvas1.drawColor(backgroundColor);
        canvas1.drawBitmap(BitmapFactory.decodeResource(resources,iconDrawableId), ((canvas1.getWidth() - resourceWidth) / 2), ((canvas1.getHeight() - resourceHeight) / 2), color);

        return iconBitmap;
    }
}