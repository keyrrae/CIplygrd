/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.models;

import android.util.Log;

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
    private HashMap<String, Town>  townMapOld = new HashMap<>();
    private HashMap <String, Integer> idPositionMap = new HashMap<>();
    private TownDataChangedListener townDataChangedListener = null;
    private TownDataUpdatedListener townDataUpdatedListener = null;


    private static TownManager instance = null;

    private TownManager() {
    }

    public interface TownDataChangedListener{
        void onDataChanged(List<Town> townList, HashMap<String, Integer> idPositionHashMap);
    }

    public interface TownDataUpdatedListener{
        void onDataUpdated(List<Town> townList);
    }

    public void setOnTownDataChangedListener( TownDataChangedListener listener ){
        this.townDataChangedListener = listener;
    }

    public void setOnTownDataUpdatedListener( TownDataUpdatedListener listener ){
        this.townDataUpdatedListener = listener;
    }

    public void informTownDataChanged() {
        if(townDataChangedListener != null) {
            townDataChangedListener.onDataChanged(getAllTowns(),idPositionMap);
        }
    }

    public static TownManager getInstance() {
        if (instance == null) {
            instance = new TownManager();
        }
        return instance;
    }

    public void addTown(Town town){
        townMap.put(town.getId(), town);
        idPositionMap.put(town.getId(),townMap.size()-1);
        //informTownDataChanged();
    }

    public HashMap<String,Integer> getIdPositionMap(){
        return this.idPositionMap;
    }

    public void addTownList(List<Town> townList){
        if(townList.size()>0) {
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
                    townMapOld.put(town.toJson(), town);
                }
                informTownDataChanged();
            }
        }
    }

    public Town getTownById(String id){
        Town town = null;
        if (townMap.containsKey(id)){
            town = townMap.get(id);
        }
        return town;
    }

    public void removeTownById(String id){
        if (townMap.containsKey(id)) {
            townMap.remove(id);
        }
        informTownDataChanged();
    }

    public void removeTown(Town town) {
        removeTownById(town.getId());
    }

    public List<Town> getAllTowns(){
        List<Town> res = new ArrayList<>();
        townMap.values();
        res.addAll(townMap.values());

        return res;
    }

    public void clearTowns(){
        townMap.clear();
        townMapOld.clear();
        informTownDataChanged();
    }
}