package io.github.sher1234.service.ui.e;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.EmployeeAdapter;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.FilterEmployees;
import io.github.sher1234.service.functions.Navigate;
import io.github.sher1234.service.functions.TaskEmployeeList;
import io.github.sher1234.service.model.base.Employee;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.response.Employees;
import io.github.sher1234.service.ui.e.fragment.Excel;
import io.github.sher1234.service.ui.h.EmployeeBoard;
import io.github.sher1234.service.util.Strings;

public class EmployeeList extends AppCompatActivity implements TaskEmployeeList.TaskUpdate,
        Toolbar.OnMenuItemClickListener, EmployeeAdapter.ItemClick, View.OnClickListener {

    private final Common common = new Common();
    private FloatingActionButton fab;
    private TaskEmployeeList taskEL;
    private FilterEmployees funFE;
    private boolean exit = false;
    private Navigate funN;

    private AppCompatTextView textViewTitle;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity);
        funN = new Navigate(this, 4);
        taskEL = new TaskEmployeeList(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.filter);
        toolbar.setOnMenuItemClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        textViewTitle = findViewById(R.id.textViewTitle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        funFE = new FilterEmployees(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.download);
        fab.setOnClickListener(this);
        onFragmentDetached();
        fetch(funFE.getQuery());
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

    public void onFragmentAttached() {
        toolbar.getMenu().findItem(R.id.refresh).setVisible(false).setEnabled(false);
        toolbar.getMenu().findItem(R.id.filter).setVisible(false).setEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.back);
        textViewTitle.setText(R.string.records);
        funN.onLockDrawer();
        fab.hide();
    }

    public void onFragmentDetached() {
        toolbar.getMenu().findItem(R.id.refresh).setVisible(true).setEnabled(true);
        toolbar.getMenu().findItem(R.id.filter).setVisible(true).setEnabled(true);
        toolbar.setNavigationIcon(R.drawable.menu);
        textViewTitle.setText(R.string.records);
        funN.onResetToolbar();
        funN.onUnlockDrawer();
        fab.show();
    }

    private void onRefresh(List<Employee> employees) {
        EmployeeAdapter employeeAdapter;
        if (recyclerView.getAdapter() != null) {
            employeeAdapter = (EmployeeAdapter) recyclerView.getAdapter();
            employeeAdapter.onUpdateEmployees(employees);
        } else {
            employeeAdapter = new EmployeeAdapter(this, employees);
            recyclerView.setAdapter(employeeAdapter);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.filter) {
            funFE.onToggleFilter();
            return true;
        } else if (menuItem.getItemId() == R.id.refresh) {
            fetch(funFE.getQuery());
            return true;
        }
        return false;
    }

    @Override
    public void onFetched(@Nullable Employees employees, int i) {
        common.dismissProgressDialog();
        if (employees != null) {
            Snackbar.make(recyclerView, employees.Message, Snackbar.LENGTH_SHORT).show();
            onRefresh(employees.Employees);
        } else {
            if (i == 306)
                Snackbar.make(recyclerView, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(recyclerView, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(recyclerView, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskEL.onNetworkError(this, this, funFE.getQuery());
        }
    }

    @Override
    public void fetch(Query query) {
        taskEL.onCallsRefresh(query, this);
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
    }

    @Override
    public void onItemClick(Employee employee, View v) {
        Intent intent = new Intent(this, EmployeeBoard.class);
        intent.putExtra(Strings.ExtraData, employee);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, new Excel())
                    .commit();
    }
}