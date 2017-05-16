/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.ucsb.cs.cs190i.papertown.R;

public class NewTitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_title);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final EditText ed = (EditText) findViewById(R.id.editText_newtitle);

        Button button = (Button) findViewById(R.id.button_newtitle_done);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",ed.getText().toString());
                Log.i("ed","text = "+ed.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();


                //if don't want to return data:
//                Intent returnIntent = new Intent();
//                setResult(Activity.RESULT_CANCELED, returnIntent);
//                finish();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_newtown, menu);  //apply the customized menu
        return true;
    }

}
