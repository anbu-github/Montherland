package com.dev.montherland.model;

/**
 * Created by pf-05 on 2/12/2016.
 */
public class GarmentListModel1 {
    String garmentType;
    String garmentTypeId;
    String garmentQuantity;
    String garmentName;
    String washType;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    String expectedDelivery;

    public String getGarmentInstruction() {
        return garmentInstruction;
    }

    public void setGarmentInstruction(String garmentInstruction) {
        this.garmentInstruction = garmentInstruction;
    }

    String garmentInstruction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    String styleNumber;

    public String getGarmentQty() {
        return garmentQty;
    }

    public void setGarmentQty(String garmentQty) {
        this.garmentQty = garmentQty;
    }

    public String getGarmentName() {
        return garmentName;
    }

    public void setGarmentName(String garmentName) {
        this.garmentName = garmentName;
    }

    String garmentQty;

    public String getGarmentType() {
        return garmentType;
    }

    public void setGarmentType(String garmentType) {
        this.garmentType = garmentType;
    }

    public String getGarmentTypeId() {
        return garmentTypeId;
    }

    public void setGarmentTypeId(String garmentTypeId) {
        this.garmentTypeId = garmentTypeId;
    }

    public String getGarmentQuantity() {
        return garmentQuantity;
    }

    public void setGarmentQuantity(String garmentQuantity) {
        this.garmentQuantity = garmentQuantity;
    }
}
