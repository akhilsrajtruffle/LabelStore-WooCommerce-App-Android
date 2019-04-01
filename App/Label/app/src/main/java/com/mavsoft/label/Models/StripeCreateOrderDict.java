package com.mavsoft.label.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeCreateOrderDict {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("description")
    @Expose
    private String description;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
