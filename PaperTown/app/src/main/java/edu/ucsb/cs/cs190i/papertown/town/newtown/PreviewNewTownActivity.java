package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.ucsb.cs.cs190i.papertown.ImageAdapter;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;

public class PreviewNewTownActivity extends AppCompatActivity {

  private GridView imageGrid;
  private ArrayList<Uri> uriList = new ArrayList<>();

  private String mode = "detail";

  ArrayList<String> uriStringArrayList;

  float lat = 34.415320f;
  float lng = -119.84023f;

  private TownBuilder townBuilder = new TownBuilder();
  private List<String> remoteImageUrls = new ArrayList<>();
  private FirebaseStorage storage;
  private FirebaseDatabase database;
  private DatabaseReference townRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview_new_town);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail); // Attaching the layout to the toolbar object
    setSupportActionBar(toolbar);
    toolbar.setTitle("Preview");
    toolbar.setSubtitle("");
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    Intent intent = getIntent();
    townBuilder = (TownBuilder) intent.getSerializableExtra("TOWN_BUILDER");
    this.imageGrid = (GridView) findViewById(R.id.detail_image_grid);
    uriList.addAll(townBuilder.getUrisLocal());

    FirebaseApp.initializeApp(this);
    storage = FirebaseStorage.getInstance();
    database = FirebaseDatabase.getInstance();
    townRef = database.getReference("towns");

      Button button_test_detail = (Button) findViewById(R.id.button_test_detail);
      button_test_detail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.i("dataToD", "button_test_detail OnClickListener");

              if(mode.equals("preview")){
                  Log.i("dataToD", "SUBMIT!");


                if(storage != null) {
                  StorageReference storageRef = storage.getReference();

                  for(Uri uri: uriList) {
                    StorageReference riversRef = storageRef.child("images/" + uri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(uri);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                      }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if(downloadUrl != null) {
                          remoteImageUrls.add(downloadUrl.toString());
                          if(remoteImageUrls.size() == uriList.size()){
                            townBuilder.setImages(remoteImageUrls);
                            DatabaseReference newTown = townRef.child(townBuilder.getId());
                            newTown.setValue(townBuilder.build(), new DatabaseReference.CompletionListener() {
                              @Override
                              public void onComplete(DatabaseError databaseError,
                                                     DatabaseReference databaseReference) {
                                Toast.makeText(
                                        PreviewNewTownActivity.this,
                                        "Successfully submitted network",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                              }
                            });
                          }
                        }
                      }
                    });
                  }
                }
              }
              else{
                  finish();
              }
              Log.i("dataToD", "button_test_detail OnClickListener");
                      //.setInformation   ???
          }
      });

    String s = getIntent().getStringExtra("dataToD");
    Log.i("dataToD", "data = "+s);

    uriStringArrayList = getIntent().getStringArrayListExtra("uriStringArrayList");
      mode = getIntent().getStringExtra("mode");
      if(mode==null){
          mode = "detail";
      }

    Town passedInTown = (Town)getIntent().getSerializableExtra("town");
    if(passedInTown!=null) {
      Log.i("dataToD", "passedInTown getDescription = "+passedInTown.getDescription().toString());
    }


      //change button color
      if(mode!=null&&mode.equals("preview")) {
          //change color of submission button
          button_test_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
          button_test_detail.setText("SUBMIT !");
      }
      else{
          //change color of submission button
//          button_test_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//          button_test_detail.setText("DONE");
        button_test_detail.setVisibility(Button.INVISIBLE);
      }


    //process uriStringArrayList, put data into uriList
    if(uriStringArrayList!=null&&uriStringArrayList.size()>0) {
      for (int i = 0; i < uriStringArrayList.size(); i++) {
        uriList.add(Uri.parse(uriStringArrayList.get(i)));
        Log.i("manu", "uriList["+i+"] = "+uriList.get(i));
      }
    }
    else{
      Toast.makeText(getApplicationContext(), "Cannot get images, default images used!", Toast.LENGTH_SHORT).show();
      this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
      this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));
        this.uriList.add(Uri.parse("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg"));

    }

    TextView title = (TextView) findViewById(R.id.detail_town_title);
    title.setText(townBuilder.getTitle());

    TextView address = (TextView) findViewById(R.id.detail_address);
    address.setText(townBuilder.getAddress());

    TextView description = (TextView) findViewById(R.id.detail_town_description);
    description.setText(townBuilder.getDescription());

    TextView category = (TextView) findViewById(R.id.detail_town_category);
    category.setText(townBuilder.getCategory());

    TextView info = (TextView) findViewById(R.id.detail_town_information);
    info.setText(townBuilder.getUserAlias());

    //load uriStringArrayList
    if(uriList!=null) {
      if(uriList.size()>0) {
        ImageView detail_town_image = (ImageView) findViewById(R.id.detail_town_image);
        Picasso.with(this).load(uriList.get(0))
                .fit()
                .into(detail_town_image);
        this.imageGrid.setAdapter(new ImageAdapter(this, uriList));
      }
    }



    //adjust the gridView hright

    //handle the google Maps

    MyMapFragment mapFragment = ((MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map));

    if (mapFragment != null) {
      mapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
          //enable myLocationButton
          if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            //map.getUiSettings().setMyLocationButtonEnabled(true);
          }
          else {
            Log.i("manu", "Error - checkSelfPermission!!");
          }
          //end of enabling myLocationButton

          BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker);
          BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker3);
          BitmapDescriptor icon3 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker2);

          String category = townBuilder.getCategory();

          if(category!=null&&!category.isEmpty()) {
            if (category.equals("place")) {
              //add markers
              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Ohh!")
                      .snippet("TsadADSadsA")
                      .icon(icon1));
            } else if (category.equals("creature")) {

              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Underclass beauty")
                      .snippet("Get sunburn in my head")
                      .icon(icon2));
            } else if (category.equals("event")) {
              map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                      .title("Big thing!")
                      .snippet("Meat carnival")
                      .icon(icon3));

              //end of adding markers
            }
          }

          if (map != null) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    new LatLng(
                        townBuilder.getLat(),
                        townBuilder.getLng()
                    ),
                    15
                )
            );
          }
        }
      });

      mapFragment.setListener(new MyMapFragment.OnTouchListener() {
        @Override
        public void onTouch() {
          ScrollView mScrollView = (ScrollView)findViewById(R.id.scrollView_detail);
          mScrollView.requestDisallowInterceptTouchEvent(true);
        }
      });
    } else {
      Log.i("manu", "Error - Map Fragment was null!!");
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_preview_new_town, menu);
    return true;
  }
}
