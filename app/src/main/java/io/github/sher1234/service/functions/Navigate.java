package io.github.sher1234.service.functions;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.ui.v1.a.Splash;
import io.github.sher1234.service.ui.v1.b.Board;
import io.github.sher1234.service.ui.v1.c.CallList;
import io.github.sher1234.service.ui.v1.d.AddCall;
import io.github.sher1234.service.ui.v1.e.EmployeeList;
import io.github.sher1234.service.ui.v1.f.Privileges;
import io.github.sher1234.service.ui.v1.g.Profile;

public class Navigate implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navView;
    private final Toolbar toolbar;
    private final User user;
    private final int i;

    public Navigate(@NotNull AppCompatActivity activity, int i) {
        this.drawerLayout = activity.findViewById(R.id.drawerLayout);
        this.navView = activity.findViewById(R.id.navigationView);
        this.toolbar = activity.findViewById(R.id.toolbar);
        user = new TaskUser().getUser();
        this.activity = activity;
        this.i = i;
        setNavigationView();
        onResumeActivity();
        onResetToolbar();
    }

    public void onResetToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navView))
                    drawerLayout.closeDrawer(navView);
                else
                    drawerLayout.openDrawer(navView);
            }
        });
    }

    public void onLockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void onUnlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                if (i != 1)
                    startActivity(Board.class, true);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.calls:
                if (i != 2)
                    startActivity(CallList.class, true);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.add:
                if (i != 3)
                    startActivity(AddCall.class, false);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.records:
                if (i != 4)
                    startActivity(EmployeeList.class, true);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.privileges:
                if (i != 5)
                    startActivity(Privileges.class, true);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.account:
                if (i != 6)
                    startActivity(Profile.class, true);
                drawerLayout.closeDrawer(navView);
                return true;

            case R.id.exit:
                new TaskUser().onLogout();
                drawerLayout.closeDrawer(navView);
                startActivity(Splash.class, true);
                return true;

            default:
                return false;
        }
    }

    private void startActivity(Class c, boolean exit) {
        activity.startActivity(new Intent(activity, c));
        if (exit)
            activity.finish();
    }

    private void setNavigationView() {
        AppCompatTextView textA = navView.getHeaderView(0).findViewById(R.id.textView1);
        AppCompatTextView textB = navView.getHeaderView(0).findViewById(R.id.textView2);
        textB.setText(user.Phone);
        textA.setText(user.Name);
        if (user.isAdmin())
            navView.inflateMenu(R.menu.admin);
        else
            navView.inflateMenu(R.menu.user);
        this.navView.setNavigationItemSelectedListener(this);
        onResumeActivity();
    }

    public void onResumeActivity() {
        switch (i) {
            case 1:
                navView.setCheckedItem(R.id.home);
                break;

            case 2:
                navView.setCheckedItem(R.id.calls);
                break;

            case 4:
                navView.setCheckedItem(R.id.records);
                break;

            case 5:
                navView.setCheckedItem(R.id.privileges);
                break;

            case 6:
                navView.setCheckedItem(R.id.account);
                break;
        }
    }
}