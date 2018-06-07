package io.github.sher1234.service.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.fragment.Start;

public class Login extends AppCompatActivity {

    public static final String F_TAG = "FRAGMENT-TAG";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null)
            fragment = Start.newInstance();
        else
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, F_TAG);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment, F_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
