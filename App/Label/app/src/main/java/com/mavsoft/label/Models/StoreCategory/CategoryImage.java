package com.mavsoft.label.Models.StoreCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryImage {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_created")
    @Expose
    private String date_created;
    @SerializedName("date_created_gmt")
    @Expose
    private String date_created_gmt;
    @SerializedName("date_modified")
    @Expose
    private String date_modified;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("alt")
    @Expose
    private String alt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_created_gmt() {
        return date_created_gmt;
    }

    public void setDate_created_gmt(String date_created_gmt) {
        this.date_created_gmt = date_created_gmt;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
