/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.geo;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.ucsb.cs.cs190i.papertown.GeoHash;
import edu.ucsb.cs.cs190i.papertown.GeoTownListAdapter;
import edu.ucsb.cs.cs190i.papertown.R;

import edu.ucsb.cs.cs190i.papertown.RecyclerItemClickListener;
import edu.ucsb.cs.cs190i.papertown.TownMapIcon;
import edu.ucsb.cs.cs190i.papertown.application.PaperTownApplication;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.splash.SplashScreenActivity;
import edu.ucsb.cs.cs190i.papertown.town.account.AccountActivity;
import edu.ucsb.cs.cs190i.papertown.town.newtown.NewTownActivity;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;
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
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GeoTownListAdapter mAdapter;
    private TownMapIcon tmi;
    private int snappingPosition = -1;
    private String pressedTownId;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private final int zoomLevelThreshold = 16;

    private boolean ifCollasped = true;

    private float currentMapZoomLeverl = 0;
    private boolean ifNothingSelected = true;

    LatLng currLoc = new LatLng(0.0, 0.0);
    /*
    * Define a request code to send to Google Play services This code is
    * returned in Activity.onActivityResult
    */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public List<Town> towns = new ArrayList<>();
    public List<Town> townsOld = new ArrayList<>();

    RecyclerView mRecyclerView;
    public LinearLayoutManager linearLayoutManager;

    private ImageView pre_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setSubtitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.add_town:
                        Intent newTownIntent = new Intent(GeoActivity.this, NewTownActivity.class);
                        newTownIntent.putExtra("LAT", currLoc.latitude);
                        newTownIntent.putExtra("LNG", currLoc.longitude);
                        startActivity(newTownIntent);
                        break;
                    case R.id.list_view:
                        Intent townListIntent = new Intent(GeoActivity.this, TownListActivity.class);
                        townListIntent.putExtra("townArrayList", new ArrayList<Town>(towns));
                        startActivity(townListIntent);
                        break;
                    case R.id.action_settings:
                        FirebaseAuth.getInstance().signOut();
                        Intent splashIntent = new Intent(GeoActivity.this, SplashScreenActivity.class);
                        startActivity(splashIntent);
                        finish();
                        break;
                }
                return true;
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent townListIntent = new Intent(GeoActivity.this, AccountActivity.class);
               // townListIntent.putExtra("townArrayList", new ArrayList<Town>(towns));
                startActivity(townListIntent);
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

//        SearchView searchView = (SearchView) findViewById(R.id.search);
//        searchView.setQueryHint("Where to");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //TODO: perform the search
//                Toast.makeText(GeoActivity.this, query, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });

        mRecyclerView = (RecyclerView) findViewById(R.id.geo_town_list);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //initData();


        //add snapping effect
        final SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new GeoTownListAdapter(towns, getApplicationContext());
        if (towns.size() > 0) {
            if (ifCollasped) {
                Log.i("onQueryTextChange", "expand");
                expand(mRecyclerView);
                ifCollasped = false;
            }
        } else {
            if (!ifCollasped) {
                Log.i("onQueryTextChange", "collapse");
                collapse(mRecyclerView);
                ifCollasped = true;
            }
        }
        mRecyclerView.setAdapter(mAdapter);

        //ini track bar color
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("onGlobalLayout", "onGlobalLayout");
                if (currentMapZoomLeverl > zoomLevelThreshold&&ifNothingSelected) {
                    Log.i("onGlobalLayout", "PrimaryPink");
                    if (mRecyclerView.getChildCount() != 0) {
                        View v = mRecyclerView.getChildAt(0);
                        ImageView bar3 = (ImageView) v.findViewById(R.id.geo_town_pick_bar);
                        bar3.setBackgroundColor(getResources().getColor(R.color.PrimaryPink));

                        pressedTownId = towns.get(0).getId();
                        updateMapMarkers();
                        //pre_view = (ImageView) v.findViewById(R.id.geo_town_pick_bar);
                    }
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.i("onScrollStateChanged", "newState = " + newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL||newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                    //clear color bar when scrolling
//                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
//                        View v = recyclerView.getChildAt(i);
//                        ImageView bar2 = (ImageView) v.findViewById(R.id.geo_town_pick_bar);
//                        bar2.setBackgroundColor(Color.TRANSPARENT);
//                    }
                    clearAllColorBar(recyclerView);


                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View centerView = helper.findSnapView(linearLayoutManager);
                    snappingPosition = linearLayoutManager.getPosition(centerView);
                    pressedTownId = towns.get(snappingPosition).getId();


                    updateMapMarkers();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currLoc, 17);
                    map.animateCamera(cameraUpdate);

                    // set track bar color
                    ImageView bar = (ImageView) centerView.findViewById(R.id.geo_town_pick_bar);
                    bar.setBackgroundColor(getResources().getColor(R.color.PrimaryPink));
                }
            }
        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                        intent.putExtra("town", towns.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );

    }


    private void clearAllColorBar(RecyclerView recyclerView){
        //clear color bar when scrolling
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View v = recyclerView.getChildAt(i);
            ImageView bar2 = (ImageView) v.findViewById(R.id.geo_town_pick_bar);
            bar2.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public static void expand(final View v) {
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        //clear things
        pressedTownId = null;
        updateMapMarkers();
        clearAllColorBar((RecyclerView)v);
        ifNothingSelected = true;


        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    private void updateMapMarkers() {

        for (Marker marker : mMarkerArray) {
            marker.remove();
        }


        Town town;
        for (int i = 0; i < towns.size(); i++) {
            town = towns.get(i);
            String category = town.getCategory();
            double lat = town.getLat();
            double lng = town.getLng();
            if (category != null && !category.isEmpty()) {
                if (pressedTownId != null && !pressedTownId.isEmpty() && town.getId().equals(pressedTownId)) {
                    //Log.e("Snapped Item Position:", "snappingPosition = " + snappingPosition);
                    tmi = new TownMapIcon(getApplicationContext(), category, true);
                    currLoc = new LatLng(lat, lng);
                } else {
                    tmi = new TownMapIcon(getApplicationContext(), category, false);
                }
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title(town.getTitle())
                        .snippet(town.getCategory())
                        .icon(BitmapDescriptorFactory.fromBitmap(tmi.getIconBitmap())));
                mMarkerArray.add(marker);
            }
        }
    }

    // click on marker to move RecyclerView (card list)

    @Override
    public boolean onMarkerClick(final Marker marker) {
        ifNothingSelected = false;
        Log.d("marker", "" + marker.getTitle());
        for (int i = 0; i < towns.size(); i++) {
            //View v = mRecyclerView.getChildAt(i);

            //String titleText = ((TextView) v.findViewById(R.id.geo_town_title)).getText().toString();
            String titleText = towns.get(i).getTitle();
            if (titleText.equals(marker.getTitle())) {
                Log.d("Child at i ", "" + i);
                pressedTownId = towns.get(i).getId();
                // move RecyclerView
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
                smoothScroller.setTargetPosition(i);
                linearLayoutManager.startSmoothScroll(smoothScroller);

                updateMapMarkers();
//                marker.showInfoWindow();
                break;
            }

        }
        clearAllColorBar(mRecyclerView);
        return true;
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready

            currentMapZoomLeverl = map.getCameraPosition().zoom;
            Log.i("loadMap", "currentMapZoomLeverl = " + currentMapZoomLeverl);

            map.setOnMarkerClickListener(this);
            map.setBuildingsEnabled(true);
            map.setIndoorEnabled(true);
            map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                    double neLat = bounds.northeast.latitude;
                    double swLat = bounds.southwest.latitude;
                    double neLng = bounds.northeast.longitude;
                    double swLng = bounds.southwest.longitude;

                    if (neLat - swLat > 5 || neLng - swLng > 5) {
                        // dont update when zoomed out
                        return;
                    }

                    List<String> allGeoCodes = GeoHash.genAllGeoHash(neLat, swLat, neLng, swLng);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    if (database != null) {

                        for (String code : allGeoCodes) {
                            Query query = database.getReference("towns").orderByChild("geoHash").equalTo(code);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    currentMapZoomLeverl = map.getCameraPosition().zoom;
                                    Log.i("onDataChange", "currentMapZoomLeverl = " + currentMapZoomLeverl);


                                    //only update when zoom level is higher than a threshold
                                    if (currentMapZoomLeverl > zoomLevelThreshold) {

                                        //make a backup of towns
                                        townsOld.clear();
                                        for (int i = 0; i < towns.size(); i++) {
                                            townsOld.add(towns.get(i));
                                        }
                                        //clear towns List
                                        towns.clear();


                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Town town = ds.getValue(Town.class);
                                            //Log.i("onTownsChange:", "towns.add(town), towns sien = " + townsOld.size() + ", town = " + town.getTitle());
                                            towns.add(town);
                                        }

                                        //compare towns lists
                                        boolean isEqual = true;
                                        if (townsOld.size() == towns.size()) {
                                            for (int i = 0; i < towns.size(); i++) {
                                                if (!townsOld.get(i).getId().equals(towns.get(i).getId())) {
                                                    isEqual = false;
                                                    break;
                                                }
                                            }
                                        } else {
                                            isEqual = false;
                                        }

                                        Log.i("onTownsChange:", "isEqual = " + isEqual);

                                        //                                        expand(mRecyclerView);
                                        // Log.i("onDataChange", "towns.size() = " + towns.size());
                                        if (towns.size() > 0) {
                                            if (ifCollasped) {
                                                Log.i("onDataChange", "expand");
                                                expand(mRecyclerView);
                                                ifCollasped = false;
                                            }
                                        } else {
                                            if (!ifCollasped) {
                                                Log.i("onDataChange", "collapse");
                                                collapse(mRecyclerView);
                                                ifCollasped = true;
                                            }
                                        }

                                        if (!isEqual) {
                                            updateMapMarkers();
                                            //override adapter
                                            mAdapter = new GeoTownListAdapter(towns, getApplicationContext());
                                            mRecyclerView.setAdapter(mAdapter);
                                        }

                                    } else {
                                        if (!ifCollasped) {
                                            Log.i("onDataChange", "collapse");
                                            collapse(mRecyclerView);
                                            ifCollasped = true;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            });
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
            //Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            currLoc = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currLoc, 17);
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
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        currLoc = new LatLng(location.getLatitude(), location.getLongitude());
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

//    private void initData() {
//        towns = new ArrayList<>();
//
//        List<String> imgs1 = new ArrayList<>();
//        imgs1.add("https://s-media-cache-ak0.pinimg.com/564x/58/82/11/588211a82d4c688041ed5bf239c48715.jpg");
//
//        List<String> imgs2 = new ArrayList<>();
//        imgs2.add("https://s-media-cache-ak0.pinimg.com/564x/5f/d1/3b/5fd13bce0d12da1b7480b81555875c01.jpg");
//
//        List<String> imgs3 = new ArrayList<>();
//        imgs3.add("https://s-media-cache-ak0.pinimg.com/564x/8f/af/c0/8fafc02753b860c3213ffe1748d8143d.jpg");
//
//
//        Town t1 = new TownBuilder()
//                .setTitle("Mother Susanna Monument")
//                .setCategory("place")
//                .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
//                .setAddress("6510 El Colegio Rd Apt 1223")
//                .setLat(35.594559f)
//                .setLng(-117.899149f)
//                .setUserId("theUniqueEye")
//                .setImages(imgs1)
//                .setSketch("")
//                .build();
//
//        Town t2 = new TownBuilder()
//                .setTitle("Father Crowley Monument")
//                .setCategory("place")
//                .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
//                .setAddress("6510 El Colegio Rd Apt 1223")
//                .setLat(35.594559f)
//                .setLng(-117.899149f)
//                .setUserId("theUniqueEye")
//                .setImages(imgs2)
//                .setSketch("")
//                .build();
//
//        Town t3 = new TownBuilder()
//                .setTitle("Wonder Land")
//                .setCategory("creature")
//                .setDescription("Discription here. ipsum dolor sit amet, consectetur adipisicing elit")
//                .setAddress("Rabbit Hole 1901C")
//                .setLat(35.594559f)
//                .setLng(-117.899149f)
//                .setUserId("Sams to Go")
//                .setImages(imgs3)
//                .setSketch("")
//                .build();
//
//        towns.add(t1);
//        towns.add(t2);
//        towns.add(t3);
//        towns.add(t1);
//        towns.add(t2);
//        towns.add(t3);
//        towns.add(t1);
//        towns.add(t2);
//        towns.add(t3);
//    }
}