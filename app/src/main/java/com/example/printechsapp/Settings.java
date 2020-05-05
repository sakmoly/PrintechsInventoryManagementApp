package com.example.printechsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import static com.example.printechsapp.utils.ConstantsClass.ACCEPT_MORE_ORDER_QTY;
import static com.example.printechsapp.utils.ConstantsClass.ORDER_ENABLED;
import static com.example.printechsapp.utils.ConstantsClass.PREFERENCES_NAME;
import static com.example.printechsapp.utils.ConstantsClass.TOTAL_ORDER_QTY;

public class Settings extends AppCompatActivity {
    private Toolbar toolbar;
    private Switch orderSwitch,moreQtySwitch;
    private EditText enterQty;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getBoolean(ORDER_ENABLED, false)) orderSwitch.setChecked(true);
        else orderSwitch.setChecked(false);

        if (pref.getBoolean(ACCEPT_MORE_ORDER_QTY, false)) moreQtySwitch.setChecked(true);
        else moreQtySwitch.setChecked(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // SharedPreferences
        pref   = getSharedPreferences(PREFERENCES_NAME,0);
        editor = pref.edit();
        orderSwitch    = findViewById(R.id.switch_order);
        moreQtySwitch  = findViewById(R.id.switch_more_qty);
        enterQty       = findViewById(R.id.order_qty_editText);
        toolbar        = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(ORDER_ENABLED, true).apply();
                } else editor.putBoolean(ORDER_ENABLED, false).apply();
            }
        });
        moreQtySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(ACCEPT_MORE_ORDER_QTY, true).apply();
                } else editor.putBoolean(ACCEPT_MORE_ORDER_QTY, false).apply();
            }
        });
        enterQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(TOTAL_ORDER_QTY, s + "").apply();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
