package com.dev.montherland.model;

/**
 * Created by pf-05 on 2/8/2016.
 */
public class Purchase_Order_Model {

    String id;
    String Date;
    String customer_contact;
    String cusomer_company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public String getCusomer_company() {
        return cusomer_company;
    }

    public void setCusomer_company(String cusomer_company) {
        this.cusomer_company = cusomer_company;
    }
}
