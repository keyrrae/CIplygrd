
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

public class NewDescriptionActivity extends AppCompatActivity {
    private Town passedInTown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_description);

        passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
        String dataPassIn = passedInTown.getDescription();
        Log.i("ed","dataPassIn = "+dataPassIn);
        if(!dataPassIn.isEmpty()&&dataPassIn!=null){
            Log.i("ed","dataPassIn2 = "+dataPassIn);
            ((EditText)findViewById(R.id.editText_new_description)).setText(dataPassIn);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                EditText ev = (EditText) findViewById(R.id.editText_new_description);
                //returnIntent.putExtra("result", ev.getText().toString());
                passedInTown.setDescription(ev.getText().toString());
                returnIntent.putExtra("result",passedInTown);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
