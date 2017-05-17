

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

import edu.ucsb.cs.cs190i.papertown.R;

import static edu.ucsb.cs.cs190i.papertown.town.newtown.SelectImageActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

/**
 * Created by Zhenyu on 2017-05-17.
 */

public class SelelctImageGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private  int[] Imageid;
    private  Uri[] ImageUris;
    ImageView imageView;




    public SelelctImageGrid(Context c,String[] web,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    public SelelctImageGrid(Context c,String[] web,Uri[] ImageUris ) {
        mContext = c;
        this.ImageUris = ImageUris;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ImageUris.length+1;
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
            if(position!=ImageUris.length) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                //TextView textView = (TextView) grid.findViewById(R.id.grid_text);
                imageView = (ImageView) grid.findViewById(R.id.grid_image);
                //textView.setText(web[position]);
                //imageView.setImageResource(Imageid[position]);
                //imageView.setImageURI(ImageUris[position]);
                //imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.test));
                Log.i("addOnItemTouchListener", "ImageUris["+position+"] = "+ImageUris[position]);





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
                        imageView.setImageURI(ImageUris[position]);
//                Picasso.with(mContext).load(ImageUris[position])
//                        .resize(20,20)
//                        .fit()
//                        .into(imageView);
                    }
                }








//                imageView.setImageURI(ImageUris[position]);
////                Picasso.with(mContext).load(ImageUris[position])
////                        .resize(20,20)
////                        .fit()
////                        .into(imageView);









            }
            else{
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_single_newtown_selectimage, null);
                //TextView textView = (TextView) grid.findViewById(R.id.grid_text);
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                //textView.setText(web[position]);
                imageView.setBackgroundColor(Color.GRAY);
                imageView.setImageResource(R.drawable.ic_add_white_24dp);
                //imageView.setImageURI(ImageUris[position]);
            }
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        Log.i("my", "permission requestCode = "+requestCode);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("my", "permission.READ_EXTERNAL_STORAGE2");
//                    //normal request goes here
//                    imageView.setImageURI(ImageUris[position]);
////                Picasso.with(mContext).load(ImageUris[position])
////                        .resize(20,20)
////                        .fit()
////                        .into(imageView);
//
//                } else {
//                    Log.i("my", "permission.READ_EXTERNAL_STORAGE denied");
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
////                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
}