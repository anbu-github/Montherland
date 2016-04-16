package com.dev.montherland.model;

/**
 * Created by pf-05 on 2/4/2016.
 */


public class Create_Address_Model {

    String id;
    String success;
    String name;
    String Addressline1;
    String Addressline2;
    String Addressline3;
    String city;
    String date;
    String customerContact;

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    String stateid;
    Boolean isChecked=false;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }



    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerCompany() {
        return customerCompany;
    }

    public void setCustomerCompany(String customerCompany) {
        this.customerCompany = customerCompany;
    }

    String customerCompany;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddressline1() {
        return Addressline1;
    }

    public void setAddressline1(String addressline1) {
        Addressline1 = addressline1;
    }

    public String getAddressline2() {
        return Addressline2;
    }

    public void setAddressline2(String addressline2) {
        Addressline2 = addressline2;
    }

    public String getAddressline3() {
        return Addressline3;
    }

    public void setAddressline3(String addressline3) {
        Addressline3 = addressline3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String zipcode;
    String state;
    String country;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
