/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.geo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.ucsb.cs.cs190i.papertown.R;

import edu.ucsb.cs.cs190i.papertown.application.PaperTownApplication;
import edu.ucsb.cs.cs190i.papertown.splash.SplashScreenActivity;
import edu.ucsb.cs.cs190i.papertown.town.newtown.NewTownActivity;
import edu.ucsb.cs.cs190i.papertown.town.townlist.TownListActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.CRED;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.EMAIL;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.PREF_NAME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOKEN_TIME;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.TOWN_IMAGE_LIST;
import static edu.ucsb.cs.cs190i.papertown.application.AppConstants.USERID;

@RuntimePermissions
public class GeoActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

  private SupportMapFragment mapFragment;
  private GoogleMap map;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  private static final int PICK_IMAGE = 1234;
  private long UPDATE_INTERVAL = 60000;  /* 60 secs */
  private long FASTEST_INTERVAL = 5000; /* 5 secs */

   /*
   * Define a request code to send to Google Play services This code is
   * returned in Activity.onActivityResult
   */
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geo);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {

        switch(item.getItemId()){
          case R.id.add_town:
            Intent newTownIntent = new Intent(GeoActivity.this, NewTownActivity.class);
            startActivity(newTownIntent);
            /*
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
            break;
          case R.id.list_view:
            Intent townListIntent = new Intent(GeoActivity.this, TownListActivity.class);
            startActivity(townListIntent);
            break;
          case R.id.action_settings:
            cleanSharedPreferences();
            Intent splashIntent = new Intent(GeoActivity.this, SplashScreenActivity.class);
            startActivity(splashIntent);
            finish();
            break;
        }
        return true;
      }
    });

    if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
      throw new IllegalStateException("You forgot to supply a Google Maps API key");
    }

    mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
    if (mapFragment != null) {
      mapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
          loadMap(map);
        }
      });
    } else {
      Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
    }

    SearchView searchView = (SearchView) findViewById(R.id.search);
    searchView.setQueryHint("Where to");
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        //TODO: perform the search
        Toast.makeText(GeoActivity.this, query, Toast.LENGTH_SHORT).show();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return true;
      }
    });
  }

  private void cleanSharedPreferences(){
    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
    editor.remove(TOKEN);
    editor.remove(TOKEN_TIME);
    editor.remove(USERID);
    editor.remove(EMAIL);
    editor.remove(CRED);
    editor.commit();
  }

  protected void loadMap(GoogleMap googleMap) {
    map = googleMap;
    if (map != null) {
      // Map is ready
      map.setBuildingsEnabled(true);
      map.setIndoorEnabled(true);
      Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
      GeoActivityPermissionsDispatcher.getMyLocationWithCheck(this);
    } else {
      Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    GeoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @SuppressWarnings("all")
  @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  void getMyLocation() {
    if (map != null) {
      // Now that map has loaded, let's get our location!
      map.setMyLocationEnabled(true);
      map.getUiSettings().setCompassEnabled(true);
      mGoogleApiClient = new GoogleApiClient.Builder(this)
          .addApi(LocationServices.API)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this).build();
      connectClient();
    }
  }

  protected void connectClient() {
    // Connect the client.
    if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
      mGoogleApiClient.connect();
    }
  }

  /*
   * Called when the Activity becomes visible.
  */
  @Override
  protected void onStart() {
    super.onStart();
    connectClient();
  }

  /*
  * Called when the Activity is no longer visible.
  */
  @Override
  protected void onStop() {
    // Disconnecting the client invalidates it.
    if (mGoogleApiClient != null) {
      mGoogleApiClient.disconnect();
    }
    super.onStop();
  }

  /*
   * Handle results returned to the FragmentActivity by Google Play services
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Decide what to do based on the original request code
    switch (requestCode) {

      case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
        switch (resultCode) {
          case Activity.RESULT_OK:
            mGoogleApiClient.connect();
            break;
        }
        break;
      case PICK_IMAGE:
        ArrayList<Uri> res = new ArrayList<>();
        if(resultCode == RESULT_OK){
          if(data.getData() != null) {
            Uri uri = data.getData();
            res.add(uri);
          } else {
            ClipData clipData = data.getClipData();
            if(clipData != null){
              for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                res.add(uri);
              }
            }
          }
          Intent newTownIntent = new Intent(GeoActivity.this, NewTownActivity.class);
          newTownIntent.putParcelableArrayListExtra(TOWN_IMAGE_LIST, res);
          startActivity(newTownIntent);
        }
        break;
    }
  }

  private boolean isGooglePlayServicesAvailable() {
    // Check that Google Play services is available
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    // If Google Play services is available
    if (ConnectionResult.SUCCESS == resultCode) {
      // In debug mode, log the status
      Log.d("Location Updates", "Google Play services is available.");
      return true;
    } else {
      // Get the error dialog from Google Play services
      Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
          CONNECTION_FAILURE_RESOLUTION_REQUEST);

      // If Google Play services can provide an error dialog
      if (errorDialog != null) {
        // Create a new DialogFragment for the error dialog
        ErrorDialogFragment errorFragment = new ErrorDialogFragment();
        errorFragment.setDialog(errorDialog);
        errorFragment.show(getSupportFragmentManager(), "Location Updates");
      }

      return false;
    }
  }

  @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  void showDeniedForCamera() {
    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    finish();
  }
  /*
   * Called by Location Services when the request to connect the client
   * finishes successfully. At this point, you can request the current
   * location or start periodic updates
   */
  @SuppressWarnings("all")
  @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  @Override
  public void onConnected(Bundle dataBundle) {
    // Display the connection status
    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (location != null) {
      Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
      map.animateCamera(cameraUpdate);
    } else {
      Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
    }
    startLocationUpdates();
  }

  @SuppressWarnings("all")
  @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  protected void startLocationUpdates() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
        mLocationRequest, this);
  }

  @Override
  public void onLocationChanged(Location location) {
    // Report to the UI that the location was updated
    String msg = "Updated Location: " +
        Double.toString(location.getLatitude()) + "," +
        Double.toString(location.getLongitude());
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

  }

  /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
  @Override
  public void onConnectionSuspended(int i) {
    if (i == CAUSE_SERVICE_DISCONNECTED) {
      Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    } else if (i == CAUSE_NETWORK_LOST) {
      Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
    }
  }

  /*
   * Called by Location Services if the attempt to Location Services fails.
   */
  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
    if (connectionResult.hasResolution()) {
      try {
        // Start an Activity that tries to resolve the error
        connectionResult.startResolutionForResult(this,
            CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
      } catch (IntentSender.SendIntentException e) {
        // Log the error
        e.printStackTrace();
      }
    } else {
      Toast.makeText(getApplicationContext(),
          "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
    }
  }

  // Define a DialogFragment that displays the error dialog
  public static class ErrorDialogFragment extends DialogFragment {

    // Global field to contain the error dialog
    private Dialog mDialog;

    // Default constructor. Sets the dialog field to null
    public ErrorDialogFragment() {
      super();
      mDialog = null;
    }

    // Set the dialog to display
    public void setDialog(Dialog dialog) {
      mDialog = dialog;
    }

    // Return a Dialog to the DialogFragment.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      return mDialog;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_geo, menu);

    return true;
  }
}
