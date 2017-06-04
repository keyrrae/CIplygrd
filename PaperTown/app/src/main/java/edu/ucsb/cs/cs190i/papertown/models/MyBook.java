/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Zhenyu on 2017-06-04.
 */

public class MyBook extends RealmObject {
    @Required
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
