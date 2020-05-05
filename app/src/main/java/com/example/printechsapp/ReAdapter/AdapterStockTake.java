//package com.example.printechsapp.ReAdapter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.printechsapp.ReModel.Connect;
//import com.example.printechsapp.R;
//import com.example.printechsapp.ReModel.Model_StockTake;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdapterStockTake extends RecyclerView.Adapter<AdapterStockTake.ProductViewHolder> {
//    private List<Model_StockTake> stockTakes;
//
//    Context context;
//
//
//    public AdapterStockTake(List<Model_StockTake> stockTakes,Context context) {
//        this.stockTakes = stockTakes;
//        this.context=context;
//    }
//
//    @NonNull
//    @Override
//    public AdapterStockTake.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_stock_take,parent,false);
//        ProductViewHolder viewHolder=new ProductViewHolder(view);
//        return viewHolder;     }
//
//    @Override
//    public void onBindViewHolder(@NonNull AdapterStockTake.ProductViewHolder holder, int position) {
//        Model_StockTake stockTake = stockTakes.get(position);
//        Log.w("list",stockTake.getcYear()+"");
//
//
//
//        holder.month.setText(stockTake.getcMonth()+"");
//        holder.qty.setText(stockTake.getQTY()+"");
//
//        holder.munite.setText(stockTake.getcMinute()+"");
//        holder.year.setText(stockTake.getcYear()+"");
//
//        holder.date.setText(stockTake.getcDay()+"");
//        holder.hour.setText(stockTake.getcHour()+"");
//
//
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return stockTakes.size();
//    }
//
//
//
//
//    public class ProductViewHolder extends RecyclerView.ViewHolder {
//        TextView date,month,year,hour,munite,qty;
//        ListView barcode;
//        public ProductViewHolder(@NonNull View itemView) {
//            super(itemView);
//            date=itemView.findViewById(R.id.date);
//            month=itemView.findViewById(R.id.month);
//            year=itemView.findViewById(R.id.year);
//            hour=itemView.findViewById(R.id.hour);
//            munite=itemView.findViewById(R.id.munite);
//            qty=itemView.findViewById(R.id.qty);
//
//
//
//        }
//    }
//}
