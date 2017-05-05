/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.models;

import android.icu.util.ULocale;

import java.util.List;

/**
 * Created by xuanwang on 5/4/17.
 */

public class Town {
  private String id = "";
  private String title;
  private String category;
  private String description;
  private String address;
  private float lat;
  private float lng;
  private String userId;
  private List<String> imageUrls;
  private String sketch;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private Town(
       String title,
       String category,
       String description,
       String address,
       float lat,
       float lng,
       String userId,
       List<String> imageUrls,
       String sketch
  ){
    this.title = title;
    this.category = category;
    this.description = description;
    this.address = address;
    this.lat = lat;
    this.lng = lng;
    this.userId = userId;
    this.imageUrls = imageUrls;
    this.sketch = sketch;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public float getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public float getLng() {
    return lng;
  }

  public void setLng(float lng) {
    this.lng = lng;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getImageUrls() {
    return imageUrls;
  }

  public void setImageUrls(List<String> imageUrls) {
    this.imageUrls = imageUrls;
  }

  public String getSketch() {
    return sketch;
  }

  public void setSketch(String sketch) {
    this.sketch = sketch;
  }

  public static class Builder{

    private String title;
    private String category;
    private String description;
    private String address;
    private float lat;
    private float lng;
    private String userId;
    private List<String> imageUrls;
    private String sketch;

    public Builder(){

    }

    public Builder setTitle(String title){
      this.title = title;
      return this;
    }

    public Builder setCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder setLat(float lat) {
      this.lat = lat;
      return this;

    }

    public Builder setLng(float lng) {
      this.lng = lng;
      return this;

    }

    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder setDescription(String des){
      this.description = des;
      return this;
    }

    public Builder setAddress(String address){
      this.address = address;
      return this;
    }

    public Builder setImages(List<String> imageUrls){
      this.imageUrls = imageUrls;
      return this;
    }

    public Builder setSketch(String sk){
      this.sketch = sk;
      return this;
    }

    public Town build(){
      return
          new Town(
            this.title,
            this.category,
            this.description,
            this.address,
            this.lat,
            this.lng,
            this.userId,
            this.imageUrls,
            this.sketch
          );
    }
  }
}
