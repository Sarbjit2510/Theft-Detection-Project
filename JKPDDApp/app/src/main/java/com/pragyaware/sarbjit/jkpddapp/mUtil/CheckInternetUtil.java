package com.pragyaware.sarbjit.jkpddapp.mUtil;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by C9 on 09/07/16.
 */
public class CheckInternetUtil {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            if (connectivity.getActiveNetworkInfo() != null && connectivity.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }


        }
        return false;
    }
}
