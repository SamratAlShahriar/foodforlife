package tws.foodforlife.app.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPref {
    public static final String USER_DB = "db_user";

    private final String U_ID = "uid";
    private final String U_NAME = "u_name";
    private final String U_IMAGE_URL = "u_image_url";
    private final String U_EMAIL = "u_email";
    private final String U_PHONE = "u_phone";
    private final String U_DOB = "u_dob";
    private final String U_REG_NO = "u_reg_no";
    private final String U_TYPE = "u_type";
    private final String U_ADDRESS = "u_address";

    private SharedPreferences userPreference;

    public UserSharedPref(Context context) {
        userPreference = context.getSharedPreferences(USER_DB, Context.MODE_PRIVATE);
    }

    public void setU_ID(String id) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_ID, id);
        editor.apply();
    }

    public void clearU_ID() {
        if (userPreference.contains(U_ID)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_ID);
            editor.apply();
        }
    }

    public String getU_ID() {
        if (userPreference.contains(U_ID)) {
            return userPreference.getString(U_ID, "");
        }
        return null;
    }

    public void setU_NAME(String name) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_NAME, name);
        editor.apply();
    }

    public void clearU_NAME() {
        if (userPreference.contains(U_NAME)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_NAME);
            editor.apply();
        }
    }

    public String getU_NAME() {
        if (userPreference.contains(U_NAME)) {
            return userPreference.getString(U_NAME, "");
        }
        return null;
    }

    public void setU_IMAGE_URL(String imageUrl) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_IMAGE_URL, imageUrl);
        editor.apply();
    }

    public void clearU_IMAGE_URL() {
        if (userPreference.contains(U_IMAGE_URL)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_IMAGE_URL);
            editor.apply();
        }
    }

    public String getU_IMAGE_URL() {
        if (userPreference.contains(U_IMAGE_URL)) {
            return userPreference.getString(U_IMAGE_URL, "");
        }
        return null;
    }

    public void setU_EMAIL(String email) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_EMAIL, email);
        editor.apply();
    }

    public void clearU_EMAIL() {
        if (userPreference.contains(U_EMAIL)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_EMAIL);
            editor.apply();
        }
    }

    public String getU_EMAIL() {
        if (userPreference.contains(U_EMAIL)) {
            return userPreference.getString(U_EMAIL, "");
        }
        return null;
    }

    public void setU_PHONE(String phone) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_PHONE, phone);
        editor.apply();
    }

    public void clearU_PHONE() {
        if (userPreference.contains(U_PHONE)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_PHONE);
            editor.apply();
        }
    }

    public String getU_PHONE() {
        if (userPreference.contains(U_PHONE)) {
            return userPreference.getString(U_PHONE, "");
        }
        return null;
    }

    public void setU_DOB(String dob) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_DOB, dob);
        editor.apply();
    }

    public void clearU_DOB() {
        if (userPreference.contains(U_DOB)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_DOB);
            editor.apply();
        }
    }

    public String getU_DOB() {
        if (userPreference.contains(U_DOB)) {
            return userPreference.getString(U_DOB, "");
        }
        return null;
    }

    public void setU_REG_NO(String reg_no) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_REG_NO, reg_no);
        editor.apply();
    }

    public void clearU_REG_NO() {
        if (userPreference.contains(U_REG_NO)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_REG_NO);
            editor.apply();
        }
    }

    public String getU_REG_NO() {
        if (userPreference.contains(U_REG_NO)) {
            return userPreference.getString(U_REG_NO, "");
        }
        return null;
    }

    public void setU_ADDRESS(String address) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_ADDRESS, address);
        editor.apply();
    }

    public void clearU_ADDRESS() {
        if (userPreference.contains(U_ADDRESS)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_ADDRESS);
            editor.apply();
        }
    }

    public String getU_ADDRESS() {
        if (userPreference.contains(U_ADDRESS)) {
            return userPreference.getString(U_ADDRESS, "");
        }
        return null;
    }

    public void setU_TYPE(String type) {
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(U_TYPE, type);
        editor.apply();
    }

    public void clearU_TYPE() {
        if (userPreference.contains(U_TYPE)) {
            SharedPreferences.Editor editor = userPreference.edit();
            editor.remove(U_TYPE);
            editor.apply();
        }
    }

    public String getU_TYPE() {
        if (userPreference.contains(U_TYPE)) {
            return userPreference.getString(U_TYPE, "");
        }
        return null;
    }

    public void clearAll(){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.clear();
        editor.apply();
    }


}

