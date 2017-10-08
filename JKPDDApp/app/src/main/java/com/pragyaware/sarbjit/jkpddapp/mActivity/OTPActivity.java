package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.FontTextViewThin;

public class OTPActivity extends AppCompatActivity {

    EditText et1, et2, et3, et4;
    Button btSubmit;
    String uotp;
    FontTextViewThin tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        tvPhone = (FontTextViewThin) findViewById(R.id.phone_no);
        btSubmit = (Button) findViewById(R.id.submit_btn);
        et1 = (EditText) findViewById(R.id.et_1);
        et2 = (EditText) findViewById(R.id.et_2);
        et3 = (EditText) findViewById(R.id.et_3);
        et4 = (EditText) findViewById(R.id.et_4);
        Intent i = getIntent();
        final String otp = i.getStringExtra("OTP");
        String phoneNo = i.getStringExtra("MobileNo");
        tvPhone.setText(phoneNo);
        Log.d("HS", "HS" + otp + PreferenceUtil.getInstance(getApplicationContext()).getUserContactId());
        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et1.getText().toString().length() == 1)     //size as per your requirement
                {
                    et2.requestFocus();
                }
            }

            @Override

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                uotp = et1.getText().toString();
            }

        });

        et2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et2.getText().toString().length() == 1)     //size as per your requirement
                {
                    et3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                uotp = et1.getText().toString() + et2.getText().toString();
            }

        });

        et3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et3.getText().toString().length() == 1)     //size as per your requirement
                {
                    et4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                uotp = et1.getText().toString() + et2.getText().toString() + et3.getText().toString();
            }

        });

        et4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et4.getText().toString().length() == 1)     //size as per your requirement
                {
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                uotp = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString();
            }

        });
        Log.d("io", "io" + uotp);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (CheckInternetUtil.isConnected(getApplicationContext())) {
                        if (uotp.equals(otp)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            DialogUtil.showDialogOK("info", "You entered wrong OTP", OTPActivity.this);
                        }
                    }

                }
            }
        });


    }

    public boolean validate() {
        if (et1.getText().toString().equalsIgnoreCase("")) {
            et1.setError("Please First Digit");
            et1.requestFocus();
            return false;
        } else if (et2.getText().toString().equalsIgnoreCase("")) {
            et2.setError("Please Second Digit");
            et2.requestFocus();
            return false;
        } else if (et3.getText().toString().equalsIgnoreCase("")) {
            et3.setError("Please Third Digit");
            et3.requestFocus();
            return false;
        } else if (et4.getText().toString().equalsIgnoreCase("")) {
            et4.setError("Please Fourth Digit");
            et4.requestFocus();
            return false;
        }
        return true;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here
                et1.setText(String.valueOf(message.charAt(0)));
                et2.setText(String.valueOf(message.charAt(1)));
                et3.setText(String.valueOf(message.charAt(2)));
                et4.setText(String.valueOf(message.charAt(3)));
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}
