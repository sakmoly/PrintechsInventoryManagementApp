package com.example.printechsapp.physical_stock_count;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.printechsapp.database.DatabaseClass;
import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;

public class PhysicalStockList extends AppCompatActivity {
    private Activity activity = PhysicalStockList.this;
    private String bc;
    private ConstraintLayout lay_show;
    private TextView noData;
    private static final int REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private int scannedArraySize;
    public static ViewGroup parentLayout;
    private int tagCount = 0;
    public static int tagNo;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DatabaseClass databaseClass;
    private Set<String> getAllTimeArray = new LinkedHashSet<>();// LinkedHashSet
    private ArrayList<ItemModel> getAllArrayList = new ArrayList<>();
    private AdapterAllData adapter;
    private EditText searchEdit;

    public static ArrayList<ItemModel> singleArray = new ArrayList<>();
    public static ArrayList<ArrayList<ItemModel>> allArray = new ArrayList<>();
    //ArrayList<Model_StockTake> rcxlist = new ArrayList<>();

    private TextWatcher searchTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (adapter != null) adapter.getFilter().filter(s);
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //tagCount = 0;
        getallScannedDataSQLite();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        databaseClass = new DatabaseClass(getApplicationContext());
        //getAllSQLite();
        toolbar       = findViewById(R.id.toolbar2);
        parentLayout  = (ViewGroup) findViewById(R.id.linear);
        noData        = findViewById(R.id.no_data_all);
        fab           = findViewById(R.id.next);
        recyclerView  = findViewById(R.id.recyclerView_getall);
        searchEdit    = findViewById(R.id.search_main_item);
        searchEdit.addTextChangedListener(searchTextChangeListener);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(PhysicalStockList.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhysicalStockScanning.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
    }


    private void getallScannedDataSQLite() {
        getAllTimeArray.clear();
        getAllArrayList.clear();

        getAllTimeArray = databaseClass.getAllScannedTimeMillies(); // getAllScannedItems();
        for (String str : getAllTimeArray) {
            //System.out.println(s);
            getAllArrayList.add(databaseClass.getSingleRaw(str));
        }
        if (isArrayListEmpty(getAllArrayList)){
            if (adapter != null) adapter.notifyDataSetChanged();
            noData.setVisibility(View.VISIBLE);
        }else {
            noData.setVisibility(View.GONE);
            adapter = new AdapterAllData(getAllArrayList, activity);
            recyclerView.setAdapter(adapter);
        }
    }


    class AdapterAllData extends RecyclerView.Adapter<AdapterAllData.ViewHolder> implements Filterable {
        private ArrayList<ItemModel> getAllArray;
        private ArrayList<ItemModel> listTemp;
        private Context context;
        private String checkDuplicateValue;
        public AdapterAllData(ArrayList<ItemModel> getAllArray, Context context) {
            this.getAllArray = getAllArray;
            this.listTemp = getAllArray;
            this.context = context;
        }
        @NonNull
        @Override
        public AdapterAllData.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterAllData.ViewHolder holder, int position) {
            final ItemModel model = getAllArray.get(position);

            holder.descriptionTxt.setVisibility(View.GONE);
            holder.dateTxt.setText(model.getTotalScannedDate());
            holder.timeTxt.setText(model.getTotalScannedTime());
            //holder.descriptionTxt.setText(model.getDescription2());//model.getCurrentTimeMillies()
            holder.totalQtyTxt.setText(model.getQTY());
            holder.barcode_txt.setText(model.getCurrentTimeMillies());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionsDialog(model);
                }
            });
        }

        private void showOptionsDialog(final ItemModel model) {
            String[] colors = {"Scan Barcode", "View Items"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.select_an_option));
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(PhysicalStockList.this, PhysicalStockScanning.class);
                            intent.putExtra("isFromItemList", true);
                            intent.putExtra("timeMillies", model.getCurrentTimeMillies());
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(PhysicalStockList.this, Show_ReciptData.class);
                            intent.putExtra("timeMillies", model.getCurrentTimeMillies());
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

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        getAllArray = listTemp;
                    } else {
                        ArrayList<ItemModel> filteredList = new ArrayList<>();
                        for (ItemModel row : listTemp) {
                            if (row.getBarcode().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        getAllArray = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = getAllArray;
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    getAllArray = (ArrayList<ItemModel>) filterResults.values;
                    if (isArrayListEmpty(getAllArray))
                        noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView descriptionTxt,dateTxt,timeTxt,totalQtyTxt,barcode_txt;
            private RelativeLayout parentView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                descriptionTxt = itemView.findViewById(R.id.desc_main);
                dateTxt        = itemView.findViewById(R.id.date_main);
                timeTxt        = itemView.findViewById(R.id.time_main);
                totalQtyTxt    = itemView.findViewById(R.id.total_qty);
                parentView     = itemView.findViewById(R.id.parent_item_view);
                barcode_txt    = itemView.findViewById(R.id.barcode_txt);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK)
            {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addLayout() {
        final View layout2 = LayoutInflater.from(this).inflate(R.layout.single_item_layout, parentLayout, false);
        TextView descriptionTxt = layout2.findViewById(R.id.desc_main);
        TextView dateTxt        = layout2.findViewById(R.id.date_main);
        TextView timeTxt        = layout2.findViewById(R.id.time_main);
        TextView parentView     = layout2.findViewById(R.id.parent_item_view);


        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagNo = (Integer) layout2.getTag();
                Intent intent=new Intent(PhysicalStockList.this, Show_ReciptData.class);
                    intent.putExtra("key",allArray.get(tagNo));
                startActivity(intent);
            }
        });

        layout2.setTag(tagCount);
        parentLayout.addView(layout2);
    }
}
