package io.github.sher1234.service.functions.v4.user;

import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.firebase.model.user.Phone;
import io.github.sher1234.service.firebase.model.user.User;

class OnOfflineData {

    @NotNull private SharedPreferences preferences;

    OnOfflineData() {
        preferences = AppController.instance.getPrefs();
    }

    void onSetData(@NotNull User user) {
        preferences.edit().putString("phone_number", user.phone.number).apply();
        preferences.edit().putString("phone_code", user.phone.code).apply();
        preferences.edit().putLong("recent", user.recent.getTime()).apply();
        preferences.edit().putString("first_name", user.firstName).apply();
        preferences.edit().putString("last_name", user.lastName).apply();
        preferences.edit().putBoolean("enabled", user.enabled).apply();
        preferences.edit().putBoolean("admin", user.admin).apply();
        preferences.edit().putString("email", user.email).apply();
        preferences.edit().putString("eid", user.eid).apply();
        preferences.edit().putString("uid", user.uid).apply();
    }

    @NotNull
    User onGetData() {
        User user = new User();
        user.phone = new Phone();
        user.recent = new Date();
        user.recent.setTime(preferences.getLong("recent", new Date().getTime()));
        user.phone.number = preferences.getString("phone_number", null);
        user.phone.code = preferences.getString("phone_code", "+91");
        user.firstName = preferences.getString("first_name", null);
        user.lastName = preferences.getString("last_name", null);
        user.enabled = preferences.getBoolean("enabled", false);
        user.admin = preferences.getBoolean("admin", false);
        user.email = preferences.getString("email", null);
        user.eid = preferences.getString("eid", null);
        user.uid = preferences.getString("uid", null);
        return user;
    }
}