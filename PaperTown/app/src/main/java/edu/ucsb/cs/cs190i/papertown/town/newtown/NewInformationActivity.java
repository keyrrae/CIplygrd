

/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;

public class NewInformationActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_new_information);

        passedInTown = TownManager.getInstance().getNewTown();

        String dataPassIn = passedInTown.getUserAlias();
        String[] dataPassInList = dataPassIn.split(",");
        Log.i("ed","dataPassIn = "+dataPassIn);
        if(!dataPassIn.isEmpty()&&dataPassIn!=null){
            Log.i("ed","dataPassIn2 = "+dataPassIn);
            if(dataPassInList!=null&&dataPassInList.length>0) {
                ((EditText) findViewById(R.id.editText_new_firstName)).setText(dataPassInList[0]);
                if (dataPassInList.length > 1) {
                    ((EditText) findViewById(R.id.editText_new_lastName)).setText(dataPassInList[1]);
                }
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_info);
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
                        EditText ev1 = (EditText) findViewById(R.id.editText_new_firstName);
                        EditText ev2 = (EditText) findViewById(R.id.editText_new_lastName);
                        //returnIntent.putExtra("result", ev1.getText().toString()+","+ev2.getText().toString());
                        passedInTown.setUserAlias(ev1.getText().toString()+","+ev2.getText().toString());
                        //returnIntent.putExtra("result",passedInTown);
                        //setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                }
                return true;
            }
        });

        findViewById(R.id.button_new_name_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ed","onClick");
                EditText ev1 = (EditText) findViewById(R.id.editText_new_firstName);
                EditText ev2 = (EditText) findViewById(R.id.editText_new_lastName);
                passedInTown.setUserAlias(ev1.getText().toString()+","+ev2.getText().toString());
                finish();
            }
        });
    }
}
