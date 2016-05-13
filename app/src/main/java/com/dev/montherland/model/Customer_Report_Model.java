package com.dev.montherland.model;

/**
 * Created by pf-05 on 4/29/2016.
 */
public class Customer_Report_Model {
    String name;
    String total_orders;
    String started_orders;
    String inprogress_orders;
    String completed_orders;
    String cancelled_orders;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(String total_orders) {
        this.total_orders = total_orders;
    }

    public String getStarted_orders() {
        return started_orders;
    }

    public void setStarted_orders(String started_orders) {
        this.started_orders = started_orders;
    }

    public String getInprogress_orders() {
        return inprogress_orders;
    }

    public void setInprogress_orders(String inprogress_orders) {
        this.inprogress_orders = inprogress_orders;
    }

    public String getCompleted_orders() {
        return completed_orders;
    }

    public void setCompleted_orders(String completed_orders) {
        this.completed_orders = completed_orders;
    }

    public String getCancelled_orders() {
        return cancelled_orders;
    }

    public void setCancelled_orders(String cancelled_orders) {
        this.cancelled_orders = cancelled_orders;
    }
}
