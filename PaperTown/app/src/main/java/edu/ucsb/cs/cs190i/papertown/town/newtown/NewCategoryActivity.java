

/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;

public class NewCategoryActivity extends AppCompatActivity {
    private Town passedInTown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        Button button1 = (Button) findViewById(R.id.button_cate1);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result","place");
                passedInTown.setCategory("place");
                returnIntent.putExtra("result",passedInTown);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.button_cate2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result","creature");
                passedInTown.setCategory("creature");
                returnIntent.putExtra("result",passedInTown);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_cate3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result","event");
                passedInTown.setCategory("event");
                returnIntent.putExtra("result",passedInTown);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
