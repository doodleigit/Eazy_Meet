package net.doodlei.android.eazymeet.utils;

import android.content.Context;
import android.content.SharedPreferences;

import net.doodlei.android.eazymeet.home.model.User;

public class PreferenceManager {

    private static PreferenceManager preferenceManager = null;
    private SharedPreferences preferences;

    private final String PREFERENCE_NAME = "user_info";
    private final String ID = "id";
    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String COUNTRY_CODE_ID = "country_code_id";
    private final String MOBILE = "mobile";
    private final String MEDIA_ID = "media_id";
    private final String IS_VERIFIED = "is_verified";
    private final String USER_IMAGE = "user_image";
    private final String COUNTRY_NAME = "country_name";
    private final String COUNTRY_ID = "country_id";
    private final String COUNTRY_PHONE_CODE = "country_phone_code";

    public static PreferenceManager newInstance(Context context) {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager(context);
        }
        return preferenceManager;
    }

    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setUserData(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ID, user.getId().toString());
        editor.putString(FIRST_NAME, user.getFirstName());
        editor.putString(LAST_NAME, user.getLastName());
        editor.putString(COUNTRY_CODE_ID, user.getCountryCodeId().toString());
        editor.putString(MOBILE, user.getMobile());
        editor.putString(MEDIA_ID, user.getMediaId().toString());
        editor.putString(IS_VERIFIED, user.getIsVerified().toString());
        editor.putString(USER_IMAGE, user.getUserImage());
        editor.putString(COUNTRY_NAME, user.getCountryName());
        editor.putString(COUNTRY_ID, user.getCountryId().toString());
        editor.putString(COUNTRY_PHONE_CODE, user.getCountryPhoneCode().toString());
        editor.apply();
    }

    public User getUserData() {
        User user = new User();
        user.setId(Integer.parseInt(preferences.getString(ID, "")));
        user.setFirstName(preferences.getString(FIRST_NAME, ""));
        user.setLastName(preferences.getString(LAST_NAME, ""));
        user.setCountryCodeId(Integer.parseInt(preferences.getString(COUNTRY_CODE_ID, "")));
        user.setMobile(preferences.getString(MOBILE, ""));
        user.setMediaId(Integer.parseInt(preferences.getString(MEDIA_ID, "")));
        user.setIsVerified(Integer.parseInt(preferences.getString(IS_VERIFIED, "")));
        user.setUserImage(preferences.getString(USER_IMAGE, ""));
        user.setCountryName(preferences.getString(COUNTRY_NAME, ""));
        user.setCountryId(Integer.parseInt(preferences.getString(COUNTRY_ID, "")));
        user.setCountryPhoneCode(Integer.parseInt(preferences.getString(COUNTRY_PHONE_CODE, "")));
        return user;
    }

    private void setData(String key, String data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public String getID() {
        return preferences.getString(ID, "");
    }

    public void setID(String ID) {
        setData(this.ID, ID);
    }

    public String getFIRST_NAME() {
        return preferences.getString(FIRST_NAME, "");
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        setData(this.FIRST_NAME, FIRST_NAME);
    }

    public String getLAST_NAME() {
        return preferences.getString(LAST_NAME, "");
    }

    public void setLAST_NAME(String LAST_NAME) {
        setData(this.LAST_NAME, LAST_NAME);
    }

    public String getCOUNTRY_CODE_ID() {
        return preferences.getString(COUNTRY_CODE_ID, "");
    }

    public void setCOUNTRY_CODE_ID(String COUNTRY_CODE_ID) {
        setData(this.COUNTRY_CODE_ID, COUNTRY_CODE_ID);
    }

    public String getMOBILE() {
        return preferences.getString(MOBILE, "");
    }

    public void setMOBILE(String MOBILE) {
        setData(this.MOBILE, MOBILE);
    }

    public String getMEDIA_ID() {
        return preferences.getString(MEDIA_ID, "");
    }

    public void setMEDIA_ID(String MEDIA_ID) {
        setData(this.MEDIA_ID, MEDIA_ID);
    }

    public String getIS_VERIFIED() {
        return preferences.getString(IS_VERIFIED, "");
    }

    public void setIS_VERIFIED(String IS_VERIFIED) {
        setData(this.IS_VERIFIED, IS_VERIFIED);
    }

    public String getUSER_IMAGE() {
        return preferences.getString(USER_IMAGE, "");
    }

    public void setUSER_IMAGE(String USER_IMAGE) {
        setData(this.USER_IMAGE, USER_IMAGE);
    }

    public String getCOUNTRY_NAME() {
        return preferences.getString(COUNTRY_NAME, "");
    }

    public void setCOUNTRY_NAME(String COUNTRY_NAME) {
        setData(this.COUNTRY_NAME, COUNTRY_NAME);
    }

    public String getCOUNTRY_ID() {
        return preferences.getString(COUNTRY_ID, "");
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        setData(this.COUNTRY_ID, COUNTRY_ID);
    }

    public String getCOUNTRY_PHONE_CODE() {
        return preferences.getString(COUNTRY_PHONE_CODE, "");
    }

    public void setCOUNTRY_PHONE_CODE(String COUNTRY_PHONE_CODE) {
        setData(this.COUNTRY_PHONE_CODE, COUNTRY_PHONE_CODE);
    }
}
