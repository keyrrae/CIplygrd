/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.towndetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.ucsb.cs.cs190i.papertown.R;

/**
 * Created by EYE on 31/05/2017.
 */

public class SingleImageActivity extends AppCompatActivity {
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);

        LinearLayout l = (LinearLayout) findViewById(R.id.single_layout);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            uri = Uri.parse(extras.getString("ImageUri"));
            //Log.d("URI",""+uri);
        }

        final ImageView single_image = (ImageView) findViewById(R.id.single_image);

        Picasso.with(getApplicationContext()).load(uri).into(single_image);
    }
}
