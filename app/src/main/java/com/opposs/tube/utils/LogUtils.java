package com.opposs.tube.utils;

import android.util.Log;

/**
 * Created by xcl on 16/2/24.
 *
 * 日志输出类
 */
public class LogUtils {


    public static final String TAG = "LogUtils";

    private static boolean DEBUG = true;

    public static void i (String tag,String msg){

        if(DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void i (String msg){

        if(DEBUG) {
            Log.i(TAG, msg);
        }

    }


}
