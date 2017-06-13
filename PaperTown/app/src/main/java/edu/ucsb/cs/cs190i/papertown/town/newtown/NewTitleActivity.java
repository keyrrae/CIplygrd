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
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;

public class NewTitleActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_new_title);

        final EditText ed = (EditText) findViewById(R.id.editText_newtitle);
        passedInTown = TownManager.getInstance().getNewTown();


        String dataPassIn = passedInTown.getTitle();
        Log.i("ed", "dataPassIn = " + dataPassIn);
        if (!dataPassIn.isEmpty() && dataPassIn != null) {
            Log.i("ed", "dataPassIn2 = " + dataPassIn);
            ((EditText) findViewById(R.id.editText_newtitle)).setText(dataPassIn);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_title);
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
                        passedInTown.setTitle(ed.getText().toString());
                        finish();
                        break;
                }
                return true;
            }
        });

        findViewById(R.id.button_new_title_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ed", "onClick");
                passedInTown.setTitle(ed.getText().toString());
                Intent intent = new Intent(getApplicationContext(), NewAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
