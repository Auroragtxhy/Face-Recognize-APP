package com.gt.facerecognition.utils;

public class Constants {

    //base url
    public static final String BASE_URL = "http://192.168.0.102:8911/face/";

    public static final String SERVER_RETURNS_USER_INFO = "server_return_user_info";
    public static final String SERVER_RETURN_ACCOUNT = "server_return_account";
    public static final String SERVER_RETURN_NAME = "server_return_name";
    public static final String SERVER_RETURN_GENDER = "server_return_gender";

    public static final String LOG_LOGIN = "DetermineLoginInterface";
    public static final String HAS_LOGGED = "hasLogged";
    public static final String USER_ACCOUNT = "user_account";
    public static final String USER_PASSWORD = "user_password";


    //查看具体条目新开Activity时传递的数据的Key
    public static final String ALL_USER_NAME = "all_user_name";
    public static final String ALL_USER_TIME = "all_user_time";
    public static final String ALL_USER_RESULT = "all_user_result";
    public static final String ALL_USER_IMAGE_URL = "all_user_image_url";

    //蓝牙服务有关的全局定义
    public final static String SERVICE_UUID =  "0000ffe0-0000-1000-8000-00805f9b34fb";
    public final static String CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public final static String UUID_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";

    public final static int BLUETOOTH_CONNECTED = 1;
    public final static int BLUETOOTH_DISCONNECTED = 2;

    //获取权限
    public static final int REQUEST_PERMISSION_CODE = 10010;
}
