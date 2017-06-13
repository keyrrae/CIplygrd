package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import edu.ucsb.cs.cs190i.papertown.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import java.util.ArrayList;
import java.util.List;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.utils.ImageCompressor;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

@RuntimePermissions
public class SelectImageActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 112;
    List<Uri> imageUris;
    final int NEW_PHOTO_REQUEST = 5;
    GridView grid;
    ImageCompressor imageCompressor;
    private Town passedInTown;
    private boolean isNewSession = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        imageUris = new ArrayList<>();
        grid = (GridView) findViewById(R.id.grid);

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("onClick", "onItemLongClick");
                if(position<imageUris.size())
                imageUris.remove(position);
                SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, imageUris.toArray(new Uri[imageUris.size()]));
                grid.setAdapter(adapter);
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_selectimg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNewSession) {
                    passedInTown.setImageUrls(new ArrayList<String>());
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", passedInTown);
                setResult(Activity.RESULT_FIRST_USER, returnIntent);
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_and_exit:
                        Log.i("dataToD", "setNavigationOnClickListener");
                        //process Uri array data
                        ArrayList<String> uriStringArrayList = new ArrayList<>();
                        for (int i = 0; i < imageUris.size(); i++) {
                            uriStringArrayList.add(imageUris.get(i).toString());
                        }
                        passedInTown.setImageUrls(uriStringArrayList);
                        finish();
                        break;
                }
                return true;
            }
        });
        imageCompressor = new ImageCompressor();
        imageCompressor.setOnCompressFinishedListener(new ImageCompressor.CompressFinishedListener() {
            @Override
            public void onCompressFinished(List<String> imgUris) {
                Log.i("my", "imgUris = "+imgUris.toString());
                for(int i = 0 ; i<imgUris.size();i++){
                    imageUris.add(Uri.parse(imgUris.get(i)));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SelelctImageGrid adapter = new SelelctImageGrid(SelectImageActivity.this, imageUris.toArray(new Uri[imageUris.size()]));
                        grid.setAdapter(adapter);
                    }
                });
            }
        });

        passedInTown = TownManager.getInstance().getNewTown();

        for(int i = 0 ; i<passedInTown.getImageUrls().size();i++){
            String[] tempSplit = passedInTown.getImageUrls().get(i).split(":");
            if(tempSplit!=null&&tempSplit.length>0&&tempSplit[0].contains("content")){
                isNewSession = true;
                break;
            }
        }

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.i("my", "permission.READ_EXTERNAL_STORAGE");
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            if (passedInTown != null) {
                final List<String> dataPassIn = passedInTown.getImageUrls();
                if (dataPassIn != null) {
                    //compress the image from the uri
                    imageCompressor.doThething(dataPassIn,this);
                }
            } else {
                Toast.makeText(this, "Wired case!!!!!", Toast.LENGTH_SHORT).show();
                String s = getIntent().getStringExtra(EXTRA_MESSAGE);
                //compress the image from the uri
                ImageCompressor imageCompressor = new ImageCompressor();
                Uri uri = imageCompressor.compress(s, getApplicationContext());
            }
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("addOnItemTouchListener", "onItemClick position =" + position);
                    if (position == imageUris.size()) {
                        SelectImageActivityPermissionsDispatcher.dispatchImagePickingWithCheck(SelectImageActivity.this);
                    }
                }
            });
        }


        findViewById(R.id.button_image_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ed","onClick");
                //process Uri array data
                ArrayList<String> uriStringArrayList = new ArrayList<>();
                for (int i = 0; i < imageUris.size(); i++) {
                    uriStringArrayList.add(imageUris.get(i).toString());
                }
                passedInTown.setImageUrls(uriStringArrayList);
                Intent intent = new Intent(getApplicationContext(), NewTitleActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_next, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_PHOTO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final List<Uri> selected = Matisse.obtainResult(data);
                imageCompressor.doThething2(selected,this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("onActivityResult", "NEW_TITLE_REQUEST RESULT_CANCELED");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SelectImageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Town passedInTown = (Town) getIntent().getSerializableExtra("townPassIn");
                    if (passedInTown != null) {
                        List<String> dataPassIn = passedInTown.getImageUrls();
                        //compress the image from the uri
                        imageCompressor.doThething(dataPassIn,this);
                    }
                } else {
                    Log.i("my", "permission.READ_EXTERNAL_STORAGE denied");
                }
            }
        }
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
}
