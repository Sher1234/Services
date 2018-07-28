package io.github.sher1234.service.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.EmployeeRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.FilterEmployees;
import io.github.sher1234.service.model.base.Employee;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.response.Employeez;
import io.github.sher1234.service.util.BottomMenuListener;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.ResultListener;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Employees extends AppCompatActivity
        implements NavigateActivity, ResultListener, Toolbar.OnMenuItemClickListener {

    private final Functions functions = new Functions();
    private FilterEmployees filterEmployees;
    private RecyclerView recyclerView;
    private boolean exit = false;
    private View mProgressView;
    private QueryTask task;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(
                new BottomMenuListener(getSupportFragmentManager(), 2));
        findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
        bottomAppBar.setOnMenuItemClickListener(this);
        bottomAppBar.replaceMenu(R.menu.calls);
        bottomAppBar.setFabAttached(false);

        filterEmployees = FilterEmployees.newInstance();
        mProgressView = findViewById(R.id.progressView);
        recyclerView = findViewById(R.id.recyclerView);
        onResultChange(null, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }
        exit = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit = false;
            }
        }, 1500);
    }

    private void onDataSetUpdate(List<Employee> users) {
        Toast.makeText(this, "Loading employees...", Toast.LENGTH_SHORT).show();
        EmployeeRecycler employeeRecycler;
        if (recyclerView.getAdapter() != null) {
            employeeRecycler = (EmployeeRecycler) recyclerView.getAdapter();
            employeeRecycler.setUsers(users);
            employeeRecycler.notifyDataSetChanged();
            return;
        }
        employeeRecycler = new EmployeeRecycler(this, users);
        recyclerView.setAdapter(employeeRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void navigateToActivity(@NotNull Class c, boolean finish) {
        startActivity(new Intent(this, c));
        if (finish)
            finish();
    }

    @Override
    public void onResultChange(@Nullable Query query, boolean resetQuery) {
        if (resetQuery) {
            String regsT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE U.Email = R.Email OR " +
                    "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = U.EmployeeID";
            String visit = "SELECT COUNT(*) FROM VisitedCallsX V WHERE U.Email = V.Email OR " +
                    "U.EmployeeID = V.Email";
            String pendT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE (U.Email = R.Email OR " +
                    "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = " +
                    "U.EmployeeID) AND R.IsCompleted = '0'";
            this.query = new Query();
            this.query.Query = "SELECT Email, Name, Phone, EmployeeID, (" +
                    regsT + ") as TotalR, (" +
                    visit + ") as TotalV, (" +
                    pendT + ") as TotalP  FROM UsersX U WHERE IsRegistered = 1 AND IsAdmin = 0 " +
                    "ORDER BY Name";
            this.query.Table = "Employees";
        } else if (query != null)
            this.query = query;
        if (task != null)
            task.cancel(true);
        task = null;
        task = new QueryTask(this.query);
        task.execute();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.filter) {
            filterEmployees.showNow(getSupportFragmentManager(), Strings.TAG + "F.E");
            return true;
        } else if (menuItem.getItemId() == R.id.refresh) {
            onResultChange(null, false);
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private final Query query;
        private Employeez employeez;
        private int i = 4869;

        QueryTask(Query query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, mProgressView);
            employeez = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Employeez> call = api.GetQueryEmployees(query.getQueryMap());
            call.enqueue(new Callback<Employeez>() {
                @Override
                public void onResponse(@NonNull Call<Employeez> call, @NonNull Response<Employeez> response) {
                    if (response.body() != null) {
                        employeez = response.body();
                        i = employeez.Code;
                    } else {
                        employeez = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Employeez> call, @NonNull Throwable t) {
                    employeez = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    employeez = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, mProgressView);
            if (employeez != null) {
                Toast.makeText(AppController.getInstance(), employeez.Message, Toast.LENGTH_SHORT).show();
                if (employeez.Code == 1)
                    onDataSetUpdate(employeez.Employees);
            } else if (i == 306)
                Toast.makeText(Employees.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(Employees.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(Employees.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}