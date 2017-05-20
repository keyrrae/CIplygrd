

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
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;

import static edu.ucsb.cs.cs190i.papertown.town.newtown.SelectImageActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

/**
 * Created by Zhenyu on 2017-05-17.
 */

public class SelectImageGridAdapter extends BaseAdapter {
    private Context mContext;
  private List<Uri> imageUris = new ArrayList<>();
    ImageView imageView;

  public SelectImageGridAdapter(Context c, List<Uri> imageUris ) {
    mContext = c;
    this.imageUris.addAll(imageUris);
  }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUris.size() + 1;
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
            if(position!=imageUris.size()) {
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                imageView = (ImageView) grid.findViewById(R.id.grid_image);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale((SelectImageActivity)mContext,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            // Explain to the user why we need to read the contacts
                            Log.i("my", "permission.READ_EXTERNAL_STORAGE");

                        }

                        ActivityCompat.requestPermissions((SelectImageActivity)mContext,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant

//                        return;
                    }
                    else{
                        Log.i("my", "permission.READ_EXTERNAL_STORAGE3");
                        //normal request goes here
                        //imageView.setImageURI(ImageUris[position]);
                Picasso.with(mContext).load(imageUris.get(position))
                        .fit()
                        .into(imageView);
                    }
                }

            }
            else{
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                imageView.setBackgroundColor(Color.GRAY);
                imageView.setImageResource(R.drawable.ic_add_white_24dp);
            }
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}