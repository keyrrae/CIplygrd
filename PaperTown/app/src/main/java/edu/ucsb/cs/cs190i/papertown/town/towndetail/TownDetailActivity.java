/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.towndetail;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.ucsb.cs.cs190i.papertown.ImageAdapter;
import edu.ucsb.cs.cs190i.papertown.R;

public class TownDetailActivity extends AppCompatActivity {

  private GridView imageGrid;
  private ArrayList<Uri> uriList;


  String title = "";
  String address = "";
  String category = "";
  String description = "";
  String information = "";
//  Uri[] uriList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_town_detail);





    this.imageGrid = (GridView) findViewById(R.id.detail_image_grid);
    this.uriList = new ArrayList<Uri>();
    this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
    this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));

    String s = getIntent().getStringExtra("dataToD");
    Log.i("dataToD", "data = "+s);

    title = getIntent().getStringExtra("title");
    address = getIntent().getStringExtra("address");
    description = getIntent().getStringExtra("description");
    category = getIntent().getStringExtra("category");
    information = getIntent().getStringExtra("information");

    //load title
    if(title!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_town_title);
      detail_town_description.setText(title);
    }

    //load address
    if(address!=null) {
      TextView detail_town_description = (TextView) findViewById(R.id.detail_address);
      detail_town_description.setText(address);
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


    //load images
    if(uriList!=null) {
      if(uriList.size()>0) {
        ImageView detail_town_image = (ImageView) findViewById(R.id.detail_town_image);
        Picasso.with(this).load(uriList.get(0))
                .fit()
                .into(detail_town_image);
        this.imageGrid.setAdapter(new ImageAdapter(this, this.uriList));
      }
    }
  }
}
