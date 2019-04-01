
package com.mavsoft.label.Models.WpLoginResponseResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mavsoft.label.Models.WpLoginResponseResultUser.WpLoginResponseResultUser;

public class WpLoginResponseResult {

    @SerializedName("cookie")
    @Expose
    private String cookie;

    @SerializedName("user")
    @Expose
    private WpLoginResponseResultUser user;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public WpLoginResponseResultUser getUser() {
        return user;
    }

    public void setUser(WpLoginResponseResultUser user) {
        this.user = user;
    }
}
