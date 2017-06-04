/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.town.newtown.NewTownActivity;
//import edu.ucsb.cs.cs190i.papertown.town.newtown.TownDatabaseHelper;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;

public class AccountActivity extends AppCompatActivity {


    boolean ifLikedExpanded = false;
    boolean ifDraftExpanded = false;
    public List<Town> towns_liked_2;
    public List<Town> towns_liked;
    public List<Town> towns_draft_2;
    public List<Town> towns_draft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initData();  //get towns for liked and drafts

        towns_draft_2 = new ArrayList<>();
        towns_liked_2 = new ArrayList<>();

        if(towns_draft.size()>2) {
            towns_draft_2.add(towns_draft.get(0));
            towns_draft_2.add(towns_draft.get(1));
        }else{
            towns_draft_2 = towns_draft;
            findViewById(R.id.textView_draft_more).setVisibility(View.INVISIBLE);
        }

        if(towns_liked.size()>2) {
            towns_liked_2.add(towns_liked.get(0));
            towns_liked_2.add(towns_liked.get(1));
        }
        else{
            towns_liked_2 = towns_liked;
            findViewById(R.id.textView_liked_more).setVisibility(View.INVISIBLE);
        }

        final GridView gridview_liked = (GridView) findViewById(R.id.gridView_liked);
        gridview_liked.setAdapter(new GridViewImageAdapter(this, towns_liked_2));

        gridview_liked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                //Intent intent = new Intent(getApplicationContext(), NewTownActivity.class);
                intent.putExtra("town", towns_liked.get(position));
                startActivity(intent);
            }
        });

        final GridView gridview_draft = (GridView) findViewById(R.id.gridView_draft);
        gridview_draft.setAdapter(new GridViewImageAdapter(this, towns_draft_2));

        gridview_draft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                Intent intent = new Intent(getApplicationContext(), NewTownActivity.class);
                intent.putExtra("town", towns_draft.get(position));
                startActivity(intent);
            }
        });








        findViewById(R.id.textView_liked_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "textView_liked_more");

                if (!ifLikedExpanded) {
                    gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked));
                    ((TextView) findViewById(R.id.textView_liked_more)).setText("Less");
                } else {
                    gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked_2));
                    ((TextView) findViewById(R.id.textView_liked_more)).setText("More");
                }
                ifLikedExpanded = !ifLikedExpanded;
            }
        });

        findViewById(R.id.textView_draft_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "textView_draft_more");


                if (!ifDraftExpanded) {
                    gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft));
                    ((TextView) findViewById(R.id.textView_draft_more)).setText("Less");
                } else {
                    gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft_2));
                    ((TextView) findViewById(R.id.textView_draft_more)).setText("More");
                }
                ifDraftExpanded = !ifDraftExpanded;

            }
        });
    }


    private void initData() {
        towns_draft = new ArrayList<>();
        towns_liked = new ArrayList<>();
        List<String> imgs1 = new ArrayList<>();
        imgs1.add("https://s-media-cache-ak0.pinimg.com/564x/58/82/11/588211a82d4c688041ed5bf239c48715.jpg");

        List<String> imgs2 = new ArrayList<>();
        imgs2.add("https://s-media-cache-ak0.pinimg.com/564x/5f/d1/3b/5fd13bce0d12da1b7480b81555875c01.jpg");

        List<String> imgs3 = new ArrayList<>();
        imgs3.add("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg");


        Town t1 = new TownBuilder()
                .setTitle("Mother Susanna Monument")
                .setCategory("place")
                //.setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
                .setAddress("35.594559f,-117.899149f")
                .setLat(35.594559f)
                .setLng(-117.899149f)
                .setUserId("theUniqueEye")
                .setImages(imgs1)
                .setSketch("")
                .build();

        Town t2 = new TownBuilder()
                .setTitle("Father Crowley Monument")
                .setCategory("place")
                //.setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
                .setAddress("35.594559f,-117.899149f")
                .setLat(35.594559f)
                .setLng(-117.899149f)
                .setUserId("theUniqueEye")
                .setImages(imgs2)
                .setSketch("")
                .build();

        Town t3 = new TownBuilder()
                .setTitle("Wonder Land")
                .setCategory("creature")
               // .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
                .setAddress("35.594559f,-117.899149f")
                .setLat(35.594559f)
                .setLng(-117.899149f)
                .setUserId("Sams to Go")
                .setImages(imgs3)
                .setSketch("")
                .build();

        towns_liked.add(t1);
        towns_liked.add(t1);
        towns_liked.add(t3);
        towns_liked.add(t2);
        towns_liked.add(t2);
        towns_liked.add(t3);
        towns_liked.add(t1);



//        //read draft towns from DB
//        TownDatabaseHelper dbHelper;
//        // ==========  town database  ============
//
//        TownDatabaseHelper.Initialize(this);
//        dbHelper = TownDatabaseHelper.GetInstance();
//
//        towns_draft = dbHelper.getALLTownsFromDB();
//        Log.i("onClick", "townRead = "+towns_draft);
//        // ========== end of town database  ============




//        towns_draft.add(t2);
//        towns_draft.add(t3);
//        towns_draft.add(t1);
//        towns_draft.add(t2);
//        towns_draft.add(t3);
//        towns_draft.add(t1);
//        towns_draft.add(t2);
    }
}
