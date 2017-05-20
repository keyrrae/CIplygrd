

/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class NewTownActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory{
//  static ListView listview;
  private ImageSwitcher mSwitcher;
    private  ImageView imageView_newTown;

  final int NEW_TITLE_REQUEST = 0;
  final int NEW_ADDRESS_REQUEST = 1;
  final int NEW_CATEGORY_REQUEST = 2;
  final int NEW_DESCRIPTION_REQUEST = 3;
  final int NEW_INFORMATION_REQUEST =4;
  final int NEW_PHOTO_REQUEST = 5;
  final int PICK_IMAGE_MULTIPLE = 6;

  String title = "";
  String address = "";
  String category = "";
  String description = "";
  String information = "";
  Uri[] uriList;
  Uri imageUri = null;

  static int imageCount = 0;

  int itemLeft = 6;


  private Integer[] mImageIds = {
          R.drawable.door, R.drawable.light, R.drawable.corner,
          R.drawable.mc, R.drawable.light, R.drawable.door,
          R.drawable.light, R.drawable.corner};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_town);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_done);
    setSupportActionBar(toolbar);
      getSupportActionBar().setTitle(null);
      toolbar.setTitle("");
      toolbar.setSubtitle("");
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
              Log.i("dataToD", "setNavigationOnClickListener");
              finish();
          }
      });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });





    //setup switcher
      imageView_newTown = (ImageView) findViewById(R.id.imageView_newTown);
//    mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//      @Override
//      public View makeView() {
//        ImageView myView = new ImageView(getApplicationContext());
//        myView.setScaleType(ImageView.ScaleType.FIT_XY);
//        myView.setLayoutParams(new
//                ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        return myView;
//      }
//    });
//    mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
//            android.R.anim.fade_in));
//    mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
//            android.R.anim.fade_out));

      imageView_newTown.setImageDrawable(getResources().getDrawable(R.drawable.brick));


    //set up imageview onclick
    //ImageView selectImage = (ImageView) findViewById(R.id.imageView);
      imageView_newTown.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i("onClick", "selectImage click");




        //stat camera rool
        Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , NEW_PHOTO_REQUEST);//one can be replaced with any action code

//                //start camera roll for multiple images
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);

      }
    });









    //listview = (ListView) findViewById(R.id.listView);
    String[] values = new String[]{"Add Title",
            "Set Address",
            "Select Category",
            "Add Description",
            "Your Information"};

    String[] decriptions = new String[]{"Give a name to the item",
            "Where did you find the item",
            "What is the type of the item",
            "Be precise, concise and professional",
            "Required to keep track of the item"};

//        boolean[] checked = new boolean[]{false,
//                false,
//                false,
//                false,
//                false};


    final ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < values.length; ++i) {
      list.add(values[i]);
    }


//        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, values);
//
//        //send data to the adaptor
//        adapter.setCheckBoxValue(checked);
//        adapter.setDecriptionsValue(decriptions);


    TextView title_title = (TextView) findViewById(R.id.title_title);
    title_title.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NewTitleActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, "asdf");
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
        //intent.putExtra(EXTRA_MESSAGE, "asdf");
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
        //intent.putExtra(EXTRA_MESSAGE, "asdf");
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
        //intent.putExtra(EXTRA_MESSAGE, "asdf");
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
        //intent.putExtra(EXTRA_MESSAGE, "asdf");
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
        if(itemLeft>0){
          Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
        else{
          Log.i("onClick", "Preview !");

            //passing data to detailView


            Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
            intent.putExtra("dataToD", "asdf");  //passing data...


            intent.putExtra("title", title);  //passing data...
            intent.putExtra("address", address);  //passing data...
            intent.putExtra("description", description);  //passing data...
            intent.putExtra("category", category);  //passing data...
            intent.putExtra("information", information);  //passing data...
            intent.putExtra("mode", "preview");  //passing data...

            //process Uri array data
            ArrayList<String> uriStringArrayList = new ArrayList<>();
            for(int i = 0; i<uriList.length;i++){
                uriStringArrayList.add(uriList[i].toString());
            }
            intent.putStringArrayListExtra("uriStringArrayList", uriStringArrayList);  //passing Uri array data...


            //pass town as an object
            //processing address to latlng
            String[] separated = address.split(",");
            float lat = Float.parseFloat(separated[0]);
            float lng = Float.parseFloat(separated[1]);

//            Town testTown = new  Town.Builder()
//                    .setTitle(title)
//                    .setAddress(address)
//                    .setCategory(category)
//                    .setDescription(description)
//                    .setLat(lat)
//                    .setLng(lng)
//                    .setImages(uriStringArrayList)
//                    .build();

//            intent.putExtra("town", testTown);

            //startActivityForResult(intent, NEW_DESCRIPTION_REQUEST);   //not need to get results
            startActivity (intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);




           // finish();

        }



      }
    });


  }

//    private void animate4Swicher() {
//
//        int fadeInDuration = 500; // Configure time values here
//        int timeBetween = 3000;
//        int fadeOutDuration = 1000;
//
//
//        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
//        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
//        fadeIn.setDuration(fadeInDuration);
//
//        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
//        fadeOut.setStartOffset(fadeInDuration + timeBetween);
//        fadeOut.setDuration(fadeOutDuration);
//
//
//        AnimationSet animation = new AnimationSet(false); // change to false
//        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);
//        animation.setRepeatCount(1);
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
//                if (images.length - 1 > imageIndex) {
//                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
//                }
//                else {
//                    if (forever == true){
//                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
//                    }
//                }
//            }
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//            public void onAnimationStart(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//    }

//    private void animate(final ImageView imageView, final Uri images[], final int imageIndex, final boolean forever) {
//
//        //imageView <-- The View which displays the images
//        //images[] <-- Holds R references to the images to display
//        //imageIndex <-- index of the first image to show in images[]
//        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.
//
//        int fadeInDuration = 500; // Configure time values here
//        int timeBetween = 3000;
//        int fadeOutDuration = 1000;
//
//        imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
//       // imageView.setImageResource(images[imageIndex]);
//        imageView.setImageURI(images[imageIndex]); // use Uri as resource
//
//        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
//        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
//        fadeIn.setDuration(fadeInDuration);
//
//        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
//        fadeOut.setStartOffset(fadeInDuration + timeBetween);
//        fadeOut.setDuration(fadeOutDuration);
//
//        AnimationSet animation = new AnimationSet(false); // change to false
//        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);
//        animation.setRepeatCount(1);
//        imageView.setAnimation(animation);
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
//                if (images.length - 1 > imageIndex) {
//                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
//                }
//                else {
//                    if (forever == true){
//                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
//                    }
//                }
//            }
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//            public void onAnimationStart(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to


    if (requestCode == NEW_TITLE_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String result = data.getStringExtra("result");
        Log.i("onActivityResult", "result = " + result);
        title = result;
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Log.i("onActivityResult", "NEW_TITLE_REQUEST RESULT_CANCELED");
        //Write your code if there's no result
      }
    }


    if (requestCode == NEW_ADDRESS_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String result = data.getStringExtra("result");
        Log.i("onActivityResult", "result = " + result);
        address = result;
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Log.i("onActivityResult", "NEW_ADDRESS_REQUEST RESULT_CANCELED");
        //Write your code if there's no result
      }
    }

    if (requestCode == NEW_CATEGORY_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String result = data.getStringExtra("result");
        Log.i("onActivityResult", "result = " + result);
        category = result;
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Log.i("onActivityResult", "NEW_CATEGORY_REQUEST RESULT_CANCELED");
        //Write your code if there's no result
      }
    }

    if (requestCode == NEW_DESCRIPTION_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String result = data.getStringExtra("result");
        Log.i("onActivityResult", "result = " + result);
        description = result;
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Log.i("onActivityResult", "NEW_DESCRIPTION_REQUEST RESULT_CANCELED");
        //Write your code if there's no result
      }
    }
    if (requestCode == NEW_INFORMATION_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String result = data.getStringExtra("result");
        Log.i("onActivityResult", "result = " + result);
        information = result;
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
//                ImageView selectImage = (ImageView) findViewById(R.id.imageView);
//                selectImage.setImageURI(selectedImageURI);

        Intent intent = new Intent(getApplicationContext(), SelectImageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, selectedImageURI.toString());
        startActivityForResult(intent, NEW_PHOTO_REQUEST);




      }

      if (resultCode == RESULT_FIRST_USER) {  //final confirmed return
        ArrayList<Uri> arrayList = data.getParcelableArrayListExtra("multipleImage");
        uriList = arrayList.toArray(new Uri[0]);  //put URiaa arrayList to array
        //String result = data.getStringExtra("result");
        //Log.i("onActivityResult", "result = " + result);

        Log.i("mSwitcher", "uriList[0] = "+uriList[0].toString());


        //set Uri list as resource
        //ImageView selectImage = (ImageView) findViewById(R.id.imageView);
        //selectImage.setImageURI(Uri.parse(result));
        //imageUri = Uri.parse(result);
        ImageView c = (ImageView) findViewById(R.id.checkbox_0);
        c.setImageResource(R.drawable.ic_check_box_white_24dp);
        Picasso.with(getApplicationContext()).load(uriList[0])
                .into(imageView_newTown);

        //start animation
//
//        (new Thread(new Runnable()
//        {
//
//          @Override
//          public void run()
//          {
//            while (!Thread.interrupted())
//              try
//              {
//                Thread.sleep(3000);  //3s
//                runOnUiThread(new Runnable() // start actions in UI thread
//                {
//
//                  @Override
//                  public void run()
//                  {
//                    Log.i("mSwitcher", "imageCount outside = "+imageCount);
//                    if(imageCount<uriList.length) {
//                      Log.i("mSwitcher", "imageCount = "+imageCount);
//                      Log.i("mSwitcher", "riList[imageCount] = "+uriList[imageCount].toString());
//                      getApplicationContext().grantUriPermission("edu.ucsb.cs.cs190i.papertown.town.newtown", uriList[imageCount],
//                              Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                      mSwitcher.setImageURI(uriList[imageCount]);
//
//
//
//
//
//                      imageCount++;
//                    }
//                    else{
//                      imageCount = 0;
//                      Log.i("mSwitcher", "imageCount = "+imageCount);
//                      Log.i("mSwitcher", "riList[imageCount] = "+uriList[imageCount].toString());
//                      getApplicationContext().grantUriPermission("edu.ucsb.cs.cs190i.papertown.town.newtown", uriList[imageCount],
//                              Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                      mSwitcher.setImageURI(uriList[imageCount]);
//                      imageCount++;
//                    }
//
////                                        //displayData(); // this action have to be in UI thread
////                                        if(imageCount==0) {
////                                            mSwitcher.setImageResource(R.drawable.door);
////                                            imageCount++;
////                                        }
////                                        else if(imageCount==1){
////                                            mSwitcher.setImageResource(R.drawable.light);
////                                            imageCount++;
////                                        }
////                                        else if(imageCount==2){
////                                            mSwitcher.setImageResource(R.drawable.corner);
////                                            imageCount++;
////                                        }
////                                        else if(imageCount==3){
////                                            mSwitcher.setImageResource(R.drawable.mc);
////                                            imageCount++;
////                                        }
////                                        else{
////                                            imageCount = 0;
////                                        }
//
//                  }
//                });
//              }
//              catch (InterruptedException e)
//              {
//                // ooops
//              }
//          }
//        })).start(); // the while thread will start in BG thread
//
//        //end of animation
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Log.i("onActivityResult", "NEW_PHOTO_REQUEST RESULT_CANCELED");
        //Write your code if there's no result
      }
    }

    //update view
    checkAllInformation();

  }


  //dynamically get view
  public View getViewByPosition(int pos, ListView listView) {
    final int firstListItemPosition = listView.getFirstVisiblePosition();
    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

    if (pos < firstListItemPosition || pos > lastListItemPosition) {
      return listView.getAdapter().getView(pos, null, listView);
    } else {
      final int childIndex = pos - firstListItemPosition;
      return listView.getChildAt(childIndex);
    }
  }


  void checkAllInformation(){
    int counter = 0;

    Log.i("all", "title = "+title);
    Log.i("all", "address = "+address);
    Log.i("all", "category = "+category);
    Log.i("all", "description = "+description);
    Log.i("all", "information = "+information);



    if(title!=null&&!title.isEmpty()) {
      Log.i("checkAllInformation", "title!=null");
      counter++;
      //setChecked(listview.getChildAt(0),title);
      setChecked((TextView) findViewById(R.id.title_title),
              (TextView) findViewById(R.id.description_title),
              (ImageView) findViewById(R.id.checkbox1),
              title);
    }

    if(address!=null&&!address.isEmpty()) {
      Log.i("checkAllInformation", "address!=null");
      counter++;
      //setChecked(listview.getChildAt(1),address);
      setChecked((TextView) findViewById(R.id.title_address),
              (TextView) findViewById(R.id.description_address),
              (ImageView) findViewById(R.id.checkbox2),
              address);
    }

    if(category!=null&&!category.isEmpty()) {
      Log.i("checkAllInformation", "category!=null");
      counter++;
      //setChecked(listview.getChildAt(2),category);
      setChecked((TextView) findViewById(R.id.title_cate),
              (TextView) findViewById(R.id.description_cate),
              (ImageView) findViewById(R.id.checkbox3),
              category);
    }

    if(description!=null&&!description.isEmpty()) {
      Log.i("checkAllInformation", "description!=null");
      counter++;
      //setChecked(listview.getChildAt(3),description);
      setChecked((TextView) findViewById(R.id.title_description),
              (TextView) findViewById(R.id.description_description),
              (ImageView) findViewById(R.id.checkbox4),
              description);
    }

    if(information!=null&&!information.isEmpty()) {
      Log.i("checkAllInformation", "information!=null");
      counter++;
      //setChecked(listview.getChildAt(4),information);
      setChecked((TextView) findViewById(R.id.title_information),
              (TextView) findViewById(R.id.description_information),
              (ImageView) findViewById(R.id.checkbox5),
              information);
    }

    if(uriList!=null) {
      Log.i("checkAllInformation", "uriList!=null");
      counter++;
      //setChecked(listview.getChildAt(4),information);
      setChecked((TextView) findViewById(R.id.title_image),
              null,
              (ImageView) findViewById(R.id.checkbox_0),
              information);
    }

    if(imageUri!=null) {
      Log.i("checkAllInformation", "information!=null");
      counter++;
    }




    //update itemLeft
    itemLeft = 6-counter;

    //update step left text view
    Button button_step_left = (Button) findViewById(R.id.button_step_left);
    if(itemLeft==1){
      button_step_left.setText(itemLeft+" step to finish");
    }
    else {
      button_step_left.setText(itemLeft+" steps to finish");
    }



    //update progress bar
    ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
    pb.setProgress(       (int)(100.0*(counter)/6.0 )   );
    //if all items finish, enable submission
    if(itemLeft==0){
      Toast.makeText(getApplicationContext(), "All information ready!", Toast.LENGTH_SHORT).show();
      enableSubmission();
    }

    //return counter;
  }


  void enableSubmission(){

    //change colors

    //change color of submission button
    Button button_step_left = (Button) findViewById(R.id.button_step_left);
    button_step_left.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
    button_step_left.setText("PREVIEW !");


    //change the color of the progress bar
    ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
    pb.setProgress(0);  //only show background
    pb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

  }

  void setChecked(TextView t1, TextView t2, ImageView i1, String desctiption_in) {
    //ImageView c = (ImageView) view.findViewById(R.id.imageView_check);
    i1.setImageResource(R.drawable.ic_check_box_black_24dp);

    //TextView title = (TextView) view.findViewById(R.id.title1);
    t1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

    if(t2!=null) {
      //TextView description = (TextView) view.findViewById(R.id.description);
      t2.setText(desctiption_in);
    }
  }


//
//  void setChecked(View view,String in) {
//    ImageView c = (ImageView) view.findViewById(R.id.imageView_check);
//    c.setImageResource(R.drawable.ic_check_box_black_24dp);
//
//    TextView title = (TextView) view.findViewById(R.id.title1);
//    title.setTextColor(Color.rgb(29,191,151));
//
//    TextView description = (TextView) view.findViewById(R.id.description);
//    description.setText(in);
//  }

//  void setUnchecked(View view) {
//    ImageView c = (ImageView) view.findViewById(R.id.imageView_check);
//    c.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
//
//    TextView title = (TextView) view.findViewById(R.id.title1);
//    title.setTextColor(Color.GRAY);
//  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //mSwitcher.setImageResource(mImageIds[position]);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @Override
  public View makeView() {
    return null;
  }


  private class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

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





