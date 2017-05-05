/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.application;

import okhttp3.MediaType;

/**
 * Created by xuanwang on 4/22/17.
 */

public class AppConstants {
  public static final String PREF_NAME = "SharedPreference";
  public static final String EMAIL = "Email";
  public static final String CRED = "Credential";
  public static final String USERID = "UserID";
  public static final String TOKEN = "Token";
  public static final String TOKEN_TIME = "Token_time";
  public static final int TOKEN_LIFETIME = 86400;


  public static final String HOST_NAME_LOCAL = "monimenta.herokuapp.com";
  public static final String HOST_NAME_HEROKU = "monimenta.herokuapp.com";
  public static final String ENDPOINT = "https://monimenta.herokuapp.com/";

  public static final MediaType JSON_BODY
      = MediaType.parse("application/json; charset=utf-8");

}
