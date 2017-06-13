package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;
import edu.ucsb.cs.cs190i.papertown.models.TownRealm;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;
import io.realm.Realm;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class NewTownActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

    private ImageView imageView_newTown;

    final int NEW_PHOTO_REQUEST = 5;
    final int DISPLAY_PREVIEW_REQUEST = 4;

    private int itemLeft = 6;
    private Town outputTown;
    private Town passedInTown;
    private Realm mRealm;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_draft, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("NewTownActivity", "onCreate");
        setContentView(R.layout.activity_new_town);
        ButterKnife.bind(this);
        mRealm = Realm.getInstance(getApplicationContext());



        //========test




        //=========== end of test


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_done);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
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
                Toast.makeText(getApplicationContext(), "Your town is saved.", Toast.LENGTH_LONG).show();
                Log.i("toJson", "result = " + outputTown.toJson());
                //save town to realm
                mRealm.beginTransaction();
                TownRealm townRealm = mRealm.createObject(TownRealm.class);
                townRealm.setTownId(outputTown.getId());
                townRealm.setTownJson(outputTown.toJson());
                mRealm.commitTransaction();
                return true;
            }
        });

        imageView_newTown = (ImageView) findViewById(R.id.imageView_newTown);
        imageView_newTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outputTown.getImageUrls() == null || outputTown.getImageUrls().size() == 0) {
                    NewTownActivityPermissionsDispatcher.dispatchImagePickingWithCheck(NewTownActivity.this);
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectImageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }
            }
        });

        //get extra, see  if the passed in town is null or not to decide if need to initialize a new town
        passedInTown = (Town) getIntent().getSerializableExtra("town");
        if (passedInTown != null) {
            TownManager.getInstance().setNewTown(passedInTown);
            outputTown = TownManager.getInstance().getNewTown();
        } else {
            outputTown = TownManager.getInstance().iniNewTown();
        }

        TextView title_title = (TextView) findViewById(R.id.title_title);
        title_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTitleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        TextView title_address = (TextView) findViewById(R.id.title_address);
        title_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAddressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        TextView title_cate = (TextView) findViewById(R.id.title_cate);
        title_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewCategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        TextView title_description = (TextView) findViewById(R.id.title_description);
        title_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        TextView title_information = (TextView) findViewById(R.id.title_information);
        title_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewInformationActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        //step left button event
        Button button_step_left = (Button) findViewById(R.id.button_step_left);
        button_step_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "button_step_left click");
                if (itemLeft > 0) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("onClick", "Preview !");
                    //passing data to the detailActivity
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    outputTown.setDate(mdformat.format(cal.getTime()));
                    Log.i("onClick", "Preview, outputTown. getImageUriString() = " + outputTown.getImageUriString());
                    Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                    intent.putExtra("town", outputTown);
                    intent.putExtra("mode", "preview");
                    startActivityForResult(intent, DISPLAY_PREVIEW_REQUEST);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_PHOTO_REQUEST) {
            Log.i("onActivityResult", "NEW_PHOTO_REQUEST");
            if (resultCode == RESULT_OK) {
                List<Uri> selected = Matisse.obtainResult(data);
                Intent intent = new Intent(getApplicationContext(), SelectImageActivity.class);
               ArrayList<String> tempList = new ArrayList<>();
                for(int i=0; i<selected.size(); i++){
                    tempList.add(selected.get(i).toString());
                }
                outputTown.setImageUrls(tempList);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        }

        if (requestCode == DISPLAY_PREVIEW_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }

        //update view
        checkAndUpdateAllInformation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NewTownActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showDeniedForCamera() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    public void dispatchImagePicking() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(NEW_PHOTO_REQUEST);
    }

    void checkAndUpdateAllInformation() {
        //update imagePreview

        List<String> imagePaths = outputTown.getImageUrls();
        {
            if (imagePaths != null && imagePaths.size() > 0 && imagePaths.get(0) != null && !imagePaths.get(0).isEmpty()) {
                Picasso.with(getApplicationContext()).load(imagePaths.get(0))
                        .error(R.drawable.dummyimage)
                        .placeholder(R.drawable.dummyimage)
                        .into(imageView_newTown);
            } else {
                Picasso.with(getApplicationContext()).load(R.drawable.dummyimage)
                        .error(R.drawable.dummyimage)
                        .placeholder(R.drawable.dummyimage)
                        .into(imageView_newTown);
            }
        }

        int counter = 0;
        if (outputTown.getTitle() != null && !outputTown.getTitle().isEmpty()) {
            Log.i("checkAllInformation", "title!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_title),
                    (TextView) findViewById(R.id.description_title),
                    (ImageView) findViewById(R.id.checkbox1),
                    outputTown.getTitle());
        }

        if (outputTown.getAddress() != null && !outputTown.getAddress().isEmpty()) {
            Log.i("checkAllInformation", "address!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_address),
                    (TextView) findViewById(R.id.description_address),
                    (ImageView) findViewById(R.id.checkbox2),
                    outputTown.getAddress());
        }

        if (outputTown.getCategory() != null && !outputTown.getCategory().isEmpty()) {
            Log.i("checkAllInformation", "category!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_cate),
                    (TextView) findViewById(R.id.description_cate),
                    (ImageView) findViewById(R.id.checkbox3),
                    outputTown.getCategory());
        }

        if (outputTown.getDescription() != null && outputTown.getDescription().size()>0 &&outputTown.getDescription().get(0)!=null&& !outputTown.getDescription().get(0).isEmpty()) {
            Log.i("checkAllInformation", "description!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_description),
                    (TextView) findViewById(R.id.description_description),
                    (ImageView) findViewById(R.id.checkbox4),
                    outputTown.getDescription().get(0));
        }

        if (outputTown.getUserAlias() != null && !outputTown.getUserAlias().replace(",", "").isEmpty()) {
            Log.i("checkAllInformation", "information!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_information),
                    (TextView) findViewById(R.id.description_information),
                    (ImageView) findViewById(R.id.checkbox5),
                    outputTown.getUserAlias());
        }

        if (outputTown.getImageUrls() != null && outputTown.getImageUrls().size() != 0 && outputTown.getImageUrls().get(0) != null && !outputTown.getImageUrls().get(0).isEmpty()) {
            Log.i("checkAllInformation", "uriList!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_image),
                    null,
                    (ImageView) findViewById(R.id.checkbox_0),
                    outputTown.getUserAlias());
        }else{
            setUnChecked((TextView) findViewById(R.id.title_image),
                    null,
                    (ImageView) findViewById(R.id.checkbox_0),
                    outputTown.getUserAlias());
        }

        //update itemLeft
        itemLeft = 6 - counter;

        //update step left text view
        Button button_step_left = (Button) findViewById(R.id.button_step_left);
        if (itemLeft == 1) {
            button_step_left.setText(itemLeft + " step to finish");
        } else {
            button_step_left.setText(itemLeft + " steps to finish");
        }

        //update progress bar
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress((int) (100.0 * (counter) / 6.0));
        //if all items finish, enable submission
        if (itemLeft == 0) {
            Toast.makeText(getApplicationContext(), "All information ready!", Toast.LENGTH_SHORT).show();
            enableSubmission();
        }
        //return counter;
    }


    void enableSubmission() {
        //change color of submission button
        Button button_step_left = (Button) findViewById(R.id.button_step_left);
        button_step_left.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryPink));
        button_step_left.setText("PREVIEW !");

//        //change the color of the progress bar
//        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
//        //pb.setProgress(10);  //only show background
//        //pb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Medium_Green));

    }

    void setChecked(TextView t1, TextView t2, ImageView i1, String description_in) {
        i1.setImageResource(R.drawable.ic_check_box_black_24dp);
        i1.clearColorFilter();
        t1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryPink));

        if (t2 != null&&!(description_in.isEmpty())) {
            t2.setText(description_in);
        }
    }

    void setUnChecked(TextView t1, TextView t2, ImageView i1, String description_in) {
        i1.setImageResource(R.drawable.ic_check_box_outline_blank_white_24dp);
        i1.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.LighterGray));
        t1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.LighterGray));
        if (t2 != null&&!(description_in.isEmpty())) {
            t2.setText(description_in);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public View makeView() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("NewTownActivity", "onResume");
        //update view
        checkAndUpdateAllInformation();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("NewTownActivity", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("NewTownActivity", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NewTownActivity", "onDestroy");
        TownManager.getInstance().clearTowns();
    }
}