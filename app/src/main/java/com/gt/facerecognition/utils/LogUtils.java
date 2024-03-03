package com.gt.facerecognition.utils;

import android.util.Log;

public class LogUtils {
    private static int currentLev = 4;       //编写代码时该值为4，上线软件时将该值设置为0，即可不输出任何信息
    private static final int DEBUG_LEV = 4;  //注意命名规则，final使用大写加下换线分割
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Object object, String msg) {
        if (currentLev >=  DEBUG_LEV) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
    public static void i(Object object, String msg) {
        if (currentLev >=  INFO_LEV) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
    public static void w(Object object, String msg) {
        if (currentLev >=  WARNING_LEV) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
    public static void e(Object object, String msg) {
        if (currentLev >=  ERROR_LEV) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
}
