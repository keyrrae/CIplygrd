/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.towndetail;

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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;

/**
 * Created by EYE on 02/06/2017.
 */

public class UpdateDescriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_description);

//        Town passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
//        String dataPassIn = passedInTown.getDescription();
//        Log.i("ed","dataPassIn = "+dataPassIn);
//        if(!dataPassIn.isEmpty()&&dataPassIn!=null){
//            Log.i("ed","dataPassIn2 = "+dataPassIn);
//            ((EditText)findViewById(R.id.editText_new_description)).setText(dataPassIn);
//        }


        Intent intent = getIntent();
        TextView updateReference = (TextView) findViewById(R.id.update_reference);
        updateReference.setText(intent.getStringExtra("townDescription"));

        EditText editText = (EditText) findViewById(R.id.update_text);
        editText.setHint(DateFormat.getDateInstance().format(new Date()));

        // TODO dynamically change the height of the scrollView
        ScrollView scrollView = (ScrollView) findViewById(R.id.update_scroll);
        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
        Log.d("scroll_height_1",""+layoutParams.height);
        if(layoutParams.height>5){
            layoutParams.height = 5;
            Log.d("scroll_height_2",""+layoutParams.height);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_desctiption);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //TODO set update text to intent ... to data base ...
                Intent returnIntent = new Intent();
                EditText updateText = (EditText) findViewById(R.id.update_text);
                returnIntent.putExtra("updateText", updateText.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);

                Toast.makeText(UpdateDescriptionActivity.this, "Updating story", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return true;
    }

}
