package io.github.sher1234.service.ui.v1.h;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskEmployee;
import io.github.sher1234.service.model.base.Employee;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.ui.v1.h.fragment.Board;
import io.github.sher1234.service.ui.v1.h.fragment.Calls;
import io.github.sher1234.service.ui.v1.h.fragment.Detail;
import io.github.sher1234.service.util.Strings;

public class EmployeeBoard extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, TaskEmployee.TaskUpdate {

    private final Common common = new Common();
    private Dashboard dashboard;
    private TaskEmployee taskE;

    private BottomNavigationView navView;
    private AppCompatTextView textViewTitle;
    private AppBarLayout appBarLayout;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity);
        employee = (Employee) getIntent().getSerializableExtra(Strings.ExtraData);
        if (employee == null) {
            Toast.makeText(this, "Undefined error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(this);
        textViewTitle = findViewById(R.id.textViewTitle);
        appBarLayout = findViewById(R.id.appBarLayout);
        taskE = new TaskEmployee();
        fetch();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info)
            return onFragmentSelected(Detail.getInstance(dashboard));
        if (item.getItemId() == R.id.dashboard)
            return onFragmentSelected(Board.getInstance(dashboard));
        if (item.getItemId() == R.id.calls)
            return onFragmentSelected(Calls.getInstance(dashboard));
        return false;
    }

    private boolean onFragmentSelected(@NotNull Fragment fragment) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f != null) getSupportFragmentManager().beginTransaction().remove(f).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).commit();
        return true;
    }

    private void onDataRefresh(Dashboard dashboard) {
        this.dashboard = dashboard;
        navView.setSelectedItemId(R.id.info);
    }

    public void onFragmentAttached(boolean b, @StringRes int s) {
        appBarLayout.setLiftOnScroll(b);
        textViewTitle.setText(s);
    }

    public void onFragmentDetached() {
        appBarLayout.setLiftOnScroll(false);
        textViewTitle.setText(null);
    }

    @Override
    public void onFetched(@Nullable Dashboard dashboard, int i) {
        common.dismissProgressDialog();
        if (dashboard != null) {
            Snackbar.make(textViewTitle, dashboard.message, Snackbar.LENGTH_SHORT).show();
            onDataRefresh(dashboard);
        } else {
            if (i == 306)
                Snackbar.make(textViewTitle, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(textViewTitle, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(textViewTitle, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskE.onNetworkError(this, this, employee.UserID);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
    }

    @Override
    public void fetch() {
        taskE.onRefreshBoard(this, employee.UserID);
    }
}