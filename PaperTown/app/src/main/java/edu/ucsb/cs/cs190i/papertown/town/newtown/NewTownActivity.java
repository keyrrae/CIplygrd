/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.Card;
import com.facebook.litho.widget.Text;
import com.facebook.litho.widget.VerticalGravity;

import edu.ucsb.cs.cs190i.papertown.R;

public class NewTownActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_new_town);
    Toolbar toolbar = new Toolbar(this);
        //(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if(actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    final ComponentContext componentContext = new ComponentContext(this);

    Component<Text> textComponent = Text.create(componentContext)
        .text("Hello World")
        .textColor(Color.GREEN)
        .textSizeDip(28)
        .verticalGravity(VerticalGravity.CENTER)
        .textAlignment(Layout.Alignment.ALIGN_CENTER)
        .build();

    Component<Card> cardComponent = Card.create(componentContext)
        .content(textComponent)
        .build();

    final LithoView lithoView = LithoView.create(
        this,
        cardComponent);

    setContentView(lithoView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_new_town, menu);
    return true;
  }
}
