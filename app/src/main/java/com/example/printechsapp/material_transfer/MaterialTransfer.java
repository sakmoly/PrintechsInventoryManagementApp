package com.example.printechsapp.material_transfer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseQueryMT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;

public class MaterialTransfer extends AppCompatActivity {
    private Activity activity = MaterialTransfer.this;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton next;
    private AdapterMaterialTransfer adapter;
    private DatabaseQueryMT dbMT;
    private Set<String> getAllTimeArray = new LinkedHashSet<>(); // LinkedHashSet
    private ArrayList<ItemModel> getAllArrayList = new ArrayList<>();
    private TextView noData;

    @Override
    protected void onResume() {
        super.onResume();
        getallScannedDataSQLite();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt_list);
        dbMT            = new DatabaseQueryMT(getApplicationContext());
        recyclerView       = findViewById(R.id.recyclerView_mt);
        next               = findViewById(R.id.next_mt);
        toolbar            = findViewById(R.id.toolbar2);
        noData             = findViewById(R.id.no_data_all_mt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialTransferScanning.class);
                startActivity(intent);
            }
        });
    }



    private void getallScannedDataSQLite() {
        getAllTimeArray.clear();
        getAllArrayList.clear();

        getAllTimeArray = dbMT.getAllOrderMT_TimeMillies();
        for (String str : getAllTimeArray) {
            //System.out.println(s);
            getAllArrayList.add(dbMT.getSingleItemOrderMT(str));
        }
        if (isArrayListEmpty(getAllArrayList)){
            if (adapter != null) adapter.notifyDataSetChanged();
            noData.setVisibility(View.VISIBLE);
        }else {
            noData.setVisibility(View.GONE);
            adapter = new AdapterMaterialTransfer(activity, getAllArrayList);
            recyclerView.setAdapter(adapter);
        }
    }


    class AdapterMaterialTransfer extends RecyclerView.Adapter<AdapterMaterialTransfer.ViewHolder>{
        private Context context;
        private ArrayList<ItemModel> getAllArray;
        public AdapterMaterialTransfer(Context context, ArrayList<ItemModel> arrayList){
            this.getAllArray=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public AdapterMaterialTransfer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent ,false);
            return new AdapterMaterialTransfer.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterMaterialTransfer.ViewHolder holder, int position) {
            final ItemModel model = getAllArray.get(position);

            holder.descriptionTxt.setVisibility(View.GONE);
            holder.dateTxt.setText(model.getTotalScannedDate());
            holder.timeTxt.setText(model.getTotalScannedTime());
            //holder.descriptionTxt.setText(model.getDescription2());//model.getCurrentTimeMillies()
            holder.totalQtyTxt.setText(model.getQTY());
            holder.barcode_txt.setText(model.getCurrentTimeMillies());
            Log.w("ID_VALUE_INTENT_1", model.getOrder_Id() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionsDialog(model);
                }
            });
        }

        private void showOptionsDialog(final ItemModel model) {
            String[] colors = {"Material Receipt", "View Items"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.select_an_option));
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(activity, MaterialTransferScanning.class);
                            intent.putExtra("isFromItemList_Order", true);
                            intent.putExtra("timeMillies", model.getCurrentTimeMillies());
                            intent.putExtra("Order_ID",    model.getOrder_Id());
                            intent.putExtra("item_model",  model);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(activity, TransferItemsListActivity.class);
                            intent.putExtra("items_model", model);
                            startActivity(intent);
                            break;
                    }
                }
            });
            builder.show();
        }

        @Override
        public int getItemCount() {
            if (isArrayListEmpty(getAllArray)) return 0; else return getAllArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView descriptionTxt,dateTxt,timeTxt,totalQtyTxt,barcode_txt;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                descriptionTxt = itemView.findViewById(R.id.desc_main);
                dateTxt        = itemView.findViewById(R.id.date_main);
                timeTxt        = itemView.findViewById(R.id.time_main);
                totalQtyTxt    = itemView.findViewById(R.id.total_qty);
                barcode_txt    = itemView.findViewById(R.id.barcode_txt);
            }
        }
    }
}
