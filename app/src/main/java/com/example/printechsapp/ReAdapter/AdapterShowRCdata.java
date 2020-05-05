package com.example.printechsapp.ReAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printechsapp.ReModel.ItemModel;
import com.example.printechsapp.R;

import java.util.ArrayList;

import static com.example.printechsapp.physical_stock_count.PhysicalStockList.allArray;
import static com.example.printechsapp.physical_stock_count.PhysicalStockList.parentLayout;
import static com.example.printechsapp.physical_stock_count.PhysicalStockList.tagNo;
import static com.example.printechsapp.utils.CommonUtils.isArrayListEmpty;


public class AdapterShowRCdata extends RecyclerView.Adapter<AdapterShowRCdata.ProductViewHolder> {
    private ArrayList<ItemModel> arrayList;
    private Context context;
    private boolean isFromReciptData;


    public AdapterShowRCdata(ArrayList<ItemModel> singleArray, Context context, boolean isFromReciptData) {
        this.arrayList = singleArray;
        this.context=context;
        this.isFromReciptData=isFromReciptData;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_show_rcdata,parent,false);
        ProductViewHolder viewHolder = new ProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        final ItemModel model = arrayList.get(position);


        holder.barcode.setText(model.getBarcode());
        holder.qty.setText(model.getQTY() + "");

        holder.desc1.setText(model.getProduct());
        holder.des2.setText(model.getItem_Code());


        holder.brand.setText(model.getBrand());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(holder.getAdapterPosition());

                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

                if (isFromReciptData){
                    /*if (singleArray_Recipt != null)
                        singleArray_Recipt.remove(position);*/
                }else {
                    if (allArray != null){
                        allArray.get(tagNo).remove(position);
                        // Update Ui
                        if (isArrayListEmpty(arrayList))
                        {
                            parentLayout.removeViewAt(tagNo);
                            allArray.remove(tagNo);
                            updateLayoutTagIds();
                        }
                    }
                }
                toastMsg("Deleted Successfully");
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = holder.barcode.getText().toString();
                String brand   = holder.brand.getText().toString();
                String qty     = holder.qty.getText().toString();
                String desc1   = holder.desc1.getText().toString();
                String desc2   = holder.des2.getText().toString();
                editValidation(barcode,brand,qty,desc1,desc2, position, model);
            }
        });
    }


    private void editValidation(String barcode, String brand, String qty, String desc1, String desc2, int position, ItemModel model) {
        boolean isError = false;
        if (TextUtils.isEmpty(barcode)) {
            isError = true; toastMsg("Please enetr a barcode");
        }

        if (TextUtils.isEmpty(brand)){
            isError = true; toastMsg("Please enetr a brand");
        }

        if (TextUtils.isEmpty(qty)){
            isError = true; toastMsg("Please enetr qty");
        }

        if (TextUtils.isEmpty(desc1)){
            isError = true; toastMsg("Please enetr description 1");
        }

        if (TextUtils.isEmpty(desc2)){
            isError = true; toastMsg("Please enetr description 2");
        }

        if (!isError){
            model.setBarcode(barcode);
            model.setBrand(brand);
            model.setQTY(qty);
            notifyDataSetChanged();

            if (isFromReciptData){
                /*if (singleArray_Recipt != null){
                    singleArray_Recipt.get(position).setBarcode(barcode);
                    singleArray_Recipt.get(position).setBrand(brand);
                    singleArray_Recipt.get(position).setQTY(qty);
                    singleArray_Recipt.get(position).setDescription(desc1);
                    singleArray_Recipt.get(position).setDescription2(desc2);
                }*/
            }else {
                if (allArray != null){
                    allArray.get(tagNo).get(position).setBarcode(barcode);
                    allArray.get(tagNo).get(position).setBrand(brand);
                    allArray.get(tagNo).get(position).setQTY(qty);
                    allArray.get(tagNo).get(position).setDescription(desc1);
                    allArray.get(tagNo).get(position).setDescription2(desc2);
                }
            }
            toastMsg("Updated Successfully");
        }
    }

    private void updateLayoutTagIds() {
        if (parentLayout != null){
            if (parentLayout.getChildCount() != 0){
                for (int i=0 ; i<allArray.size() ; i++){
                    parentLayout.getChildAt(i).setTag(i);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isArrayListEmpty(arrayList)) return 0; else return arrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView rn,location,barcode,qty,desc1,des2,brand,uom;
        private ImageView delete,edit;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            barcode=itemView.findViewById(R.id.barcode);
            qty=(TextView)itemView.findViewById(R.id.qty);
            desc1=itemView.findViewById(R.id.desc1);
            des2=(TextView)itemView.findViewById(R.id.des2);
            brand=(TextView)itemView.findViewById(R.id.brand);
            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
        }
    }

    private void toastMsg(String msg){
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }
}
