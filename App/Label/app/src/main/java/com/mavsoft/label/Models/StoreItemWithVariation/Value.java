
package com.mavsoft.label.Models.StoreItemWithVariation;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("vc_grid_id")
    @Expose
    private List<Object> vcGridId = null;

    public List<Object> getVcGridId() {
        return vcGridId;
    }

    public void setVcGridId(List<Object> vcGridId) {
        this.vcGridId = vcGridId;
    }

}
