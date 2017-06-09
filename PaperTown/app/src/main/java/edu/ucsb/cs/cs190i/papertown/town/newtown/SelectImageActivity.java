/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.utils.ImageCompressor;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

@RuntimePermissions
public class SelectImageActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 112;
    final int PICK_PHOTO_REQUEST = 5;
    Uri[] imageUris;
    final int NEW_PHOTO_REQUEST = 5;
    GridView grid;
    private Town passedInTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_selectimg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.save_and_exit:

                        Log.i("dataToD", "setNavigationOnClickListener");
                        Intent returnIntent = new Intent();

                        ArrayList<Uri> uriList = new ArrayList<Uri>(Arrays.asList(imageUris));
                        Log.i("mSwitcher", "imageUris[0] = " + imageUris[0].toString());

                        //process Uri array data
                        ArrayList<String> uriStringArrayList = new ArrayList<>();
                        for (int i = 0; i < uriList.size(); i++) {
                            uriStringArrayList.add(uriList.get(i).toString());
                        }
                        passedInTown.setImageUrls(uriStringArrayList);
                        returnIntent.putExtra("result", passedInTown);
                        setResult(Activity.RESULT_FIRST_USER, returnIntent);
                        finish();
                }
                return true;
            }
        });

        passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.i("my", "permission.READ_EXTERNAL_STORAGE");
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            if (passedInTown != null) {
                List<String> dataPassIn = passedInTown.getImageUrls();
                if (dataPassIn != null) {
                Log.i("ed", "dataPassIn = " + dataPassIn);
                Log.i("ed", "dataPassIn2 = " + dataPassIn);


                    //compress the image from the uri
                    ProgressDialog progress = new ProgressDialog(SelectImageActivity.this);
                    progress.setTitle("UPLOADING");
                    progress.setMessage("Compressing Images");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                for (int i = 0; i < dataPassIn.size(); i++) {
                    Uri uri = Uri.parse(dataPassIn.get(i));
                    String[] split = uri.toString().split(":");
                    if(!split[0].equals("file")) {
                        ImageCompressor imageCompressor = new ImageCompressor();
                        uri = imageCompressor.compress(dataPassIn.get(i),getApplicationContext());
                    }

                    imageUris = addUri(imageUris, uri);

                }
                    progress.dismiss();
                }
            } else {

                String s = getIntent().getStringExtra(EXTRA_MESSAGE);
                Log.i("onActivityResult", "getStringExtra = " + s);

                //compress the image from the uri
                Uri uri = Uri.parse(s);
                ImageCompressor imageCompressor = new ImageCompressor();
                uri = imageCompressor.compress(s,getApplicationContext());
                imageUris = addUri(imageUris, uri);
            }
            SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, imageUris);
            grid = (GridView) findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("addOnItemTouchListener", "onItemClick position =" + position);
                    if (position == imageUris.length) {
                        SelectImageActivityPermissionsDispatcher.dispatchImagePickingWithCheck(SelectImageActivity.this);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_next, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_PHOTO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // TODO Crash when update more images.
                List<Uri> selected = Matisse.obtainResult(data);
                //compress the image from the uri
                ProgressDialog progress = new ProgressDialog(SelectImageActivity.this);
                progress.setTitle("UPLOADING");
                progress.setMessage("Compressing Images");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                for(int i=0; i<selected.size(); i++){
                    ImageCompressor imageCompressor = new ImageCompressor();
                    Uri uri = imageCompressor.compress(selected.get(i).toString(),getApplicationContext());
                    imageUris = addUri(imageUris, uri);
                }
                progress.dismiss();

                SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, imageUris);
                Log.i("onActivityResult", "imageUris.length = " + imageUris.length);
                grid = (GridView) findViewById(R.id.grid);
                grid.setAdapter(adapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_TITLE_REQUEST RESULT_CANCELED");
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SelectImageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Town passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
                        if (passedInTown != null) {
                            List<String> dataPassIn = passedInTown.getImageUrls();

                            //compress the image from the uri
                            ProgressDialog progress = new ProgressDialog(SelectImageActivity.this);
                            progress.setTitle("UPLOADING");
                            progress.setMessage("Compressing Images");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();

                            for (int i = 0; i < dataPassIn.size(); i++) {
                                ImageCompressor imageCompressor = new ImageCompressor();
                                Uri uri = imageCompressor.compress(dataPassIn.get(i),getApplicationContext());

                                imageUris = addUri(imageUris, uri);
                            }
                            progress.dismiss();
                        } else {
                            String s = getIntent().getStringExtra(EXTRA_MESSAGE);
                            ImageCompressor imageCompressor = new ImageCompressor();
                            Uri uri = imageCompressor.compress(s,getApplicationContext());
                            imageUris = addUri(imageUris, uri);
                        }

                    SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, imageUris);
                    grid = (GridView) findViewById(R.id.grid);
                    grid.setAdapter(adapter);
                    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Log.i("addOnItemTouchListener", "onItemClick position =" + position);
                            if (position == imageUris.length) {

                                Log.i("my", "permission.READ_EXTERNAL_STORAGE3");
                                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), PICK_PHOTO_REQUEST);//one can be replaced with any action code
                            }
                        }
                    });
                } else {
                    Log.i("my", "permission.READ_EXTERNAL_STORAGE denied");
                }
            }
        }
    }

    Uri[] addUri(Uri[] urilist, Uri in) {
        if (urilist != null) {
            Uri[] output = new Uri[urilist.length + 1];

            for (int i = 0; i < urilist.length; i++) {
                output[i] = urilist[i];
            }
            output[urilist.length] = in;
            return output;
        } else {
            Uri[] output = {in};
            return output;
        }
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showDeniedForCamera() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    public void dispatchImagePicking() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(NEW_PHOTO_REQUEST);
    }

}
