package com.mavsoft.label.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpLoginPostData.WpLoginPostData;
import com.mavsoft.label.Models.WpLoginResponseResult.WpLoginResponseResult;

public class WpLoginResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private WpLoginResponseResult wpLoginResponseResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WpLoginResponseResult getWpLoginResponseResult() {
        return wpLoginResponseResult;
    }

    public void setWpLoginResponseResult(WpLoginResponseResult wpLoginResponseResult) {
        this.wpLoginResponseResult = wpLoginResponseResult;
    }
}
