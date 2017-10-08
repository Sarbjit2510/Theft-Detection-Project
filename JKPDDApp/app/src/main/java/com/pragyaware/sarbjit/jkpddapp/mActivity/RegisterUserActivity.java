package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mModel.Complaint;
import com.pragyaware.sarbjit.jkpddapp.mModel.MultipleImage;
import com.pragyaware.sarbjit.jkpddapp.mModel.UpdateComplaintModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.PermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RegisterUserActivity extends AppCompatActivity {
    public static final int OTP_ACTIVITY = 1;
    EditText name_edtVw, phone_edtVw;
    Button submit_btn;
    Context con;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        con = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // checkPermissions();
            PermissionUtil permissionUtil = new PermissionUtil(this);
            permissionUtil.checkPermissions();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_edtVw = (EditText) findViewById(R.id.name_edtVw);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        phone_edtVw = (EditText) findViewById(R.id.phone_edtVw);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    submitData();
                }
            }
        });


    }


    private void submitData() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();


        try {
            JSONObject object = new JSONObject();
            object.put("usrName", name_edtVw.getText().toString());
            object.put("usrMobile", phone_edtVw.getText().toString());
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.LOGIN, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject object = new JSONObject(response);
                        if (object.getString("Status").equalsIgnoreCase("1")) {
                            PreferenceUtil.getInstance(getApplicationContext()).setUserContactId(object.getString("UserID"));
                            PreferenceUtil.getInstance(getApplicationContext()).setUsername(name_edtVw.getText().toString());
                            Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                            intent.putExtra("OTP", object.getString("OTP"));
                            intent.putExtra("MobileNo", phone_edtVw.getText().toString());
                            startActivityForResult(intent, OTP_ACTIVITY);
                        } else {
                            DialogUtil.showDialogOK("Alert!", object.getString("Msg"), con);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            Intent i = new Intent(RegisterUserActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == OTP_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                PreferenceUtil.getInstance(getApplicationContext()).setLoggedIn(true);
                PreferenceUtil.getInstance(getApplicationContext()).setUserType(com.pragyaware.sarbjit.jkpddapp.common.Constants.NORMAL_USER);
                startMainActivity();
            }
        }
    }

    private void startMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean validate() {
        if (name_edtVw.getText().toString().equalsIgnoreCase("")) {
            name_edtVw.setError("Please Enter Name");
            name_edtVw.requestFocus();
            return false;
        } else if (phone_edtVw.getText().toString().equalsIgnoreCase("")) {
            phone_edtVw.setError("Please Enter Phone No");
            phone_edtVw.requestFocus();
            return false;
        } else if (phone_edtVw.getText().toString().length() != 10) {
            phone_edtVw.setError("Please Enter Valid Phone No");
            phone_edtVw.requestFocus();
            return false;
        }
        return true;
    }

    public static class UploadUserComplaints extends AsyncTask<String, Void, String> {

        @SuppressLint("StaticFieldLeak")
        Context con;


        UploadUserComplaints(Context con) {
            this.con = con;
        }

        @Override
        protected String doInBackground(String... urls) {

            Realm realm = Realm.getDefaultInstance();
            try {
                RealmResults<Complaint> pendingComplaints = realm.where(Complaint.class)
                        .equalTo("complaintID", 0)
                        .findAll();

                for (final Complaint complaint : pendingComplaints) {
                    try {
                        postData(complaint, realm, con);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            } finally {
                realm.close();
            }
            return null;
        }

        private void postData(final Complaint complaint, final Realm realm, final Context con) {

            try {
                JSONObject object = new JSONObject();
                object.put("complaintUserID", PreferenceUtil.getInstance(con).getUserContactId());
                object.put("complaintComment", complaint.getRemarks());
                object.put("complaintLat", complaint.getLat());
                object.put("complaintLng", complaint.getLng());

                StringEntity entity = new StringEntity(object.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.REGISTER_COMPLAINT, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                submitMediaComplaint(jsonObject.getString("ComplaintID"), complaint.getImages());
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        // remove single match
                                        complaint.setServerStatus(Complaint.SERVER_UPLOADED);
                                        complaint.deleteFromRealm();
                                    }
                                });
                            } else {
                                DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), con);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogUtil.showDialogOK(null, e.getMessage(), con);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        dialog.dismiss();
                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            RequestParams params = new RequestParams();
//            params.put("Method", "20");
//            params.put("Districtname", complaint.getDistrict());
//            params.put("Address", complaint.getAddress());
//            params.put("Image1", "");
//            params.put("Image2", "");
//            params.put("Image", image);
//            params.put("Lat", complaint.getLat());
//            params.put("Lng", complaint.getLng());
//            params.put("Remarks", complaint.getRemarks());
//            params.put("Contactid", complaint.getComplaintContactID());
//
//            Constants.getSyncClient().post(con, Constants.API_URL, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//
//                    try {
//
//                        String response = new String(responseBody, "UTF-8");
////                        JSONObject jsonObj = XML.toJSONObject(response);
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            Log.d("State", "State" + object.getString("Status"));
//                            if (object.getString("Status").equalsIgnoreCase("1")) {
//                                //showDialog(object.getString("ComplaintId"));
//
//
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                }
//            });

        }

        private void submitMediaComplaint(String complaintID, RealmList<MultipleImage> images) {
            for (MultipleImage multipleImage : images) {
                submitMediaData(complaintID, multipleImage);
            }
        }

        private void submitMediaData(String complaintID, MultipleImage multipleImage) {
            try {
                JSONObject object = new JSONObject();
                object.put("mediaComplaintID", complaintID);
                object.put("mediaData", multipleImage.getImage());
                object.put("mediaType", "0");
                StringEntity entity = new StringEntity(object.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.COMPLAINT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {

                            } else {
                                DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), con);
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        dialog.dismiss();
                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String result) {

        }
    }

    public static class UploadComplaintsStatus extends AsyncTask<Void, Void, Void> {
        @SuppressLint("StaticFieldLeak")
        private Context con;

        UploadComplaintsStatus(Context con) {
            this.con = con;
        }

        @Override
        protected Void doInBackground(Void... urls) {
            Realm realm = Realm.getDefaultInstance();
            try {
                RealmResults<UpdateComplaintModel> pendingStatusComplaints = realm.where(UpdateComplaintModel.class)
                        .findAll();
//            final RealmResults<Complaint> results = realm.where(Complaint.class)
//                    .equalTo("serverStatus", 1)
//                    .greaterThan("complaintID", 0)
//                    .findAll();

                for (final UpdateComplaintModel complaint : pendingStatusComplaints) {
                    try {
//                       new Handler(Looper.getMainLooper()) {
//                            @Override
//                            public void handleMessage(Message message) {
//                                postData(complaint);
//                            }
//                        };
                        postData(complaint, realm);
//                        if (complaint.getServerStatus() == Complaint.SERVER_UPLOADED) {
//
//                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } finally {
                realm.close();
            }
            return null;
            // we use the OkHttp library from https://github.com/square/okhttp

        }

        private void postData(final UpdateComplaintModel complaint, final Realm realm) {

            try {
                JSONObject object = new JSONObject();
                object.put("ID", complaint.getID());
                object.put("complaintOfficerComment", complaint.getComplaintOfficerComment());
                object.put("complaintDefaulterExists", complaint.getComplaintDefaulterExists());
                object.put("complaintDefaulterAcc", complaint.getComplaintDefaulterAcc());
                object.put("complaintStat", complaint.getComplaintStat());
                object.put("complaintDefaulterPenality", complaint.getComplaintDefaulterPenality());
                StringEntity entity = new StringEntity(object.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.UPDATE_STATUS, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                submitImages(complaint.getID(), complaint.getImages());
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        // remove single match
                                        complaint.deleteFromRealm();
                                    }
                                });
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        progressDialog.dismiss();
                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        private void submitImages(String id, RealmList<MultipleImage> images) {
            for (MultipleImage multipleImage : images) {
                submitApiData(id, multipleImage);
            }
        }

        private void submitApiData(String id, MultipleImage multipleImage) {
            try {
                JSONObject object = new JSONObject();
                object.put("mediaComplaintID", id);
                object.put("mediaData", multipleImage.getImage());
                object.put("mediaType", "1");
                StringEntity entity = new StringEntity(object.toString());

                Constants.getSyncClient().post(con, Constants.API_URL + Constants.COMPLAINT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {

                            } else {
                                DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), con);
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        dialog.dismiss();
                    }
                });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}



