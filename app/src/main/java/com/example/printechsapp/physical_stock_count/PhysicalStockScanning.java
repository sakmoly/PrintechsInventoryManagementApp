package com.example.printechsapp.physical_stock_count;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.printechsapp.database.DatabaseClass;
import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.SearchBarcodeActivity;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static com.example.printechsapp.utils.CommonUtils.showKeyBoard;
import static com.example.printechsapp.utils.CommonUtils.toastMsg;
import static com.example.printechsapp.utils.DateTimeUtils.getTimeIn12Hrs;
import static com.example.printechsapp.utils.DateTimeUtils.getTodaysDateIn_DD_MM_YYYY;

@SuppressWarnings("unchecked")
public class PhysicalStockScanning extends AppCompatActivity {
    private Activity activity = PhysicalStockScanning.this;
    public Context context;
    private int totalBarcodeScanned = 0, totalQtyCount = 0, itemsCount = 0, lastBarcodeCount = 0;
    private CheckBox cb;
    private ImageView list;
    private TextView  desc1, des2, uom,code,unit, batchText;
    private ImageView btnnn;
    private LinearLayout info_layout, batchNoLayout;
    private EditText qty, batchNo,test;
    private EditText tv_barcode,name;
    private Spinner location_spinner, rn_spinner;
    private Button save;
    private String[] locationList = {"LOC1", "LOC2", "LOC3", "LOC4", "LOC5"};
    private String[] rnlist = {"PL", "PL", "PL", "PL"};
    private String locationValue;
    //public static ArrayList<Connect> singleArray_Recipt = new ArrayList<>();
    private boolean isFromItemList;
    private String time_millies;
    private DatabaseClass databaseClass;
    private ArrayList<ItemModel> allDataArray;
    private boolean isBarcodeExist = true;
    private RelativeLayout parentLayout,layout_summary;
    private Toolbar toolbar;
    private ImageView searchBarcode;
    // Summery Fields
    private TextView total_barcode_scanned,total_scanned_quantities,last_barcode_qty,total_sameBarcode_scanned;
    private String lastColumnId;


    @Override
    protected void onResume() {
        super.onResume();
        getSummaryInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt_data);
        databaseClass = new DatabaseClass(getApplicationContext());
        if (getIntent().getExtras() != null){
            isFromItemList = getIntent().getExtras().getBoolean("isFromItemList");
            if (isFromItemList) {
                time_millies  = getIntent().getExtras().getString("timeMillies");
            } else time_millies = System.currentTimeMillis() + "";

        } else time_millies = System.currentTimeMillis() + "";
        Log.w("TIME_MILLI", time_millies + "");


        // Summery Field
        total_barcode_scanned    = findViewById(R.id.total_barcode_scanned);
        total_scanned_quantities = findViewById(R.id.total_scanned_quantities);
        last_barcode_qty         = findViewById(R.id.last_barcode_qty);
        total_sameBarcode_scanned= findViewById(R.id.total_sameBarcode_scanned);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final DatabaseClass db = new DatabaseClass(getApplicationContext());
        allDataArray = db.getAlldata();

        parentLayout        = findViewById(R.id.parentLayout);
        location_spinner    = findViewById(R.id.location);
        rn_spinner          = findViewById(R.id.rn);
        tv_barcode          = (EditText) findViewById(R.id.barcode);
        batchNo             = (EditText) findViewById(R.id.no);
        unit                = (TextView) findViewById(R.id.unit);
        desc1               = (TextView) findViewById(R.id.desc1);
        batchNoLayout       = findViewById(R.id.batchNo_layout);
        batchText           = findViewById(R.id.bat_name);
        des2                = (TextView) findViewById(R.id.des2);
        uom                 = (TextView) findViewById(R.id.uom);
        qty                 = findViewById(R.id.qty);
        code                = findViewById(R.id.code);
        cb                  = findViewById(R.id.cb);
        info_layout         = findViewById(R.id.info_layout);
        test                = findViewById(R.id.test);
        layout_summary      = findViewById(R.id.layout_summary);
        btnnn               = (ImageView) findViewById(R.id.btnnn);
        list                = (ImageView) findViewById(R.id.list);
        save                = findViewById(R.id.save);
        searchBarcode       = findViewById(R.id.et_referenceno);
        searchBarcode.bringToFront();
        searchBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = tv_barcode.getText().toString();
                Intent intent = new Intent(activity, SearchBarcodeActivity.class);
                intent.putExtra("search_barcode",barcode);
                startActivityForResult(intent, 2);
            }
        });

        batchNoLayout.setVisibility(INVISIBLE);


        ArrayAdapter aa = new ArrayAdapter(PhysicalStockScanning.this, R.layout.spinner_list, locationList);
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


        ArrayAdapter rndata = new ArrayAdapter(PhysicalStockScanning.this, R.layout.spinner_list, rnlist);
        rndata.setDropDownViewResource(R.layout.spinner_list);
        rn_spinner.setAdapter(rndata);


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StockTake_List.class);
                intent.putExtra("timeMillies",time_millies + "");
                startActivityForResult(intent,1);
            }
        });

        barCodeKeyPress();

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb.isChecked()){
                    qty.setText("1");
                    qty.setFocusable(false);
                    tv_barcode.requestFocus();
                    toastMsg(activity, "Auto");
                } else {
                    qty.setText("");
                    qty.setFocusableInTouchMode(true);
                }

            }
        });


        final String count = qty.getText().toString();
        if (count.isEmpty()){
            tv_barcode.requestFocus();
        }


        batchNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // Update Batch# in db
                    String batchTxt = batchNo.getText().toString().trim();
                    String batchValue = TextUtils.isEmpty(batchTxt) ? "0" : batchTxt;
                    ItemModel co = new ItemModel();
                    co.setBatch(batchValue);
                    db.updateBatchNo(co, lastColumnId);


                    if (cb.isChecked()) {
                        tv_barcode.setText("");
                        tv_barcode.requestFocus();

                        batchNo.setText("");
                        batchNoLayout.setVisibility(INVISIBLE);

                        clearTextValues();
                    } else qty.requestFocus();
                }
                return false;
            }
        });


        qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    batchNoLayout.setVisibility(INVISIBLE);

                    // Update Qty in db
                    String qtyTxt = qty.getText().toString().trim();
                    String qtyValue = TextUtils.isEmpty(qtyTxt) ? "0" : qtyTxt;
                    ItemModel co = new ItemModel();
                    co.setUnit(qtyValue);
                    db.updateUnit(co, lastColumnId);

                    tv_barcode.requestFocus();

                    updateTotalCountinDB();
                    getSummaryInfo();

                    clearTextValues();
                }
                return false;
            }
        });



        layout_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info_layout.getVisibility() == View.VISIBLE) {
                    btnnn.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    ObjectAnimator.ofFloat(btnnn, "rotation", 0, 180).start();

                    Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                    info_layout.startAnimation(animSlideUp);
                    info_layout.setVisibility(INVISIBLE);
                    info_layout.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    btnnn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    ObjectAnimator.ofFloat(btnnn, "rotation", 0, 180).start();

                    info_layout.setVisibility(View.VISIBLE);
                    Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
                    info_layout.startAnimation(animSlideDown);
                    info_layout.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    private void barCodeKeyPress() {
        tv_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String valuecode = tv_barcode.getText().toString();
                    if (TextUtils.isEmpty(valuecode)) {
                        // BarCode is empty
                        batchNoLayout.setVisibility(INVISIBLE);
                        qty.setFocusable(false);
                        tv_barcode.requestFocus();
                        toastMsg(activity, getString(R.string.plz_enter_a_barcode));

                        clearTextValues();
                    }else {
                        if (!cb.isChecked())
                            qty.setFocusableInTouchMode(true);
                        for (int i = 0; i < allDataArray.size(); i++) {
                            if (allDataArray.get(i).getBarcode().equals(valuecode)) {
                                isBarcodeExist = true;

                                String desc_val     = allDataArray.get(i).getDescription();
                                String desc2_val    = allDataArray.get(i).getDescription2();
                                String brand_val    = allDataArray.get(i).getBrand();
                                String item_val     = allDataArray.get(i).getItem_Code();
                                String product_val  = allDataArray.get(i).getProduct();
                                String attr_val     = allDataArray.get(i).getAttr();
                                String size_val     = allDataArray.get(i).getSize();
                                String uom_val      = allDataArray.get(i).getUOM();
                                String unit_val     = allDataArray.get(i).getUnit();
                                String price_val    = allDataArray.get(i).getPrice();
                                String cost_val     = allDataArray.get(i).getCost();
                                String batch_val    = allDataArray.get(i).getBatch();
                                uom.setText(uom_val);
                                des2.setText(desc2_val);
                                desc1.setText(desc_val);
                                unit.setText(unit_val);
                                code.setText(cost_val);
                                qty.setText("1");
                                qty.selectAll();


                                if (allDataArray.get(i).getBatch().trim().equals("0")) {
                                    batchNoLayout.setVisibility(INVISIBLE);
                                } else {
                                    batchNoLayout.setVisibility(View.VISIBLE);
                                    batchNo.setText(allDataArray.get(i).getBatch());
                                    batchNo.selectAll();
                                }

                                Log.w("SQL_DB_VALUES",desc_val + "\n" + brand_val + "\n" +
                                        item_val + "\n" + product_val + "\n" + desc2_val + "\n" + attr_val + "\n" + size_val + "\n" + uom + "\n"
                                        + unit + "\n" +  price_val + "\n" + cost_val + "\n" + batch_val);


                                itemsCount += 1;
                                // Insert to DB
                                ItemModel connect = new ItemModel(valuecode, item_val, product_val, desc_val,desc2_val, attr_val,
                                        size_val, uom_val, unit_val, price_val, cost_val,brand_val,batch_val, "0",
                                        time_millies + "", getTodaysDateIn_DD_MM_YYYY(), getTimeIn12Hrs(),
                                        locationValue);
                                databaseClass.addItemInfo(connect);
                                lastColumnId = databaseClass.getLastItemID();
                                Log.w("LAST_COLUMN", lastColumnId + "");

                                clearALLDataIfAuto(batch_val);

                                if (cb.isChecked()){
                                    updateTotalCountinDB();
                                    getSummaryInfo();
                                }

                                break;
                            } else isBarcodeExist = false;
                        }

                        if (!isBarcodeExist) {
                            msgAlert(activity, getString(R.string.not_found),getString(R.string.barcode_not_exists));
                        }
                    }
                }
                return false;
            }
        });
    }

    // https://stackoverflow.com/questions/4297763/disabling-of-edittext-in-android
    private void clearALLDataIfAuto(String batch) {
        // If Auto and "Batch No" is empty
        if (cb.isChecked()){
            if (!TextUtils.isEmpty(batch)) {
                if (batch.trim().equals("0")) {
                    clearTextValues();
                    showKeyBoard(activity);
                }
            } else tv_barcode.setText("");

            tv_barcode.requestFocus();
        }
    }

    private void clearTextValues() {
        tv_barcode.setText("");
        if (!cb.isChecked())
            qty.setText("");
        batchNo.setText("");
        code.setText("");
        uom.setText("");
        unit.setText("");
        des2.setText("");
        desc1.setText("");
    }

    private void updateTotalCountinDB() {
        String qty_new = qty.getText().toString();
        if (!TextUtils.isEmpty(qty_new))
            totalQtyCount += Integer.parseInt(qty_new);

        ItemModel co = new ItemModel();
        co.setQTY(totalQtyCount + "");
        databaseClass.updateTotalQTYCount(co,time_millies + "");
    }
    private void getSummaryInfo() {
        totalQtyCount       = databaseClass.getTotalQty(time_millies);
        itemsCount          = databaseClass.getDistinctBarcode(time_millies);
        totalBarcodeScanned = databaseClass.getTotalBarcode(time_millies);
        lastBarcodeCount    = databaseClass.getLastBarcodeCode(time_millies);
        Log.e("VALUES_SUM", totalQtyCount + "\n" + itemsCount + "\n" + totalBarcodeScanned + "\n" + lastBarcodeCount);
        // Total Scanned Qty
        total_scanned_quantities.setText(totalQtyCount + "");
        // Total Items Scanned
        total_barcode_scanned.setText(itemsCount + "");
        // Items Qty Scanned
        total_sameBarcode_scanned.setText(totalBarcodeScanned + "");
        // Last Scanned . Qty
        last_barcode_qty.setText(lastBarcodeCount + "");
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
                        tv_barcode.requestFocus();
                        showKeyBoard(activity);
                        dialog.dismiss();
                    }
                })
                .build().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                ItemModel connect = data.getParcelableExtra("barcode_model");
                tv_barcode.setText(connect.getBarcode());

                String batch_num = connect.getBatch();
                if (!TextUtils.isEmpty(batch_num)) {

                    if (!connect.getBatch().trim().equals("0")){
                        batchNo.setText(batch_num);
                        batchNoLayout.setVisibility(View.VISIBLE);
                    } else batchNoLayout.setVisibility(INVISIBLE);

                } else batchNoLayout.setVisibility(INVISIBLE);

                qty.setText(connect.getUnit());

                tv_barcode.requestFocus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {}
        }
    }
}




