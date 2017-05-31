/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.towndetail;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import edu.ucsb.cs.cs190i.papertown.ImageAdapter;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.TownMapIcon;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;
import edu.ucsb.cs.cs190i.papertown.town.newtown.myMapFragment;


public class TownDetailActivity extends AppCompatActivity {

    private GridView imageGrid;
    private ArrayList<Uri> uriList;

    private String mode = "detail";

    private String title = "";
    private String address = "";
    private String category = "";
    private String description = "";
    private String information = "";
    private ArrayList<String> uriStringArrayList;

    private TownMapIcon tmi;

    private float lat = 34.415320f;
    private float lng = -119.84023f;

    private List<String> remoteImageUrls = new ArrayList<>();
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference townRef;
    private Town passedInTown;

    private Integer[] mImageIds = {
            R.drawable.door, R.drawable.light, R.drawable.corner,
            R.drawable.mc, R.drawable.light, R.drawable.door,
            R.drawable.light, R.drawable.corner};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setElevation(0);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("detail_to_main", "back");
                finish();
            }
        });

        this.imageGrid = (GridView) findViewById(R.id.detail_image_grid);
        this.uriList = new ArrayList<Uri>();

        mode = getIntent().getStringExtra("mode");
        if (mode == null) {
            mode = "detail";
        }
        passedInTown = (Town) getIntent().getSerializableExtra("town");

        passedInTown.setUserId(UserSingleton.getInstance().getUid());

        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        townRef = database.getReference("towns");

        Button button_test_detail = (Button) findViewById(R.id.button_test_detail);
        button_test_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("dataToD", "button_test_detail OnClickListener");

                if (mode.equals("preview")) {
                    Log.i("dataToD", "SUBMIT!");

                    final ProgressDialog progress = new ProgressDialog(TownDetailActivity.this);
                    progress.setTitle("UPLOADING");
                    progress.setMessage("Wait while uploading your town...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    if (storage != null) {
                        StorageReference storageRef = storage.getReference();

                        for (Uri uri : uriList) {
                            StorageReference riversRef = storageRef.child("images/" + uri.getLastPathSegment());
                            UploadTask uploadTask = riversRef.putFile(uri);

                            // Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    @SuppressWarnings("VisibleForTests")
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    if (downloadUrl != null) {
                                        remoteImageUrls.add(downloadUrl.toString());
                                        if (remoteImageUrls.size() == uriList.size()) {
                                            passedInTown.setImageUrls(remoteImageUrls);
                                            DatabaseReference newTown = townRef.child(passedInTown.getId());
                                            // Town town = townBuilder.build();
                                            newTown.setValue(passedInTown, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError,
                                                                       DatabaseReference databaseReference) {

                                                    // To dismiss the spinner dialog
                                                    progress.dismiss();

                                                    Toast.makeText(
                                                            TownDetailActivity.this,
                                                            "Successfully submitted network",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else {
                    finish();
                }
            }
        });


        if (passedInTown != null) {
            Log.i("dataToD", "passedInTown getDescription = " + passedInTown.getTitle().toString());
            title = passedInTown.getTitle();
            address = passedInTown.getAddress();
            description = passedInTown.getDescription();
            category = passedInTown.getCategory();
            information = passedInTown.getUserAlias();
            uriStringArrayList = new ArrayList<String>(passedInTown.getImageUrls());

        }

        //change button color
        if (mode != null && mode.equals("preview")) {
            //change color of submission button
            button_test_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryPink));
            button_test_detail.setText("SUBMIT !");


        } else {
            //change color of submission button
            button_test_detail.setVisibility(Button.INVISIBLE);
        }


        //process uriStringArrayList, put data into uriList
        if (uriStringArrayList != null && uriStringArrayList.size() > 0) {
            for (int i = 0; i < uriStringArrayList.size(); i++) {
                uriList.add(Uri.parse(uriStringArrayList.get(i)));
//                Log.i("manu", "uriList[" + i + "] = " + uriList.get(i));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot get images, default images used!", Toast.LENGTH_SHORT).show();
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
            this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));

        }

        //load title
        if (title != null) {
            TextView detail_town_description = (TextView) findViewById(R.id.detail_town_title);
            detail_town_description.setText(title);
            // townBuilder.setTitle(title);
        }

        //load address and physical address
        if (address != null) {
            TextView detail_town_description = (TextView) findViewById(R.id.detail_address);
            detail_town_description.setText(address);
            // townBuilder.setAddress(address);

            //processing address to latlng
            String[] separated = address.split(",");
            if (separated.length > 0) {
                lat = Float.parseFloat(separated[0]);
                lng = Float.parseFloat(separated[1]);
                //   townBuilder.setLatLng(lat, lng);
            }

            TextView detail_physical_address = (TextView) findViewById(R.id.detail_physical_address);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                detail_physical_address.setText(address+"\n"+city+", "+state+", "+country+", "+postalCode);
            }
            catch (Exception e){
                Toast.makeText(
                        TownDetailActivity.this,
                        "Unable to obtain the address from the GPS coordinates.",
                        Toast.LENGTH_SHORT
                ).show();
                detail_physical_address.setText("NOT AVAILABLE");
            }

        }

        //load description
        if (description != null) {
            TextView detail_town_description = (TextView) findViewById(R.id.detail_town_description);
            detail_town_description.setText(description);
            //   townBuilder.setDescription(description);
        }

        //load category
        if (category != null) {
            TextView detail_town_description = (TextView) findViewById(R.id.detail_town_category);
            detail_town_description.setText(category);
            //  townBuilder.setCategory(category);
        }

        //load information
        if (information != null) {
            TextView detail_town_description = (TextView) findViewById(R.id.detail_town_information);
            detail_town_description.setText(information);
            //   townBuilder.setUserAlias(information);
        }

        //load uriStringArrayList
        if (uriList != null) {
            if (uriList.size() > 0) {
                final ImageView detail_town_image = (ImageView) findViewById(R.id.detail_town_image);
                detail_town_image.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getApplicationContext()).load(uriList.get(0))
                                .resize(detail_town_image.getMeasuredWidth(), detail_town_image.getMeasuredHeight())
                                .centerCrop()
                                .into(detail_town_image);
                    }
                });

                this.imageGrid.setAdapter(new ImageAdapter(this, uriList));
            }
        }
        //handle the google Maps

        myMapFragment mapFragment = ((myMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map));

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    //enable myLocationButton
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        //map.getUiSettings().setMyLocationButtonEnabled(true);
                    } else {
                        Log.i("manu", "Error - checkSelfPermission!!");
                    }
                    //end of enabling myLocationButton

                    //add markers
                    if (category != null && !category.isEmpty()) {
                        tmi = new TownMapIcon(getApplicationContext(), category, false);
                        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                                .title(title)
                                .snippet(category)
                                .icon(BitmapDescriptorFactory.fromBitmap(tmi.getIconBitmap())));
                    }
                    //end of adding markers


                    //camera animation
                    if (map != null) {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));  //add animation
                    }
                    //end of camera animation

                }
            });

            mapFragment.setListener(new myMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollView_detail);
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
            });


        } else {
            Log.i("manu", "Error - Map Fragment was null!!");
        }
    }
}
