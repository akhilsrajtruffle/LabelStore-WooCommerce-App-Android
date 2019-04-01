
package com.mavsoft.label.Models.StoreOrderResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxLine {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rate_code")
    @Expose
    private String rateCode;
    @SerializedName("rate_id")
    @Expose
    private Integer rateId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("compound")
    @Expose
    private Boolean compound;
    @SerializedName("tax_total")
    @Expose
    private String taxTotal;
    @SerializedName("shipping_tax_total")
    @Expose
    private String shippingTaxTotal;
    @SerializedName("meta_data")
    @Expose
    private List<Object> metaData = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getCompound() {
        return compound;
    }

    public void setCompound(Boolean compound) {
        this.compound = compound;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public String getShippingTaxTotal() {
        return shippingTaxTotal;
    }

    public void setShippingTaxTotal(String shippingTaxTotal) {
        this.shippingTaxTotal = shippingTaxTotal;
    }

    public List<Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<Object> metaData) {
        this.metaData = metaData;
    }

}
