
package com.mavsoft.label.Models.StoreOrderHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreOrderHistory {

    @SerializedName("custAddress")
    @Expose
    private String custAddress;
    @SerializedName("custName")
    @Expose
    private String custName;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemNames")
    @Expose
    private String itemNames;
    @SerializedName("itemSubtotal")
    @Expose
    private Double itemSubtotal;
    @SerializedName("orderTotal")
    @Expose
    private Double orderTotal;

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemNames() {
        return itemNames;
    }

    public void setItemNames(String itemNames) {
        this.itemNames = itemNames;
    }

    public Double getItemSubtotal() {
        return itemSubtotal;
    }

    public void setItemSubtotal(Double itemSubtotal) {
        this.itemSubtotal = itemSubtotal;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

}
