

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;

public class NewInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_information);

        Town passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
        String dataPassIn = passedInTown.getUserAlias();
        String[] dataPassInList = dataPassIn.split(",");
        Log.i("ed","dataPassIn = "+dataPassIn);
        if(!dataPassIn.isEmpty()&&dataPassIn!=null){
            Log.i("ed","dataPassIn2 = "+dataPassIn);
            ((EditText)findViewById(R.id.editText_new_firstName)).setText(dataPassInList[0]);
            if(dataPassInList.length>1) {
                ((EditText) findViewById(R.id.editText_new_lastName)).setText(dataPassInList[1]);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                EditText ev1 = (EditText) findViewById(R.id.editText_new_firstName);
                EditText ev2 = (EditText) findViewById(R.id.editText_new_lastName);
                returnIntent.putExtra("result", ev1.getText().toString()+","+ev2.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
