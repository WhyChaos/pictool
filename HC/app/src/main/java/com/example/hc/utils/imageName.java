package com.example.hc.utils;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class imageName {
    public static String get_imageName(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String imageName = simpleDateFormat.format(date);
        return imageName;
    }
}
