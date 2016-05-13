package com.dev.montherland.model;

/**
 * Created by pf-05 on 5/10/2016.
 */
public class Order_Receipt_Model {
    String dc_number;
    String quantity;
    String dc_date;
    String id;

    public String getDc_number() {
        return dc_number;
    }

    public void setDc_number(String dc_number) {
        this.dc_number = dc_number;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDc_date() {
        return dc_date;
    }

    public void setDc_date(String dc_date) {
        this.dc_date = dc_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
