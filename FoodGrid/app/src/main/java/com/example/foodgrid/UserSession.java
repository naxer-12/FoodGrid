package com.example.foodgrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class UserSession {

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String SHARED_PREFERENCE_NAME = "User";
    public static final String USER_LOGIN_STATUS = "IsUserLoggedIn";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PASSWORD = "Password";

    public UserSession(Context context) {
        this.context = context;
        shared = context.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE);
        editor = shared.edit();
    }

    public void userLoginSession(String uEmail, String uPassword){
        editor.putBoolean(USER_LOGIN_STATUS, true);
        editor.putString(KEY_EMAIL, uEmail);
        editor.putString(KEY_PASSWORD, uPassword);

        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return shared.getBoolean(USER_LOGIN_STATUS, false);
    }

    public boolean checkLoginStatus(){
        if(!this.isUserLoggedIn()){
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }


    public void logout(){
        editor.clear();
        editor.commit();

        Intent i  = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
