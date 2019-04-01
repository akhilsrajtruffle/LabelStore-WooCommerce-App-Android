
package com.mavsoft.label.Models.WpLoginPostData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpLoginPostData {

    @SerializedName("nonce")
    @Expose
    private String token;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
