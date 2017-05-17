

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
import android.view.View;
import android.widget.Button;

import edu.ucsb.cs.cs190i.papertown.R;

public class NewCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);


        Button button1 = (Button) findViewById(R.id.button_cate1);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",R.string.town_category_place);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.button_cate2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",R.string.town_category_creature);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_cate3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",R.string.town_category_event);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button button4 = (Button) findViewById(R.id.button_new_category_done);
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","event");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });



    }
}
