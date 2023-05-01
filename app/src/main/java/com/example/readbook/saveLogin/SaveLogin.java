package com.example.readbook.saveLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveLogin {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SaveLogin(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public void save(String email, String name, String type) {
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("type", type);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public String getEmail() {
        return sp.getString("email", "");
    }

    public String getName() {
        return sp.getString("name", "");
    }

    public String getType() {
        return sp.getString("type", "");
    }
}
