package edu.ucsb.cs.cs190i.papertown.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TownManager {
    private SortedMap<String, Town> townMap = new TreeMap<>();
    private HashMap<String, Town> townMapOld = new HashMap<>();
    private HashMap<String, Integer> idPositionMap = new HashMap<>();
    private TownDataChangedListener townDataChangedListener = null;
    private SingleTownChangedListener singleTownChangedListener = null;

    private Town newTown;


    private static TownManager instance = null;

    private TownManager() {
    }

    public interface TownDataChangedListener {
        void onDataChanged(List<Town> townList, HashMap<String, Integer> idPositionHashMap);
    }

    public interface SingleTownChangedListener {
        void onSingleTownChanged();
    }

    public void setOnTownDataChangedListener(TownDataChangedListener listener) {
        this.townDataChangedListener = listener;
    }

    public void setOnSingleTownChangeListener(SingleTownChangedListener listener) {
        this.singleTownChangedListener = listener;
    }


    public void informTownDataChanged() {
        if (townDataChangedListener != null) {
            townDataChangedListener.onDataChanged(getAllTowns(), idPositionMap);
        }
    }

    public void informSingleTownLChanged() {
        if (singleTownChangedListener != null) {
            singleTownChangedListener.onSingleTownChanged();
        }
    }

    public static TownManager getInstance() {
        if (instance == null) {
            instance = new TownManager();
        }
        return instance;
    }


    //addTown now not informTownDataChanged
    public void addTown(Town town) {
        townMap.put(town.getId(), town);
        idPositionMap.put(town.getId(), townMap.size() - 1);
        //informTownDataChanged();
    }

    public HashMap<String, Integer> getIdPositionMap() {
        return this.idPositionMap;
    }

    public void addTownList(List<Town> townList) {
        if (townList.size() > 0) {
            boolean ifNotify = false;

            for (Town town : townList) {
                addTown(town);
                town = townMapOld.put(town.toJson(), town);
                if (town == null) {
                    ifNotify = true;
                }
            }

            if (ifNotify) {
                clearTowns();
                for (Town town : townList) {
                    addTown(town);
                    townMapOld.put(town.toJson(), town);  //stored as JSon to know any changes in town
                }
                informTownDataChanged();
            }
        }
    }

    public Town getTownById(String id) {
        Town town = null;
        if (townMap.containsKey(id)) {
            town = townMap.get(id);
        }
        return town;
    }

    public int getIdByTown(Town town) {
        return idPositionMap.get(town.getId());
    }

    public void removeTownById(String id) {
        if (townMap.containsKey(id)) {
            townMap.remove(id);
        }
        informTownDataChanged();
    }


    public void removeTown(Town town) {
        removeTownById(town.getId());
    }

    public List<Town> getAllTowns() {
        List<Town> res = new ArrayList<>();
        townMap.values();
        res.addAll(townMap.values());
        return res;
    }

    public void clearTowns() {
        townMap.clear();
        townMapOld.clear();
        informTownDataChanged();
    }


    //=========  add by ZY, remove if needed


    public void increaseTownLikesById(String id) {
        getTownById(id).increaseLikes();
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("towns").child(id).child("numOfLikes");
        likesRef.setValue(getTownById(id).getNumOfLikes(),
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError err, DatabaseReference ref) {
                        if (err == null) {
                            Log.d("INC_LIKE", "Setting num of likes succeeded");
                        }
                    }
                }
        );

        //update town
        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference().child("towns").child(getTownById(id).getId());
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addTown(dataSnapshot.getValue(Town.class));  //update town
                informSingleTownLChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public void decreaseTownLikesById(String id) {
        getTownById(id).decreaseLikes();
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("towns").child(id).child("numOfLikes");
        likesRef.setValue(getTownById(id).getNumOfLikes(),
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError err, DatabaseReference ref) {
                        if (err == null) {
                            Log.d("INC_LIKE", "Setting num of likes succeeded");
                        }
                    }
                }
        );

        //update town
        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference().child("towns").child(getTownById(id).getId());
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addTown(dataSnapshot.getValue(Town.class));  //update town
                informSingleTownLChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void addTownDescriptionById(String id, String des) {
        getTownById(id).addDescription(des);

        DatabaseReference descriptionRef =  FirebaseDatabase.getInstance().getReference().child("towns").child(id).child("description");
        descriptionRef.setValue(getTownById(id).getDescription(),
                new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError err, DatabaseReference ref){
                        if (err == null) {
                            Log.d("SET_DES", "Setting description succeeded");
                        }
                    }
                });

        //update town
        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference().child("towns").child(getTownById(id).getId());
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addTown(dataSnapshot.getValue(Town.class));  //update town
                informSingleTownLChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void addTownImageUrisById(String id, List<String> imageUris) {
        final List<String> imageUrisFirebase = new ArrayList<>();
        final String townId = id;
        //upload images to server
        FirebaseStorage storage;
        storage = FirebaseStorage.getInstance();
        if (storage != null) {
            StorageReference storageRef = storage.getReference();

            for (String uriString : imageUris) {
                Uri uri = Uri.parse(uriString);
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
                        Log.d("IMAGES", "downloadUrl = "+downloadUrl.toString());
                        //imageUrisFirebase.add(downloadUrl.toString());

                        getTownById(townId).insertSingleImageUri(downloadUrl.toString());


                        //getTownById(id).addImageUris(imageUris);
                        //getTownById(townId).insertImageUris(imageUrisFirebase);


                        DatabaseReference descriptionRef = FirebaseDatabase.getInstance().getReference().child("towns").child(townId).child("imageUrls");
                        descriptionRef.setValue(getTownById(townId).getImageUrls(),
                                new DatabaseReference.CompletionListener() {
                                    public void onComplete(DatabaseError err, DatabaseReference ref){
                                        if (err == null) {
                                            Log.d("IMAGES", "Setting images succeeded");
                                        }
                                    }
                                });

                        //update town
                        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference().child("towns").child(getTownById(townId).getId());
                        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addTown(dataSnapshot.getValue(Town.class));  //update town
                                informSingleTownLChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        }
    }


    public void setNewTown(Town newTown){
        this.newTown = newTown;
    }

    public Town getNewTown(){
        return  newTown;
    }

    public Town iniNewTown(){
        newTown = new TownBuilder()
                .setTitle("")
                .setAddress("")
                .setCategory("")
                .setDescription(new ArrayList<String>())
                .setUserAlias("")
                .setLat(0)
                .setLng(0)
                .setImages(new ArrayList<String>())
                .setUserId(UserSingleton.getInstance().getUid())
                .build();

        return newTown;
    }


    public void clearNewTown(){
        newTown = null;
    }


}