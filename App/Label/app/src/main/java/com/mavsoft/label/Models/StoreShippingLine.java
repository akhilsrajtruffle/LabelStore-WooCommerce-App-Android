package com.mavsoft.label.Models;

/**
 * Created by user on 11/09/2017.
 */

public class StoreShippingLine {
    String method_title;
    String method_id;
    String total;

    public StoreShippingLine(String methodId, String methodTitle, String total) {
        this.method_title = methodTitle;
        this.method_id = methodId;
        this.total = total;
    }

    public String getMethod_title() {
        return method_title;
    }

    public String getMethod_id() {
        return method_id;
    }

    public String getTotal() {
        return total;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public void setTotal(String total) {
        this.total = total;
    }


}
