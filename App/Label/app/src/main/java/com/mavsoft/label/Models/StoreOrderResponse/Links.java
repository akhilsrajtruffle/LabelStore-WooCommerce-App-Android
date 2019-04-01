
package com.mavsoft.label.Models.StoreOrderResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.*;

public class Links {

    @SerializedName("self")
    @Expose
    private List<Self> self = null;
    @SerializedName("collection")
    @Expose
    private List<com.mavsoft.label.Models.Collection> collection = null;

    public List<Self> getSelf() {
        return self;
    }

    public void setSelf(List<Self> self) {
        this.self = self;
    }

    public List<com.mavsoft.label.Models.Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<com.mavsoft.label.Models.Collection> collection) {
        this.collection = collection;
    }

}
