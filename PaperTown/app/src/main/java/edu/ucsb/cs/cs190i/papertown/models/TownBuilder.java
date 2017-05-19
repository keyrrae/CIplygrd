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
import java.util.UUID;

import edu.ucsb.cs.cs190i.papertown.GeoHash;

/**
 * Created by Zhenyu on 2017-05-19.
 */

public class TownBuilder {

    private String id = "";
    private String geoHash = "";
    private String title = "";
    private String category = "";
    private String description = "";
    private String address = "";
    private double lat = 0.0f;
    private double lng = 0.0f;
    private String userId = "";
    private List<String> imageUrls = new ArrayList<>();
    private String sketch = "";
    private String userAlias = "";

    public TownBuilder(){
        id = UUID.randomUUID().toString() + System.currentTimeMillis();
    }

    public TownBuilder setTitle(String title){
        this.title = title;
        return this;
    }

    public TownBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public TownBuilder setLat(double lat) {
        this.lat = lat;
        this.geoHash = GeoHash.genGeoHash(lat, lng);
        return this;
    }

    public TownBuilder setLng(double lng) {
        this.lng = lng;
        this.geoHash = GeoHash.genGeoHash(lat, lng);
        return this;
    }

    public TownBuilder setLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.geoHash = GeoHash.genGeoHash(lat, lng);
        return this;
    }

    public TownBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public TownBuilder setDescription(String des){
        this.description = des;
        return this;
    }

    public TownBuilder setAddress(String address){
        this.address = address;
        return this;
    }

    public TownBuilder setImages(List<String> imageUrls){
        this.imageUrls = imageUrls;
        return this;
    }

    public TownBuilder setSketch(String sk){
        this.sketch = sk;
        return this;
    }

    public TownBuilder setUserAlias(String al){
        this.userAlias = al;
        return this;
    }

    public String getId() {
        return id;
    }

    public Town build(){
        return
                new Town(
                        this.id,
                        this.geoHash,
                        this.title,
                        this.category,
                        this.description,
                        this.address,
                        this.lat,
                        this.lng,
                        this.userId,
                        this.imageUrls,
                        this.sketch,
                        this.userAlias
                );
    }

}