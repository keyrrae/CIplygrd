package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

        passedInTown = TownManager.getInstance().getNewTown();

            if (passedInTown.getDescription()!=null&&passedInTown.getDescription().size()>0&&!passedInTown.getDescription().get(0).isEmpty() && passedInTown.getDescription().get(0) != null) {
                String dataPassIn = passedInTown.getDescription().get(0);
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
                        EditText ev = (EditText) findViewById(R.id.editText_new_description);
                        if (passedInTown.getDescription() == null) {
                            passedInTown.setDescription(new ArrayList<String>());
                        }
                        if(passedInTown.getDescription().size()==0){
                            passedInTown.getDescription().add(ev.getText().toString());
                        }else{
                            passedInTown.getDescription().set(0,ev.getText().toString());
                        }
                        finish();
                        break;
                }
                return true;
            }
        });


        findViewById(R.id.button_new_description_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ed","onClick");
                EditText ev = (EditText) findViewById(R.id.editText_new_description);
                if (passedInTown.getDescription() == null) {
                    passedInTown.setDescription(new ArrayList<String>());
                }
                if(passedInTown.getDescription().size()==0){
                    passedInTown.getDescription().add(ev.getText().toString());
                }else{
                    passedInTown.getDescription().set(0,ev.getText().toString());
                }
                Intent intent = new Intent(getApplicationContext(), NewInformationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
