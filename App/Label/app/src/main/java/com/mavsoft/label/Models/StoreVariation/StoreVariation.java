
package com.mavsoft.label.Models.StoreVariation;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreVariation {

    @SerializedName("variations")
    @Expose
    private List<List<Variation>> variations = null;

    public List<List<Variation>> getVariations() {
        return variations;
    }

    public void setVariations(List<List<Variation>> variations) {
        this.variations = variations;
    }

}
