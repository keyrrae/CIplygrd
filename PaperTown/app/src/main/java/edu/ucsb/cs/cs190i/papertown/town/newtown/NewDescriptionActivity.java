
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;

public class NewDescriptionActivity extends AppCompatActivity {
    private Town passedInTown;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_next, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_description);

        //passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
        passedInTown = TownManager.getInstance().getNewTown();

            if (passedInTown.getDescription()!=null&&passedInTown.getDescription().size()>0&&!passedInTown.getDescription().get(0).isEmpty() && passedInTown.getDescription().get(0) != null) {
                String dataPassIn = passedInTown.getDescription().get(0);
                Log.i("ed", "dataPassIn = " + dataPassIn);
                Log.i("ed", "dataPassIn2 = " + dataPassIn);
                ((EditText) findViewById(R.id.editText_new_description)).setText(dataPassIn);
            }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.save_and_exit:
                        //Intent returnIntent = new Intent();
                        EditText ev = (EditText) findViewById(R.id.editText_new_description);
                        //returnIntent.putExtra("result", ev.getText().toString());
                       //passedInTown.addDescription(ev.getText().toString());
                        if (passedInTown.getDescription() == null) {
                            passedInTown.setDescription(new ArrayList<String>());
                        }
                        //this.description.add(description);
                        if(passedInTown.getDescription().size()==0){
                            passedInTown.getDescription().add(ev.getText().toString());
                        }else{
                            passedInTown.getDescription().set(0,ev.getText().toString());
                        }

                        //returnIntent.putExtra("result",passedInTown);
                        //setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                }
                return true;
            }
        });

    }
}
