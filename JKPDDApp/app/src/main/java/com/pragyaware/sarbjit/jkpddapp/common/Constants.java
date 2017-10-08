package com.pragyaware.sarbjit.jkpddapp.common;

/**
 * Created by C9 on 09/07/16.
 */
public class Constants {

    public static final int NORMAL_USER = 13;
    public static final int OFFICER = 14;

    public static final String PENDING_STATUS = "Pending";
    public static final String THEFT_DETECTED_STATUS = "Theft Detected";
    public static final String THEFT_NOT_DETECTED_STATUS = "Theft Not Detected";
    public static final String ALL = "All";


    public static final String COMPLETED_STATUS = "Completed";
    public static final String IN_PROGRESS_STATUS = "In progress";

    interface Preferences {
        String PREF_CURRENT_CENSUS_CODE = "PREF_CURRENT_CENSUS_CODE";
        String PREF_CURRENT_VILLAGE_NAME = "PREF_CURRENT_VILLAGE_NAME";

        String PREF_CURRENT_BILL_SUB_DIVISION = "PREF_CURRENT_BILL_SUB_DIVISION";
        String PREF_CURRENT_LEDGER_CODE = "PREF_CURRENT_LEDGER_CODE";
        String PREF_FORCE_TO_CURRENT_LOC = "PREF_FORCE_TO_CURRENT_LOC";

        String PREF_CURRENT_USER_NAME = "PREF_CURRENT_USER_NAME";
        String PREF_CURRENT_USER_DISTRICT_ID = "PREF_CURRENT_USER_DIST_ID";
        String PREF_LOGIN_CONTACT_ID = "PREF_LOGIN_CONTACT_ID";

        String PREF_USER_EXIT_ASSET_MAPPING = "PREF_USER_EXIT_ASSET_MAPPING";
        String PREF_USER_EXIT_HH_MAPPING = "PREF_USER_EXIT_HH_MAPPING";
        String PREF_CURRENT_LANDMARK_ID = "PREF_CURRENT_LANDMARK_ID";
        String PREF_CURRENT_HABTATION_ID = "PREF_CURRENT_HABTATION_ID";
        String PREF_CURRENT_DT_ASSET_ID = "PREF_CURRENT_DT_ASSET_ID";

        String PREF_CURRENT_HT_ASSET_ID = "PREF_CURRENT_HT_ASSET_ID";
        String PREF_CURRENT_LT_ASSET_ID = "PREF_CURRENT_LT_ASSET_ID";

        String PREF_USER_IN_HT_ASSET = "PREF_USER_IN_HT_ASSET";
        String PREF_USER_IN_LT_ASSET = "PREF_USER_IN_LT_ASSET";
        String PREF_USER_IN_HH_ASSET = "PREF_USER_IN_HH_ASSET";

        String PREF_USER_HH_MAP_DONE = "PREF_USER_HH_MAP_DONE";

        String PREF_CURRENT_LATITUDE = "PREF_CURRENT_LATITUDE";
        String PREF_CURRENT_LONGITUDE = "PREF_CURRENT_LONGITUDE";
        String PREF_VILLAGE_SUMMARY_DONE = "PREF_VILLAGE_SUMMARY_DONE";

        String PREF_USER_ASSET_MAP_DONE = "PREF_USER_ASSET_MAP_DONE";

        //---- JKPDDAPP
        String PREF_USER_CONTACT_ID = "PREF_USER_CONTACT_ID";
        String PREF_LOGGED_IN = "PREF_LOGGED_IN";
        String PREF_USER_TYPE = "PREF_USER_TYPE";



    }

}
