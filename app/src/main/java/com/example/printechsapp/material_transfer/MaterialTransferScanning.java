package com.example.printechsapp.material_transfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.SearchBarcodeActivity;
import com.example.printechsapp.database.DatabaseClass;
import com.example.printechsapp.database.DatabaseQueryMT;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static com.example.printechsapp.utils.CommonUtils.commonAlert;
import static com.example.printechsapp.utils.CommonUtils.showKeyBoard;
import static com.example.printechsapp.utils.CommonUtils.toastMsg;
import static com.example.printechsapp.utils.ConstantsClass.ACCEPT_MORE_ORDER_QTY;
import static com.example.printechsapp.utils.ConstantsClass.ORDER_ENABLED;
import static com.example.printechsapp.utils.ConstantsClass.PREFERENCES_NAME;
import static com.example.printechsapp.utils.DateTimeUtils.getTimeIn12Hrs;
import static com.example.printechsapp.utils.DateTimeUtils.getTodaysDateIn_DD_MM_YYYY;

public class MaterialTransferScanning extends AppCompatActivity {
    private Activity activity = MaterialTransferScanning.this;
    private static final int REQUEST_CODE = 1;
    private static final int SEARCH_REQUEST_CODE = 3;
    private String[] locationList = {"LOC1", "LOC2", "LOC3", "LOC4", "LOC5"};
    private Toolbar toolbar;
    private Spinner from_spinner, to_spinner;
    private EditText orderEdt, barcodeEdt, batchEdt, qtyEdt;
    private TextView desc1Txt, desc2Txt, costTxt, uomTxt, unitTxt, orderTotalQTYTxt, totalScannedQtys_Txt,
            lastBarcodeQty_Txt;
    private RelativeLayout orderQty_layout;
    private LinearLayout fromLocLayout, batchNoLayout;
    private String fromlocationValue, tolocationValue;
    private ImageView search_order;

    private boolean isFromItemList, isOrderEnabled = false;
    private boolean isBarcodeExist;
    private int qtyValue = 0, scannedSingleItemQTY = 0, itemMaxQTY = 0;
    private String valuecode, lastColumnId;
    private String time_millies, maxOrderQTY, OrderID;
    private String desc_val, desc2_val, uom_val, price_val, cost_val,
            product_val, item_val, attr_val, brand_val, size_val;
    private ItemModel itemModel;

    private DatabaseClass databaseClass;
    private DatabaseQueryMT dbMT;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<ItemModel> allDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_transfer_scanning);
        // SharedPreferences
        pref   = getSharedPreferences(PREFERENCES_NAME,0);
        editor = pref.edit();
        databaseClass  = new DatabaseClass(getApplicationContext());
        dbMT           = new DatabaseQueryMT(getApplicationContext());

        initViews();


        if (getIntent().getExtras() != null){
            isFromItemList = getIntent().getExtras().getBoolean("isFromItemList_Order");
            if (isFromItemList) {
                itemModel     = getIntent().getParcelableExtra("item_model");
                time_millies  = getIntent().getExtras().getString("timeMillies");
                OrderID       = getIntent().getExtras().getString("Order_ID");
                if (!TextUtils.isEmpty(OrderID)){
                    maxOrderQTY   = dbMT.getMTOrderTotalQTY(OrderID);
                    allDataArray  = dbMT.getAllMTOrderItems(OrderID);
                } else allDataArray  = databaseClass.getAlldata();
                orderEdt.setText(OrderID);
                orderTotalQTYTxt.setText(maxOrderQTY);
                // Disable Order
                orderEdt.setEnabled(false);
                from_spinner.setEnabled(false);
                to_spinner.setEnabled(false);
                //orderNumber.setBackground(ContextCompat.getDrawable(activity, R.drawable.dimm_background_corner));
                search_order.setVisibility(View.GONE);
                preselectLocation(itemModel.getFromLoc(), itemModel.getToLoc());
            } else {
                time_millies  = System.currentTimeMillis() + "";
                allDataArray  = databaseClass.getAlldata();
            }
        } else {
            time_millies  = System.currentTimeMillis() + "";
            allDataArray  = databaseClass.getAlldata();
        }
        Log.w("TIME_MILLI", time_millies + "");



        enableDisableOrder();
        search_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderID = orderEdt.getText().toString();
                Intent intent = new Intent(activity, SearchOrdersMT.class);
                intent.putExtra("search_order", orderID);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        // Search Barcode exists
        findViewById(R.id.search_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderID = orderEdt.getText().toString();
                if (!TextUtils.isEmpty(orderID)){
                    Intent intent = new Intent(activity, OrderItemsListMT.class);
                    intent.putExtra("search_barcode", barcodeEdt.getText().toString());
                    intent.putExtra("order_id",       orderID);
                    intent.putExtra("is_search_barcode", true);
                    startActivityForResult(intent, SEARCH_REQUEST_CODE);
                }else {
                    if (isOrderEnabled) toastMsg(activity, "Please select order ID!");
                    else {
                        Intent intent = new Intent(activity, SearchBarcodeActivity.class);
                        intent.putExtra("search_barcode", barcodeEdt.getText().toString());
                        startActivityForResult(intent, SEARCH_REQUEST_CODE);
                    }
                }
            }
        });


        batchNoKeyPress();
        // QTY & Barcode Key Press Fn's
        if (getIntent().getExtras() == null){
            isOrderEnabled = pref.getBoolean(ORDER_ENABLED, false);
            barCodeKeyPress(isOrderEnabled);
            qtyKeyPress(isOrderEnabled);
            // Location Spinner
            selectLocations();
        }else {
            // If Edit Function
            if (!TextUtils.isEmpty(OrderID)){
                if (!OrderID.equals("0")){
                    isOrderEnabled = true;
                    barCodeKeyPress(isOrderEnabled);
                    qtyKeyPress(isOrderEnabled);
                }
            } else {
                isOrderEnabled = false;
                barCodeKeyPress(isOrderEnabled);
                qtyKeyPress(isOrderEnabled);
            }
        }

        getSummaryInfo();
    }

    private void initViews() {
        from_spinner     = findViewById(R.id.from_location_spinner);
        to_spinner       = findViewById(R.id.to_location_spinner);
        orderEdt         = findViewById(R.id.order_num_editText);
        barcodeEdt       = findViewById(R.id.barcode_transfer);
        batchEdt         = findViewById(R.id.batchNo_tr);
        search_order     = findViewById(R.id.search_order);
        qtyEdt           = findViewById(R.id.qty_tr);
        desc1Txt         = findViewById(R.id.desc1_tr);
        desc2Txt         = findViewById(R.id.des2_tr);
        costTxt          = findViewById(R.id.cost_tr);
        uomTxt           = findViewById(R.id.uom_tr);
        unitTxt          = findViewById(R.id.unit_tr);
        orderQty_layout  = findViewById(R.id.orderQty_layout);
        orderTotalQTYTxt    = findViewById(R.id.order_totalQty);
        totalScannedQtys_Txt  = findViewById(R.id.total_scanned_quantities);
        lastBarcodeQty_Txt    = findViewById(R.id.last_barcode_qty);
        fromLocLayout    = findViewById(R.id.from_spinner_layout);
        batchNoLayout    = findViewById(R.id.batchNo_layoutMT);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void batchNoKeyPress() {
        batchEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    qtyEdt.requestFocus();
                }
                return false;
            }
        });
    }

    // commonAlert(activity, getString(R.string.alert), "Please select an order!");
    private void qtyKeyPress(final boolean isOrderEnabled) {
        qtyEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    batchNoLayout.setVisibility(INVISIBLE);

                    // Very IMP *** Function
                    String qtyVal  = qtyEdt.getText().toString().trim();
                    if (isOrderEnabled){
                        if (itemMaxQTY != 0){
                            Log.w("ITEM_QTY", itemMaxQTY + ""); // Max item qty
                            if (!TextUtils.isEmpty(qtyVal))
                                qtyValue = Integer.parseInt(qtyVal); // Total we scanned qty count

                            int scannedSubTotal  = scannedSingleItemQTY + qtyValue;
                            if (scannedSubTotal <= itemMaxQTY){
                                insertIntoDB();
                            } else {
                                if (pref.getBoolean(ACCEPT_MORE_ORDER_QTY, false)){
                                    insertIntoDB();
                                }else {
                                    commonAlert(activity, getString(R.string.alert), "Item count exceeds maximum value " + itemMaxQTY);
                                    clearTextValues();
                                }
                            }
                        } else toastMsg(activity, "DB Item count is 0!");
                    } else {
                        if (!TextUtils.isEmpty(qtyVal))
                            qtyValue = Integer.parseInt(qtyVal); // Total we scanned count
                        insertIntoDB();
                    }


                    barcodeEdt.requestFocus();
                    getSummaryInfo();
                    clearTextValues();
                }
                return false;
            }
        });
    }


    private void barCodeKeyPress(final boolean isOrderEnabled) {
        barcodeEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String orderID = orderEdt.getText().toString();
                    if (isOrderEnabled && TextUtils.isEmpty(orderID)){
                        msgAlert(activity, getString(R.string.alert), "Please select an order!");
                        clearTextValues();
                    } else {
                        valuecode = barcodeEdt.getText().toString();
                        if (TextUtils.isEmpty(valuecode)) {
                            // BarCode is empty
                            barcodeEdt.requestFocus();
                            msgAlert(activity, getString(R.string.alert),getString(R.string.plz_enter_a_barcode));
                            clearTextValues();
                        } else {
                            //qty.setFocusableInTouchMode(true);
                            for (int i = 0; i < allDataArray.size(); i++) {
                                if (allDataArray.get(i).getBarcode().equals(valuecode)){
                                    isBarcodeExist = true;
                                    itemMaxQTY           = dbMT.itemMaxQTY_MT(valuecode, orderID);
                                    scannedSingleItemQTY = dbMT.scannedItemMaxQTY_MT(valuecode, orderID, time_millies);

                                    lastColumnId = allDataArray.get(i).getColunm_id();
                                    desc_val     = allDataArray.get(i).getDescription();
                                    desc2_val    = allDataArray.get(i).getDescription2();
                                    uom_val      = allDataArray.get(i).getUOM();
                                    String unit_val  = allDataArray.get(i).getUnit();
                                    price_val    = allDataArray.get(i).getPrice();
                                    cost_val     = allDataArray.get(i).getCost();
                                    product_val  = allDataArray.get(i).getProduct();
                                    item_val     = allDataArray.get(i).getItem_Code();
                                    attr_val     = allDataArray.get(i).getAttr();
                                    String batch_val  = allDataArray.get(i).getBatch();
                                    brand_val    = allDataArray.get(i).getBrand();
                                    size_val     = allDataArray.get(i).getSize();
                                    uomTxt.setText(uom_val);
                                    desc2Txt.setText(desc_val);
                                    desc1Txt.setText(desc2_val);
                                    unitTxt.setText(unit_val);
                                    costTxt.setText(cost_val);
                                    qtyEdt.setText("1");
                                    qtyEdt.selectAll();
                                    //current_price.setText(price_val);

                                    if (allDataArray.get(i).getBatch().trim().equals("0")) {
                                        batchNoLayout.setVisibility(INVISIBLE);
                                    } else {
                                        batchNoLayout.setVisibility(View.VISIBLE);
                                        batchEdt.setText(batch_val);
                                        batchEdt.selectAll();
                                    }

                                    break;
                                }
                                else isBarcodeExist = false;
                            }
                            if(!isBarcodeExist) {
                                msgAlert(activity, getString(R.string.not_found),getString(R.string.barcode_not_exists));
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    private void insertIntoDB(){
        // Unit Value
        String qtyTxt         = qtyEdt.getText().toString();
        String unit_value     = TextUtils.isEmpty(qtyTxt) ? "0" : qtyTxt;
        // Batch Value
        String batchTxt       = batchEdt.getText().toString().trim();
        String batch_Value    = TextUtils.isEmpty(batchTxt) ? "0" : batchTxt;
        // Order Id
        String orderIdTxt     = orderEdt.getText().toString();
        String order_id_Value = TextUtils.isEmpty(orderIdTxt) ? "0" : orderIdTxt;

        ItemModel connect = new ItemModel(valuecode, order_id_Value, item_val, product_val, desc_val,desc2_val, attr_val,
                size_val, uom_val, unit_value, price_val, cost_val, brand_val, batch_Value, "0",
                time_millies + "", getTodaysDateIn_DD_MM_YYYY(), getTimeIn12Hrs(),
                fromlocationValue, tolocationValue);
        dbMT.addItemMTOrder(connect);
        lastColumnId = databaseClass.getLastItemID();
        Log.w("LAST_COLUMN", lastColumnId + "");
    }


    private void clearTextValues() {
        barcodeEdt.setText("");
        batchEdt.setText("");
        costTxt.setText("");
        uomTxt.setText("");
        unitTxt.setText("");
        desc2Txt.setText("");
        desc1Txt.setText("");
        qtyEdt.setText("");
    }

    private void getSummaryInfo() {
        int totalScannedQty           = dbMT.getTotalQtyMT(time_millies);
        int lastBarcodeCount_value    = dbMT.getLastBarcodeCodeMT(time_millies);
        Log.e("VALUES_SUM_2", totalScannedQty + "\n" + lastBarcodeCount_value);
        // Total Scanned Qty
        totalScannedQtys_Txt.setText(totalScannedQty + "");
        // Last Scanned . Qty
        lastBarcodeQty_Txt.setText(lastBarcodeCount_value + "");

        updateTotalQTYCountOrder(totalScannedQty);
    }

    private void updateTotalQTYCountOrder(int totalScannedQty) {
        ItemModel co = new ItemModel();
        co.setQTY(totalScannedQty + "");
        dbMT.updateTotalQTYCountMT(co,time_millies + "");
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String order_num  = data.getStringExtra("order_num");
                maxOrderQTY = data.getStringExtra("orderTotalQTY");
                orderEdt.setText(order_num);
                orderTotalQTYTxt.setText(maxOrderQTY);

                if (!TextUtils.isEmpty(order_num)) {
                    allDataArray = dbMT.getAllMTOrderItems(order_num);
                    Log.w("ItemsTotal", allDataArray.size() + "");
                }

            }else if (requestCode == SEARCH_REQUEST_CODE  && resultCode  == RESULT_OK){
                // Search Barcode Function
                ItemModel connect = data.getParcelableExtra("barcode_model");
                barcodeEdt.setText(connect.getBarcode());

                String batch_num = connect.getBatch();
                if (!TextUtils.isEmpty(batch_num)) {

                    if (!connect.getBatch().trim().equals("0")){
                        batchEdt.setText(batch_num);
                        batchNoLayout.setVisibility(View.VISIBLE);
                    } else batchNoLayout.setVisibility(INVISIBLE);

                } else batchNoLayout.setVisibility(INVISIBLE);

                qtyEdt.setText("1");

                barcodeEdt.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void selectLocations(){
        ArrayAdapter aa = new ArrayAdapter(activity, R.layout.spinner_list, locationList);
        aa.setDropDownViewResource(R.layout.spinner_list);
        // From Location
        from_spinner.setAdapter(aa);
        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                fromlocationValue = locationList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // To Location
        to_spinner.setAdapter(aa);
        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                tolocationValue = locationList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void enableDisableOrder() {
        if (getIntent().getExtras() == null){
            if (pref.getBoolean(ORDER_ENABLED, false)) {
                //order_layout.setVisibility(View.VISIBLE);
                search_order.setVisibility(View.VISIBLE);
                orderQty_layout.setVisibility(View.VISIBLE);
            } else {
                //order_layout.setVisibility(View.GONE);
                search_order.setVisibility(View.GONE);
                orderQty_layout.setVisibility(View.GONE);
            }
        }
    }


    private void preselectLocation(String fromLoc, String toLoc) {
        Log.w("LOC_VALUE", fromLoc + "");
        //ArrayAdapter<CharSequence> langAdapter11 = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, gender );
        ArrayAdapter adapterSpinner = new ArrayAdapter(activity, R.layout.spinner_list, locationList);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_list);
        // From Loc
        from_spinner.setAdapter(adapterSpinner);
        if (!TextUtils.isEmpty(fromLoc)) {
            int spinnerPosition = adapterSpinner.getPosition(fromLoc);
            from_spinner.setSelection(spinnerPosition);
        }
        // To Loc
        to_spinner.setAdapter(adapterSpinner);
        if (!TextUtils.isEmpty(toLoc)) {
            int spinnerPosition = adapterSpinner.getPosition(toLoc);
            to_spinner.setSelection(spinnerPosition);
        }
    }

    private void msgAlert(Context context, String title, String message) {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(message)
                //.setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        barcodeEdt.requestFocus();
                        showKeyBoard(activity);
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
}
