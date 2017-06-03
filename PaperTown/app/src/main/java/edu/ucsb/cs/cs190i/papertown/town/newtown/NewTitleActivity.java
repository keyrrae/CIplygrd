

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;

public class NewTitleActivity extends AppCompatActivity {
    private Town passedInTown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_title);

        final EditText ed = (EditText) findViewById(R.id.editText_newtitle);
        passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
        String dataPassIn = passedInTown.getTitle();
        Log.i("ed","dataPassIn = "+dataPassIn);
        if(!dataPassIn.isEmpty()&&dataPassIn!=null){
            Log.i("ed","dataPassIn2 = "+dataPassIn);
            ((EditText)findViewById(R.id.editText_newtitle)).setText(dataPassIn);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                passedInTown.setTitle(ed.getText().toString());
                returnIntent.putExtra("result",passedInTown);
                Log.i("ed","text = "+ed.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });



        ((Button)findViewById(R.id.button_new_title_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ed","onClick");
//                Intent intent = new Intent(getApplicationContext(), NewAddressActivity.class);
//                intent.putExtra("townPassIn", outputTown);
//                startActivityForResult(intent, NEW_ADDRESS_REQUEST);
//                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }
}
