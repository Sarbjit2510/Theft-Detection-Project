package com.pragyaware.sarbjit.jkpddapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by DalbirSingh on 17-07-2016.
 */
public class PreferenceUtil {

    private static final String USERNAME = "userName";
    private static final String DESIGNATION = "designation";
    private static final String DISTRICT = "district";
    @SuppressLint("StaticFieldLeak")
    private static PreferenceUtil instance;
    private SharedPreferences prefs;
    private Context appContext;

    public static PreferenceUtil getInstance(Context appContext) {
        if (instance == null) {
            instance = new PreferenceUtil();
            instance.appContext = appContext;

        }
        if (instance.prefs == null) {
            instance.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        }

        return instance;
    }

    private SharedPreferences.Editor getEditor() {
        if (prefs == null) {
            instance.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        }
        return prefs.edit();
    }


    public boolean isLoggedIn() {
        return instance.prefs.getBoolean(Constants.Preferences.PREF_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isForce) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(Constants.Preferences.PREF_LOGGED_IN, isForce); // value to store
        editor.commit();
    }

    private int getUserType() {
        return instance.prefs.getInt(Constants.Preferences.PREF_USER_TYPE, 0);
    }

    public void setUserType(int userType) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(Constants.Preferences.PREF_USER_TYPE, userType); // value to store
        editor.commit();
    }

    private void removePrefs() {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(Constants.Preferences.PREF_CURRENT_CENSUS_CODE);
        editor.remove(Constants.Preferences.PREF_CURRENT_BILL_SUB_DIVISION);
        editor.remove(Constants.Preferences.PREF_CURRENT_LEDGER_CODE);
        editor.remove(Constants.Preferences.PREF_FORCE_TO_CURRENT_LOC);
        editor.remove(Constants.Preferences.PREF_USER_EXIT_ASSET_MAPPING);
        editor.remove(Constants.Preferences.PREF_USER_EXIT_HH_MAPPING);
        editor.remove(Constants.Preferences.PREF_CURRENT_LANDMARK_ID);
        editor.remove(Constants.Preferences.PREF_CURRENT_HABTATION_ID);
        editor.remove(Constants.Preferences.PREF_CURRENT_DT_ASSET_ID);
        editor.remove(Constants.Preferences.PREF_CURRENT_HT_ASSET_ID);
        editor.remove(Constants.Preferences.PREF_CURRENT_LT_ASSET_ID);
        editor.remove(Constants.Preferences.PREF_USER_IN_HT_ASSET);
        editor.remove(Constants.Preferences.PREF_USER_IN_LT_ASSET);
        editor.remove(Constants.Preferences.PREF_USER_IN_HH_ASSET);
        editor.remove(Constants.Preferences.PREF_USER_HH_MAP_DONE);

        editor.remove(Constants.Preferences.PREF_CURRENT_LATITUDE);
        editor.remove(Constants.Preferences.PREF_CURRENT_LONGITUDE);
        editor.remove(Constants.Preferences.PREF_CURRENT_VILLAGE_NAME);
        editor.remove(Constants.Preferences.PREF_VILLAGE_SUMMARY_DONE);
        editor.remove(Constants.Preferences.PREF_USER_ASSET_MAP_DONE);
        editor.remove(Constants.Preferences.PREF_USER_CONTACT_ID);
        editor.commit();
    }

    public void logoutPref() {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(Constants.Preferences.PREF_CURRENT_USER_DISTRICT_ID);
        editor.remove(Constants.Preferences.PREF_CURRENT_USER_NAME);
        editor.remove(Constants.Preferences.PREF_LOGIN_CONTACT_ID);
        editor.commit();
        removePrefs();
    }


    public String getUserContactId() {
        return instance.prefs.getString(Constants.Preferences.PREF_LOGIN_CONTACT_ID, "0");

    }

    public void setUserContactId(String contactId) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(Constants.Preferences.PREF_LOGIN_CONTACT_ID, contactId); // value to store
        editor.commit();
    }

    public String getUsername() {
        return instance.prefs.getString(USERNAME, "");
    }

    public void setUsername(String name) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(USERNAME, name); // value to store
        editor.commit();
    }

    public String getDesignation(){
        return instance.prefs.getString(DESIGNATION, "");
    }

    public void setDesignation(String designation){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(DESIGNATION, designation); // value to store
        editor.commit();
    }

    public void setDistrict(String district){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(DISTRICT, district); // value to store
        editor.commit();
    }

    public String getDistrict(){
        return instance.prefs.getString(DISTRICT, "");
    }

    public boolean isOfficer() {
        int userType = getUserType();
        return userType == Constants.OFFICER;
    }

}
