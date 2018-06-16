package io.github.sher1234.service.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.fragment.Main1;
import io.github.sher1234.service.util.NavigationHost;
import io.github.sher1234.service.util.NavigationIconClickListener;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    private Fragment fragment;
    private static final String F_TAG = "TAG-FRAGMENT-MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(this,
                findViewById(R.id.coordinatorLayout),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu),
                getResources().getDrawable(R.drawable.ic_close)));
        if (savedInstanceState == null)
            fragment = new Main1();
        else
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, F_TAG);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,
                R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                .add(R.id.frameLayout, fragment, F_TAG).commit();
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
}