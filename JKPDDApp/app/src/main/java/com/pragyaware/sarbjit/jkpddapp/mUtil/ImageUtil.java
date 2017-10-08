package com.pragyaware.sarbjit.jkpddapp.mUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sarbjit on 3/31/2017.
 */
public class ImageUtil {

    // Activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private static String mImageFileLocation = "";

    public static String takePhoto(Activity context) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile(context);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String authorities = context.getPackageName() + ".fileprovider";
        Uri photoURI = FileProvider.getUriForFile(context, authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        context.startActivityForResult(callCameraApplicationIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        return mImageFileLocation;
    }

    private static File createImageFile(Context context) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }


}
