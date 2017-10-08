package com.pragyaware.sarbjit.jkpddapp.mUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by sarbjit on 05/13/2017.
 */
public class Constants {
    public static final String API_URL = "http://recpdcl.pragyaware.com/WS/i/";
    public static final String IMG_URL = "http://recpdcl.pragyaware.com/i/getIMGBIN?q=";
    public static final String LOGIN = "ClientLogin";
    public static final String REGISTER_COMPLAINT = "ComplaintSave";
    public static final String COMPLAINT_MEDIA = "ComplaintMedia";
    public static final String USER_COMPLAINT_LIST = "ComplaintList?";
    public static final String OFFICER_LOGIN = "OfficerLogin";
    public static final String OFFICER_COMPLAINT_LIST = "ComplaintOfficerList?";
    public static final String UPDATE_STATUS = "ComplaintUpdate";
    public static final String CHANGE_PASSWORD = "OfficerChangePass";

    public static AsyncHttpClient getClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(20000);
        client.setTimeout(40000);
        client.setMaxRetriesAndTimeout(1, 5000);
        return client;
    }

    public static SyncHttpClient getSyncClient() {
        SyncHttpClient client = new SyncHttpClient();
        client.setConnectTimeout(20000);
        client.setTimeout(40000);
        client.setMaxRetriesAndTimeout(1, 5000);
        return client;
    }

    // id|100|50|40

}
