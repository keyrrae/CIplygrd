/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.models;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by xuanwang on 6/2/17.
 */

public class TownManager {
    private SortedMap<String, Town> townMap = new TreeMap<>();
    private TownDataChangedListener listener = null;

    private static TownManager instance = null;

    private TownManager() {
    }

    public interface TownDataChangedListener{
        void onDataChanged(List<Town> townList);
    }

    public void setOnTownDataChangedListener( TownDataChangedListener listener ){
        this.listener = listener;
    }

    public void informTownDataChanged() {
        if(listener != null) {
            listener.onDataChanged(getAllTowns());
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
        informTownDataChanged();
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
        informTownDataChanged();
    }
}