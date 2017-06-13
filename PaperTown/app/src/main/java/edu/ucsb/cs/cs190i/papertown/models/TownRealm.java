package edu.ucsb.cs.cs190i.papertown.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class TownRealm extends RealmObject {
    @Required
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