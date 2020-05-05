package com.example.printechsapp.material_receipt;

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

import com.example.printechsapp.SearchBarcodeActivity;
import com.example.printechsapp.database.DatabaseClass;
import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseQueryOrder;
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

public class MaterialReciptScanning extends AppCompatActivity {
    private Activity activity = MaterialReciptScanning.this;
    private static final int REQUEST_CODE = 1;
    public  static final int SEARCH_REQUEST_CODE = 3;
    private String[] locationList = {"LOC1", "LOC2", "LOC3", "LOC4", "LOC5"};
    private Spinner location_spinner;
    private ImageView search_order;
    private String locationValue, valuecode, lastColumnId;
    private Toolbar toolbar;
    private EditText orderNumber,barcode, batchNo, qty;
    private TextView totalScannedQtys_Txt, desc1, desc2, cost, uom, unit;
    private TextView orderTotalQTYQTxt, last_barcode_qty;
    private boolean isBarcodeExist;
    private LinearLayout batchNoLayout, order_layout;
    private RelativeLayout orderQtyLayout;
    private boolean isFromItemList, isOrderEnabled = false;
    private String time_millies, maxOrderQTY, OrderID;
    private String desc_val, desc2_val, uom_val, price_val, cost_val,
            product_val, item_val, attr_val, brand_val, size_val;
    private int qtyValue = 0, scannedSingleItemQTY = 0, itemMaxQTY = 0;
    private ItemModel itemModel;


    private DatabaseClass databaseClass;
    private DatabaseQueryOrder dbOrder;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<ItemModel> allDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityedit_materialrecipt);
        // SharedPreferences
        pref   = getSharedPreferences(PREFERENCES_NAME,0);
        editor = pref.edit();
        databaseClass  = new DatabaseClass(getApplicationContext());
        dbOrder        = new DatabaseQueryOrder(getApplicationContext());

        initViews();

        if (getIntent().getExtras() != null){
            isFromItemList = getIntent().getExtras().getBoolean("isFromItemList_Order");
            if (isFromItemList) {
                itemModel     = getIntent().getParcelableExtra("item_model");
                time_millies  = getIntent().getExtras().getString("timeMillies");
                OrderID       = getIntent().getExtras().getString("Order_ID");
                if (!TextUtils.isEmpty(OrderID)){
                    maxOrderQTY   = dbOrder.getOrderTotalQTY(OrderID);
                    allDataArray  = dbOrder.getAllOrderItems(OrderID);
                } else allDataArray  = databaseClass.getAlldata();
                orderNumber.setText(OrderID);
                orderTotalQTYQTxt.setText(maxOrderQTY);
                // Disable Order
                orderNumber.setEnabled(false);
                location_spinner.setEnabled(false);
                //orderNumber.setBackground(ContextCompat.getDrawable(activity, R.drawable.dimm_background_corner));
                search_order.setVisibility(View.GONE);
                preselectLocation(itemModel.getLoc());
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
                String orderID = orderNumber.getText().toString();
                Intent intent = new Intent(activity, SearchOrders.class);
                intent.putExtra("search_order", orderID);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        // Search Barcode exists
        findViewById(R.id.search_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderID = orderNumber.getText().toString();
                if (!TextUtils.isEmpty(orderID)){
                    Intent intent = new Intent(activity, OrderItemsMR.class);
                    intent.putExtra("search_barcode", barcode.getText().toString());
                    intent.putExtra("order_id",       orderID);
                    intent.putExtra("is_search_barcode", true);
                    startActivityForResult(intent, SEARCH_REQUEST_CODE);
                }else {
                    if (isOrderEnabled) toastMsg(activity, "Please select order ID!");
                    else {
                        Intent intent = new Intent(activity, SearchBarcodeActivity.class);
                        intent.putExtra("search_barcode", barcode.getText().toString());
                        startActivityForResult(intent, SEARCH_REQUEST_CODE);
                    }
                }
            }
        });


        batchNoKeyPress();
        // QTY & Barcode Key Press Fn's
        if (getIntent().getExtras() == null) {
            isOrderEnabled = pref.getBoolean(ORDER_ENABLED, false);
            barCodeKeyPress(isOrderEnabled);
            qtyKeyPress(isOrderEnabled);
            // Location Spinner
            selectLocation();
        } else {
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
        location_spinner    = findViewById(R.id.location_spinner);
        search_order        = findViewById(R.id.search_order);
        toolbar             = findViewById(R.id.toolbar2);
        orderNumber         = findViewById(R.id.order_num_editText);
        totalScannedQtys_Txt = findViewById(R.id.total_scanned_quantities);
        barcode             = findViewById(R.id.barcode_orderEdt);
        batchNo             = findViewById(R.id.batchNo_o);
        qty                 = findViewById(R.id.qty_o);
        desc1               = findViewById(R.id.desc1_o);
        desc2               = findViewById(R.id.des2_o);
        cost                = findViewById(R.id.cost_o);
        uom                 = findViewById(R.id.uom_o);
        unit                = findViewById(R.id.unit_o);
        orderQtyLayout      = findViewById(R.id.orderQty_layout);
        order_layout        = findViewById(R.id.order_layout);
        last_barcode_qty    = findViewById(R.id.last_barcode_qty);
        orderTotalQTYQTxt   = findViewById(R.id.order_totalQty);
        batchNoLayout       = findViewById(R.id.batchNo_layoutOrder);
        batchNoLayout.setVisibility(INVISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        barcode.requestFocus();
    }


    private void batchNoKeyPress() {
        batchNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    qty.requestFocus();
                }
                return false;
            }
        });
    }

    // commonAlert(activity, getString(R.string.alert), "Please select an order!");
    private void qtyKeyPress(final boolean isOrderEnabled) {
        qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    batchNoLayout.setVisibility(INVISIBLE);

                    // Very IMP *** Function
                    String qtyVal  = qty.getText().toString().trim();
                    if (isOrderEnabled){
                        if (itemMaxQTY != 0){
                            Log.w("ITEM_QTY", itemMaxQTY + " - " + scannedSingleItemQTY); // Max item qty
                            if (!TextUtils.isEmpty(qtyVal))
                                qtyValue = Integer.parseInt(qtyVal); // scanned qty Value

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


                    barcode.requestFocus();
                    getSummaryInfo();
                    clearTextValues();
                }
                return false;
            }
        });
    }


    private void barCodeKeyPress(final boolean isOrderEnabled) {
        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String orderID = orderNumber.getText().toString();
                    if (isOrderEnabled && TextUtils.isEmpty(orderID)){
                        msgAlert(activity, getString(R.string.alert), "Please select an order!");
                        clearTextValues();
                    } else {
                        valuecode = barcode.getText().toString();
                        if (TextUtils.isEmpty(valuecode)) {
                            // BarCode is empty
                            barcode.requestFocus();
                            msgAlert(activity, getString(R.string.alert),getString(R.string.plz_enter_a_barcode));
                            clearTextValues();
                        } else {
                            //qty.setFocusableInTouchMode(true);
                            for (int i = 0; i < allDataArray.size(); i++) {
                                if (allDataArray.get(i).getBarcode().equals(valuecode)){
                                    isBarcodeExist = true;
                                    itemMaxQTY           = dbOrder.singleItemQTY(valuecode, orderID);
                                    scannedSingleItemQTY = dbOrder.scannedItemQTY(valuecode, orderID, time_millies);

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
                                    uom.setText(uom_val);
                                    desc2.setText(desc_val);
                                    desc1.setText(desc2_val);
                                    unit.setText(unit_val);
                                    cost.setText(cost_val);
                                    qty.setText("1");
                                    qty.selectAll();
                                    //current_price.setText(price_val);

                                    if (allDataArray.get(i).getBatch().trim().equals("0")) {
                                        batchNoLayout.setVisibility(INVISIBLE);
                                    } else {
                                        batchNoLayout.setVisibility(View.VISIBLE);
                                        batchNo.setText(batch_val);
                                        batchNo.selectAll();
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
        String qtyTxt         = qty.getText().toString();
        String unit_value     = TextUtils.isEmpty(qtyTxt) ? "0" : qtyTxt;
        // Batch Value
        String batchTxt       = batchNo.getText().toString().trim();
        String batch_Value    = TextUtils.isEmpty(batchTxt) ? "0" : batchTxt;
        // Order Id
        String orderIdTxt     = orderNumber.getText().toString();
        String order_id_Value = TextUtils.isEmpty(orderIdTxt) ? "0" : orderIdTxt;

        ItemModel connect = new ItemModel(valuecode, order_id_Value, item_val, product_val, desc_val,desc2_val, attr_val,
                size_val, uom_val, unit_value, price_val, cost_val, brand_val, batch_Value, "0",
                time_millies + "", getTodaysDateIn_DD_MM_YYYY(), getTimeIn12Hrs(),
                locationValue);
        dbOrder.addItemOrder(connect);
        lastColumnId = databaseClass.getLastItemID();
        Log.w("LAST_COLUMN", lastColumnId + "");
    }


    private void clearTextValues() {
        barcode.setText("");
        batchNo.setText("");
        cost.setText("");
        uom.setText("");
        unit.setText("");
        desc2.setText("");
        desc1.setText("");
        qty.setText("");
    }

    private void getSummaryInfo() {
        int totalScannedQty           = dbOrder.getTotalQty(time_millies);
        int lastBarcodeCount_value    = dbOrder.getLastBarcodeCode(time_millies);
        Log.e("VALUES_SUM_2", totalScannedQty + "\n" + lastBarcodeCount_value);
        // Total Scanned Qty
        totalScannedQtys_Txt.setText(totalScannedQty + "");
        // Last Scanned . Qty
        last_barcode_qty.setText(lastBarcodeCount_value + "");

        updateTotalQTYCountOrder(totalScannedQty);
    }

    private void updateTotalQTYCountOrder(int totalScannedQty) {
        ItemModel co = new ItemModel();
        co.setQTY(totalScannedQty + "");
        dbOrder.updateTotalQTYCountOrder(co,time_millies + "");
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String order_num  = data.getStringExtra("order_num");
                maxOrderQTY = data.getStringExtra("orderTotalQTY");
                orderNumber.setText(order_num);
                orderTotalQTYQTxt.setText(maxOrderQTY);

                if (!TextUtils.isEmpty(order_num)) {
                    allDataArray = dbOrder.getAllOrderItems(order_num);
                    Log.w("ItemsTotal", allDataArray.size() + "");
                }

            }else if (requestCode == SEARCH_REQUEST_CODE  && resultCode  == RESULT_OK){
                // Search Barcode Function
                ItemModel connect = data.getParcelableExtra("barcode_model");
                barcode.setText(connect.getBarcode());

                String batch_num = connect.getBatch();
                if (!TextUtils.isEmpty(batch_num)) {

                    if (!connect.getBatch().trim().equals("0")){
                        batchNo.setText(batch_num);
                        batchNoLayout.setVisibility(View.VISIBLE);
                    } else batchNoLayout.setVisibility(INVISIBLE);

                } else batchNoLayout.setVisibility(INVISIBLE);

                qty.setText("1");

                barcode.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void selectLocation(){
        ArrayAdapter aa = new ArrayAdapter(activity, R.layout.spinner_list, locationList);
        aa.setDropDownViewResource(R.layout.spinner_list);
        location_spinner.setAdapter(aa);
        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                locationValue = locationList[position];
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
                orderQtyLayout.setVisibility(View.VISIBLE);
            } else {
                //order_layout.setVisibility(View.GONE);
                search_order.setVisibility(View.GONE);
                orderQtyLayout.setVisibility(View.GONE);
            }
        }
    }

    private void preselectLocation(String locationValue) {
        Log.w("LOC_VALUE", locationValue + "");
        //ArrayAdapter<CharSequence> langAdapter11 = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, gender );
        ArrayAdapter adapterSpinner = new ArrayAdapter(activity, R.layout.spinner_list, locationList);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_list);
        location_spinner.setAdapter(adapterSpinner);
        if (!TextUtils.isEmpty(locationValue)) {
            int spinnerPosition = adapterSpinner.getPosition(locationValue);
            location_spinner.setSelection(spinnerPosition);
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
                        barcode.requestFocus();
                        showKeyBoard(activity);
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
}
