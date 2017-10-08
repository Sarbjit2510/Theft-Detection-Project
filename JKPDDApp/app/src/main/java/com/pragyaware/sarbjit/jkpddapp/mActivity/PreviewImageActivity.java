package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.squareup.picasso.Picasso;

public class PreviewImageActivity extends AppCompatActivity {

    ImageView previewImgVw;
    String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        previewImgVw = (ImageView) findViewById(R.id.previewImgVw);

        mId = getIntent().getStringExtra("data");

        String Url = Constants.IMG_URL + mId + "|200|200|80";

        Picasso.with(this).load(Url).into(previewImgVw);


    }
}
