package io.github.sher1234.service.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.fragment.Start1;
import io.github.sher1234.service.fragment.Start5;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.NavigationHost;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.UserPreferences;

public class StartActivity extends AppCompatActivity implements NavigationHost, UserPreferences {

    private static final String F_TAG = "FRAGMENT-TAG-START";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (savedInstanceState == null)
            fragment = new Start1();
        else
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, F_TAG);
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        if (preferences.getBoolean("status", false)) {
            if (preferences.getBoolean("exists", false)) {
                Toast.makeText(this, "Logging in, Please wait", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Complete Account Details", Toast.LENGTH_SHORT).show();
                fragment = Start5.newInstance(getUserFromPreferences(preferences));
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left,
                R.anim.enter_left, R.anim.exit_right);
        transaction.add(R.id.frameLayout, fragment, F_TAG).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragment = getSupportFragmentManager().findFragmentByTag(F_TAG);
        if (fragment != null)
            getSupportFragmentManager().putFragment(outState, F_TAG, fragment);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        else
            super.onBackPressed();
    }

    private User getUserFromPreferences(@NotNull SharedPreferences preferences) {
        User user = new User();
        user.setName(preferences.getString("Name", null));
        user.setEmail(preferences.getString("Email", null));
        user.setPhone(preferences.getString("Phone", null));
        user.setAdmin(preferences.getBoolean("IsAdmin", false));
        user.setPassword(preferences.getString("Password", null));
        user.setEmployeeID(preferences.getString("EmployeeID", null));
        return user;
    }

    @Override
    public void navigateTo(@NotNull Fragment fragment, boolean isBackStacked) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left,
                                R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.frameLayout, fragment, F_TAG);
        if (isBackStacked)
            transaction.addToBackStack(F_TAG);
        transaction.commit();
    }

    @Override
    @SuppressLint("CommitPrefEdits")
    public void updateUserPreferences(@NotNull User user) {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("status", true);
        editor.putString("Email", user.getEmail());
        editor.putString("Password", user.getPassword());
        editor.putString("Name", user.getName());
        editor.putString("Phone", user.getPhone());
        editor.putBoolean("IsAdmin", user.isAdmin());
        editor.putString("EmployeeID", user.getEmployeeID());
        editor.putBoolean("exists", user.isExists());
        editor.apply();
    }
}