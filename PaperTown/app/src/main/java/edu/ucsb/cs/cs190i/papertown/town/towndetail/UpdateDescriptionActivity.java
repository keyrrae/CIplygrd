package edu.ucsb.cs.cs190i.papertown.town.towndetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import edu.ucsb.cs.cs190i.papertown.R;

public class UpdateDescriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_description);

        Intent intent = getIntent();
        TextView updateReference = (TextView) findViewById(R.id.update_reference);
        updateReference.setText(intent.getStringExtra("townDescription"));

        EditText editText = (EditText) findViewById(R.id.update_text);
        editText.setHint(DateFormat.getDateInstance().format(new Date()));
        updateReference.setMovementMethod(new ScrollingMovementMethod());

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
