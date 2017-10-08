package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.mAdaptar.UserComAdaptar;

import java.util.ArrayList;
import java.util.Collections;

public class TheftActivity extends AppCompatActivity {

    String[] dataStrings, mediaImage, officerImageStrings;
    TextView tv_compId, tv_compDate, tv_compAddress, tv_compStatus;
    TextView ed_name, /*ed_designation,*/
            ed_remarks, plentyt_edtVw;
    GridView complaint_grdVw, officer_grdVw;
    ImageView track_btn;
    LinearLayout officer_layout, officer_media_layout;
    ArrayList<String> strings, officerImage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft);

        tv_compId = (TextView) findViewById(R.id.tv_compId);
        tv_compDate = (TextView) findViewById(R.id.tv_compDate);
        tv_compAddress = (TextView) findViewById(R.id.tv_compAddress);
        tv_compStatus = (TextView) findViewById(R.id.tv_compStatus);
        ed_name = (TextView) findViewById(R.id.ed_name);

        ed_remarks = (TextView) findViewById(R.id.ed_remarks);
        plentyt_edtVw = (TextView) findViewById(R.id.plentyt_edtVw);
        complaint_grdVw = (GridView) findViewById(R.id.complaint_grdVw);
        officer_grdVw = (GridView) findViewById(R.id.officer_grdVw);
        track_btn = (ImageView) findViewById(R.id.track_btn);
        officer_layout = (LinearLayout) findViewById(R.id.officer_layout);
        officer_media_layout = (LinearLayout) findViewById(R.id.officer_media_layout);

        strings = new ArrayList<>();
        officerImage = new ArrayList<>();
        dataStrings = getIntent().getStringArrayExtra("data");
        tv_compId.setText(dataStrings[0]);
        tv_compAddress.setText(dataStrings[1]);
        tv_compDate.setText(dataStrings[9]);
        tv_compStatus.setText(dataStrings[10]);

        if (dataStrings[6].contains(",")) {
            mediaImage = dataStrings[6].split(",");
            Collections.addAll(strings, mediaImage);
        } else {
            strings.add(dataStrings[6]);
        }

        if (dataStrings[11].contains(",")) {
            officerImageStrings = dataStrings[11].split(",");
            Collections.addAll(officerImage, officerImageStrings);
            officer_grdVw.setAdapter(new UserComAdaptar(this, officerImage));
        } else {
            if (!dataStrings[11].equalsIgnoreCase("")){
                officer_media_layout.setVisibility(View.VISIBLE);
                officerImage.add(dataStrings[11]);
                officer_grdVw.setAdapter(new UserComAdaptar(this, officerImage));
            } else {
                officer_media_layout.setVisibility(View.GONE);
            }

        }

        if (dataStrings[10].equalsIgnoreCase("Pending")) {
            officer_layout.setVisibility(View.GONE);
        } else {
            officer_layout.setVisibility(View.VISIBLE);
        }

        complaint_grdVw.setAdapter(new UserComAdaptar(this, strings));
        ed_name.setText(getString(R.string.name) + " : " + dataStrings[7]);
//        ed_designation.setText("");
        ed_remarks.setText(getString(R.string.remarks) + " : " + dataStrings[8]);
        plentyt_edtVw.setText(getString(R.string.plenty_impose) + " : " + dataStrings[5]);

        complaint_grdVw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        officer_grdVw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        complaint_grdVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(TheftActivity.this, PreviewImageActivity.class).putExtra("data", strings.get(i)));
            }
        });

        officer_grdVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(TheftActivity.this, PreviewImageActivity.class).putExtra("data", officerImage.get(position)));
            }
        });

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = "com.google.android.apps.maps";
                String query = "google.navigation:q=" + dataStrings[1];

                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(query));
                startActivity(intent);
            }
        });

    }
}
