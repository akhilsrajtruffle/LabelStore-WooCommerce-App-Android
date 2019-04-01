package com.mavsoft.label.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpLoginPostData.WpLoginPostData;

public class WpLoginPost {

    @SerializedName("data")
    @Expose
    private WpLoginPostData wpLoginPostData;

    public WpLoginPostData getWpLoginPostData() {
        return wpLoginPostData;
    }

    public void setWpLoginPostData(WpLoginPostData wpLoginPostData) {
        this.wpLoginPostData = wpLoginPostData;
    }
}
