package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujiyuu75.sequent.Sequent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.PhotoGridAdaptar;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.ViewPagerAdaptar;
import com.pragyaware.sarbjit.jkpddapp.mModel.MultipleImage;
import com.pragyaware.sarbjit.jkpddapp.mModel.OfficerComplModel;
import com.pragyaware.sarbjit.jkpddapp.mModel.UpdateComplaintModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmList;

public class UpdateStatusActivity extends AppCompatActivity {
    public static final String UPDATE = "update";
    public Button submit_btn;
    public OfficerComplModel complaintList;
    RadioGroup theft_grp, consumer_grp; //location_grp,
    EditText account_edtVw, plentyt_edtVw, remark_edtVw;
    ImageView capture_imgVw, track_btn, leftNav, rightNav;
    String ExistingConsumer = "", TheftDetected = "", imageString = ""; //LocationFound = "",
    int consumer_grp_value, theft_grp_value; //location_grp_value,
    GridView photoGrid/*, complaintGrid*/;
    ArrayList<Bitmap> bitmaps;
    RealmList<MultipleImage> base64;
    PhotoGridAdaptar adaptar;
    int count = 0;
    ArrayList<String> strings;
    TextView tv_compId, tv_compDate, tv_compAddress;
    private Realm realm;
    ScrollView main_layout;
    LinearLayout account_layout, update_layout, photo_layout, plenty_layout, consumer_layout;
    ViewPager photoViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_update_status);
        complaintList = getIntent().getParcelableExtra(UPDATE);
        account_layout = (LinearLayout) findViewById(R.id.account_layout);
        theft_grp = (RadioGroup) findViewById(R.id.theft_grp);
        consumer_grp = (RadioGroup) findViewById(R.id.consumer_grp);
        account_edtVw = (EditText) findViewById(R.id.account_edtVw);
        plentyt_edtVw = (EditText) findViewById(R.id.plentyt_edtVw);
        remark_edtVw = (EditText) findViewById(R.id.remark_edtVw);
        capture_imgVw = (ImageView) findViewById(R.id.capture_imgVw);
        photoGrid = (GridView) findViewById(R.id.photoGrid);
        update_layout = (LinearLayout) findViewById(R.id.update_layout);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        track_btn = (ImageView) findViewById(R.id.track_btn);
        tv_compId = (TextView) findViewById(R.id.tv_compId);
        main_layout = (ScrollView) findViewById(R.id.main_layout);
        photoViewPager = (ViewPager) findViewById(R.id.photoViewPager);
        leftNav = (ImageView) findViewById(R.id.left_nav);
        rightNav = (ImageView) findViewById(R.id.right_nav);
        photo_layout = (LinearLayout) findViewById(R.id.photo_layout);
        plenty_layout = (LinearLayout) findViewById(R.id.plenty_layout);
        consumer_layout = (LinearLayout) findViewById(R.id.consumer_layout);

        Sequent.origin(main_layout).anim(this, R.anim.fade_in).start();

        bitmaps = new ArrayList<>();
        base64 = new RealmList<>();
        strings = new ArrayList<>();
        count = 0;

        if (complaintList.getComplaintMedia().contains(",")) {
            String[] strings1 = complaintList.getComplaintMedia().split(",");
            Collections.addAll(strings, strings1);
        } else {
            strings.add(complaintList.getComplaintMedia());
        }

        if (complaintList.getComplaintStat().equalsIgnoreCase("Pending")) {
            update_layout.setVisibility(View.VISIBLE);
        } else {
            update_layout.setVisibility(View.GONE);
        }

        // Images left navigation
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = photoViewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    photoViewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    photoViewPager.setCurrentItem(tab);
                }
            }
        });

        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = photoViewPager.getCurrentItem();
                tab++;
                photoViewPager.setCurrentItem(tab);
            }
        });

        String id = String.valueOf(complaintList.getID());
        tv_compId.setText(id);
        tv_compDate = (TextView) findViewById(R.id.tv_compDate);
        tv_compDate.setText(complaintList.getComplaintStamp());
        tv_compAddress = (TextView) findViewById(R.id.tv_compAddress);
        tv_compAddress.setText(complaintList.getComplaintAddress());

        consumer_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.consumer_yes_radio == i) {
                    ExistingConsumer = "Yes";
                    consumer_grp_value = 1;
                    account_layout.setVisibility(View.VISIBLE);
                } else {
                    ExistingConsumer = "No";
                    consumer_grp_value = 0;
                    account_layout.setVisibility(View.GONE);
                }
            }
        });


        theft_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.theft_yes_radio == i) {
                    TheftDetected = "Yes";
                    theft_grp_value = 2;
                    consumer_layout.setVisibility(View.VISIBLE);
                    account_layout.setVisibility(View.VISIBLE);
                    plenty_layout.setVisibility(View.VISIBLE);
                    photo_layout.setVisibility(View.VISIBLE);
                } else if (R.id.theft_no_radio == i) {
                    TheftDetected = "No";
                    theft_grp_value = 3;
                    consumer_layout.setVisibility(View.GONE);
                    account_layout.setVisibility(View.GONE);
                    plenty_layout.setVisibility(View.GONE);
                    photo_layout.setVisibility(View.GONE);
                } else {
                    TheftDetected = "";
                    theft_grp_value = 0;
                }
            }
        });

        capture_imgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageString = ImageUtil.takePhoto(UpdateStatusActivity.this);
            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    if (CheckInternetUtil.isConnected(UpdateStatusActivity.this)) {
                        postData();
                    } else {
                        DialogUtil.showToast("Internet Connection Not Available..", UpdateStatusActivity.this, false);
                        saveUpdateStatus();
                    }
                }
            }
        });

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        final Bitmap bitmap = UserComplaintActivity.getBitmapFromVectorDrawable(this, R.drawable.add_image);
        bitmaps.add(bitmap);
        adaptar = new PhotoGridAdaptar(this, bitmaps);
        photoGrid.setAdapter(adaptar);

        photoViewPager.setAdapter(new ViewPagerAdaptar(UpdateStatusActivity.this, strings));

//        complaintGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                startActivity(new Intent(UpdateStatusActivity.this, PreviewImageActivity.class).putExtra("data", strings.get(i)));
//            }
//        });

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == bitmaps.size() - 1) {
                    imageString = ImageUtil.takePhoto(UpdateStatusActivity.this);
                }
            }
        });

    }

    private void saveUpdateStatus() {
        try {

            realm.beginTransaction();
            UpdateComplaintModel complaintModel = new UpdateComplaintModel();
            complaintModel.setID(complaintList.getID());
            complaintModel.setImages(base64);
            if (account_layout.getVisibility() == View.VISIBLE) {
                complaintModel.setComplaintDefaulterAcc(account_edtVw.getText().toString());
            } else {
                complaintModel.setComplaintDefaulterAcc("");
            }
            complaintModel.setComplaintDefaulterExists(String.valueOf(consumer_grp_value));
            int penalty = 0;
            if (!plentyt_edtVw.getText().toString().isEmpty()) {
                penalty = Integer.parseInt(plentyt_edtVw.getText().toString());
            }
            complaintModel.setComplaintDefaulterPenality(String.valueOf(penalty));
            complaintModel.setComplaintOfficerComment(remark_edtVw.getText().toString());
            complaintModel.setComplaintStat(String.valueOf(theft_grp_value));
            realm.insert(complaintModel);
        } finally {
            realm.commitTransaction();
        }
        DialogUtil.showToast("Complaint saved local successfully", getApplicationContext(), true);
        finish();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }

    }

//    private void saveComplaint(OfficerComplModel complaint) {
//        try {
//
//            realm.beginTransaction();
//
//            complaint.setUdpateStatusPhoto(image);
//            complaint.setLocationFound(0);
//            complaint.setExistingConsumer(consumer_grp_value);
//            complaint.setTheftDetected(theft_grp_value);
//            complaint.setAccountNo(account_edtVw.getText().toString());
//            int penalty = 0;
//            if (!plentyt_edtVw.getText().toString().isEmpty()) {
//                penalty = Integer.parseInt(plentyt_edtVw.getText().toString());
//            }
//            complaint.setPenaltyAmount(penalty);
//            complaint.setServerStatus(Complaint.SERVER_PENDING);
//            realm.insertOrUpdate(complaint);
//        } finally {
//            realm.commitTransaction();
//        }
//        DialogUtil.showToast("Complaint saved local successfully", getApplicationContext(), true);
//        finish();
//
//    }

    private void fetchLocation() {
        String query = "google.navigation:q=" + complaintList.getComplaintAddress();
        Uri uri = Uri.parse(query);
        //     intent.setData(uri);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            JSONObject object = new JSONObject();
            object.put("ID", complaintList.getID());
            object.put("complaintOfficerComment", remark_edtVw.getText().toString());
            object.put("complaintDefaulterExists", consumer_grp_value);
            object.put("complaintDefaulterAcc", account_edtVw.getText().toString());
            object.put("complaintStat", theft_grp_value);
            int penalty = 0;
            if (plenty_layout.getVisibility() == View.VISIBLE) {
                if (!plentyt_edtVw.getText().toString().isEmpty()) {
                    penalty = Integer.parseInt(plentyt_edtVw.getText().toString());
                }
            }

            object.put("complaintDefaulterPenality", penalty);
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.UPDATE_STATUS, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            if (base64.size() != 0)
                                submitImages(complaintList.getID(), base64, progressDialog);
                            else {
                                progressDialog.dismiss();
                                showDialog("Complaint Status of Id " + complaintList.getID() + " has been Updated");
                            }

                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.dismiss();
                }
            });


        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void submitImages(String id, RealmList<MultipleImage> base64, ProgressDialog dialog) {
        for (MultipleImage multipleImage : base64) {
            submitApiData(id, multipleImage, dialog);
        }
    }

    private void submitApiData(final String id, MultipleImage multipleImage, final ProgressDialog dialog) {

        try {
            JSONObject object = new JSONObject();
            object.put("mediaComplaintID", id);
            object.put("mediaData", multipleImage.getImage());
            object.put("mediaType", "1");
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.COMPLAINT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            count++;
                            if (count == base64.size()) {
                                dialog.dismiss();
                                showDialog("Complaint Status of Id " + id + " has been Updated");
                            }
                        } else {
                            dialog.dismiss();
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), UpdateStatusActivity.this);
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


//    private void postData() {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please wait...");
//        dialog.setCancelable(false);
//        dialog.show();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 15;
//        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
//                options);
//        ByteArrayOutputStream bao = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bao);
//        byte[] ba = bao.toByteArray();
//        String image = Base64.encodeToString(ba, Base64.DEFAULT);
//
//        RequestParams params = new RequestParams();
//        params.put("Method", "23");
//        params.put("ComplaintID", complaintModel.getId());
//        params.put("ComplaintStatus", 1);
//        params.put("Image", image);
//        params.put("LocationFound", location_grp_value);
//        params.put("ExistingConsumer", consumer_grp_value);
//        params.put("TheftDetected", theft_grp_value);
//        params.put("AcNo", account_edtVw.getText().toString());
//        if(complaintModel.getDefaulterName()==null) {
//            params.put("name ", "NA");
//        }
//        else {
//            params.put("name ", complaintModel.getDefaulterName());
//        }
//        params.put("PenaltyAmount ", plentyt_edtVw.getText().toString());
//        if(complaintModel.getAddress()==null)
//        {
//            params.put("address ", "NA");
//        }
//        else {
//            params.put("address ", complaintModel.getAddress());
//        }
//        Log.v("URL:", Constants.API_URL + params.toString());
//        Constants.getClient().post(this, Constants.API_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                dialog.dismiss();
//                try {
//                    String s = new String(responseBody, "UTF-8");
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String status = object.getString("Status");
//                        String response = object.getString("Response");
//                        dialog.dismiss();
//                        if (status.equalsIgnoreCase("1")) {
//                            DialogUtil.showDialogOK(null, response, UpdateStatusActivity.this);
//                        }
//                       else if (status.equalsIgnoreCase("2")) {
//                            DialogUtil.showDialogOK(null, response, UpdateStatusActivity.this);
//                        }
//                        else {
//                            dialog.dismiss();
//                            DialogUtil.showDialogOK(null, response, UpdateStatusActivity.this);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                dialog.dismiss();
//            }
//        });
//
//    }

    public void showDialog(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        dialog.setContentView(R.layout.thanks_layout);

        // Set dialog title
        dialog.setTitle(null);

        TextView ok_txtVw = (TextView) dialog.findViewById(R.id.ok_txtVw);
        TextView id_txtVw = (TextView) dialog.findViewById(R.id.tv_complaint_id);
        id_txtVw.setText(msg);
        ok_txtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
//                Intent i = new Intent(UserComplaintActivity.this, MainActivity.class);
//                startActivity(i);

            }
        });

        dialog.show();

    }


    private boolean validate() {

        if (TheftDetected.equalsIgnoreCase("")) {
            theft_grp.requestFocus();
            DialogUtil.showToast("Please Select Theft Radio", this, false);
            return false;
        }
        if (consumer_layout.getVisibility() == View.VISIBLE) {
            if (ExistingConsumer.equalsIgnoreCase("")) {
                consumer_grp.requestFocus();
                DialogUtil.showToast("Please Select Existing Consumer Radio", this, false);
                return false;
            }
        }
        if (account_layout.getVisibility() == View.VISIBLE) {
            if (ExistingConsumer.equalsIgnoreCase("Yes")) {
                if (account_edtVw.getText().toString().equalsIgnoreCase("")) {
                    account_edtVw.setError("Please Enter Account No");
                    account_edtVw.requestFocus();
                    return false;
                }
            }
        }
        if (remark_edtVw.getText().toString().equalsIgnoreCase("")) {
            remark_edtVw.setError("Please Enter Your Remark");
            remark_edtVw.requestFocus();
            return false;
        }
        if (photo_layout.getVisibility() == View.VISIBLE) {
            if (imageString.equalsIgnoreCase("") && base64.size() == 0) {
                DialogUtil.showToast("Please Capture Image", this, false);
                return false;
            }
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageString, bmOptions);
                bmOptions.inSampleSize = 10;
                bmOptions.inJustDecodeBounds = false;
                Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(imageString, bmOptions);
                bitmaps.add(0, photoReducedSizeBitmp);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photoReducedSizeBitmp.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                byte[] ba = baos.toByteArray();
                String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

                realm.beginTransaction();
                MultipleImage multipleImage = realm.createObject(MultipleImage.class);
                multipleImage.setImage(ba1);
                base64.add(multipleImage);
                realm.commitTransaction();
                adaptar.notifyDataSetChanged();

            } else if (resultCode == RESULT_CANCELED) {
                imageString = "";
                // user cancelled Image capture
                Toast.makeText(this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                imageString = "";
                // failed to capture image
                Toast.makeText(this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
