package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.PermissionUtil;

public class OfficerDashboard extends AppCompatActivity {

    WebView webview;
    AlertDialog ad;
    AlertDialog.Builder builder;
    Context con;
    LinearLayout user_layout, view_complaint_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);

        con = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_layout = (LinearLayout) findViewById(R.id.user_layout);
        view_complaint_layout = (LinearLayout) findViewById(R.id.view_complaint_layout);

        webview = (WebView) findViewById(R.id.webview1);

        webview.setFocusable(true);
        webview.setFocusableInTouchMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.clearCache(true);

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // checkPermissions();
            PermissionUtil permissionUtil = new PermissionUtil(this);
            permissionUtil.checkPermissions();
        }


        user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfficerDashboard.this, ViewComplaints.class));
            }
        });

        view_complaint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfficerDashboard.this, ChangePassword.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(null);

        builder.setMessage("Do you want to Exit App.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }

        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (PreferenceUtil.getInstance(getApplicationContext()).isOfficer()) {
            getMenuInflater().inflate(R.menu.drawer, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (PreferenceUtil.getInstance(getApplicationContext()).isOfficer()) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                builder = new AlertDialog.Builder(OfficerDashboard.this);
//        builder.setMessage("Activity2");
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (PreferenceUtil.getInstance(con).isLoggedIn()) {
                            PreferenceUtil.getInstance(getApplicationContext()).setLoggedIn(false);
                            PreferenceUtil.getInstance(con).logoutPref();
                            Intent intent = new Intent(OfficerDashboard.this, RegisterUserActivity.class);
                            startActivity(intent);
                            finish();
                        }

//                Toast.makeText(getApplicationContext(), "Successfully navigated", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ad.dismiss();
                    }
                });
                ad = builder.create();
                ad.show();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void openURLTranscript() {
        if (CheckInternetUtil.isConnected(getApplicationContext())) {
            final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);

            webview.setVisibility(View.VISIBLE);
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    pd.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    pd.dismiss();
                }
            });
            webview.loadUrl("http://recpdcl.pragyaware.com/?h=1&d=" + PreferenceUtil.getInstance(getApplicationContext()).getDistrict());
            webview.requestFocus();
            webview.invalidate();
            (new RegisterUserActivity.UploadUserComplaints(getApplicationContext())).execute();
            (new RegisterUserActivity.UploadComplaintsStatus(getApplicationContext())).execute();
        } else {
            webview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getDataFromServer();
        openURLTranscript();

    }


}
