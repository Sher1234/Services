package io.github.sher1234.service.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.api.File;
import io.github.sher1234.service.fragment.Main1;
import io.github.sher1234.service.fragment.Main5;
import io.github.sher1234.service.model.base.User;

public class MainActivity extends AppCompatActivity {

    public static final String F_TAG = "FRAGMENT-TAG";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            fragment = new Main1();
        else
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, F_TAG);
        SharedPreferences preferences = getSharedPreferences(File.UserPreferences, MODE_PRIVATE);
        if (preferences.getBoolean("status", false)) {
            if (preferences.getBoolean("exists", false)) {
                Toast.makeText(this, "Logging in, Please wait", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this, StartUp.class));
                // finish();
            } else {
                Toast.makeText(this, "Complete Account Details", Toast.LENGTH_SHORT).show();
                fragment = Main5.newInstance(getUserFromPreferences(preferences));
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

    @Override
    protected void onStart() {
        super.onStart();
        permissionRequest();
    }

    private void permissionRequest() {
        String[] strings;
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            strings = new String[5];
            strings[0] = Manifest.permission.INTERNET;
            strings[1] = Manifest.permission.ACCESS_FINE_LOCATION;
            strings[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
            strings[3] = Manifest.permission.READ_EXTERNAL_STORAGE;
            strings[4] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        } else {
            strings = new String[4];
            strings[0] = Manifest.permission.INTERNET;
            strings[1] = Manifest.permission.ACCESS_FINE_LOCATION;
            strings[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
            strings[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, strings, 4869);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 4869) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Error", Toast.LENGTH_SHORT).show();
        }
    }


}