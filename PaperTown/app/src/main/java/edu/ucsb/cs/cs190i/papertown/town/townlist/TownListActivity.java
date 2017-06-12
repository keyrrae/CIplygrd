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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<Town> allTowns = new ArrayList<>();
    private GridView imageGrid;
    private ArrayList<Bitmap> bitmapList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_list);

        //============   get all towns   ==========
        DatabaseReference townsDatabase;
        townsDatabase = FirebaseDatabase.getInstance().getReference().child("towns");
        townsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allTowns.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Town t = ds.getValue(Town.class);
                    allTowns.add(t);
                }

                if (allTowns != null && allTowns.size() > 0) {
                    Collections.reverse(allTowns);
                    ListTownAdapter mAdapter = new ListTownAdapter(allTowns);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //=================   end of get all towns    =============

    mRecyclerView = (RecyclerView) findViewById(R.id.list_town);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(linearLayoutManager);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.i("RecyclerItemClr", "onItemClick");

                        Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                        intent.putExtra("town", allTowns.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.i("RecyclerItemClr", "onLongItemClick");
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

    final SearchView searchView = (SearchView) findViewById(R.id.search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Log.i("onQueryTextSubmit", "query = "+query);
        return false;
      }


      @Override
      public boolean onQueryTextChange(String s) {
        // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
        List<Town> searchResults = new ArrayList<Town>();
        for(int i = 0 ; i<allTowns.size();i++){
          if(allTowns.get(i).getTitle().toLowerCase().contains(s.toLowerCase())){
            //Log.i("onQueryTextChange", "found = "+allTowns.get(i).getTitle());
            searchResults.add(allTowns.get(i));
          }
        }
        ListTownAdapter mAdapter = new ListTownAdapter(searchResults);
        mRecyclerView.setAdapter(mAdapter);

                return false;
            }
        });


        return true;
    }

}
