package com.example.printechsapp.ReModel;

public class OrderModel {
    private String Colunm_id;
    private String Order_id;
    private String Order_date;
    private String Order_Qty;
    private String Supplier_Name;

    public OrderModel() {
    }

    public OrderModel(String order_id, String order_Date, String order_Qty, String supplier_Name) {
        Order_id = order_id;
        Order_date = order_Date;
        Order_Qty = order_Qty;
        Supplier_Name = supplier_Name;
    }

    public String getColunm_id() {
        return Colunm_id;
    }

    public void setColunm_id(String colunm_id) {
        Colunm_id = colunm_id;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public String getOrder_Qty() {
        return Order_Qty;
    }

    public void setOrder_Qty(String order_Qty) {
        Order_Qty = order_Qty;
    }

    public String getSupplier_Name() {
        return Supplier_Name;
    }

    public void setSupplier_Name(String supplier_Name) {
        Supplier_Name = supplier_Name;
    }
}
