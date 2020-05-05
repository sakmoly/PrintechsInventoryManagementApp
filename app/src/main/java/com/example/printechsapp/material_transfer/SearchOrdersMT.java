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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.printechsapp.R;
import com.example.printechsapp.ReModel.OrderModel;
import com.example.printechsapp.database.DatabaseQueryMT;
import com.example.printechsapp.material_receipt.OrderItemsMR;

import java.util.ArrayList;

import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;

public class SearchOrdersMT extends AppCompatActivity {
    private Activity activity = SearchOrdersMT.this;
    private Toolbar toolbar;
    private EditText searchEdt;
    private TextView noData;
    private RecyclerView recyclerView;
    private ArrayList<OrderModel> orderArray;
    private AdapterOrderMT adapter;
    private String getOrder;
    private DatabaseQueryMT dbMT;
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
        setContentView(R.layout.activity_search_orders_mt);
        dbMT       = new DatabaseQueryMT(getApplicationContext());
        if (getIntent().getExtras() != null){
            getOrder = getIntent().getExtras().getString("search_order");
        }
        toolbar       = findViewById(R.id.toolbar_search_order);
        searchEdt     = findViewById(R.id.search_orderNumber);
        noData        = findViewById(R.id.no_data_order);
        recyclerView  = findViewById(R.id.recyclerView_order);
        searchEdt.addTextChangedListener(searchTextChangeListener);
        searchEdt.setText(getOrder);
        searchEdt.selectAll();

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


    private void getSQLiteData(){
        orderArray = dbMT.getAllOrdersMT();
        adapter = new AdapterOrderMT(orderArray, activity);
        recyclerView.setAdapter(adapter);

        if (adapter != null) adapter.getFilter().filter(getOrder);

        if (isArrayListEmpty(orderArray))
            noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
    }

    class AdapterOrderMT extends RecyclerView.Adapter<AdapterOrderMT.ViewHolder> implements Filterable {
        private ArrayList<OrderModel> arrayItems;
        private ArrayList<OrderModel> listTemp;
        private Context context;
        public AdapterOrderMT(ArrayList<OrderModel> arrayItems, Context context) {
            this.arrayItems = arrayItems;
            this.listTemp = arrayItems;
            this.context = context;
        }
        @NonNull
        @Override
        public AdapterOrderMT.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
            return new AdapterOrderMT.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterOrderMT.ViewHolder holder, final int position) {
            final OrderModel model = arrayItems.get(position);
            holder.order_id.setText("Order ID : " + model.getOrder_id());
            holder.supplier_name.setText(model.getSupplier_Name());
            holder.totalOdrerQty.setText(model.getOrder_Qty());
            holder.date.setText(model.getOrder_date());
            holder.time.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("order_num", model.getOrder_id());
                    returnIntent.putExtra("orderTotalQTY", model.getOrder_Qty());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
            holder.arrowOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderItemsMR.class);
                    intent.putExtra("order_id", model.getOrder_id());
                    startActivity(intent);
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
                        ArrayList<OrderModel> filteredList = new ArrayList<>();
                        for (OrderModel row : listTemp) {
                            if (row.getOrder_id().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }else if (row.getSupplier_Name().toLowerCase().contains(charString.toLowerCase())){
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
                    arrayItems = (ArrayList<OrderModel>) filterResults.values;
                    if (isArrayListEmpty(arrayItems))
                        noData.setVisibility(View.VISIBLE); else noData.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView order_id,supplier_name, totalOdrerQty, date, time;
            private ImageButton arrowOrder;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                order_id = itemView.findViewById(R.id.order_id);
                supplier_name   = itemView.findViewById(R.id.supplier_name);
                totalOdrerQty = itemView.findViewById(R.id.total_item_qty);
                arrowOrder  = itemView.findViewById(R.id.arrowOrder);
                date = itemView.findViewById(R.id.date_main);
                time = itemView.findViewById(R.id.time_main);
            }
        }
    }
}
