
/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import edu.ucsb.cs.cs190i.papertown.R;

public class NewAddressActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,GoogleApiClient.ConnectionCallbacks {
    Location location;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_new_address);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_check_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                if(location!=null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    returnIntent.putExtra("result", latitude + "," + longitude);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Location unset!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set up the textView
        TextView tv = ((TextView)findViewById(R.id.textView_new_address));
        tv.setText("Where did you find the secret item?");
        //end of setting up the textView


        //set up the EditText
        EditText ev = ((EditText)findViewById(R.id.editText_new_address));
        ev.setHint("Street Address");
        //end of setting up the EditText

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_new_address));

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {

            Log.i("manu", "Error - Map Fragment was null!!");
        }

    }


    @Override
    public void onMapReady(final GoogleMap map) {


            //checkSelfPermission
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
                //map.getUiSettings().setMyLocationButtonEnabled(true);

                locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();

                location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));


            }
            else {
                Log.i("manu", "Error - checkSelfPermission!!");
            }
            //end of checkSelfPermission


            //register myLocationButton event

            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                @Override
                public boolean onMyLocationButtonClick()
                {

                    Log.i("manu", "onMyLocationButtonClick!!");
                    try {
                        location = map.getMyLocation();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.i("manu", "latitude = " + latitude);
                        Log.i("manu", "longitude = " + longitude);


                        EditText ev = ((EditText) findViewById(R.id.editText_new_address));
                        ev.setText(latitude + ", " + longitude);
                    }
                    catch(Exception e){
                        Log.e("manu", "e = "+e);
                    }




                    //TODO: Any custom actions
                    return false;
                }
            });

            //end of registering myLocationButton event


            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker3);
            BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker3);
            BitmapDescriptor icon3 = BitmapDescriptorFactory.fromResource(R.drawable.test_marker3);


            //add markers
            map.addMarker(new MarkerOptions().position(new LatLng(34.415320, -119.840233))
                    .title("Ohh!")
                    .snippet("TsadADSadsA")
                    .icon(icon1));

            map.addMarker(new MarkerOptions().position(new LatLng(34.416875, -119.826565))
                    .title("Underclass beauty")
                    .snippet("Get sunburn in my head")
                    .icon(icon2));

            map.addMarker(new MarkerOptions().position(new LatLng(34.409815, -119.845069))
                    .title("Big thing!")
                    .snippet("Meat carnival")
                    .icon(icon3));

            //end of adding markers


            //camera animation
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(/*some location*/, 10));

            if (map != null) {
                map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(new LatLng(34.414913, -119.839406), 15));  //gps and zoom level
            }


            //end of camera animation



    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

//    private Location getLastKnownLocation() {
//        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
//        List<String> providers = locationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            Location l = locationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//        }
//        return bestLocation;
//    }

}
