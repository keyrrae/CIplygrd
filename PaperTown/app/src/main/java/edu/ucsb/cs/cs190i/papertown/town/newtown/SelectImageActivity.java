/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.ucsb.cs.cs190i.papertown.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

import edu.ucsb.cs.cs190i.papertown.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SelectImageActivity extends AppCompatActivity
{
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 112;
    final int PICK_PHOTO_REQUEST = 5;
    Uri[] imageUris;
    final int NEW_PHOTO_REQUEST = 5;
    GridView grid;
    String[] web = {
            "Google"
    } ;

    //TODO
    //Need to fix the gridView

    int[] imageId = {
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test,
            R.drawable.test

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_selectimg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Log.i("dataToD", "setNavigationOnClickListener");
                Intent returnIntent = new Intent();

                ArrayList<Uri> uriList = new ArrayList<Uri>(Arrays.asList(imageUris)); //new ArrayList is only needed if you absolutely need an ArrayList
                Log.i("mSwitcher", "imageUris[0] = "+imageUris[0].toString());
                returnIntent.putParcelableArrayListExtra    ("multipleImage", uriList);
                //returnIntent.putExtra("result", imageUris[0].toString());
                setResult(Activity.RESULT_FIRST_USER, returnIntent);
                finish();

                //if don't want to return data:
//                Intent returnIntent = new Intent();
//                setResult(Activity.RESULT_CANCELED, returnIntent);
//                finish();
            }
        });



        String s = getIntent().getStringExtra(EXTRA_MESSAGE);
        Log.i("onActivityResult", "getStringExtra = " + s);

//         imageUris = { Uri.parse(s),
//                Uri.parse(s),
//                Uri.parse(s)};

        imageUris = addUri(imageUris,Uri.parse(s));
        //Uri[] imageUrisWithAdd = addUri(imageUris,Uri.parse(s));
        SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, web, imageUris);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("addOnItemTouchListener", "onItemClick position =" + position);


                //respond to the add more image event
                if(position == imageUris.length){

                    Log.i("addOnItemTouchListener", "last one!");

//                    //stat camera roll
//                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) , NEW_PHOTO_REQUEST);//one can be replaced with any action code

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {


                            // Should we show an explanation?
                            if (shouldShowRequestPermissionRationale(
                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                // Explain to the user why we need to read the contacts
                                Log.i("my", "permission.READ_EXTERNAL_STORAGE");

                            }

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant

//                        return;
                        }
                        else{
                            Log.i("my", "permission.READ_EXTERNAL_STORAGE3");
                            //normal request goes here
                            Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), PICK_PHOTO_REQUEST);//one can be replaced with any action code
                        }
                    }



                }
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });







//        //add button
//        Button button = (Button) findViewById(R.id.button_new_image_done);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//
//
//            }
//
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to


        if (requestCode == NEW_PHOTO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri selectedImageURI = data.getData();
                Log.i("onActivityResult", "result = " + selectedImageURI.toString());

                //Uri[] imageUris = { selectedImageURI,selectedImageURI,selectedImageURI};
                imageUris = addUri(imageUris,selectedImageURI);

                SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, web, imageUris);
                Log.i("onActivityResult", "imageUris.length = " + imageUris.length);
                grid=(GridView)findViewById(R.id.grid);
                grid.setAdapter(adapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_TITLE_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("my", "permission requestCode = "+requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("my", "permission.READ_EXTERNAL_STORAGE2");

                    //to start activity after the first time asking for permission
                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), PICK_PHOTO_REQUEST);//one can be replaced with any action code
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("my", "permission.READ_EXTERNAL_STORAGE denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
//                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    Uri[] addUri(Uri[] urilist, Uri in) {
        if(urilist!=null) {
            Uri[] output = new Uri[urilist.length+1];

            for (int i = 0; i < urilist.length; i++) {
                output[i] = urilist[i];
            }
            output[urilist.length] = in;
            return output;
        }
        else{
            Uri[] output = {in};
            return output;
        }



    }

}
