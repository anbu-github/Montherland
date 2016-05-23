package com.dev.montherland.model;

/**
 * Created by pf-05 on 5/17/2016.
 */
public class Order_Receipts_Details_Model {

    String id="";
    String washType="";
    String styleNum="";
    String garmentType="";
    String quantityOrdered="";
    String OrderReceipts="";
    String OrderDelivery="";
    String expectedDeliveryDate="";

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    public String getStyleNum() {
        return styleNum;
    }

    public void setStyleNum(String styleNum) {
        this.styleNum = styleNum;
    }

    public String getGarmentType() {
        return garmentType;
    }

    public void setGarmentType(String garmentType) {
        this.garmentType = garmentType;
    }

    public String getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(String quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public String getOrderReceipts() {
        return OrderReceipts;
    }

    public void setOrderReceipts(String orderReceipts) {
        OrderReceipts = orderReceipts;
    }

    public String getOrderDelivery() {
        return OrderDelivery;
    }

    public void setOrderDelivery(String orderDelivery) {
        OrderDelivery = orderDelivery;
    }
}
