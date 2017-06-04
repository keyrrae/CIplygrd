/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.account;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Zhenyu on 2017-06-04.
 */

public class TownRealm extends RealmObject {  @Required
private String townJson;
    private String townId = "";

    public String getTownJson() {
        return townJson;
    }

    public void setTownJson(final String townJson) {
        this.townJson = townJson;
    }

    public String getTownId(){
        return this.townId;
    }

    public void setTownId(String townId){
        this.townId = townId;
    }
}