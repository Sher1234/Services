package io.github.sher1234.service.ui.v1.g;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Navigate;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.ui.v1.a.Splash;
import io.github.sher1234.service.ui.v1.g.fragment.ChangePassword;
import io.github.sher1234.service.ui.v1.g.fragment.EditProfile;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewTitle, text1, text2, text3, text4, text5;
    private Toolbar toolbar;

    private boolean exit = false;
    private Navigate funN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity);
        toolbar = findViewById(R.id.toolbar);
        funN = new Navigate(this, 6);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        textViewTitle = findViewById(R.id.textViewTitle);
        text5 = findViewById(R.id.text5);
        text4 = findViewById(R.id.text4);
        text3 = findViewById(R.id.text3);
        text2 = findViewById(R.id.text2);
        text1 = findViewById(R.id.text1);
        onFragmentDetached();
    }

    @Override
    protected void onResume() {
        super.onResume();
        funN.onResumeActivity();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (exit) {
            super.onBackPressed();
            return;
        }
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        else {
            exit = true;
            Snackbar.make(textViewTitle, "Press back again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    public void onFragmentAttached(@StringRes int s) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back);
        textViewTitle.setText(s);
        funN.onLockDrawer();
    }

    public void onFragmentDetached() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        textViewTitle.setText(R.string.profile);
        funN.onUnlockDrawer();
        funN.onResetToolbar();
        onRefresh();
    }

    private void onRefresh() {
        User user = new TaskUser().getUser();
        if (user != null) {
            if (user.isAdmin())
                text2.setVisibility(View.VISIBLE);
            text5.setText(user.EmployeeID);
            text4.setText(user.Phone);
            text3.setText(user.Email);
            text1.setText(user.Name);
        } else {
            String s = "null";
            text5.setText(s);
            text4.setText(s);
            text3.setText(s);
            text1.setText(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, new EditProfile())
                    .commit();
        } else if (v.getId() == R.id.button2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, new ChangePassword())
                    .commit();
        } else if (v.getId() == R.id.button3) {
            new TaskUser().onLogout();
            startActivity(new Intent(this, Splash.class));
            finish();
        }
    }
}