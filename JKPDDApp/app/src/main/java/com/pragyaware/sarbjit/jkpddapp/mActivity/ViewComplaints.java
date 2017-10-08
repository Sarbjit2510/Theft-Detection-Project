package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.Constants;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.CustomAdapter;
import com.pragyaware.sarbjit.jkpddapp.mModel.OfficerComplModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.pragyaware.sarbjit.jkpddapp.common.Constants.THEFT_DETECTED_STATUS;

public class ViewComplaints extends AppCompatActivity {

    private static final int UPDATE_STATUS_FLAG = 10;
    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    Context context;
    Spinner search_spinner;
    RecyclerView.LayoutManager layoutManager;
    private Realm realm;
    private OfficerComplModel updateComplaint;
    String[] items = {com.pragyaware.sarbjit.jkpddapp.common.Constants.PENDING_STATUS, THEFT_DETECTED_STATUS, com.pragyaware.sarbjit.jkpddapp.common.Constants.THEFT_NOT_DETECTED_STATUS, com.pragyaware.sarbjit.jkpddapp.common.Constants.ALL};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);
        realm = Realm.getDefaultInstance();

        search_spinner = (Spinner) findViewById(R.id.search_spinner);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        context = this;
        fetchComplaints();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }

    }

    private void updateSpinner() {
        search_spinner.setAdapter(new ArrayAdapter<>(this, R.layout.custom_adapter, R.id.spin_txtVw, items));
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String status = items[i];
                RealmResults<OfficerComplModel> result = null;
                if (i == 1) {
                    result = realm.where(OfficerComplModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", Constants.COMPLETED_STATUS).findAll();

                } else if (i == 2) {
                    result = realm.where(OfficerComplModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", Constants.IN_PROGRESS_STATUS).findAll();

                } else if (i == 3) {
                    result = realm.where(OfficerComplModel.class).findAll();
                } else if (i == 0) {

                    result = realm.where(OfficerComplModel.class).equalTo("complaintStat", status)
                            .or()
                            .equalTo("complaintStat", Constants.PENDING_STATUS).findAll();
                }

                adapter = new CustomAdapter(ViewComplaints.this.context, result);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchComplaints() {

        if (CheckInternetUtil.isConnected(getApplicationContext())) {


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            String Url = com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.API_URL + com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.OFFICER_COMPLAINT_LIST
                    + "complaintOfficerID=" + PreferenceUtil.getInstance(this).getUserContactId();

            com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.getClient().get(this, Url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    try {
                        String resBody = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(resBody);

                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {

                            final RealmResults<OfficerComplModel> results = realm.where(OfficerComplModel.class)
                                    .notEqualTo("ID", "0")
                                    .equalTo("userId", PreferenceUtil.getInstance(context).getUserContactId())
                                    .findAll();
                            // All changes to data must happen in a transaction
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    // Delete all matches
                                    results.deleteAllFromRealm();
                                }
                            });
                            JSONArray jsonArray = jsonObject.getJSONArray("Rows");
                            try {
                                realm.beginTransaction();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    OfficerComplModel model = realm.createObject(OfficerComplModel.class);
                                    model.setUserId(PreferenceUtil.getInstance(context).getUserContactId());
                                    model.setComplaintStamp(object.getString("complaintStamp"));
                                    model.setID(object.getString("ID"));
                                    model.setComplaintAddress(object.getString("complaintAddress"));
                                    model.setComplaintComment(object.getString("complaintComment"));
                                    model.setComplaintUser(object.getString("complaintUser"));
                                    model.setComplaintMedia(object.getString("complaintMedia"));
                                    if (object.getString("complaintStat").equalsIgnoreCase("1")) {
                                        model.setComplaintStat(Constants.PENDING_STATUS);
                                    } else if (object.getString("complaintStat").equalsIgnoreCase("2")) {
                                        model.setComplaintStat(Constants.COMPLETED_STATUS);
                                    } else {
                                        model.setComplaintStat(Constants.IN_PROGRESS_STATUS);
                                    }

                                    realm.insert(model);
                                }
                            } finally {
                                realm.commitTransaction();
                            }

                            updateSpinner();

                        } else {
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), context);
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


//            RequestParams params = new RequestParams();
//            params.put("method", "22");
//            params.put("district", divsion);
//            params.put("contactid", PreferenceUtil.getInstance(getApplicationContext()).getUserContactId());
//            Log.v("URL:", com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.API_URL + params.toString());
//
//            com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.getClient().get(this, com.pragyaware.sarbjit.jkpddapp.mUtil.Constants.API_URL + params, new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                    dialog.dismiss();
//                    // List<Complaint> complaintList = new RealmList<Complaint>();
//                    String resBody = new String(response);
//                    if (!resBody.isEmpty()) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(resBody);
//                            if (jsonObject.has("Data")) {
//                                JSONArray jsonArray = jsonObject.getJSONArray("Data");
//                                if (jsonArray.length() > 0) {
//                                    String status = jsonArray.getJSONObject(0).getString("Status");
//                                    String res = jsonArray.getJSONObject(0).getString("Response");
//                                    if ("1".equalsIgnoreCase(status)) {
//
//
//                                        final RealmResults<Complaint> results = realm.where(Complaint.class)
//                                                .notEqualTo("complaintID", 0)
//                                                .equalTo("complaintContactID", PreferenceUtil.getInstance(context).getUserContactId())
//                                                .findAll();
//
//                                        // All changes to data must happen in a transaction
//                                        realm.executeTransaction(new Realm.Transaction() {
//                                            @Override
//                                            public void execute(Realm realm) {
//                                                // Delete all matches
//                                                results.deleteAllFromRealm();
//                                            }
//                                        });
//                                        try {
//                                            realm.beginTransaction();
//                                            for (int j = 0; j < jsonArray.length(); j++) {
//                                                JSONObject cJsonObj = jsonArray.getJSONObject(j);
//
//
//                                                Number maxId = realm.where(Complaint.class).max("ID");
//                                                // If there are no rows, currentId is null, so the next id must be 1
//                                                // If currentId is not null, increment it by 1
//                                                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
//                                                // User object created with the new Primary key
//
//                                                Complaint complaint = realm.createObject(Complaint.class, nextId); // Create a new object
//                                                complaint.setComplaintID(Long.valueOf(cJsonObj.getString("ComplaintID")));
//                                                complaint.setDistrict(cJsonObj.getString("DistrictName"));
//                                                complaint.setAddress(cJsonObj.getString("Address"));
//                                                complaint.setLat(cJsonObj.getString("Lat"));
//                                                complaint.setLng(cJsonObj.getString("Lng"));
//                                                MultipleImage multipleImage = realm.createObject(MultipleImage.class);
//                                                multipleImage.setImage(cJsonObj.getString("PhotoPath"));
//                                                complaint.images.add(multipleImage);
//                                                complaint.setComplaintStatus(cJsonObj.getString("ComplaintStatus"));
////                                                if (cJsonObj.getString("ComplaintStatus").trim().equalsIgnoreCase("Pending")){
////
////                                                } else {
////
////                                                }
//
//                                                complaint.setComplaintDate(cJsonObj.getString("Date"));
//                                                complaint.setComplaintResponse(cJsonObj.getString("Response"));
//                                                complaint.setMarkedTo(cJsonObj.getString("MarkedTo"));
//                                                complaint.setComplaintContactID(PreferenceUtil.getInstance(context).getUserContactId());
//                                                complaint.setServerStatus(2);
//                                                realm.insert(complaint);
//                                                //complaintList.add(complaint);
//
//                                            }
//                                        } finally {
//                                            realm.commitTransaction();
//                                        }
//                                        RealmResults<Complaint> result = realm.where(Complaint.class).equalTo("complaintStatus", Constants.PENDING_STATUS).findAll();
//                                        adapter = new CustomAdapter(ViewComplaints.this.context, result);
//                                        recyclerView.setAdapter(adapter);
//
//                                    } else {
//                                        DialogUtil.showDialogOK("INFO", res, ViewComplaints.this.context);
//                                    }
//                                }
//                            } else {
//                                DialogUtil.showDialogOK("INFO", "No Data Found", ViewComplaints.this.context);
//                            }
//
//                        } catch (Exception ex) {
//                            Log.e(Constants.TAG, "Error While parsing response", ex);
//                            DialogUtil.showDialogOK("ERROR", ex.getMessage(), ViewComplaints.this.context);
//                        }
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onRetry(int retryNo) {
//                    // called when request is retried
//                }
//            });
        } else {
            RealmResults<OfficerComplModel> result = realm.where(OfficerComplModel.class).findAll();
            adapter = new CustomAdapter(ViewComplaints.this.context, result);
            recyclerView.setAdapter(adapter);
        }
    }

    public void updateStatus(OfficerComplModel complaint) {

        updateComplaint = complaint;
        Intent intent = new Intent(this, UpdateStatusActivity.class);
        intent.putExtra(UpdateStatusActivity.UPDATE, complaint);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, UPDATE_STATUS_FLAG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == UPDATE_STATUS_FLAG) {
            if (resultCode == RESULT_OK) {

                removeObject(updateComplaint);

            }
            fetchComplaints();
        }
    }

    private void removeObject(final OfficerComplModel complaint) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                complaint.deleteFromRealm();
                synchronized (adapter) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.add_item) {
//            //check if any items to add
//            if (removedItems.size() != 0) {
//                addRemovedItemToList();
//            } else {
//                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return true;
//    }
//
//    private void addRemovedItemToList() {
//        int addItemAtListPosition = 3;
//        data.add(addItemAtListPosition, new DataModel(
//                MyData.nameArray[removedItems.get(0)],
//                MyData.versionArray[removedItems.get(0)
//                        ]
//        ));
//        adapter.notifyItemInserted(addItemAtListPosition);
//        removedItems.remove(0);
//    }

}

