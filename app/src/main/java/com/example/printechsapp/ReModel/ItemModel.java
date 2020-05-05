package com.example.printechsapp.ReModel;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
    public String Colunm_id;
    public String Barcode;
    public String Item_Code;
    public String Product;
    public String Description;
    public String Description2;
    public String Attr;
    public String Size;
    public String UOM;
    public String Unit;
    public String Price;
    public String Cost;
    public String Brand;
    public String Batch;
    public String Loc;

    // Used in Scanning Function
    public String QTY;
    public String currentTimeMillies; // used as unique id for each array scanned array
    public String totalScannedDate;
    public String totalScannedTime;

    // Order Table Fields
    public String Order_Id;
    // Material transfer
    public String fromLoc;
    public String toLoc;


    public ItemModel() {
    }


    // Primary Use (Common item insert)
    public ItemModel(String barcode, String item_Code, String product, String description, String description2, String attr,
                     String size, String UOM, String unit, String price, String cost, String brand, String batch) {
        //this.Colunm_id = colunm_id;
        this.Barcode = barcode;
        this.Item_Code = item_Code;
        this.Product = product;
        this.Description = description;
        this.Description2 = description2;
        this.Attr = attr;
        this.Size = size;
        this.UOM = UOM;
        this.Unit = unit;
        this.Price = price;
        this.Cost = cost;
        this.Brand = brand;
        this.Batch = batch;
    }


    // Physical stock count Table
    public ItemModel(String barcode, String item_Code, String product, String description, String description2,
                     String attr, String size, String UOM, String unit, String price, String cost, String brand,
                     String batch, String totalItems, String currentTimeMillies, String totalScannedDate,
                     String totalScannedTime, String location) {
        //this.Colunm_id = colunm_id;
        this.Barcode = barcode;
        this.Item_Code = item_Code;
        this.Product = product;
        this.Description = description;
        this.Description2 = description2;
        this.Attr = attr;
        this.Size = size;
        this.UOM = UOM;
        this.Unit = unit;
        this.Price = price;
        this.Cost = cost;
        this.Brand = brand;
        this.Batch = batch;
        // Extra Fields
        this.QTY = totalItems;
        this.currentTimeMillies = currentTimeMillies;
        this.totalScannedDate = totalScannedDate;
        this.totalScannedTime = totalScannedTime;
        this.Loc = location;
    }


    // ========================================= Meterial Recept Fn =============================================
    // Order Ite Table data
    public ItemModel(String barcode, String order_id, String item_Code, String product, String description,
                     String description2, String attr, String size, String uom, String unit,
                     String price, String cost, String brand, String batch, String loc) {
        Barcode = barcode;
        Order_Id = order_id;
        Item_Code = item_Code;
        Product = product;
        Description = description;
        Description2 = description2;
        Attr = attr;
        Size = size;
        UOM = uom;
        Unit = unit;
        Price = price;
        Cost = cost;
        Brand = brand;
        Batch = batch;
        Loc = loc;
    }

    public ItemModel(String barcode, String order_id, String item_Code, String product, String description, String description2,
                     String attr, String size, String UOM, String unit, String price, String cost, String brand,
                     String batch, String totalItems, String currentTimeMillies, String totalScannedDate,
                     String totalScannedTime, String location) {
        //this.Colunm_id = colunm_id;
        this.Barcode = barcode;
        this.Item_Code = item_Code;
        this.Order_Id = order_id;
        this.Product = product;
        this.Description = description;
        this.Description2 = description2;
        this.Attr = attr;
        this.Size = size;
        this.UOM = UOM;
        this.Unit = unit;
        this.Price = price;
        this.Cost = cost;
        this.Brand = brand;
        this.Batch = batch;
        // Extra Fields
        this.QTY = totalItems;
        this.currentTimeMillies = currentTimeMillies;
        this.totalScannedDate = totalScannedDate;
        this.totalScannedTime = totalScannedTime;
        this.Loc = location;
    }

    // ========================================= Meterial Transfer Fn =============================================
    public ItemModel(String barcode, String order_id, String item_Code, String product, String description, String description2,
                     String attr, String size, String UOM, String unit, String price, String cost, String brand,
                     String batch, String totalItems, String currentTimeMillies, String totalScannedDate,
                     String totalScannedTime, String fromloc, String toloc) {
        //this.Colunm_id = colunm_id;
        this.Barcode = barcode;
        this.Item_Code = item_Code;
        this.Order_Id = order_id;
        this.Product = product;
        this.Description = description;
        this.Description2 = description2;
        this.Attr = attr;
        this.Size = size;
        this.UOM = UOM;
        this.Unit = unit;
        this.Price = price;
        this.Cost = cost;
        this.Brand = brand;
        this.Batch = batch;
        // Extra Fields
        this.QTY = totalItems;
        this.currentTimeMillies = currentTimeMillies;
        this.totalScannedDate = totalScannedDate;
        this.totalScannedTime = totalScannedTime;
        this.fromLoc = fromloc;
        this.toLoc = toloc;
    }


    protected ItemModel(Parcel in) {
        Colunm_id = in.readString();
        Barcode = in.readString();
        Item_Code = in.readString();
        Product = in.readString();
        Description = in.readString();
        Description2 = in.readString();
        Attr = in.readString();
        Size = in.readString();
        UOM = in.readString();
        Unit = in.readString();
        Price = in.readString();
        Cost = in.readString();
        Brand = in.readString();
        Batch = in.readString();
        Loc = in.readString();
        QTY = in.readString();
        currentTimeMillies = in.readString();
        totalScannedDate = in.readString();
        totalScannedTime = in.readString();
        Order_Id = in.readString();
        fromLoc = in.readString();
        toLoc = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getColunm_id() {
        return Colunm_id;
    }

    public void setColunm_id(String colunm_id) {
        Colunm_id = colunm_id;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getItem_Code() {
        return Item_Code;
    }

    public void setItem_Code(String item_Code) {
        this.Item_Code = item_Code;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription2() {
        return Description2;
    }

    public void setDescription2(String description2) {
        Description2 = description2;
    }

    public String getAttr() {
        return Attr;
    }

    public void setAttr(String attr) {
        Attr = attr;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getCurrentTimeMillies() {
        return currentTimeMillies;
    }

    public void setCurrentTimeMillies(String currentTimeMillies) {
        this.currentTimeMillies = currentTimeMillies;
    }

    public String getTotalScannedDate() {
        return totalScannedDate;
    }

    public void setTotalScannedDate(String totalScannedDate) {
        this.totalScannedDate = totalScannedDate;
    }

    public String getTotalScannedTime() {
        return totalScannedTime;
    }

    public void setTotalScannedTime(String totalScannedTime) {
        this.totalScannedTime = totalScannedTime;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(String order_Id) {
        Order_Id = order_Id;
    }

    public String getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(String fromLoc) {
        this.fromLoc = fromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Colunm_id);
        dest.writeString(Barcode);
        dest.writeString(Item_Code);
        dest.writeString(Product);
        dest.writeString(Description);
        dest.writeString(Description2);
        dest.writeString(Attr);
        dest.writeString(Size);
        dest.writeString(UOM);
        dest.writeString(Unit);
        dest.writeString(Price);
        dest.writeString(Cost);
        dest.writeString(Brand);
        dest.writeString(Batch);
        dest.writeString(Loc);
        dest.writeString(QTY);
        dest.writeString(currentTimeMillies);
        dest.writeString(totalScannedDate);
        dest.writeString(totalScannedTime);
        dest.writeString(Order_Id);
        dest.writeString(fromLoc);
        dest.writeString(toLoc);
    }
}
