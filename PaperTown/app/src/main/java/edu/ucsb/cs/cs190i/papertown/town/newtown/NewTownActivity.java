/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import edu.ucsb.cs.cs190i.papertown.R;

public class NewTownActivity extends AppCompatActivity {
  ItemFragment addTitleFrag;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_town);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if(actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    LinearLayout itemContainer = (LinearLayout) findViewById(R.id.item_container);
    if(savedInstanceState == null){
      setAddTitleFragment();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_new_town, menu);
    return true;
  }

  private void setAddTitleFragment(){
    Bundle bundle = new Bundle();
    bundle.putString("title", "Add Title");
    bundle.putString("desc", "desc");

    addTitleFrag = new ItemFragment();
    addTitleFrag.setArguments(bundle);
    addTitleFrag.setOnClickListener(new ItemFragment.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent editInfoIntent = new Intent(NewTownActivity.this, EditInfoActivity.class);
        startActivity(editInfoIntent);
      }
    });
    getSupportFragmentManager().beginTransaction().add(R.id.item_container, addTitleFrag, "ADD_TITLE").commit();
  }
}
