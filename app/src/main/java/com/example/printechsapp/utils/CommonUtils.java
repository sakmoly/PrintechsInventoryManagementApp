package com.example.printechsapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.printechsapp.MyApplication;
import com.example.printechsapp.R;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;

public class CommonUtils {
    public static boolean isArrayListEmpty(ArrayList<?> arrayList){
        boolean returnValue;
        if (arrayList != null){
            try {
                if (arrayList.size() != 0){
                    returnValue = false;
                }else returnValue = true;
            }catch (Exception e){
                e.printStackTrace();
                returnValue = true;
            }
        }else returnValue = true;
        return returnValue;
    }

    public static void commonAlert(Context context, String title, String message) {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(message)
                //.setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    public static void toastMsg(Context context, String msg){
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

    // https://stackoverflow.com/questions/34306202/android-show-keyboard-programmatically
    public static void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager)   context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void customToast(String message) {
        android.widget.Toast toast = new android.widget.Toast(MyApplication.getContext());
        LayoutInflater inflater = LayoutInflater.from(MyApplication.getContext());
        View view =  inflater.inflate(R.layout.layout_toast, null);
        TextView tvMessage = view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);

        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0,40);
        toast.setView(view);
        toast.show();
    }
}
