package com.mavsoft.label.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.CheckoutOrderPostData.CheckoutOrderPostData;

public class CheckoutOrderPost {

    @SerializedName("data")
    @Expose
    private CheckoutOrderPostData data;

    public CheckoutOrderPostData getData() {
        return data;
    }

    public void setData(CheckoutOrderPostData data) {
        this.data = data;
    }
}
