package com.example.printechsapp.material_transfer;

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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.database.DatabaseQueryMT;

import java.util.ArrayList;

import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;

public class OrderItemsListMT extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String order_id, barcode_extra;
    private TextView noData;
    private DatabaseQueryMT dbMT;
    //private DatabaseClass db;
    private ArrayList<ItemModel> itemsList;
    private AdapterOrderItemsMT adapter;
    private LinearLayout searchLayout;
    private boolean isSearchBarcode;
    private EditText searchEdt;
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
        setContentView(R.layout.activity_order_items_mt);
        dbMT =  new DatabaseQueryMT(getApplicationContext());
        //db      = new DatabaseClass(getApplicationContext());
        if (getIntent().getExtras() != null){
            order_id        = getIntent().getExtras().getString("order_id");
            itemsList       = dbMT.getSingleOrderItemsMT(order_id);
            // search bar code Function
            barcode_extra   = getIntent().getExtras().getString("search_barcode");
            isSearchBarcode = getIntent().getExtras().getBoolean("is_search_barcode", false);
        }
        recyclerView = findViewById(R.id.recycler_orderItem);
        noData       = findViewById(R.id.no_data_orderItem);
        searchLayout = findViewById(R.id.search_layout_orderItem);
        searchEdt    = findViewById(R.id.search_bar_order_item);
        toolbar      = findViewById(R.id.toolbar_orderitems);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchEdt.addTextChangedListener(searchTextChangeListener);
        searchEdt.setText(barcode_extra);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getItems();
    }



    private void getItems() {
        adapter = new AdapterOrderItemsMT(itemsList, OrderItemsListMT.this);
        recyclerView.setAdapter(adapter);
        if (isArrayListEmpty(itemsList)) {
            noData.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
        }
    }


    class AdapterOrderItemsMT extends RecyclerView.Adapter<AdapterOrderItemsMT.ViewHolder> implements Filterable {
        private ArrayList<ItemModel> arrayList;
        private ArrayList<ItemModel> listTemp;
        private Context context;

        public AdapterOrderItemsMT(ArrayList<ItemModel> arrayList, Context context) {
            this.arrayList = arrayList;
            this.listTemp = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public AdapterOrderItemsMT.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
            return new AdapterOrderItemsMT.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterOrderItemsMT.ViewHolder holder, int position) {
            final ItemModel item = arrayList.get(position);
            // If Order id is empty
            if (TextUtils.isEmpty(order_id)) holder.right_layout.setVisibility(View.GONE);
            holder.order_id.setText(item.getBarcode());
            holder.supplier_name.setText(item.getDescription2());
            holder.totalItemQty.setText(item.getUnit());
            holder.left_layout.setVisibility(View.GONE);
            holder.arrowOrder.setVisibility(View.GONE);
            holder.right_view.setVisibility(View.VISIBLE);
            if (isSearchBarcode){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("barcode_model" , item);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
            }
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
            private TextView order_id,supplier_name, totalItemQty;
            private ImageButton arrowOrder;
            private LinearLayout left_layout, layout_qty, right_layout;
            private View right_view;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                order_id       = itemView.findViewById(R.id.order_id);
                supplier_name  = itemView.findViewById(R.id.supplier_name);
                totalItemQty   = itemView.findViewById(R.id.total_item_qty);
                arrowOrder     = itemView.findViewById(R.id.arrowOrder);
                left_layout    = itemView.findViewById(R.id.left_layout);
                layout_qty     = itemView.findViewById(R.id.layout_qty);
                right_view     = itemView.findViewById(R.id.right_view);
                right_layout   = itemView.findViewById(R.id.right_layout);
            }
        }
    }
}
