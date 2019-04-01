
package com.mavsoft.label.Models.StoreOrderFeeLine;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreOrderFeeLine {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tax_class")
    @Expose
    private String tax_class;
    @SerializedName("tax_status")
    @Expose
    private String tax_status;
    @SerializedName("total")
    @Expose
    private String total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTax_class() {
        return tax_class;
    }

    public void setTax_class(String tax_class) {
        this.tax_class = tax_class;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
