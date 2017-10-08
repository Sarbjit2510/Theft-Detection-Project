package com.pragyaware.sarbjit.jkpddapp.mUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by DalbirSingh on 14-07-2016.
 */
public class DialogUtil {

    public static void showDialogOK(String title, String msg,
                                    Context con) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title);

        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showToast(String msg,
                                 Context con, boolean longLength) {
        if (longLength)
            Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showGPSDisabledAlertToUser(final Context con) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        alertDialogBuilder.setTitle("Info");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
        alertDialogBuilder
                .setMessage("GPS is disabled in your device. Please select USE GPS SATELLITES option form list to enable it!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        con.startActivity(callGPSSettingIntent);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public static void showDialog(String title, String msg,
                                  final Activity con) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title);

        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                con.finish();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
