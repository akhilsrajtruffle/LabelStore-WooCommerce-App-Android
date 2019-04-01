package com.mavsoft.label.Models;

import com.mavsoft.label.Models.StoreItemWithVariation.StoreItemWithVariation;

/**
 * Created by user on 11/09/2017.
 */

public class StoreBasket {

    StoreItemWithVariation storeItem;
    int qty;
    Integer variationID;
    String variationTitle;

    public StoreBasket() {
        this.variationID = -1;
        setVariationTitle("");
    }

    public StoreItemWithVariation getStoreItem() {
        return storeItem;
    }

    public String getVariationTitle() { return variationTitle; }

    public void setVariationTitle(String variationTitle) {
        this.variationTitle = variationTitle;
    }

    public void setStoreItem(StoreItemWithVariation storeItem) {
        this.storeItem = storeItem;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getVariationID() {
        return variationID;
    }

    public void setVariationID(int variationID) {
        this.variationID = variationID;
    }
}
