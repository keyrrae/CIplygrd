package edu.ucsb.cs.cs190i.papertown;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TownMapIcon {

    private Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    private Bitmap iconBitmap;

    private int resourceWidth;
    private int resourceHeight;
    private Resources resources;
    private String category;
    private int padding = 40;
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
                iconDrawableId = R.drawable.ic_location_city_black_24dp;
            } else if (category.equals("creature")) {
                iconDrawableId = R.drawable.ic_pets_black_24dp;
            } else if (category.equals("event")) {
                iconDrawableId = R.drawable.ic_chat_black_24dp;
            }
        }
        else{
            backgroundColor = resources.getColor(R.color.PrimaryPink);
            if (category.equals("place")) {
                iconDrawableId = R.drawable.ic_location_city_white_24dp;
            } else if (category.equals("creature")) {
                iconDrawableId = R.drawable.ic_pets_white_24dp;
            } else if (category.equals("event")) {
                iconDrawableId = R.drawable.ic_chat_white_24dp;
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
        color.setColor(backgroundColor);
        color.setStrokeWidth(1);

        canvas1.drawCircle(canvas1.getWidth() / 2, canvas1.getHeight() / 2, canvas1.getWidth()/2 , color);

        // modify canvas
        //canvas1.drawColor(backgroundColor);
        canvas1.drawBitmap(BitmapFactory.decodeResource(resources,iconDrawableId), ((canvas1.getWidth() - resourceWidth) / 2), ((canvas1.getHeight() - resourceHeight) / 2), color);

        return iconBitmap;
    }
}