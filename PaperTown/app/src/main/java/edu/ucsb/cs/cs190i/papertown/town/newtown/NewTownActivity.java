package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.litho.widget.Text;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class NewTownActivity extends AppCompatActivity {

  private static final int NEW_TITLE_REQUEST = 0;
  private static final int NEW_ADDRESS_REQUEST = 1;
  private static final int NEW_CATEGORY_REQUEST = 2;
  private static final int NEW_DESCRIPTION_REQUEST = 3;
  private static final int NEW_INFORMATION_REQUEST =4;
  private static final int NEW_PHOTO_REQUEST = 5;
  private static final int PREVIEW_PHOTOS_REQUEST = 6;

  private TownBuilder townBuilder = new TownBuilder();
  private boolean isAllSet = false;
  private boolean imagesSelected = false;

  /**
   *  Binding views
   */
  @BindView(R.id.imageView_newTown) ImageView imageView_newTown;
  @BindView(R.id.button_step_left) Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_town);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newTown_done);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Add a new town");

    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    Intent intent = getIntent();
    double lat = intent.getDoubleExtra("LAT", 0.0);
    double lng = intent.getDoubleExtra("LNG", 0.0);
    townBuilder.setLatLng(lat, lng);
    townBuilder.setUserId(UserSingleton.getInstance().getUid());
  }

  @OnClick(R.id.imageView_newTown)
  public void startImagePicking(View v) {
    if(imagesSelected) {
      Intent intent = new Intent(this, SelectImageActivity.class);
      intent.putParcelableArrayListExtra("multipleImages", (ArrayList<Uri>) townBuilder.getUrisLocal());
    }else{
        NewTownActivityPermissionsDispatcher.dispatchImagePickingWithCheck(this);
    }
  }

  @NeedsPermission({
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
  })
  public void dispatchImagePicking(){
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

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    NewTownActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  void showDeniedForCamera() {
    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.title_title)
  public void startTitleActivity(View v) {
    Intent intent = new Intent(getApplicationContext(), NewTitleActivity.class);
    startActivityForResult(intent, NEW_TITLE_REQUEST);
    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
  }

  @OnClick(R.id.title_address)
  public void startAddressActivity(View v) {
    Intent intent = new Intent(getApplicationContext(), NewAddressActivity.class);
    startActivityForResult(intent, NEW_ADDRESS_REQUEST);
    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
  }

  @OnClick(R.id.title_cate)
  public void startCategoryActivity(View v) {
    Intent intent = new Intent(getApplicationContext(), NewCategoryActivity.class);
    startActivityForResult(intent, NEW_CATEGORY_REQUEST);
    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
  }

  @OnClick(R.id.title_description)
  public void startDescriptionActivity(View v) {
    Intent intent = new Intent(getApplicationContext(), NewDescriptionActivity.class);
    startActivityForResult(intent, NEW_DESCRIPTION_REQUEST);
    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
  }

  @OnClick(R.id.title_information)
  public void startInformationActivity(View v) {
    Intent intent = new Intent(getApplicationContext(), NewInformationActivity.class);
    startActivityForResult(intent, NEW_INFORMATION_REQUEST);
    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
  }

  @OnClick(R.id.button_step_left)
  public void submitButtonClick(View v) {
    if(isAllSet){
      Intent intent = new Intent(this, PreviewNewTownActivity.class);
      intent.putExtra("TOWN_BUILDER", townBuilder);
      startActivity (intent);
    } else {
      Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == RESULT_OK) {
      String result = data.getStringExtra("result");
      if (requestCode != NEW_PHOTO_REQUEST &&
          requestCode != PREVIEW_PHOTOS_REQUEST && (result == null || result.equals(""))){
        return;
      }
      switch (requestCode){
        case NEW_PHOTO_REQUEST: {
          List<Uri> selected = Matisse.obtainResult(data);
          Intent intent = new Intent(this, SelectImageActivity.class);
          intent.putParcelableArrayListExtra("multipleImages", (ArrayList<Uri>) selected);
          startActivityForResult(intent, PREVIEW_PHOTOS_REQUEST);
          break;
        }
        case PREVIEW_PHOTOS_REQUEST: {
          ArrayList<Uri> arrayList = data.getParcelableArrayListExtra("multipleImages");
          townBuilder.setUrisLocal(arrayList);
          ImageView c = (ImageView) findViewById(R.id.checkbox_0);
          c.setImageResource(R.drawable.ic_check_box_white_24dp);

          TextView tv = (TextView) findViewById(R.id.title_image);
          tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
          Picasso.with(this)
              .load(arrayList.get(0))
              .into(imageView_newTown);
          imagesSelected = true;
          break;
        }
        case NEW_TITLE_REQUEST: {
          townBuilder.setTitle(result);
          setItemChecked(R.id.title_title, R.id.description_title, R.id.checkbox1,result);
          break;
        }
        case NEW_ADDRESS_REQUEST: {
          townBuilder.setAddress(result);
          setItemChecked(R.id.title_address, R.id.description_address, R.id.checkbox2,result);
          break;
        }
        case NEW_CATEGORY_REQUEST: {
          townBuilder.setCategory(result);
          setItemChecked(R.id.title_cate, R.id.description_cate, R.id.checkbox3,result);
          break;
        }
        case NEW_DESCRIPTION_REQUEST: {
          townBuilder.setDescription(result);
          setItemChecked(R.id.title_description, R.id.description_description, R.id.checkbox4,result);
          break;
        }
        case NEW_INFORMATION_REQUEST: {
          townBuilder.setUserAlias(result);
          setItemChecked(R.id.title_information, R.id.description_information, R.id.checkbox5,result);
          break;
        }
      }
    }
    int remainItems = townBuilder.remainingItems();
    setProgressBar(remainItems);
    setSubmitButton(remainItems);
    isAllSet = (remainItems == 0);
  }

  void setSubmitButton(int remainItems) {
    if (remainItems == 0){
      submitButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
      submitButton.setText("PREVIEW !");
    } else {
      submitButton.setText("" + remainItems + " steps left");
    }
  }

  void setProgressBar(int itemCount) {
    ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
    pb.setProgress((int)(100.0*(6 - itemCount)/ 6.0 ));
    if(itemCount == 0) {
      pb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
    }
  }

  void setItemChecked(int titleId,  int contentId, int imageId, String content) {
    ImageView checkMark = (ImageView) findViewById(imageId);
    checkMark.setImageResource(R.drawable.ic_check_box_black_24dp);

    TextView title = (TextView) findViewById(titleId);
    title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

    TextView detail = (TextView) findViewById(contentId);
    if(detail != null) {
      detail.setText(content);
    }
  }
}