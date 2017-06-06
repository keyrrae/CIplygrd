

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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.models.TownRealm;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;
import io.realm.Realm;

public class NewTownActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
    //    private ImageSwitcher mSwitcher;
    private ImageView imageView_newTown;

    //TownDatabaseHelper dbHelper;

    final int NEW_TITLE_REQUEST = 0;
    final int NEW_ADDRESS_REQUEST = 1;
    final int NEW_CATEGORY_REQUEST = 2;
    final int NEW_DESCRIPTION_REQUEST = 3;
    final int NEW_INFORMATION_REQUEST = 4;
    final int NEW_PHOTO_REQUEST = 5;

    private String title = "";
    private String address = "";
    private String category = "";
    private String description = "";
    private String information = "";
    private float lat = 0;
    private float lng = 0;
    private ArrayList<String> uriStringArrayList;

    private int itemLeft = 6;

    private Town outputTown;

    private Town passedInTown;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_town);


//        //handle the migration changes
//        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
//                .deleteRealmIfMigrationNeeded()
//                .build();

        mRealm = Realm.getInstance(getApplicationContext());

//        // ==========  town database  ============
//
//       TownDatabaseHelper.Initialize(this);
//        dbHelper = TownDatabaseHelper.GetInstance();
//        // ========== end of town database  ============



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


        imageView_newTown = (ImageView) findViewById(R.id.imageView_newTown);
        imageView_newTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (outputTown.getImageUrls() == null || outputTown.getImageUrls().size() == 0) {
                    //stat camera rool
                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(pickPhoto, NEW_PHOTO_REQUEST);//one can be replaced with any action code
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectImageActivity.class);
                    intent.putExtra("townPassIn", outputTown);
                    startActivityForResult(intent, NEW_PHOTO_REQUEST);
                }
            }
        });

        //get extra, see  if the passed in town is null or not to decide if need to initialize a new town
        passedInTown = (Town) getIntent().getSerializableExtra("town");
        if(passedInTown!=null){
            outputTown = passedInTown;

        }
        else {
            outputTown = new TownBuilder()
                    .setTitle(title)
                    .setAddress(address)
                    .setCategory(category)
                    //.setDescription(description)
                    .setUserAlias(information)
                    .setLat(lat)
                    .setLng(lng)
                    .setImages(uriStringArrayList)
                    .build();
            //imageView_newTown.setImageDrawable(getResources().getDrawable(R.drawable.wave));
        }

        //update view
        checkAndUpdateAllInformation();


        TextView title_title = (TextView) findViewById(R.id.title_title);
        title_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTitleActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_TITLE_REQUEST);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //finish();// kill current activity

            }
        });

        TextView title_address = (TextView) findViewById(R.id.title_address);
        title_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAddressActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_ADDRESS_REQUEST);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //finish();// kill current activity


            }
        });

        TextView title_cate = (TextView) findViewById(R.id.title_cate);
        title_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewCategoryActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_CATEGORY_REQUEST);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //finish();// kill current activity

            }
        });

        TextView title_description = (TextView) findViewById(R.id.title_description);
        title_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_DESCRIPTION_REQUEST);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //finish();// kill current activity

            }
        });

        TextView title_information = (TextView) findViewById(R.id.title_information);
        title_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewInformationActivity.class);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_INFORMATION_REQUEST);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //finish();// kill current activity

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
                    Log.i("onClick", "Preview, outputTown. getImageUriString() = "+outputTown.getImageUriString());
                    Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                    intent.putExtra("town", outputTown);
                    intent.putExtra("mode", "preview");
                    startActivity(intent);
                    //finish();
                }
            }
        });


        button_step_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Your town is saved.", Toast.LENGTH_LONG).show();

                Log.i("toJson", "result = " + outputTown.toJson());
                //save town to realm
                mRealm.beginTransaction();

                TownRealm townRealm = mRealm.createObject(TownRealm.class);
                townRealm.setTownId(outputTown.getId());
                townRealm.setTownJson(outputTown.toJson());


                mRealm.commitTransaction();

//                //save town to DB
//                int status = dbHelper.saveTownToDB(outputTown);
//                Log.i("saveTownToDB", "status = "+status);

//                //read test  moved to the AccountActivity
//                List<Town>  townRead = dbHelper.getALLTownsFromDB();
//                Log.i("onClick", "townRead = "+townRead);
                return true;
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.i("onActivityResult", "requestCode = " + requestCode);
        Log.i("onActivityResult", "resultCode = " + resultCode);
        if (requestCode == NEW_TITLE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_TITLE_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }


        if (requestCode == NEW_ADDRESS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_ADDRESS_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }

        if (requestCode == NEW_CATEGORY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_CATEGORY_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }

        if (requestCode == NEW_DESCRIPTION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_DESCRIPTION_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }
        if (requestCode == NEW_INFORMATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                String result = data.getStringExtra("result");
//                Log.i("onActivityResult", "result = " + result);
//                information = result;
//                outputTown.setUserAlias(result);

                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_INFORMATION_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }

        if (requestCode == NEW_PHOTO_REQUEST) {
            // Make sure the request was successful
            Log.i("onActivityResult", "NEW_PHOTO_REQUEST");
            if (resultCode == RESULT_OK) {
                Uri selectedImageURI = data.getData();
                Log.i("onActivityResult", "result = " + selectedImageURI.toString());
                Intent intent = new Intent(getApplicationContext(), SelectImageActivity.class);
                //intent.putExtra(EXTRA_MESSAGE, selectedImageURI.toString());
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(selectedImageURI.toString());
                outputTown.setImageUrls(tempList);
                intent.putExtra("townPassIn", outputTown);
                startActivityForResult(intent, NEW_PHOTO_REQUEST);
            }

            if (resultCode == RESULT_FIRST_USER) {  //final confirmed return
                //ArrayList<Uri> arrayList = data.getParcelableArrayListExtra("multipleImage");

                outputTown = (Town) data.getSerializableExtra("result");
                Log.i("onActivityResult", "result = " + outputTown.toString());

//                ImageView c = (ImageView) findViewById(R.id.checkbox_0);
//                c.setImageResource(R.drawable.ic_check_box_white_24dp);
//                Picasso.with(getApplicationContext()).load(Uri.parse(outputTown.getImageUrls().get(0))).into(imageView_newTown);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_PHOTO_REQUEST RESULT_CANCELED");
                //Write your code if there's no result
            }
        }

//        String imagePath = outputTown.getImageUrls().get(0);
//        if(imagePath!=null&&!imagePath.isEmpty()) {
//            Picasso.with(getApplicationContext()).load(imagePath).into(imageView_newTown);
//        }
//        else{
//            Picasso.with(getApplicationContext()).load(R.drawable.defaultimage).into(imageView_newTown);
//        }

        //update view
        checkAndUpdateAllInformation();

    }

    void checkAndUpdateAllInformation() {


        //update imagePreview

        List<String> imagePaths = outputTown.getImageUrls();
        {
            if (imagePaths!=null&&imagePaths.size()>0&&imagePaths.get(0) != null && !imagePaths.get(0).isEmpty()) {
                Picasso.with(getApplicationContext()).load(imagePaths.get(0)).into(imageView_newTown);
            } else {
                Picasso.with(getApplicationContext()).load(R.drawable.defaultimage).into(imageView_newTown);
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

        if (outputTown.getDescriptionList() != null && !outputTown.getDescriptionList().isEmpty()) {
            Log.i("checkAllInformation", "description!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_description),
                    (TextView) findViewById(R.id.description_description),
                    (ImageView) findViewById(R.id.checkbox4),
                    outputTown.getDescriptionList().get(0));
        }

        if (outputTown.getUserAlias() != null && !outputTown.getUserAlias().isEmpty()) {
            Log.i("checkAllInformation", "information!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_information),
                    (TextView) findViewById(R.id.description_information),
                    (ImageView) findViewById(R.id.checkbox5),
                    outputTown.getUserAlias());
        }

        if (outputTown.getImageUrls() != null && outputTown.getImageUrls().size() != 0&&outputTown.getImageUrls().get(0)!=null&&!outputTown.getImageUrls().get(0).isEmpty()) {
            Log.i("checkAllInformation", "uriList!=null");
            counter++;
            setChecked((TextView) findViewById(R.id.title_image),
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


        //change the color of the progress bar
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress(0);  //only show background
        pb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryPink));

    }

    void setChecked(TextView t1, TextView t2, ImageView i1, String desctiption_in) {
        i1.setImageResource(R.drawable.ic_check_box_black_24dp);
        t1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.PrimaryPink));
        if (t2 != null) {
            t2.setText(desctiption_in);
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

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}