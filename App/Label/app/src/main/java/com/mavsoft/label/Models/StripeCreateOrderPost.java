package com.mavsoft.label.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeCreateOrderPost {

    @SerializedName("dict")
    @Expose
    private StripeCreateOrderDict dict;

    public StripeCreateOrderDict getDict() {
        return dict;
    }

    public void setDict(StripeCreateOrderDict dict) {
        this.dict = dict;
    }
}
