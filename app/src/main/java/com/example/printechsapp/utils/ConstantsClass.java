package com.example.printechsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantsClass {
    public static final String PREFERENCES_NAME = "Registration";
    public static final String LANGUAGE = "language";
    public static final String IS_LOADED = "location_place";
    public static final String ORDER_ENABLED = "order_enabled";
    public static final String ACCEPT_MORE_ORDER_QTY = "accept_more_order_qtys";
    public static final String TOKEN = "Token";
    public static final String USER_ID = "user_id";
    public static final String PHONE_NO = "user_phone";
    public static final String USER_FULL_NAME = "user_fullname";
    public static final String USER_EMAIL = "user_email";
    public static final String TOTAL_ORDER_QTY = "total_order_qtys";
    // Permission check
    public static final String USER_PERMISSION = "permission_type";
    public static final String ADMIN_TXT = "admin";
    public static final String CUSTOMER_TXT = "customer";
    //Admin
    public static final String BUSINESS_ID = "business_id";



    public String date_format_yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
}
