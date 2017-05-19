/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.townlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.ListTownAdapter;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.RecyclerItemClickListener;
import edu.ucsb.cs.cs190i.papertown.geo.GeoActivity;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.splash.SplashScreenActivity;
import edu.ucsb.cs.cs190i.papertown.town.newtown.NewTownActivity;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;

import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.CRED;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.EMAIL;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.PREF_NAME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN_TIME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.USERID;

public class TownListActivity extends AppCompatActivity {
  public List<Town> towns;
  private GridView imageGrid;
  private ArrayList<Bitmap> bitmapList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_town_list);

    //

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list_town);
    //mRecyclerView.setHasFixedSize(true);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(linearLayoutManager);

    initData();

    ListTownAdapter mAdapter = new ListTownAdapter(towns);
    mRecyclerView.setAdapter(mAdapter);

    mRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
              @Override public void onItemClick(View view, int position) {
                Log.i("RecyclerItemClr", "onItemClick");

                Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                startActivity(intent);


                // do whatever
              }

              @Override public void onLongItemClick(View view, int position) {
                Log.i("RecyclerItemClr", "onLongItemClick");
                // do whatever
              }
            })
    );


    //
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
          case R.id.add_town:
            Intent newTownIntent = new Intent(TownListActivity.this, NewTownActivity.class);
            startActivity(newTownIntent);
            break;
          case R.id.geo_view:
            Intent geoTownIntent = new Intent(TownListActivity.this, GeoActivity.class);
            startActivity(geoTownIntent);
            break;
          case R.id.action_settings:
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.remove(TOKEN);
            editor.remove(TOKEN_TIME);
            editor.remove(USERID);
            editor.remove(EMAIL);
            editor.remove(CRED);
            editor.commit();
            Intent splashIntent = new Intent(TownListActivity.this, SplashScreenActivity.class);
            startActivity(splashIntent);
            finish();
            break;
        }
        return true;
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_list, menu);
    return true;
  }

  private void initData() {
    towns = new ArrayList<>();

    List<String> imgs1 = new ArrayList<>();
    imgs1.add("https://s-media-cache-ak0.pinimg.com/564x/58/82/11/588211a82d4c688041ed5bf239c48715.jpg");

    List<String> imgs2 = new ArrayList<>();
    imgs2.add("https://s-media-cache-ak0.pinimg.com/564x/5f/d1/3b/5fd13bce0d12da1b7480b81555875c01.jpg");

    List<String> imgs3 = new ArrayList<>();
    imgs3.add("http://68.media.tumblr.com/132947bb8b5319f81f8a77d3c83b3fbf/tumblr_o1z5pav4xH1s49orpo1_1280.jpg");


    Town t1 = new TownBuilder()
        .setTitle("Mother Susanna Monument")
        .setCategory("Place")
        .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
        .setAddress("6510 El Colegio Rd Apt 1223")
        .setLat(35.594559f)
        .setLng(-117.899149f)
        .setUserId("theUniqueEye")
        .setImages(imgs1)
        .setSketch("")
        .build();

    Town t2 = new TownBuilder()
        .setTitle("Father Crowley Monument")
        .setCategory("Place")
        .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
        .setAddress("6510 El Colegio Rd Apt 1223")
        .setLat(35.594559f)
        .setLng(-117.899149f)
        .setUserId("theUniqueEye")
        .setImages(imgs2)
        .setSketch("")
        .build();

    Town t3 = new TownBuilder()
        .setTitle("Wonder Land")
        .setCategory("Creature")
        .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
        .setAddress("Rabbit Hole 1901C")
        .setLat(35.594559f)
        .setLng(-117.899149f)
        .setUserId("Sams to Go")
        .setImages(imgs3)
        .setSketch("")
        .build();

    towns.add(t1);
    towns.add(t2);
    towns.add(t3);
    towns.add(t1);
    towns.add(t2);
    towns.add(t3);
    towns.add(t1);
    towns.add(t2);
    towns.add(t3);
  }
}
