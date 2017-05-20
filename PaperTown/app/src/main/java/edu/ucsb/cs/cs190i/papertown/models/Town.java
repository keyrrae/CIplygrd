package edu.ucsb.cs.cs190i.papertown.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuanwang on 5/4/17.
 */

public class Town implements Serializable {
  private String id = "";
  private String geoHash = "";
  private String title;
  private String category;
  private String description;
  private String address;
  private double lat;
  private double lng;
  private String userId;
  private List<String> imageUrls;
  private String sketch;
  private String userAlias;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Town(){}

  public String getGeoHash() {
    return geoHash;
  }

  public void setGeoHash(String geoHash) {
    this.geoHash = geoHash;
  }

  public Town(
          String id,
          String geoHash,
          String title,
          String category,
          String description,
          String address,
          double lat,
          double lng,
          String userId,
          List<String> imageUrls,
          String sketch,
          String ual
  ){
    this.id = id;
    this.geoHash = geoHash;
    this.title = title;
    this.category = category;
    this.description = description;
    this.address = address;
    this.lat = lat;
    this.lng = lng;
    this.userId = userId;
    this.imageUrls = imageUrls;
    this.sketch = sketch;
    this.userAlias = ual;
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

  public double getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public double getLng() {
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

  public String getUserAlias() {
    return userAlias;
  }

  public void setUserAlias(String userAlias) {
    this.userAlias = userAlias;
  }
}