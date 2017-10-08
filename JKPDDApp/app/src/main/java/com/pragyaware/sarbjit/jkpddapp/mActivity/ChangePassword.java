package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.ProgressDialog;
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

public class ChangePassword extends AppCompatActivity {

    EditText ed_old_pswd, ed_new_pswd, ed_cm_pswd;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ed_old_pswd = (EditText) findViewById(R.id.ed_old_pswd);
        ed_new_pswd = (EditText) findViewById(R.id.ed_new_pswd);
        ed_cm_pswd = (EditText) findViewById(R.id.ed_cm_pswd);
        submit_btn = (Button) findViewById(R.id.submit_btn);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    submitDataToServer();
                }
            }
        });

    }

    private void submitDataToServer() {

        if (CheckInternetUtil.isConnected(this)) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();

            try {
                JSONObject object = new JSONObject();
                object.put("ID", PreferenceUtil.getInstance(this).getUserContactId());
                object.put("usrPass", ed_new_pswd.getText().toString());
                object.put("usrOldPass", ed_old_pswd.getText().toString());
                StringEntity entity = new StringEntity(object.toString());

                Constants.getClient().post(this, Constants.API_URL + Constants.CHANGE_PASSWORD, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject object1 = new JSONObject(response);

                            if (object1.getString("Status").equalsIgnoreCase("1")) {
                                DialogUtil.showDialog(null, object1.getString("Msg"), ChangePassword.this);
                            } else {
                                DialogUtil.showDialogOK("Alert!", object1.getString("Msg"), ChangePassword.this);
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

        } else {
            DialogUtil.showDialogOK("Alert!", "No Internet Connection", this);
        }

    }


    private boolean validate() {

        if (ed_old_pswd.getText().toString().equalsIgnoreCase("")) {
            ed_old_pswd.setError("Please Enter Old Password");
            ed_old_pswd.requestFocus();
            return false;
        } else if (ed_new_pswd.getText().toString().equalsIgnoreCase("")) {
            ed_new_pswd.setError("Please Enter New Password");
            ed_new_pswd.requestFocus();
            return false;
        } else if (ed_new_pswd.getText().toString().length() < 5) {
            ed_new_pswd.setError("Please Enter New Password Length Greater than 5");
            ed_new_pswd.requestFocus();
            return false;
        } else if (ed_cm_pswd.getText().toString().equalsIgnoreCase("")) {
            ed_cm_pswd.setError("Please Enter Confirm Password");
            ed_cm_pswd.requestFocus();
            return false;
        } else if (ed_cm_pswd.getText().toString().length() < 5) {
            ed_cm_pswd.setError("Please Enter Confirm Password Length Greater than 5");
            ed_cm_pswd.requestFocus();
            return false;
        } else if (!ed_new_pswd.getText().toString().equalsIgnoreCase(ed_cm_pswd.getText().toString())) {
            ed_cm_pswd.setError("Password Mismatches");
            ed_new_pswd.setError("Password Mismatches");
            ed_new_pswd.requestFocus();
            ed_cm_pswd.requestFocus();
            return false;
        }

        return true;
    }
}
