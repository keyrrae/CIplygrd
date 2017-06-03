/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.account;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.TextView;

import edu.ucsb.cs.cs190i.papertown.R;

public class AccountActivity extends AppCompatActivity {


    boolean ifLikedExpanded = false;
    boolean ifDraftExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        // Keep all Images in array
        final Integer[] mThumbIds = {
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square,
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square,
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square,
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square,
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square
        };

        final Integer[] mThumbIds2 = {
                R.drawable.com_facebook_profile_picture_blank_square, R.drawable.com_facebook_profile_picture_blank_square
        };

        final Integer[] mThumbIds1 = {
                R.drawable.com_facebook_profile_picture_blank_square
        };

        final Integer[] mThumbIds0 = {

        };

        final GridView gridview = (GridView) findViewById(R.id.gridView_liked);
        gridview.setAdapter(new GridViewImageAdapter(this,mThumbIds2));

        final GridView gridview2 = (GridView) findViewById(R.id.gridView_draft);
        gridview2.setAdapter(new GridViewImageAdapter(this,mThumbIds2));

        findViewById(R.id.textView_liked_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick","textView_liked_more");

                if(!ifLikedExpanded) {
                    gridview.setAdapter(new GridViewImageAdapter(getApplicationContext(), mThumbIds));
                    ((TextView)findViewById(R.id.textView_liked_more)).setText("Less");
                }
                else{
                    gridview.setAdapter(new GridViewImageAdapter(getApplicationContext(), mThumbIds2));
                    ((TextView)findViewById(R.id.textView_liked_more)).setText("More");
                }
                ifLikedExpanded =!ifLikedExpanded;
            }
        });

        findViewById(R.id.textView_draft_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick","textView_draft_more");
                gridview2.setAdapter(new GridViewImageAdapter(getApplicationContext(),mThumbIds));
            }
        });
    }
}
