/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.towndetail;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.ucsb.cs.cs190i.papertown.ImageAdapter;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.town.newtown.myMapFragment;

public class TownDetailActivity extends AppCompatActivity {

  private GridView imageGrid;
  private ArrayList<Uri> uriList;

    private String mode = "detail";

  String title = "";
  String address = "";
  String category = "";
  String description = "";
  String information = "";
  ArrayList<String> uriStringArrayList;

  float lat = 34.415320f;
  float lng = -119.84023f;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_town_detail);



    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail); // Attaching the layout to the toolbar object
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(null);
    toolbar.setTitle("");
    toolbar.setSubtitle("");
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        Log.i("dataToD", "setNavigationOnClickListener");
        finish();
      }
    });



    this.imageGrid = (GridView) findViewById(R.id.detail_image_grid);
    this.uriList = new ArrayList<Uri>();
//    this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
//    this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));


      Button button_test_detail = (Button) findViewById(R.id.button_test_detail);
      button_test_detail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.i("dataToD", "button_test_detail OnClickListener");



              Town testTown = new  Town.Builder()
                      .setTitle(title)
                      .setAddress(address)
                      .setCategory(category)
                      .setDescription(description)
                      .setLat(lat)
                      .setLng(lng)
                      .setImages(uriStringArrayList)
                      .build();


              if(mode.equals("preview")){
                  Log.i("dataToD", "SUBMIT!");
              }
              else{
                  finish();
              }



              Log.i("dataToD", "button_test_detail OnClickListener");
                      //.setInformation   ???
          }
      });




    String s = getIntent().getStringExtra("dataToD");
    Log.i("dataToD", "data = "+s);

    title = getIntent().getStringExtra("title");
    address = getIntent().getStringExtra("address");
    description = getIntent().getStringExtra("description");
    category = getIntent().getStringExtra("category");
    information = getIntent().getStringExtra("information");
    uriStringArrayList = getIntent().getStringArrayListExtra("uriStringArrayList");
      mode = getIntent().getStringExtra("mode");
      if(mode==null){
          mode = "detail";
      }



      //change button color
      if(mode!=null&&mode.equals("preview")) {
          //change color of submission button
          button_test_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
          button_test_detail.setText("SUBMIT !");
      }
      else{
          //change color of submission button
//          button_test_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//          button_test_detail.setText("DONE");
        button_test_detail.setVisibility(Button.INVISIBLE);
      }


    //process uriStringArrayList, put data into uriList
    if(uriStringArrayList!=null&&uriStringArrayList.size()>0) {
      for (int i = 0; i < uriStringArrayList.size(); i++) {
        uriList.add(Uri.parse(uriStringArrayList.get(i)));
        Log.i("manu", "uriList["+i+"] = "+uriList.get(i));
      }
    }
    else{
      Toast.makeText(getApplicationContext(), "Cannot get images, default images used!", Toast.LENGTH_SHORT).show();
      this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
      this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));

    }

    //load title
    if(title!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_town_title);
      detail_town_description.setText(title);
    }

    //load address
    if(address!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_address);
      detail_town_description.setText(address);

        //processing address to latlng
        String[] separated = address.split(",");
        lat = Float.parseFloat(separated[0]);
        lng = Float.parseFloat(separated[1]);
    }



    //load description
    if(description!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_town_description);
      detail_town_description.setText(description);
    }



    //load category
    if(category!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_town_category);
      detail_town_description.setText(category);
    }

    //load information
    if(information!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_town_information);
      detail_town_description.setText(information);
    }


    //load uriStringArrayList
    if(uriList!=null) {
      if(uriList.size()>0) {
        ImageView detail_town_image = (ImageView) findViewById(R.id.detail_town_image);
        Picasso.with(this).load(uriList.get(0))
                .fit()
                .into(detail_town_image);
        this.imageGrid.setAdapter(new ImageAdapter(this, uriList));
      }
    }



    //adjust the gridView hright





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
          }
          else {
            Log.i("manu", "Error - checkSelfPermission!!");
          }
          //end of enabling myLocationButton

          BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker);
          BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker3);
          BitmapDescriptor icon3 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker2);


          if(category!=null&&!category.isEmpty()) {
            if (category.equals("place")) {
              //add markers
              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Ohh!")
                      .snippet("TsadADSadsA")
                      .icon(icon1));
            } else if (category.equals("creature")) {

              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Underclass beauty")
                      .snippet("Get sunburn in my head")
                      .icon(icon2));
            } else if (category.equals("event")) {
              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Big thing!")
                      .snippet("Meat carnival")
                      .icon(icon3));

              //end of adding markers
            }
          }

          //camera animation
          //map.moveCamera(CameraUpdateFactory.newLatLngZoom(/*some location*/, 10));

          if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));  //add animation
          }


          //end of camera animation


        }
      });

      mapFragment.setListener(new myMapFragment.OnTouchListener() {
        @Override
        public void onTouch() {
          ScrollView mScrollView = (ScrollView)findViewById(R.id.scrollView_detail);
          mScrollView.requestDisallowInterceptTouchEvent(true);
        }
      });


    } else {

      Log.i("manu", "Error - Map Fragment was null!!");
    }






  }







}
