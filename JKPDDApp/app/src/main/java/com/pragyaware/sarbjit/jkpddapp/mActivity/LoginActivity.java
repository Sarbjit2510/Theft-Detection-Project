package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {

    Context con;
    EditText phone_edtVw, pass_edtVw;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        con = this;
        pass_edtVw = (EditText) findViewById(R.id.pass_edtVw);
        phone_edtVw = (EditText) findViewById(R.id.phone_edtVw);

        submit_btn = (Button) findViewById(R.id.submit_btn);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (CheckInternetUtil.isConnected(LoginActivity.this)) {

                        submitData();

                    } else {
                        DialogUtil.showDialogOK("Alert!", "No Internet Connection", LoginActivity.this);
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
    }

    private void submitData() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject object = new JSONObject();
            object.put("usrMobile", phone_edtVw.getText().toString());
            object.put("usrPass", pass_edtVw.getText().toString());
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.OFFICER_LOGIN, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();

                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject object = new JSONObject(response);
                        if (object.getString("Status").equalsIgnoreCase("1")) {
                            PreferenceUtil.getInstance(getApplicationContext()).setLoggedIn(true);
                            PreferenceUtil.getInstance(getApplicationContext()).setUserType(com.pragyaware.sarbjit.jkpddapp.common.Constants.OFFICER);
                            PreferenceUtil.getInstance(getApplicationContext()).setUserContactId(object.getString("OfficerID"));
                            PreferenceUtil.getInstance(getApplicationContext()).setUsername(object.getString("OfficerName"));
                            PreferenceUtil.getInstance(getApplicationContext()).setDistrict(object.getString("OfficerDistrict"));
                            PreferenceUtil.getInstance(getApplicationContext()).setDesignation(object.getString("OfficerDesignation"));
                            startMainActivity();
                        } else {
                            DialogUtil.showDialogOK("Alert!", object.getString("Msg"), LoginActivity.this);
                        }

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dialog.dismiss();
                }
            });


        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void startMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), OfficerDashboard.class);
        startActivity(intent);

    }

    private boolean validate() {
        if (phone_edtVw.getText().toString().equalsIgnoreCase("")) {
            phone_edtVw.setError("Please Enter Mobile No.");
            phone_edtVw.requestFocus();
            return false;
        } else if (phone_edtVw.getText().toString().length() != 10) {
            phone_edtVw.setError("Please Enter Valid Mobile No.");
            phone_edtVw.requestFocus();
            return false;
        } else if (pass_edtVw.getText().toString().equalsIgnoreCase("")) {
            pass_edtVw.setError("Please Enter Password.");
            pass_edtVw.requestFocus();
            return false;
        } else if (pass_edtVw.getText().toString().length() < 4) {
            pass_edtVw.setError("Please Enter Valid Password");
            pass_edtVw.requestFocus();
            return false;
        }
        return true;
    }


}
