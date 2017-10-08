package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.Complaint_Adaptar;
import com.pragyaware.sarbjit.jkpddapp.mModel.ComplaintModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.pragyaware.sarbjit.jkpddapp.common.Constants.THEFT_DETECTED_STATUS;

public class ComplaintListActivity extends AppCompatActivity {

    RecyclerView complaint_recyclerVw;
    ArrayList<ComplaintModel> complaintModels;
    Realm realm;
    Spinner search_spinner;
    String[] items = {com.pragyaware.sarbjit.jkpddapp.common.Constants.PENDING_STATUS, THEFT_DETECTED_STATUS, com.pragyaware.sarbjit.jkpddapp.common.Constants.THEFT_NOT_DETECTED_STATUS, com.pragyaware.sarbjit.jkpddapp.common.Constants.ALL};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);

        complaint_recyclerVw = (RecyclerView) findViewById(R.id.my_recycler_view);
        search_spinner = (Spinner) findViewById(R.id.search_spinner);

        complaintModels = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        complaint_recyclerVw.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setSpinnerValue() {
        search_spinner.setAdapter(new ArrayAdapter<>(this, R.layout.custom_adapter, R.id.spin_txtVw, items));
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String status = items[i];
                RealmResults<ComplaintModel> result = null;
                if (i == 1) {
                    result = realm.where(ComplaintModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", com.pragyaware.sarbjit.jkpddapp.common.Constants.COMPLETED_STATUS).findAll();
                } else if (i == 2) {
                    result = realm.where(ComplaintModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", com.pragyaware.sarbjit.jkpddapp.common.Constants.IN_PROGRESS_STATUS).findAll();
                } else if (i == 3) {
                    result = realm.where(ComplaintModel.class).findAll();
                } else if (i == 0) {
                    result = realm.where(ComplaintModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", com.pragyaware.sarbjit.jkpddapp.common.Constants.PENDING_STATUS).findAll();
                }

                complaint_recyclerVw.setAdapter(new Complaint_Adaptar(ComplaintListActivity.this, result));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckInternetUtil.isConnected(this)) {
            getData();
        } else {
            setSpinnerValue();
        }

    }

    private void getData() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        String Url = Constants.API_URL + Constants.USER_COMPLAINT_LIST + "complaintUserID=" + PreferenceUtil.getInstance(this).getUserContactId();

        Constants.getClient().get(this, Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Rows");
                        complaintModels = new ArrayList<>();
                        final RealmResults<ComplaintModel> results = realm.where(ComplaintModel.class)
                                .notEqualTo("ID", "0")
                                .equalTo("userId", PreferenceUtil.getInstance(ComplaintListActivity.this).getUserContactId())
                                .findAll();

                        // All changes to data must happen in a transaction
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                // Delete all matches
                                results.deleteAllFromRealm();
                            }
                        });

                        try {
                            realm.beginTransaction();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                ComplaintModel model = new ComplaintModel();
                                model.setID(object.getString("ID"));
                                model.setComplaintAddress(object.getString("complaintAddress"));
                                model.setComplaintComment(object.getString("complaintComment"));
                                model.setComplaintDefaulterAcc(object.getString("complaintDefaulterAcc"));
                                model.setComplaintDefaulterExists(object.getString("complaintDefaulterExists"));
                                model.setComplaintDefaulterPenality(object.getString("complaintDefaulterPenality"));
                                model.setComplaintOfficer(object.getString("complaintOfficer"));
                                model.setComplaintOfficerComment(object.getString("complaintOfficerComment"));
                                model.setComplaintStamp(object.getString("complaintStamp"));
                                model.setComplaintStat(object.getString("complaintStat"));
                                model.setUserId(PreferenceUtil.getInstance(ComplaintListActivity.this).getUserContactId());
                                model.setComplaintMedia(object.getString("complaintMedia"));
                                model.setComplaintOfficerMedia(object.getString("complaintOfficerMedia"));
                                realm.insert(model);
                            }
                        } finally {
                            realm.commitTransaction();
                            setSpinnerValue();
                        }

                    } else {
                        DialogUtil.showDialog(null, jsonObject.getString("Msg"), ComplaintListActivity.this);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });

    }


}
