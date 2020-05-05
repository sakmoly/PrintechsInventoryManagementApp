package com.example.printechsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.ReModel.OrderModel;
import com.example.printechsapp.database.DatabaseClass;
import com.example.printechsapp.database.DatabaseQueryMT;
import com.example.printechsapp.database.DatabaseQueryOrder;
import com.example.printechsapp.material_receipt.MaterialReceipt;
import com.example.printechsapp.material_transfer.MaterialTransfer;
import com.example.printechsapp.physical_stock_count.PhysicalStockList;

import java.util.ArrayList;

import static com.example.printechsapp.utils.ConstantsClass.IS_LOADED;
import static com.example.printechsapp.utils.ConstantsClass.PREFERENCES_NAME;

public class HomeActivity extends AppCompatActivity  {
    private LinearLayout material_receipt,material_transfer, price_verification,physical_stock_count,sync;
    private Toolbar toolbar;
    private DatabaseClass databaseClass;
    private DatabaseQueryOrder dbOrder;
    private DatabaseQueryMT dbMT;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // SharedPreferences
        pref   = getSharedPreferences(PREFERENCES_NAME,0);
        editor = pref.edit();
        databaseClass = new DatabaseClass(getApplicationContext());
        dbOrder       = new DatabaseQueryOrder(getApplicationContext());
        dbMT          = new DatabaseQueryMT(getApplicationContext());
        // Inserting all Items
        if (!pref.getBoolean(IS_LOADED, false))
            insertDataToSQLite();

        material_receipt      = findViewById(R.id.material_receipt);
        material_transfer     = findViewById(R.id.material_transfer);
        price_verification    = findViewById(R.id.price_verification);
        physical_stock_count  = findViewById(R.id.physical_stock_count);
        toolbar               = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.printers));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        material_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialReceipt.class);
                startActivity(intent);
            }
        });
        material_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialTransfer.class);
                startActivity(intent);
            }
        });
        physical_stock_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhysicalStockList.class);
                startActivity(intent);
            }
        });
        price_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PriceVarification.class);
                startActivity(intent);
            }
        });
    }

    public void insertDataToSQLite() {
        editor.putBoolean(IS_LOADED, true).apply();
        databaseClass.addContact(new ItemModel("0000000000017","1001","APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1","SIZE1","PCS","1","25","12.5","GROCERY","1"));
        databaseClass.addContact(new ItemModel("0000000000020","1002","NEWFLON TART TIN 27 ","منزلية","NEWFLON TART TIN 27 ","COLOR2","SIZE2","PCS","1","300","150","HOUSEWARE","2"));
        databaseClass.addContact(new ItemModel("0000000000024","1003","APPLE CIDER VINEGAR  ","بقالةبقالة00مل","APPLE CIDER VINEGAR ","COLOR3","SIZE3","PCS","1","110","55","BEVERAGE","0"));
        databaseClass.addContact(new ItemModel("0000000000048","1004","QUINCE JAM 370GM  ","بقالةبقالةجرام","QUINCE JAM 370GM ","COLOR4","SIZE4","PCS","1","12.49","6.245","GROCERY","0"));
        databaseClass.addContact(new ItemModel("0000000000062","1005","APRICOT JAM 370GM ","بقالةبقالةجرام","APRICOT JAM 370GM ","COLOR5","SIZE5","BOX","12","35","17.5","GROCERY","0"));

        // Insert Into Order Table
        dbOrder.addOrderDetails(new OrderModel("1","05-01-2020","4", "Arun Edward"));
        dbOrder.addOrderDetails(new OrderModel("2","20-01-2020","10", "John Varghese"));
        dbOrder.addOrderDetails(new OrderModel("3","01-02-2020","12", "Reshma John"));
        dbOrder.addOrderDetails(new OrderModel("4","12-02-2020","13", "Saji K.S"));
        dbOrder.addOrderDetails(new OrderModel("5","14-02-2020","7", "Jacob"));
        dbOrder.addOrderDetails(new OrderModel("6","20-03-2020","8", "Jithin Jo"));
        dbOrder.addOrderDetails(new OrderModel("7","25-03-2020","5", "Jithin Jo"));
        // Insert Into MT Order Table
        dbMT.addMTOrderDetails(new OrderModel("1","05-01-2020","4", "Arun Edward"));
        dbMT.addMTOrderDetails(new OrderModel("2","20-01-2020","10", "John Varghese"));
        dbMT.addMTOrderDetails(new OrderModel("3","01-02-2020","12", "Reshma John"));
        dbMT.addMTOrderDetails(new OrderModel("4","12-02-2020","13", "Saji K.S"));
        dbMT.addMTOrderDetails(new OrderModel("5","14-02-2020","7", "Jacob"));
        dbMT.addMTOrderDetails(new OrderModel("6","20-03-2020","8", "Jithin Jo"));
        dbMT.addMTOrderDetails(new OrderModel("7","25-03-2020","5", "Jithin Jo"));
        ArrayList<OrderModel> list = dbOrder.getAllOrders();
        Log.w("ORDERS", list.size() + "\n" + list.get(0).getSupplier_Name());
        // 1
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000017","1","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","1","1002", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000024","1","1003", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000048","1","1004", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","0",""));
        // 2
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","2","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","5","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000048","2","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","5","25","12.5","GROCERY","1",""));
        // 3
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000017","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000024","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000048","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000062","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        // 4
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000017","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000024","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000048","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000062","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        // 5
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000064","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000065","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000066","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        // 6
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000017","6","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","6","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        // 7
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000017","7","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","0",""));
        dbOrder.insertSingleOrderItems(new ItemModel("0000000000020","7","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","2",""));
        ArrayList<ItemModel> itmlist = dbOrder.getAllOrderItems("5");
        Log.w("ITEMS", itmlist.size() + "\n" + itmlist.get(0).getBarcode());

        // 1
        dbMT.insertMTOrderItems(new ItemModel("0000000000017","1","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","1","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000024","1","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000048","1","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","0",""));
        // 2
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","2","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","5","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000048","2","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","5","25","12.5","GROCERY","1",""));
        // 3
        dbMT.insertMTOrderItems(new ItemModel("0000000000017","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000024","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000048","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000062","3","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        // 4
        dbMT.insertMTOrderItems(new ItemModel("0000000000017","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000024","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000048","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000062","4","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        // 5
        dbMT.insertMTOrderItems(new ItemModel("0000000000064","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000065","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","1","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000066","5","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","1",""));
        // 6
        dbMT.insertMTOrderItems(new ItemModel("0000000000017","6","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","6","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","4","25","12.5","GROCERY","1",""));
        // 7
        dbMT.insertMTOrderItems(new ItemModel("0000000000017","7","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","2","25","12.5","GROCERY","0",""));
        dbMT.insertMTOrderItems(new ItemModel("0000000000020","7","1001", "APRICOT JAM WITH PIE","بقالةبقالةرام","APRICOT JAM WITH PIE","COLOR1", "SIZE1","PCS","3","25","12.5","GROCERY","2",""));
        ArrayList<ItemModel> itmlist2 = dbMT.getAllMTOrderItems("5");
        Log.w("MT_ITEMS", itmlist2.size() + "\n" + itmlist2.get(0).getBarcode());
    }
}
