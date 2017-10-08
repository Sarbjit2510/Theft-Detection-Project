package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.DateUtil;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.PhotoGridAdaptar;
import com.pragyaware.sarbjit.jkpddapp.mLocation.GPSTracker;
import com.pragyaware.sarbjit.jkpddapp.mModel.Complaint;
import com.pragyaware.sarbjit.jkpddapp.mModel.MultipleImage;
import com.pragyaware.sarbjit.jkpddapp.mUtil.CheckInternetUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;
import com.pragyaware.sarbjit.jkpddapp.mUtil.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmList;

public class UserComplaintActivity extends AppCompatActivity {

    EditText /*etAddress, etDistrict, */remark_edtVw;
//    Spinner property_spinner, theft_spinner;
    Button submit_btn;
    String mImagePath = "";
    ImageView capture_imgVw;
//    TextView latlng_txtVw;
    GPSTracker gps;
    TextView tvUpload;
    String address, city, state;
    GridView imgGridVw;
    ArrayList<Bitmap> bitmaps;
    RealmList<MultipleImage> imagebase64;
    int count = 0;
    PhotoGridAdaptar adaptar;
    private Realm realm;
    boolean camera = false;
    private double lat, lng;

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 45, 45);
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complaint);
        realm = Realm.getDefaultInstance();
        if (mImagePath.equalsIgnoreCase("")) {
            mImagePath = ImageUtil.takePhoto(UserComplaintActivity.this);
        }
//        etAddress = (EditText) findViewById(R.id.address_edtVw);
//        etDistrict = (EditText) findViewById(R.id.district_edtVw);
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        imgGridVw = (GridView) findViewById(R.id.imgGridVw);
        count = 0;

        bitmaps = new ArrayList<>();
        imagebase64 = new RealmList<>();

//        etDistrict.setEnabled(false);
//        etAddress.setEnabled(false);
        remark_edtVw = (EditText) findViewById(R.id.remark_edtVw);

//        property_spinner = (Spinner) findViewById(R.id.property_spinner);
//        theft_spinner = (Spinner) findViewById(R.id.theft_spinner);

        capture_imgVw = (ImageView) findViewById(R.id.capture_imgVw);
        submit_btn = (Button) findViewById(R.id.btnSubmit);

//        latlng_txtVw = (TextView) findViewById(R.id.latlng_txtVw);


        Bitmap myLogo = getBitmapFromVectorDrawable(this, R.drawable.add_image);
        bitmaps.add(myLogo);
        adaptar = new PhotoGridAdaptar(this, bitmaps);
        imgGridVw.setAdapter(adaptar);


        if (gps == null) {
            gps = new GPSTracker(this);
            gps.startUsingGPS();
        }
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
//            latlng_txtVw.setText("Lat : " + gps.getLatitude() + " Lng : " + gps.getLongitude());
        } else {
            DialogUtil.showGPSDisabledAlertToUser(this);
        }

        getLatLng(false);

        capture_imgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera = true;
                mImagePath = ImageUtil.takePhoto(UserComplaintActivity.this);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSubmit();
            }
        });

        imgGridVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == bitmaps.size() - 1) {
                    camera = true;
                    mImagePath = ImageUtil.takePhoto(UserComplaintActivity.this);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }

    }

    @Override
    public void onBackPressed() {
        DialogUtil.showDialog("Alert!", "Are you sure you want to cancel submitting Complaint?", UserComplaintActivity.this);
    }

    public void getLatLng(boolean sync) {
        // final double lat = gps.getLatitude();
        // final double lng = gps.getLongitude();
        if (lat == 0 || lng == 0) {
            return;
        }
//        if (com.pragyaware.sarbjit.jkpddapp.common.CheckInternetUtil.isConnected(this)) {
        String location = lat + "," + lng;
        String myUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(location) + "&sensor=false";
        AsyncHttpClient httpClient;
        if (sync) {
            httpClient = Constants.getSyncClient();
        } else {
            httpClient = Constants.getClient();
        }
        httpClient.get(myUrl, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody);
                    JSONObject jso = new JSONObject(str);
                    JSONArray jsa = jso.getJSONArray("results");
                    JSONObject js2 = jsa.getJSONObject(0);

                    JSONArray jsa1 = js2.getJSONArray("address_components");
                    JSONObject js3 = jsa1.getJSONObject(3);
                    JSONObject js4 = jsa1.getJSONObject(4);

                    address = js2.getString("formatted_address");
//                    etAddress.setText(address);
                    city = js3.getString("long_name");
//                    etDistrict.setText(city);
                    state = js4.getString("long_name");
                } catch (JSONException jse) {
                    jse.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(UserComplaintActivity.this, "Location Coordinated Not Found !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
//        }
    }

    private void saveComplaint(double lat, double lng, RealmList<MultipleImage> imagePath) {
        try {

            realm.beginTransaction();
            Number maxId = realm.where(Complaint.class).max("ID");
            // If there are no rows, currentId is null, so the next id must be 1
            // If currentId is not null, increment it by 1
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            // User object created with the new Primary key

            Complaint complaint = realm.createObject(Complaint.class, nextId); // Create a new object
            complaint.setComplaintID(0);
            complaint.setDistrict("");
            complaint.setAddress("");
            complaint.setRemarks(remark_edtVw.getText().toString());
            complaint.setLat(String.valueOf(lat));
            complaint.setLng(String.valueOf(lng));
            complaint.setComplaintStatus("Pending");
            complaint.setComplaintDate(DateUtil.getToday());
            complaint.setComplaintResponse("");
            complaint.setComplaintContactID(PreferenceUtil.getInstance(getApplicationContext()).getUserContactId());
            complaint.setMarkedTo("");
            complaint.setImages(imagePath);
            complaint.setServerStatus(Complaint.SERVER_PENDING);
            realm.insert(complaint);
        } finally {
            realm.commitTransaction();
        }
        DialogUtil.showToast("Complaint saved local successfully", getApplicationContext(), true);
        finish();

    }


    public void OnSubmit() {

        if (validate()) {

            try {
                JSONObject object = new JSONObject();
                object.put("complaintUserID", PreferenceUtil.getInstance(getApplicationContext()).getUserContactId());
                object.put("complaintComment", remark_edtVw.getText().toString());
                object.put("complaintLat", lat);
                object.put("complaintLng", lng);

                if (CheckInternetUtil.isConnected(getApplicationContext())) {
                    submitData(object);
                } else {
                    saveComplaint(lat, lng, imagebase64);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
                finish();
//                Intent i = new Intent(UserComplaintActivity.this, MainActivity.class);
//                startActivity(i);

            }
        });

        dialog.show();

    }


    private void submitData(JSONObject params) {

        if (CheckInternetUtil.isConnected(this)) {

            try {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();

                StringEntity entity = new StringEntity(params.toString());

                Constants.getClient().post(this, Constants.API_URL + Constants.REGISTER_COMPLAINT, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody, "UTF-8");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                                submitMediaComplaint(jsonObject.getString("ComplaintID"), dialog);
                            } else {
                                dialog.dismiss();
                                DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), UserComplaintActivity.this);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            DialogUtil.showDialogOK(null, e.getMessage(), UserComplaintActivity.this);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        dialog.dismiss();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitMediaComplaint(String complaintID, ProgressDialog dialog) {
        for (MultipleImage multipleImage : imagebase64) {
            submitMediaData(complaintID, multipleImage, dialog);
        }
    }

    private void submitMediaData(final String complaintID, MultipleImage multipleImage, final ProgressDialog dialog) {

        try {
            JSONObject object = new JSONObject();
            object.put("mediaComplaintID", complaintID);
            object.put("mediaData", multipleImage.getImage());
            object.put("mediaType", "0");
            StringEntity entity = new StringEntity(object.toString());

            Constants.getClient().post(this, Constants.API_URL + Constants.COMPLAINT_MEDIA, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                            count++;
                            if (count == imagebase64.size()) {
                                dialog.dismiss();
                                showDialog("Your Power Theft Complaint was registered successfully. Your Complaint ID is " + complaintID + ". We will mark the complaint to concerned AEE/Operations and update Status upon inspection");
                            }
                        } else {
                            dialog.dismiss();
                            DialogUtil.showDialogOK("Alert!", jsonObject.getString("Msg"), UserComplaintActivity.this);
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

    private boolean validate() {
        if (mImagePath.equalsIgnoreCase("")) {
            DialogUtil.showToast("Please Capture Image", this, false);
            return false;
        }

        if (lat == 0 || lng == 0) {
            if (gps == null) {
                gps = new GPSTracker(this);
                gps.startUsingGPS();
            }
            if (gps.canGetLocation()) {
                lat = gps.getLatitude();
                lng = gps.getLongitude();
                if (lat == 0 || lng == 0) {
                    DialogUtil.showDialogOK("Alert!", "Unable To Fetch Location,please try again!", this);
                    return false;
                } else {
                    getLatLng(true);
                }
            } else {
                DialogUtil.showDialogOK("Alert!", "Unable To Fetch Location,please try again!", this);
                return false;
            }
        }
       /* if (remark_edtVw.getText().toString().equalsIgnoreCase("")) {
            remark_edtVw.setError("Please Enter Remark");
            remark_edtVw.requestFocus();
            return false;
        }*/
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImagePath, bmOptions);
                bmOptions.inSampleSize = 10;
                bmOptions.inJustDecodeBounds = false;
                Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImagePath, bmOptions);
                bitmaps.add(0, photoReducedSizeBitmp);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photoReducedSizeBitmp.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                byte[] ba = baos.toByteArray();
                String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                realm.beginTransaction();
                MultipleImage multipleImage = realm.createObject(MultipleImage.class);
                multipleImage.setImage(ba1);
                imagebase64.add(multipleImage);
                realm.commitTransaction();
                adaptar.notifyDataSetChanged();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                if (!camera && bitmaps.size() == 1) {
                    mImagePath = "";
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Alert!");

                    builder.setMessage("Do you want to discard the changes.");

                    builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }

                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image. Please try again.", Toast.LENGTH_SHORT)
                        .show();
                if (!camera)
                    finish();
            }
        }
    }
}
