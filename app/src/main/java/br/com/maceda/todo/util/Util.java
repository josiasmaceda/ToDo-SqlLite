package br.com.maceda.todo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by josias on 03/05/2016.
 */
public class Util {

    private static final String TAG = "UTIL";

    public static String getDateTime(long dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(dateTime);
        return dateFormat.format(date);
    }


}
