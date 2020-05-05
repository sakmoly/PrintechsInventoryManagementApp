package com.example.printechsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseClass;

import java.util.ArrayList;

import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;

public class SearchBarcodeActivity extends AppCompatActivity {
    private Activity activity = SearchBarcodeActivity.this;
    private Toolbar toolbar;
    private DatabaseClass db;
    private EditText searchEdt;
    private TextView noData;
    private RecyclerView recyclerView;
    private ArrayList<ItemModel> itemsArray = new ArrayList<>();
    private AdapterBarcode adapter;
    private String getBarcode;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new DatabaseClass(getApplicationContext());
        if (getIntent().getExtras() != null){
            getBarcode = getIntent().getExtras().getString("search_barcode");
        }
        toolbar       = findViewById(R.id.toolbar_search);
        searchEdt     = findViewById(R.id.et_referenceno);
        noData        = findViewById(R.id.no_data_barcode);
        recyclerView  = findViewById(R.id.recyclerView_barcode);
        searchEdt.addTextChangedListener(searchTextChangeListener);
        searchEdt.setText(getBarcode);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getSQLiteData();
    }


    private void getSQLiteData() {
        itemsArray = db.getAlldata();
        adapter = new AdapterBarcode(itemsArray, activity);
        recyclerView.setAdapter(adapter);

        if (adapter != null) adapter.getFilter().filter(getBarcode);

        if (isArrayListEmpty(itemsArray))
            noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
    }


    class AdapterBarcode extends RecyclerView.Adapter<AdapterBarcode.ViewHolder> implements Filterable {
        private ArrayList<ItemModel> arrayItems;
        private ArrayList<ItemModel> listTemp;
        private Context context;
        public AdapterBarcode(ArrayList<ItemModel> arrayItems, Context context) {
            this.arrayItems = arrayItems;
            this.listTemp = arrayItems;
            this.context = context;
        }
        @NonNull
        @Override
        public AdapterBarcode.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterBarcode.ViewHolder holder, int position) {
            final ItemModel model = arrayItems.get(position);
            holder.barcodeTxt.setText(model.getBarcode());
            holder.desc2Txt.setText(model.getDescription2());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("barcode_model", model);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (isArrayListEmpty(arrayItems)) return 0; else return arrayItems.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        arrayItems = listTemp;
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
                        arrayItems = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = arrayItems;
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    arrayItems = (ArrayList<ItemModel>) filterResults.values;
                    if (isArrayListEmpty(arrayItems))
                        noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView barcodeTxt,desc2Txt;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                barcodeTxt = itemView.findViewById(R.id.barcode_txt);
                desc2Txt   = itemView.findViewById(R.id.desc_main);
            }
        }
    }
}
