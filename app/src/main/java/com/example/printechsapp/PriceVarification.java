package com.example.printechsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseClass;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;

import static com.example.printechsapp.utils.CommonUtils.showKeyBoard;
import static com.example.printechsapp.utils.CommonUtils.toastMsg;

public class PriceVarification extends AppCompatActivity {
    private Activity activity = PriceVarification.this;
    private Toolbar toolbar;
    RelativeLayout layout_newprice;
    public Button Edit,Clear;
    String valuecode,isempty;
    boolean check=true;
    boolean updt=false;
    boolean val=false;
    private ArrayList<ItemModel>alldataarray;
    private ImageView searchBarcode;
    private String lastColumnId;
    EditText barcode;
    TextView  desc1, des2, uom,code,updated_price,unit, current_price;
    DatabaseClass db = new DatabaseClass(PriceVarification.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricevarification);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout_newprice     = findViewById(R.id.layout_new_price);
        Edit                = findViewById(R.id.bt_edit);
        Clear               = findViewById(R.id.clear);
        barcode             = findViewById(R.id.barcode);
        unit                = (TextView) findViewById(R.id.unit);
        desc1               = (TextView) findViewById(R.id.desc1);
        des2                = (TextView) findViewById(R.id.des2);
        uom                 = (TextView) findViewById(R.id.uom);
        code                = (TextView) findViewById(R.id.code);
        unit                = findViewById(R.id.unit);
        current_price       = (TextView)findViewById(R.id.current_price);
        updated_price       = (TextView)findViewById(R.id.updated_price);
        searchBarcode       = findViewById(R.id.et_referenceno);

        alldataarray        = db.getAlldata();

        //search
        searchBarcode.bringToFront();
        searchBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bc = barcode.getText().toString();
                Intent intent = new Intent(activity, SearchBarcodeActivity.class);
                intent.putExtra("search_barcode", bc);
                startActivityForResult(intent, 2);
            }
        });

        //show data
        barCodeKeyPress();


        //Visible Update Price
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isempty=updated_price.getText().toString();
                if (!val) {
                    layout_newprice.setVisibility(View.VISIBLE);
                    Edit.setText("Update");
                    updated_price.requestFocus();
                    val=true;
                }
                else{
                    isempty=updated_price.getText().toString();
                    if (!isempty.isEmpty()){
                        UpdatePrice();
                        Edit.setText("Edit");
                        val=false;
                    }else {
                        Toast.makeText(activity, "Enter Price", Toast.LENGTH_SHORT).show();
                        updated_price.requestFocus();
                    }
                }
            }
        });


        updated_price.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    isempty = updated_price.getText().toString();
                    if (!isempty.isEmpty()) {
                        UpdatePrice();
                    } else {
                        Toast.makeText(activity, "Enter Price", Toast.LENGTH_SHORT).show();
                        updated_price.requestFocus();
                    }
                }
                return false;
            }
        });

        //Clear The Fields
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_newprice.setVisibility(View.GONE);
                clearTextValues();
                Edit.setVisibility(View.GONE);
                barcode.requestFocus();
                Edit.setText("Edit");
                val=false;
            }
        });
    }

    private void UpdatePrice(){
                String pricetxt = updated_price.getText().toString().trim();
                String pricevalue = TextUtils.isEmpty(pricetxt) ? "0" : pricetxt;

                ItemModel co = new ItemModel();
                co.setPrice(pricevalue);
                db.updatePrice(co,lastColumnId);

                barcode.requestFocus();
                clearTextValues();
                layout_newprice.setVisibility(View.GONE);
                Edit.setVisibility(View.GONE);
                callagain();
}

    private void callagain() {
        alldataarray     = db.getAlldata();
    }

    private void barCodeKeyPress() {
        barcode.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                     valuecode = barcode.getText().toString();
                    if (TextUtils.isEmpty(valuecode)) {
                        // BarCode is empty
                        barcode.requestFocus();
                        toastMsg(activity, getString(R.string.plz_enter_a_barcode));
                        clearTextValues();
                    } else {
                        updated_price.setVisibility(View.VISIBLE);
                        updated_price.setFocusableInTouchMode(true);
                        for (int i = 0; i < alldataarray.size(); i++) {
                            if (alldataarray.get(i).getBarcode().equals(valuecode)){
                                Edit.setVisibility(View.VISIBLE);
                                check=true;
                                lastColumnId        = alldataarray.get(i).getColunm_id();
                                String desc_val     = alldataarray.get(i).getDescription();
                                String desc2_val    = alldataarray.get(i).getDescription2();
                                String uom_val      = alldataarray.get(i).getUOM();
                                String unit_val     = alldataarray.get(i).getUnit();
                                String price_val    = alldataarray.get(i).getPrice();
                                String cost_val     = alldataarray.get(i).getCost();

                                uom.setText(uom_val);
                                des2.setText(desc_val);
                                desc1.setText(desc2_val);
                                unit.setText(unit_val);
                                code.setText(cost_val);
                                current_price.setText(price_val);

                                Log.w("aaaaaaa",alldataarray.get(i).getPrice()+"");

                                break;
                            }
                            else check=false;
                        }
                       if(!check) {
                            msgAlert(activity, getString(R.string.not_found),getString(R.string.barcode_not_exists));
                        }
                    }
                }
                return false;
            }

        });

    }
    private void clearTextValues() {
        barcode.setText("");
        updated_price.setText("");
        current_price.setText("");
        code.setText("");
        uom.setText("");
        unit.setText("");
        des2.setText("");
        desc1.setText("");
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
