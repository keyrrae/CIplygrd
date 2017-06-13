package edu.ucsb.cs.cs190i.papertown.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.geo.GeoActivity;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;
import edu.ucsb.cs.cs190i.papertown.models.TownManager;
import edu.ucsb.cs.cs190i.papertown.models.TownRealm;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;
import edu.ucsb.cs.cs190i.papertown.splash.SplashScreenActivity;
import edu.ucsb.cs.cs190i.papertown.town.newtown.NewTownActivity;
//import edu.ucsb.cs.cs190i.papertown.town.newtown.TownDatabaseHelper;
import edu.ucsb.cs.cs190i.papertown.town.towndetail.TownDetailActivity;
import edu.ucsb.cs.cs190i.papertown.town.townlist.TownListActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class AccountActivity extends AppCompatActivity {

    boolean ifLikedExpanded = false;
    boolean ifDraftExpanded = false;
    boolean ifMyPostsExpanded = false;
    public List<Town> towns_liked_collapsed;
    public List<Town> towns_liked;
    public List<Town> towns_draft;
    public List<Town> towns_my_posts;

    private Realm mRealm;
    private int postsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("account_to_main", "back");
                finish();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.sign_out:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                        builder.setTitle("Log out")
                            .setMessage("Are you sure you want to sign out?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with signout
                                    FirebaseAuth.getInstance().signOut();
                                    Intent splashIntent = new Intent(AccountActivity.this, SplashScreenActivity.class);
                                    startActivity(splashIntent);
                                    finish();                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                        break;
                }
                return true;
            }
        });

        towns_liked_collapsed = new ArrayList<>();
        towns_my_posts = new ArrayList<>();

        initData();  //get towns for liked and drafts

        if(towns_draft.size()<=3) {
            findViewById(R.id.textView_draft_more).setVisibility(View.INVISIBLE);
        }

//        if(towns_liked.size()>3) {
//            towns_liked_2.add(towns_liked.get(0));
//            towns_liked_2.add(towns_liked.get(1));
//            towns_liked_2.add(towns_liked.get(2));
//        }
//        else{
//            towns_liked_2 = towns_liked;
//            findViewById(R.id.textView_liked_more).setVisibility(View.INVISIBLE);
//        }

        final GridView gridview_liked = (GridView) findViewById(R.id.gridView_liked);
        gridview_liked.setAdapter(new GridViewImageAdapter(this, towns_liked_collapsed));

        gridview_liked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                //Intent intent = new Intent(getApplicationContext(), NewTownActivity.class);
                intent.putExtra("town", towns_liked.get(position));
                startActivity(intent);
            }
        });

        final GridView gridview_draft = (GridView) findViewById(R.id.gridView_draft);

        if(towns_draft!=null&&towns_draft.size()>3) {
            List<Town> towns_draft_collased = new ArrayList<Town>();
            for (int i = 0; i < 3; i++) {
                towns_draft_collased.add(towns_draft.get(i));
            }
            gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft_collased));
        }
        else{
            gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft));
        }


        gridview_draft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                Intent intent = new Intent(getApplicationContext(), NewTownActivity.class);
                intent.putExtra("town", towns_draft.get(position));
                startActivity(intent);
            }
        });

        gridview_draft.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("onClick", "onItemLongClick");

                //delete draft
                Realm.getInstance(getApplicationContext()).executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<TownRealm> result = realm.where(TownRealm.class).equalTo("townId",towns_draft.get(position).getId()).findAll();
                        result.clear();
                    }
                });
                towns_draft.remove(position);
                if(towns_draft!=null&&towns_draft.size()<=3){
                    findViewById(R.id.textView_draft_more).setVisibility(View.INVISIBLE);
                }
                gridview_draft.setAdapter(new GridViewImageAdapter(getApplication(), towns_draft));
                return true;
            }
        });

        final GridView gridview_post = (GridView) findViewById(R.id.gridView_posts);


        gridview_post.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                Intent intent = new Intent(getApplicationContext(), TownDetailActivity.class);
                intent.putExtra("town", towns_my_posts.get(position));
                startActivity(intent);
            }
        });

        findViewById(R.id.textView_liked_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "textView_liked_more");

                if (!ifLikedExpanded) {
                    gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked));
                    ((TextView) findViewById(R.id.textView_liked_more)).setText("Less");
                } else {
                    gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked_collapsed));
                    ((TextView) findViewById(R.id.textView_liked_more)).setText("More");
                }
                ifLikedExpanded = !ifLikedExpanded;
            }
        });

        findViewById(R.id.textView_draft_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "textView_draft_more");


                if (!ifDraftExpanded) {
                    gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft));
                    ((TextView) findViewById(R.id.textView_draft_more)).setText("Less");
                } else {

                    List<Town> towns_draft_collased = new ArrayList<Town>();
                    for (int i = 0; i < 3; i++) {
                        towns_draft_collased.add(towns_draft.get(i));
                    }
                    gridview_draft.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_draft_collased));
                    ((TextView) findViewById(R.id.textView_draft_more)).setText("More");
                }
                ifDraftExpanded = !ifDraftExpanded;
            }
        });

        findViewById(R.id.textView_my_post_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "textView_draft_more");


                if (!ifMyPostsExpanded) {
                    gridview_post.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_my_posts));
                    ((TextView) findViewById(R.id.textView_my_post_more)).setText("Less");
                } else {
                    List<Town> towns_my_posts_collased= new ArrayList<Town>();
                    for(int i = 0 ; i<3;i++){
                        towns_my_posts_collased.add(towns_my_posts.get(i));
                    }
                    gridview_post.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_my_posts_collased));
                    ((TextView) findViewById(R.id.textView_my_post_more)).setText("More");
                }
                ifMyPostsExpanded = !ifMyPostsExpanded;
            }
        });

        // get username
        String userId = UserSingleton.getInstance().getUid();
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name");
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", dataSnapshot.getValue().toString());
                if(dataSnapshot.getValue().toString()!=null&&!dataSnapshot.getValue().toString().isEmpty()) {
                    ((TextView) findViewById(R.id.textView_username)).setText(dataSnapshot.getValue().toString());
                    String temp = ""+dataSnapshot.getValue().toString().charAt(0);
                    ((TextView) findViewById(R.id.textView_username_initial)).setText(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get user post numbers

        DatabaseReference townsDatabase;
        townsDatabase = FirebaseDatabase.getInstance().getReference().child("towns");
        Query q = townsDatabase.orderByChild("userId").equalTo(UserSingleton.getInstance().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Town t = ds.getValue(Town.class);
                    //Log.d("TAG", "t = "+t.toString());
                    postsCount++;
                    towns_my_posts.add(t);
                   // printTown(t);
                }

                if(postsCount<=1){
                    ((TextView) findViewById(R.id.textView_user_info)).setText(""+postsCount+" post");
                }
                else{
                    ((TextView) findViewById(R.id.textView_user_info)).setText(""+postsCount+" posts");
                }

                if(towns_my_posts.size()<=3) {
                    findViewById(R.id.textView_my_post_more).setVisibility(View.INVISIBLE);
                    gridview_post.setAdapter(new GridViewImageAdapter(getApplication(), towns_my_posts));
                }
                else{
                    List<Town> towns_my_posts_collased= new ArrayList<Town>();
                    for(int i = 0 ; i<3;i++){
                        towns_my_posts_collased.add(towns_my_posts.get(i));
                    }
                    gridview_post.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_my_posts_collased));
                    ((TextView) findViewById(R.id.textView_my_post_more)).setText("More");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get user likes
        DatabaseReference likeData = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("likes");

        likeData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> likedTownId = new ArrayList<String>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("userLikes", ds.getValue().toString());
                    likedTownId.add(ds.getValue().toString());
                }

                if(likedTownId!=null&&likedTownId.size()>0) {

                    UserSingleton.getInstance().setLikes(likedTownId);
                    //============   get all towns   ==========

                    DatabaseReference townsDatabase;
                    townsDatabase = FirebaseDatabase.getInstance().getReference().child("towns");
                    townsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Town> allTowns = new ArrayList<Town>();
                            allTowns.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Town t = ds.getValue(Town.class);
                                allTowns.add(t);
                                //printTown(t);
                            }

                            if (allTowns != null && allTowns.size() > 0) {
                                //ListTownAdapter mAdapter = new ListTownAdapter(allTowns);
                                for(int i = 0; i<likedTownId.size();i++){
                                    for(int j = 0; j<allTowns.size();j++){
                                        if(allTowns.get(j).getId().equals(likedTownId.get(i))){
                                            towns_liked.add(allTowns.get(j));
                                        }
                                    }

                                }
                            }

                            if(towns_liked.size()>3) {
                                towns_liked_collapsed.add(towns_liked.get(0));
                                towns_liked_collapsed.add(towns_liked.get(1));
                                towns_liked_collapsed.add(towns_liked.get(2));
                                gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked_collapsed));
                            }
                            else{
                                findViewById(R.id.textView_liked_more).setVisibility(View.INVISIBLE);
                                gridview_liked.setAdapter(new GridViewImageAdapter(getApplicationContext(), towns_liked));
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //=================   end of get all towns    =============
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initData() {
        towns_draft = new ArrayList<>();
        towns_liked = new ArrayList<>();

        //read draft towns from realm
        mRealm = Realm.getInstance(getApplicationContext());
        RealmResults<TownRealm> TownRealm =mRealm.allObjects(TownRealm.class);
        ArrayList<TownRealm> list = new ArrayList(mRealm.where(TownRealm.class).findAll());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for(int i = 0;i<list.size();i++){
            Town t = gson.fromJson(list.get(i).getTownJson(),Town.class);
            towns_draft.add(t);
        }
    }
}
