package com.dev.montherland.model;

import java.lang.String; /**
 * Created by pf-05 on 2/12/2016.
 */
public class GarmentListModel {
    String garmentType;
    String garmentTypeId;
    String garmentQuantity;
    String garmentName;

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
