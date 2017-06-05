/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by xuanwang on 6/2/17.
 */

public class TownManager {
    private SortedMap<String, Town> townMap = new TreeMap<>();
    private HashMap<String, Town> townMapOld = new HashMap<>();
    private HashMap<String, Integer> idPositionMap = new HashMap<>();
    private TownDataChangedListener townDataChangedListener = null;
    private SingleTownChangedListener singleTownChangedListener = null;


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


}