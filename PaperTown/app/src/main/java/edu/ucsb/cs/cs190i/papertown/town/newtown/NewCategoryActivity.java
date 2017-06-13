package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;

public class NewCategoryActivity extends AppCompatActivity {
    private Town passedInTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        passedInTown = TownManager.getInstance().getNewTown();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        Button button1 = (Button) findViewById(R.id.button_cate1);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                passedInTown.setCategory("place");
                Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.button_cate2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                passedInTown.setCategory("creature");
                Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_cate3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                passedInTown.setCategory("event");
                Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
