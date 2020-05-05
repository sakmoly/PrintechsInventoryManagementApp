package com.example.printechsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.printechsapp.ReModel.ItemModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class DatabaseClass extends SQLiteOpenHelper {
    private static DatabaseClass databaseHelper;

    private static final int Database_version      = 3;
    private static final String Database_Name      = "DataManager3";
    public static final String Table_Name          = "Item_Master";
    public static final String Colunm_id           = "id";
    public static final String Barcode             = "Barcode";
    public static final String key_Item_Code       = "Item_Code";
    public static final String Key_Product         = "Description1";
    public static final String Key_Description     = "Description2";
    public static final String Key_Description2    = "Description3";
    public static final String Key_Attr            = "Attr";
    public static final String Key_Size            = "Size";
    public static final String Key_UOM             = "UOM";
    public static final String Key_Unit            = "Unit";
    public static final String Key_Price           = "Price";
    public static final String Key_Cost            = "Cost";
    public static final String Key_Brand           = "Brand";
    public static final String Key_Batch           = "Batch";
    public static final String Key_Loc             = "Location";
    // TABLE Columns - IMP *** (Do not Update These Values)
    public static final String Scanned_Table_Name  = "Scanned_Table";
    public static final String QTY                 = "qty_db";
    public static final String TIME_MILLIES        = "time_millies";
    public static final String DATE_SCANNED        = "scanned_date";
    public static final String TIME_SCANNED        = "scanned_time";
    // Order Table Fields - TABLE Three
    public static final String Material_Recipt_Table  = "Material_Recipt_Table";
    public static final String Order_Id            = "order_id_value";
    public static final String Order_Date          = "order_date";
    public static final String Supplier_Name       = "supplier_name";
    public static final String Order_Qty           = "order_qty";
    public static final String Order_Table         = "Order_Table";
    public static final String Order_Items_Table   = "Order_items_Table";
    // Material Transfer Fields - TABLE Four
    public static final String MaterialTransfer_Items_Table  = "Material_Transfer_Items_Table";
    public static final String Order_MTransfer_Table    = "Order_Material_Transfer_Table";
    public static final String Scanned_MTransfer_Table  = "Scanned_Material_Transfer_Table";
    public static final String From_Loc            = "From_Location";
    public static final String To_Loc              = "To_Location";


    public DatabaseClass(Context context) {
        super(context, Database_Name, null, Database_version);
    }

    public static DatabaseClass getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseClass.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseClass(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Common Items Table
        String Item_Master = "CREATE TABLE " + Table_Name
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT " +")";
        db.execSQL(Item_Master);

        //////////////////////////////////// Physical Stock count TABLE (Scanned Barcode saved)////////////////
        String ScannedTable = "CREATE TABLE " + Scanned_Table_Name
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT,"
                + QTY + " TEXT," + TIME_MILLIES + " TEXT," + DATE_SCANNED + " TEXT," + TIME_SCANNED + " TEXT,"
                + Key_Loc + " TEXT " +")";
        db.execSQL(ScannedTable);

        ///////////////////////////////////// ORDER TABLES (Material Recipt Fns) /////////////////////////////
        String OrderTable = "CREATE TABLE " + Order_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Order_Id + " TEXT," + Order_Date + " TEXT,"
                + Order_Qty + " TEXT," + Supplier_Name + " TEXT " +")";
        db.execSQL(OrderTable);
        // Order Items List Table
        String OrderItems_Table = "CREATE TABLE " + Order_Items_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT," + Order_Id + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT " +")";
        db.execSQL(OrderItems_Table);
        // Scanned Order datas stored here
        String MaterialReciptTable = "CREATE TABLE " + Material_Recipt_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT," + Order_Id + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT,"
                + QTY + " TEXT," + TIME_MILLIES + " TEXT," + DATE_SCANNED + " TEXT," + TIME_SCANNED + " TEXT,"
                + Key_Loc + " TEXT " +")";
        db.execSQL(MaterialReciptTable);


        ///////////////////////////////////// Material Transfer TABLES /////////////////////////////////////////
        String MTOrderTable = "CREATE TABLE " + Order_MTransfer_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Order_Id + " TEXT," + Order_Date + " TEXT,"
                + Order_Qty + " TEXT," + Supplier_Name + " TEXT " +")";
        db.execSQL(MTOrderTable);
        // Order Items Table
        String MaterialTr_Items_Table = "CREATE TABLE " + MaterialTransfer_Items_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT," + Order_Id + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT " +")";
        db.execSQL(MaterialTr_Items_Table);
        // Scanned items table
        String ScannedMTrTable = "CREATE TABLE " + Scanned_MTransfer_Table
                + "(" + Colunm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Barcode + " TEXT," + Order_Id + " TEXT,"
                + key_Item_Code + " TEXT," + Key_Product + " TEXT," + Key_Description + " TEXT," + Key_Description2 + " TEXT,"
                + Key_Attr + " TEXT,"+ Key_Size + " TEXT," + Key_UOM + " TEXT," + Key_Unit + " TEXT,"
                + Key_Price + " TEXT," + Key_Cost + " TEXT," + Key_Brand + " TEXT," + Key_Batch + " TEXT,"
                + QTY + " TEXT," + TIME_MILLIES + " TEXT," + DATE_SCANNED + " TEXT," + TIME_SCANNED + " TEXT,"
                + Key_Loc + " TEXT," + From_Loc + " TEXT," + To_Loc + " TEXT " +")";
        db.execSQL(ScannedMTrTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database_Name);
    }


    public void addContact(ItemModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Colunm_id,           contact.Colunm_id);
        values.put(Barcode,             contact.Barcode);
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
        db.insert(Table_Name,null,values);
        db.close();
    }


    public ArrayList<ItemModel> getAlldata() {
        ArrayList<ItemModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemModel contact = new ItemModel();
                contact.setColunm_id(cursor.getString(0));
                contact.setBarcode(cursor.getString(1));
                contact.setItem_Code(cursor.getString(2));
                contact.setProduct(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setDescription2(cursor.getString(5));
                contact.setAttr(cursor.getString(6));
                contact.setSize(cursor.getString(7));
                contact.setUOM(cursor.getString(8));
                contact.setUnit(cursor.getString(9));
                contact.setPrice(cursor.getString(10));
                contact.setCost(cursor.getString(11));
                contact.setBrand(cursor.getString(12));
                contact.setBatch(cursor.getString(13));
                // Adding contact to list
                datalist.add(contact);
            } while (cursor.moveToNext());
        }
        return datalist;
    }


    // update table one single item price
    public int updatePrice(ItemModel connect, String columnId) {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Key_Price,connect.getPrice());

        return  database.update(Table_Name, values,  Colunm_id + " = ?",
                new String[] { (columnId) });
    }



    // =======================================  TABLE TWO QUERIES ====================================================================
    public void addItemInfo(ItemModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Colunm_id,           contact.Colunm_id);
        values.put(Barcode,             contact.Barcode);
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
        values.put(Key_Loc,             contact.Loc);

        db.insert(Scanned_Table_Name,null,values);
        db.close();
    }

    public LinkedHashSet<String> getAllScannedTimeMillies() {
        LinkedHashSet<String> datalist = new LinkedHashSet<>();

        String selectQuery = "SELECT  TIME_MILLIES FROM " + Scanned_Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                datalist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return datalist;
    }

    // https://stackoverflow.com/questions/33187154/selecting-one-row-from-sqlite-database-using-rawquery-and-rowid-in-android
    public ItemModel getSingleRaw(String timeMillies) {
        ArrayList<ItemModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Scanned_Table_Name + " WHERE "
                + TIME_MILLIES + " = " + timeMillies;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        ItemModel contact = new ItemModel();
        contact.setColunm_id(cursor.getString(0));
        contact.setBarcode(cursor.getString(1));
        contact.setItem_Code(cursor.getString(2));
        contact.setProduct(cursor.getString(3));
        contact.setDescription(cursor.getString(4));
        contact.setDescription2(cursor.getString(5));
        contact.setAttr(cursor.getString(6));
        contact.setSize(cursor.getString(7));
        contact.setUOM(cursor.getString(8));
        contact.setUnit(cursor.getString(9));
        contact.setPrice(cursor.getString(10));
        contact.setCost(cursor.getString(11));
        contact.setBrand(cursor.getString(12));
        contact.setBatch(cursor.getString(13));

        contact.setQTY(cursor.getString(14));
        contact.setCurrentTimeMillies(cursor.getString(15));
        contact.setTotalScannedDate(cursor.getString(16));
        contact.setTotalScannedTime(cursor.getString(17));
        contact.setLoc(cursor.getString(18));
        datalist.add(contact);

        return contact;
    }

    public ArrayList<ItemModel> getAllScannedItems() {
        ArrayList<ItemModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Scanned_Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ItemModel contact = new ItemModel();
                contact.setColunm_id(cursor.getString(0));
                contact.setBarcode(cursor.getString(1));
                contact.setItem_Code(cursor.getString(2));
                contact.setProduct(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setDescription2(cursor.getString(5));
                contact.setAttr(cursor.getString(6));
                contact.setSize(cursor.getString(7));
                contact.setUOM(cursor.getString(8));
                contact.setUnit(cursor.getString(9));
                contact.setPrice(cursor.getString(10));
                contact.setCost(cursor.getString(11));
                contact.setBrand(cursor.getString(12));
                contact.setBatch(cursor.getString(13));

                contact.setQTY(cursor.getString(14));
                contact.setCurrentTimeMillies(cursor.getString(15));
                contact.setTotalScannedDate(cursor.getString(16));
                contact.setTotalScannedTime(cursor.getString(17));
                contact.setLoc(cursor.getString(18));
                datalist.add(contact);
            } while (cursor.moveToNext());
        }
        return datalist;
    }

    public ArrayList<ItemModel> getScannedDatas(String timeMillies, String location) {
        ArrayList<ItemModel> datalist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if (!location.equals("0")){
            String query = "select * from " + Scanned_Table_Name +" where " + TIME_MILLIES + " = ? AND " + Key_Loc + " = ? ";
            cursor = db.rawQuery(query , new String[] { timeMillies,location});
        }else {
            String selectQuery = "SELECT * FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
            cursor = db.rawQuery(selectQuery, null);
        }
        if (cursor.moveToFirst()) {
            do {
                ItemModel contact = new ItemModel();
                contact.setColunm_id(cursor.getString(0));
                contact.setBarcode(cursor.getString(1));
                contact.setItem_Code(cursor.getString(2));
                contact.setProduct(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setDescription2(cursor.getString(5));
                contact.setAttr(cursor.getString(6));
                contact.setSize(cursor.getString(7));
                contact.setUOM(cursor.getString(8));
                contact.setUnit(cursor.getString(9));
                contact.setPrice(cursor.getString(10));
                contact.setCost(cursor.getString(11));
                contact.setBrand(cursor.getString(12));
                contact.setBatch(cursor.getString(13));

                contact.setQTY(cursor.getString(14));
                contact.setCurrentTimeMillies(cursor.getString(15));
                contact.setTotalScannedDate(cursor.getString(16));
                contact.setTotalScannedTime(cursor.getString(17));
                contact.setLoc(cursor.getString(18));
                datalist.add(contact);
            } while (cursor.moveToNext());
        }
        return datalist;
    }

    public void updateTotalQTYCount(ItemModel connect, String time_millies){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(QTY, connect.getQTY());

        db.update(Scanned_Table_Name, values, TIME_MILLIES + " = ?",
                new String[] { (time_millies) });
    }

    public void updateItem(ItemModel connect, String column_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Barcode,connect.getBarcode());
        values.put(Key_Unit, connect.getUnit());
        values.put(Key_UOM, connect.getUOM());
        values.put(Key_Batch, connect.getBatch());
        values.put(Key_Description,connect.getDescription());
        values.put(Key_Description2,connect.getDescription2());

        db.update(Scanned_Table_Name, values, Colunm_id + " = ?",
                new String[] { (column_id) });
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Scanned_Table_Name, Colunm_id + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // https://stackoverflow.com/questions/30994897/how-to-get-last-inserted-row-in-sqlite-android
    public String getLastItemID(){
        String column_id = null;
        //String selectQuery = "SELECT * from SQLITE_SEQUENCE";
        String selectQuery = "SELECT  * FROM " + Scanned_Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();

        if (cursor.moveToLast())
             column_id = cursor.getString(0);

        return column_id;
    }

    public void updateUnit(ItemModel connect, String columnId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Unit,connect.getUnit());
        //values.put(Barcode,connect.getBarcode());

        db.update(Scanned_Table_Name, values, Colunm_id + " = ?",
                new String[] { (columnId) });
    }


    public void updateBatchNo(ItemModel connect, String columnId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Batch,connect.getBatch());

        db.update(Scanned_Table_Name, values, Colunm_id + " = ?",
                new String[] { (columnId) });
    }

    // Summery Info Codes
    public String getSummaryInfo(String timeMillies){
        String summaryData = null;
        String selectQuery = "SELECT  Key_Description FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                summaryData = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return summaryData;
    }
    // Get Total Qtys
    public int getTotalQty(String timeMillies){
        int totalQTY = 0;
        String selectQuery = "SELECT * FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                totalQTY += Integer.parseInt(cursor.getString(9));
            } while (cursor.moveToNext());
        }
        return totalQTY;
    }
    // Get distinct barcode
    public int getDistinctBarcode(String timeMillies){
        LinkedHashSet<String> datalist = new LinkedHashSet<>();
        String selectQuery = "SELECT  Barcode FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                datalist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return datalist.size();
    }
    // Get Total barcode
    public int getTotalBarcode(String timeMillies){
        ArrayList<String> datalist = new ArrayList<>();
        String selectQuery = "SELECT  Barcode FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                datalist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return datalist.size();
    }
    // Get Last Barcode count
    public int getLastBarcodeCode(String timeMillies){
        int totalQTY = 0;
        String selectQuery = "SELECT * FROM " + Scanned_Table_Name + " where TIME_MILLIES = '" + timeMillies + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast())
            totalQTY = Integer.parseInt(cursor.getString(9));
        return totalQTY;
    }
}
