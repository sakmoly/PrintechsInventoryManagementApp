package com.example.printechsapp.material_transfer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseQueryMT;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;

import static com.example.printechsapp.utils.CommonUtils.commonAlert;
import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;
import static com.example.printechsapp.utils.CommonUtils.showKeyBoard;
import static com.example.printechsapp.utils.CommonUtils.toastMsg;

public class TransferItemsListActivity extends AppCompatActivity {
    private Activity activity = TransferItemsListActivity.this;
    private RecyclerView recycler;
    private DatabaseQueryMT dbMT;
    private TextView noData;
    private String[] locationList = {"Select All","LOC1", "LOC2", "LOC3", "LOC4", "LOC5"};
    private String locationValue = "0";
    private Spinner locSpinner;
    private ArrayList<ItemModel> itemsArray = new ArrayList<>();
    private String timeMillies,tag, orderID;
    private Toolbar toolbar;
    private AdapterMT adapter = new AdapterMT();
    private AdapterMT adapterItems;
    private Dialog myDialog;
    private String edit_barcode, edit_uom,edit_qty,edit_desc1,edit_desc2, edit_batchNo;
    private int totalQtyCount = 0;
    private EditText searchEdit;
    private LinearLayout search_layout;
    private ItemModel itemModel;
    private int itemMaxQTY = 0 , totalScannedItemQTY = 0;
    private boolean isUpdated = false;
    private EditText qty_dialogEdt;

    private TextWatcher searchTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (adapterItems != null) adapterItems.getFilter().filter(s);
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_items_list);
        dbMT = new DatabaseQueryMT(getApplicationContext());
        if (getIntent().getExtras() != null){
            //singleArray = getIntent().getExtras().getParcelableArrayList("key");
            itemModel       = getIntent().getParcelableExtra("items_model");
            timeMillies     = itemModel.getCurrentTimeMillies();
            orderID         = itemModel.getOrder_Id();
        }
        toolbar       = findViewById(R.id.toolbar2);
        recycler      = findViewById(R.id.recycler_mt);
        noData        = findViewById(R.id.no_data_item_mt);
        search_layout = findViewById(R.id.search_layout);
        locSpinner    = findViewById(R.id.location_spinner);
        searchEdit    = findViewById(R.id.search_material_transfer);
        searchEdit.addTextChangedListener(searchTextChangeListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Select Location Spinner
        ArrayAdapter<String> aa = new ArrayAdapter<>(activity, R.layout.spinner_list, locationList);
        aa.setDropDownViewResource(R.layout.spinner_list);
        locSpinner.setAdapter(aa);
        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                if(position > 0){
                    int index = arg0.getSelectedItemPosition();
                    ((TextView) arg0.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
                    ((TextView) arg0.getSelectedView()).setTextSize(16);
                    locationValue = locationList[position];
                }else if (position == 0){
                    ((TextView) arg0.getSelectedView()).setTextColor(Color.GRAY);
                    ((TextView) arg0.getSelectedView()).setTextSize(16);
                    locationValue = "0";
                }
                getSQLiteData(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });


        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        getSQLiteData(false);
    }



    private void getSQLiteData(boolean isDataUpdated) {
        if (!TextUtils.isEmpty(timeMillies)){
            itemsArray = dbMT.getMaterialTransferItems(timeMillies, locationValue);
            adapterItems = new AdapterMT(itemsArray, activity);
            recycler.setAdapter(adapterItems);

            if (isArrayListEmpty(itemsArray)) {
                noData.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
            } else {
                noData.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
            }
        }else noData.setVisibility(View.VISIBLE);
        // Get total item count
        totalQtyCount = dbMT.getTotalQtyMT(timeMillies);
        if (isDataUpdated)
            updateTotalCountinDB(timeMillies);
    }


    class AdapterMT extends RecyclerView.Adapter<AdapterMT.ViewHolder> implements Filterable {
        public ArrayList<ItemModel> arrayList = new ArrayList<>();
        public ArrayList<ItemModel> listTemp;
        private Context context;
        public AdapterMT(ArrayList<ItemModel> arrayList, Context context) {
            this.arrayList = arrayList;
            this.listTemp = arrayList;
            this.context = context;
        }
        public AdapterMT() {
        }
        @NonNull
        @Override
        public AdapterMT.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterMT.ViewHolder holder, final int position) {
            final ItemModel model = arrayList.get(position);

            holder.layout_qty.setVisibility(View.GONE);
            holder.dateTxt.setText(model.getTotalScannedDate());
            holder.timeTxt.setText(model.getTotalScannedTime());
            holder.descriptionTxt.setText(model.getDescription2());// model.getCurrentTimeMillies()
            holder.totalQtyTxt.setText(model.getQTY());
            holder.barcode_txt.setText(model.getBarcode());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(orderID)){
                        itemMaxQTY          = dbMT.itemMaxQTY_MT(model.getBarcode(), orderID);
                        totalScannedItemQTY = dbMT.scannedItemMaxQTY_MT(model.getBarcode(), orderID, timeMillies);
                    }
                    viewFullData(model, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (isArrayListEmpty(arrayList)) return 0; else return arrayList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        arrayList = listTemp;
                    } else {
                        ArrayList<ItemModel> filteredList = new ArrayList<>();
                        for (ItemModel row : listTemp) {
                            if (row.getBarcode().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }else if (row.getDescription2().toLowerCase().contains(charString.toLowerCase())){
                                filteredList.add(row);
                            }else if (row.getItem_Code().toLowerCase().contains(charString.toLowerCase())){
                                filteredList.add(row);
                            }
                        }
                        arrayList = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = arrayList;
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    arrayList = (ArrayList<ItemModel>) filterResults.values;
                    if (isArrayListEmpty(arrayList))
                        noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView descriptionTxt,dateTxt,timeTxt,totalQtyTxt,barcode_txt;
            private RelativeLayout parentView;
            private LinearLayout layout_qty;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                descriptionTxt = itemView.findViewById(R.id.desc_main);
                dateTxt        = itemView.findViewById(R.id.date_main);
                timeTxt        = itemView.findViewById(R.id.time_main);
                totalQtyTxt    = itemView.findViewById(R.id.total_qty);
                parentView     = itemView.findViewById(R.id.parent_item_view);
                layout_qty     = itemView.findViewById(R.id.layout_qty);
                barcode_txt    = itemView.findViewById(R.id.barcode_txt);
            }
        }
    }



    private void viewFullData(final ItemModel model, final int position) {
        myDialog = new Dialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.single_item_layout);
        final LinearLayout viewLayout      = myDialog.findViewById(R.id.view_layout);
        final LinearLayout editLayout      = myDialog.findViewById(R.id.edit_layout);
        // View Item Design
        Button editButton  = myDialog.findViewById(R.id.edit_item);
        Button cancelButton    = myDialog.findViewById(R.id.cancel_edit);
        final TextView barcode_v     = myDialog.findViewById(R.id.barcode_view);
        final TextView qty_v         = myDialog.findViewById(R.id.qty_view);
        final TextView desc1_v       = myDialog.findViewById(R.id.desc1_view);
        final TextView des2_v        = myDialog.findViewById(R.id.des2_view);
        final TextView uom_v         = myDialog.findViewById(R.id.uom_view);
        final TextView batchNo_v     = myDialog.findViewById(R.id.batchNo_view);
        // Edit Item Design
        Button deleteButton  = myDialog.findViewById(R.id.delete_item);
        Button saveButton    = myDialog.findViewById(R.id.save_change);
        final TextView barcode     = myDialog.findViewById(R.id.barcode);
        qty_dialogEdt              = myDialog.findViewById(R.id.qty);
        final EditText desc1       = myDialog.findViewById(R.id.desc1);
        final EditText des2        = myDialog.findViewById(R.id.des2);
        final EditText uom         = myDialog.findViewById(R.id.uom);
        final EditText batchNo     = myDialog.findViewById(R.id.batchNo);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.VISIBLE);
                viewLayout.setVisibility(View.GONE);

                qty_dialogEdt.requestFocus();
                qty_dialogEdt.selectAll();
                showKeyBoard(activity);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.GONE);
                viewLayout.setVisibility(View.VISIBLE);
                showKeyBoard(activity);
            }
        });
        // View Item
        barcode_v.setText(model.getBarcode());
        qty_v.setText(model.getUnit());
        desc1_v.setText(model.getDescription());
        des2_v.setText(model.getDescription2());
        uom_v.setText(model.getUOM());
        batchNo_v.setText(model.getBatch());
        // Edit Item
        barcode.setText(model.getBarcode());
        qty_dialogEdt.setText(model.getUnit());
        desc1.setText(model.getDescription());
        des2.setText(model.getDescription2());
        uom.setText(model.getUOM());
        batchNo.setText(model.getBatch());
        myDialog.findViewById(R.id.close_item_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlert(Integer.parseInt(model.getColunm_id()), position);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_barcode = barcode.getText().toString();
                edit_uom     = uom.getText().toString();
                edit_qty     = qty_dialogEdt.getText().toString();
                edit_desc1   = desc1.getText().toString();
                edit_desc2   = des2.getText().toString();
                edit_batchNo = batchNo.getText().toString();
                editValidation(model.getColunm_id(), model.getUnit());
                showKeyBoard(activity);
            }
        });
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    private void editValidation(String column_id, String previousUnit) {
        boolean isError = false;
        if (TextUtils.isEmpty(edit_barcode)) {
            isError = true; toastMsg(activity,"Please enter a barcode");
        }

        if (TextUtils.isEmpty(edit_uom)){
            isError = true; toastMsg(activity,"Please enter a UOM");
        }

        if (TextUtils.isEmpty(edit_qty)){
            isError = true; toastMsg(activity,"Please enter qty");
        }else {
            if (edit_qty.equals("0")){
                isError = true; toastMsg(activity,"Please enter qty");
            }
        }

        if (TextUtils.isEmpty(edit_batchNo)){
            isError = true; toastMsg(activity,"Please enter a Batch Number");
        }

        if (TextUtils.isEmpty(edit_desc1)){
            isError = true; toastMsg(activity,"Please enter description 1");
        }

        if (TextUtils.isEmpty(edit_desc2)){
            isError = true; toastMsg(activity,"Please enter description 2");
        }

        if (!isError){
            // Update Item Values in db
            ItemModel co = new ItemModel();
            co.setBarcode(edit_barcode);
            co.setUnit(edit_qty);
            co.setUOM(edit_uom);
            co.setBatch(edit_batchNo);
            co.setDescription(edit_desc1);
            co.setDescription2(edit_desc2);
            if (itemMaxQTY != 0){
                Log.w("CHECK", "Total : " + itemMaxQTY + "\n" + "Scanned : " + totalScannedItemQTY);
                totalScannedItemQTY += Integer.parseInt(edit_qty);
                Log.w("QTY",  edit_qty + " - " + "New Scanned qty :" + totalScannedItemQTY);
                if (totalScannedItemQTY <=  itemMaxQTY){
                    dbMT.updateMaterialTransferItem(co, column_id);
                    toastMsg(activity,"Updated Successfully");
                } else {
                    commonAlert(activity, getString(R.string.alert) , "Item count exceeds the maximum value " + itemMaxQTY);
                    qty_dialogEdt.setText(previousUnit);
                    qty_dialogEdt.selectAll();
                }
            }else {
                dbMT.updateMaterialTransferItem(co, column_id);
                toastMsg(activity,"Updated Successfully");
            }
            // refresh data
            getSQLiteData(true);
        }
    }


    private void deleteAlert(final int id, final int position){
        new iOSDialogBuilder(activity)
                .setTitle(getString(R.string.remove))
                .setSubtitle(getString(R.string.remove_item_msg))
                //.setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(getString(R.string.ok), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dbMT.deleteMaterialTransferItem(id);
                        // refresh data
                        getSQLiteData(true);
                        //updateView(position);

                        dialog.dismiss();
                        myDialog.dismiss();
                        toastMsg(activity,getString(R.string.item_deleted_successfully));
                    }
                })
                .setNegativeListener(getString(R.string.cancel),new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    private void updateTotalCountinDB(String timeMillies) {
        isUpdated = true;
        ItemModel co = new ItemModel();
        co.setQTY(totalQtyCount + "");
        dbMT.updateTotalQTYCountMT(co,timeMillies + "");
    }
}
