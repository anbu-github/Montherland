package com.dev.montherland.model;

import java.io.Serializable;

/**
 * Created by pf-05 on 2/12/2016.
 */
public class PurchaseOrderDetailsModel implements Serializable{

    String id;
    String success;
    String name;
    String Addressline1;
    String Addressline2;
    String Addressline3;
    String city;
    String date;
    String customerContact;
    String customerCompany;
    String status;
    String expected_delivery;
    String expected_pickup;
    String order_type;
    String instruction;

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpected_delivery() {
        return expected_delivery;
    }

    public void setExpected_delivery(String expected_delivery) {
        this.expected_delivery = expected_delivery;
    }

    public String getExpected_pickup() {
        return expected_pickup;
    }

    public void setExpected_pickup(String expected_pickup) {
        this.expected_pickup = expected_pickup;
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
