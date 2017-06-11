

/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import edu.ucsb.cs.cs190i.papertown.R;

import static edu.ucsb.cs.cs190i.papertown.town.newtown.SelectImageActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

/**
 * Created by Zhenyu on 2017-05-17.
 */

public class SelelctImageGrid extends BaseAdapter {
    private Context mContext;
    private Uri[] ImageUris;
    ImageView imageView;
    int imageViewSize = 100;

    public SelelctImageGrid(Context c, Uri[] ImageUris) {
        mContext = c;
        this.ImageUris = ImageUris;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ImageUris.length + 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            if (position != ImageUris.length) {
//                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                imageView = (ImageView) grid.findViewById(R.id.grid_image);
                Log.i("addOnItemTouchListener", "ImageUris[" + position + "] = " + ImageUris[position]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((SelectImageActivity) mContext,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Log.i("my", "permission.READ_EXTERNAL_STORAGE");

                        }
                        ActivityCompat.requestPermissions((SelectImageActivity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        Log.i("my", "permission.READ_EXTERNAL_STORAGE3");
                        Picasso.with(mContext).load(ImageUris[position])
                                .resize(imageViewSize, imageViewSize)
                                .centerCrop()
                                .error(R.drawable.defaultimage)
                                .placeholder(R.drawable.defaultimage)
                                .into(imageView);
                    }
                }
            } else {
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                imageView.setBackgroundColor(Color.LTGRAY);
                imageView.setImageResource(R.drawable.ic_add_white_18dp);
            }
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}