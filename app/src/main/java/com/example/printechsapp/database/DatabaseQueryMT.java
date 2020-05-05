package com.example.printechsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.ReModel.OrderModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.example.printechsapp.database.DatabaseClass.Barcode;
import static com.example.printechsapp.database.DatabaseClass.Colunm_id;
import static com.example.printechsapp.database.DatabaseClass.DATE_SCANNED;
import static com.example.printechsapp.database.DatabaseClass.From_Loc;
import static com.example.printechsapp.database.DatabaseClass.Key_Attr;
import static com.example.printechsapp.database.DatabaseClass.Key_Batch;
import static com.example.printechsapp.database.DatabaseClass.Key_Brand;
import static com.example.printechsapp.database.DatabaseClass.Key_Cost;
import static com.example.printechsapp.database.DatabaseClass.Key_Description;
import static com.example.printechsapp.database.DatabaseClass.Key_Description2;
import static com.example.printechsapp.database.DatabaseClass.Key_Loc;
import static com.example.printechsapp.database.DatabaseClass.Key_Price;
import static com.example.printechsapp.database.DatabaseClass.Key_Product;
import static com.example.printechsapp.database.DatabaseClass.Key_Size;
import static com.example.printechsapp.database.DatabaseClass.Key_UOM;
import static com.example.printechsapp.database.DatabaseClass.Key_Unit;
import static com.example.printechsapp.database.DatabaseClass.MaterialTransfer_Items_Table;
import static com.example.printechsapp.database.DatabaseClass.Material_Recipt_Table;
import static com.example.printechsapp.database.DatabaseClass.Order_Date;
import static com.example.printechsapp.database.DatabaseClass.Order_Id;
import static com.example.printechsapp.database.DatabaseClass.Order_Items_Table;
import static com.example.printechsapp.database.DatabaseClass.Order_MTransfer_Table;
import static com.example.printechsapp.database.DatabaseClass.Order_Qty;
import static com.example.printechsapp.database.DatabaseClass.Order_Table;
import static com.example.printechsapp.database.DatabaseClass.QTY;
import static com.example.printechsapp.database.DatabaseClass.Scanned_MTransfer_Table;
import static com.example.printechsapp.database.DatabaseClass.Supplier_Name;
import static com.example.printechsapp.database.DatabaseClass.TIME_MILLIES;
import static com.example.printechsapp.database.DatabaseClass.TIME_SCANNED;
import static com.example.printechsapp.database.DatabaseClass.To_Loc;
import static com.example.printechsapp.database.DatabaseClass.key_Item_Code;

public class DatabaseQueryMT {
    private Context context;

    public DatabaseQueryMT(Context context) {
        this.context = context;
    }

    public void addMTOrderDetails(OrderModel model) {
        //DatabaseClass dbClass = DatabaseClass.getInstance(context);
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order_Id,          model.getOrder_id());
        values.put(Order_Date,        model.getOrder_date());
        values.put(Order_Qty,         model.getOrder_Qty());
        values.put(Supplier_Name,     model.getSupplier_Name());
        db.insert(Order_MTransfer_Table,null,values);
        db.close();
    }


    public void insertMTOrderItems(ItemModel item) {
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Barcode,             item.getBarcode());
        values.put(Order_Id,            item.getOrder_Id());
        values.put(key_Item_Code,       item.getItem_Code());
        values.put(Key_Product,         item.getProduct());
        values.put(Key_Description,     item.getDescription());
        values.put(Key_Description2,    item.getDescription2());
        values.put(Key_Attr,            item.getAttr());
        values.put(Key_Size,            item.getSize());
        values.put(Key_UOM,             item.getUOM());
        values.put(Key_Unit,            item.getUnit());
        values.put(Key_Price,           item.getPrice());
        values.put(Key_Cost,            item.getCost());
        values.put(Key_Brand,           item.getBrand());
        values.put(Key_Batch,           item.getBatch());
        db.insert(MaterialTransfer_Items_Table,null,values);
        db.close();
    }

    // For Scanning Function
    public ArrayList<ItemModel> getAllMTOrderItems(String order_id) {
        ArrayList<ItemModel> datalist = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MaterialTransfer_Items_Table + " WHERE "
                + Order_Id + " = " + order_id;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ItemModel contact = new ItemModel();
                contact.setColunm_id(cursor.getString(0));
                contact.setBarcode(cursor.getString(1));
                contact.setOrder_Id(cursor.getString(2));
                contact.setItem_Code(cursor.getString(3));
                contact.setProduct(cursor.getString(4));
                contact.setDescription(cursor.getString(5));
                contact.setDescription2(cursor.getString(6));
                contact.setAttr(cursor.getString(7));
                contact.setSize(cursor.getString(8));
                contact.setUOM(cursor.getString(9));
                contact.setUnit(cursor.getString(10));
                contact.setPrice(cursor.getString(11));
                contact.setCost(cursor.getString(12));
                contact.setBrand(cursor.getString(13));
                contact.setBatch(cursor.getString(14));
                // Adding contact to list
                datalist.add(contact);
            } while (cursor.moveToNext());
        }
        return datalist;
    }
    public ArrayList<ItemModel> getSingleOrderItemsMT(String order_id) {
        ArrayList<ItemModel> datalist = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MaterialTransfer_Items_Table + " WHERE " + Order_Id + " = " + order_id;

        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ItemModel item = new ItemModel();
                item.setColunm_id(cursor.getString(0));
                item.setBarcode(cursor.getString(1));
                item.setOrder_Id(cursor.getString(2));
                item.setItem_Code(cursor.getString(3));
                item.setProduct(cursor.getString(4));
                item.setDescription(cursor.getString(5));
                item.setDescription2(cursor.getString(6));
                item.setAttr(cursor.getString(7));
                item.setSize(cursor.getString(8));
                item.setUOM(cursor.getString(9));
                item.setUnit(cursor.getString(10));
                item.setPrice(cursor.getString(11));
                item.setCost(cursor.getString(12));
                item.setBrand(cursor.getString(13));
                item.setBatch(cursor.getString(14));
                datalist.add(item);
            } while (cursor.moveToNext()); // no need for next
        }
        return datalist;
    }


    public void addItemMTOrder(ItemModel contact) {
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Colunm_id,           contact.Colunm_id);
        values.put(Barcode,             contact.Barcode);
        values.put(Order_Id,            contact.Order_Id);
        values.put(key_Item_Code,       contact.Item_Code);
        values.put(Key_Product,         contact.Product);
        values.put(Key_Description,     contact.Description);
        values.put(Key_Description2,    contact.Description2);
        values.put(Key_Attr,            contact.Attr);
        values.put(Key_Size,            contact.Size);
        values.put(Key_UOM,             contact.UOM);
        values.put(Key_Unit,            contact.Unit);
        values.put(Key_Price,           contact.Price);
        values.put(Key_Cost,            contact.Cost);
        values.put(Key_Brand,           contact.Brand);
        values.put(Key_Batch,           contact.Batch);
        values.put(QTY,                 contact.QTY);
        values.put(TIME_MILLIES,        contact.currentTimeMillies);
        values.put(DATE_SCANNED,        contact.totalScannedDate);
        values.put(TIME_SCANNED,        contact.totalScannedTime);
        values.put(From_Loc,            contact.fromLoc);
        values.put(To_Loc,              contact.toLoc);
        Log.e("YOYO", contact.Order_Id + "");
        db.insert(Scanned_MTransfer_Table,null,values);
        db.close();
    }

    public String getMTOrderTotalQTY(String orderID) {
        String orderTotalQty = "0";
        String selectQuery = "SELECT  * FROM " + Order_MTransfer_Table + " WHERE " + Order_Id + " = " + orderID;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            orderTotalQty = cursor.getString(3);
        }
        return orderTotalQty;
    }

    // Get Single item Qty (Unit count)
    public int itemMaxQTY_MT(String barcode, String order_id){
        int itemQTY = 0;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        String selectQuery = "select * from " + MaterialTransfer_Items_Table +" where " + Barcode + " = ? AND " + Order_Id + " = ? ";
        Cursor cursor = db.rawQuery(selectQuery , new String[] { barcode,order_id});
        if (cursor.moveToFirst()) {
            do {
                itemQTY = Integer.parseInt(cursor.getString(10)); // Unit value
            } while (cursor.moveToNext());
        }
        return itemQTY;
    }


    // Get Scanned Single item Qty (Unit count)
    public int scannedItemMaxQTY_MT(String barcode, String order_id, String time_millies){
        int itemQTY = 0;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db     = dbClass.getWritableDatabase();
        String selectQuery2 = "select * from " + Scanned_MTransfer_Table + " where " + Barcode + " = ? AND " + Order_Id + " = ? AND  " + TIME_MILLIES + " = ?";
        //String selectQuery = "select * from " + Scanned_MTransfer_Table +" where " + Barcode + " = ? AND " + Order_Id + " = ? ";
        Cursor cursor = db.rawQuery(selectQuery2 , new String[] { barcode,order_id,time_millies});
        if (cursor.moveToFirst()) {
            do {
                itemQTY += Integer.parseInt(cursor.getString(10)); // Unit value
            } while (cursor.moveToNext());
        }
        return itemQTY;
    }

    public void updateTotalQTYCountMT(ItemModel connect, String time_millies){
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(QTY, connect.getQTY());
        db.update(Scanned_MTransfer_Table, values, TIME_MILLIES + " = ?",
                new String[] { (time_millies) });
    }


    public ArrayList<OrderModel> getAllOrdersMT() {
        ArrayList<OrderModel> datalist = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Order_MTransfer_Table;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OrderModel order = new OrderModel();
                order.setColunm_id(cursor.getString(0));
                order.setOrder_id(cursor.getString(1));
                order.setOrder_date(cursor.getString(2));
                order.setOrder_Qty(cursor.getString(3));
                order.setSupplier_Name(cursor.getString(4));
                datalist.add(order);
            } while (cursor.moveToNext());
        }
        return datalist;
    }


    public LinkedHashSet<String> getAllOrderMT_TimeMillies() {
        LinkedHashSet<String> datalist = new LinkedHashSet<>();

        String selectQuery = "SELECT  TIME_MILLIES FROM " + Scanned_MTransfer_Table;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                datalist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return datalist;
    }
    // https://stackoverflow.com/questions/33187154/selecting-one-row-from-sqlite-database-using-rawquery-and-rowid-in-android
    public ItemModel getSingleItemOrderMT(String timeMillies) {
        ArrayList<ItemModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Scanned_MTransfer_Table + " WHERE "
                + TIME_MILLIES + " = " + timeMillies;
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        ItemModel contact = new ItemModel();
        contact.setColunm_id(cursor.getString(0));
        contact.setBarcode(cursor.getString(1));
        contact.setOrder_Id(cursor.getString(2));
        contact.setItem_Code(cursor.getString(3));
        contact.setProduct(cursor.getString(4));
        contact.setDescription(cursor.getString(5));
        contact.setDescription2(cursor.getString(6));
        contact.setAttr(cursor.getString(7));
        contact.setSize(cursor.getString(8));
        contact.setUOM(cursor.getString(9));
        contact.setUnit(cursor.getString(10));
        contact.setPrice(cursor.getString(11));
        contact.setCost(cursor.getString(12));
        contact.setBrand(cursor.getString(13));
        contact.setBatch(cursor.getString(14));

        contact.setQTY(cursor.getString(15));
        contact.setCurrentTimeMillies(cursor.getString(16));
        contact.setTotalScannedDate(cursor.getString(17));
        contact.setTotalScannedTime(cursor.getString(18));
        contact.setLoc(cursor.getString(19));
        contact.setFromLoc(cursor.getString(20));
        contact.setToLoc(cursor.getString(21));
        datalist.add(contact);

        return contact;
    }


    public ArrayList<ItemModel> getMaterialTransferItems(String timeMillies, String location) {
        ArrayList<ItemModel> datalist = new ArrayList<>();
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor;
        if (!location.equals("0")){
            String query = "select * from " + Scanned_MTransfer_Table +" where " + TIME_MILLIES + " = ? AND " + Key_Loc + " = ? ";
            cursor = db.rawQuery(query , new String[] { timeMillies,location});
        }else {
            String selectQuery = "SELECT * FROM " + Scanned_MTransfer_Table + " where TIME_MILLIES = '" + timeMillies + "'";
            cursor = db.rawQuery(selectQuery, null);
        }
        if (cursor.moveToFirst()) {
            do {
                ItemModel contact = new ItemModel();
                contact.setColunm_id(cursor.getString(0));
                contact.setBarcode(cursor.getString(1));
                contact.setOrder_Id(cursor.getString(2));
                contact.setItem_Code(cursor.getString(3));
                contact.setProduct(cursor.getString(4));
                contact.setDescription(cursor.getString(5));
                contact.setDescription2(cursor.getString(6));
                contact.setAttr(cursor.getString(7));
                contact.setSize(cursor.getString(8));
                contact.setUOM(cursor.getString(9));
                contact.setUnit(cursor.getString(10));
                contact.setPrice(cursor.getString(11));
                contact.setCost(cursor.getString(12));
                contact.setBrand(cursor.getString(13));
                contact.setBatch(cursor.getString(14));
                contact.setQTY(cursor.getString(15));
                contact.setCurrentTimeMillies(cursor.getString(16));
                contact.setTotalScannedDate(cursor.getString(17));
                contact.setTotalScannedTime(cursor.getString(18));
                contact.setLoc(cursor.getString(19));
                contact.setFromLoc(cursor.getString(20));
                contact.setToLoc(cursor.getString(21));
                datalist.add(contact);
            } while (cursor.moveToNext());
        }
        return datalist;
    }

    public void updateMaterialTransferItem(ItemModel connect, String column_id){
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Barcode,connect.getBarcode());
        values.put(Key_Unit, connect.getUnit());
        values.put(Key_UOM, connect.getUOM());
        values.put(Key_Batch, connect.getBatch());
        values.put(Key_Description,connect.getDescription());
        values.put(Key_Description2,connect.getDescription2());

        db.update(Scanned_MTransfer_Table, values, Colunm_id + " = ?",
                new String[] { (column_id) });
    }


    public void deleteMaterialTransferItem(int id) {
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        db.delete(Scanned_MTransfer_Table, Colunm_id + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // ================================== SUMMARY INFO =============================================
    // Get Total Qtys
    public int getTotalQtyMT(String timeMillies){
        int totalQTY = 0;
        String selectQuery = "SELECT * FROM " + Scanned_MTransfer_Table + " where TIME_MILLIES = '" + timeMillies + "'";
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                totalQTY += Integer.parseInt(cursor.getString(10)); // Unit total count
            } while (cursor.moveToNext());
        }
        return totalQTY;
    }

    // Get Last Barcode count
    public int getLastBarcodeCodeMT(String timeMillies){
        int totalQTY = 0;
        String selectQuery = "SELECT * FROM " + Scanned_MTransfer_Table + " where TIME_MILLIES = '" + timeMillies + "'";
        DatabaseClass dbClass = new DatabaseClass(context);
        SQLiteDatabase db = dbClass.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast())
            totalQTY = Integer.parseInt(cursor.getString(10)); // Unit value
        return totalQTY;
    }
}
