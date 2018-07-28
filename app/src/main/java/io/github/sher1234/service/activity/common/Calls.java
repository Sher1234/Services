package io.github.sher1234.service.activity.common;

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
import io.github.sher1234.service.adapter.CallRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.FilterCalls;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Registrations;
import io.github.sher1234.service.util.BottomMenuListener;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.ResultListener;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Calls extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, NavigateActivity, ResultListener {

    private final Functions functions = new Functions();
    private RecyclerView recyclerView;
    private FilterCalls filterCalls;
    private boolean exit = false;
    private View mProgressView;
    private QueryTask task;
    private Query query;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        user = AppController.getUserFromPrefs();
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(
                new BottomMenuListener(getSupportFragmentManager(), 3));
        bottomAppBar.setOnMenuItemClickListener(this);
        bottomAppBar.replaceMenu(R.menu.calls);
        filterCalls = FilterCalls.newInstance();
        mProgressView = findViewById(R.id.progressView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onResultChange(null, true);
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToActivity(RegisterCall.class, false);
            }
        });
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

    private void loadItems(List<Registration> registrations) {
        Toast.makeText(this, "Loading calls...", Toast.LENGTH_SHORT).show();
        CallRecycler callRecycler;
        if (recyclerView.getAdapter() != null) {
            callRecycler = (CallRecycler) recyclerView.getAdapter();
            callRecycler.setRegistrations(registrations);
        } else {
            callRecycler = new CallRecycler(this, registrations);
            recyclerView.setAdapter(callRecycler);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.filter) {
            filterCalls.showNow(getSupportFragmentManager(), Strings.TAG + "F");
            return true;
        } else if (menuItem.getItemId() == R.id.refresh) {
            onResultChange(null, false);
            return true;
        }
        return false;
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
            this.query = new Query();
            if (user.isAdmin())
                this.query.Query = "SELECT * FROM RegisteredCallsX ORDER BY IsCompleted ASC, DateTime ASC";
            else
                this.query.Query = "SELECT * FROM RegisteredCallsX WHERE (Email = '" + user.Email +
                        "' OR Email = '" + user.EmployeeID + "' OR AllottedTo = '" + user.Email +
                        "' OR AllottedTo = '" + user.EmployeeID + "') ORDER BY IsCompleted ASC," +
                        " DateTime ASC";
            this.query.Table = "Registrations";
        } else if (query != null)
            this.query = query;
        if (task != null)
            task.cancel(true);
        task = null;
        task = new QueryTask(this.query);
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private final Query query;
        private Registrations registrations;
        private int i = 4869;

        QueryTask(Query query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registrations = null;
            functions.showProgress(true, mProgressView);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Registrations> call = api.GetQueryRegs(query.getQueryMap());
            call.enqueue(new Callback<Registrations>() {
                @Override
                public void onResponse(@NonNull Call<Registrations> call, @NonNull Response<Registrations> response) {
                    if (response.body() != null) {
                        registrations = response.body();
                        i = registrations.Code;
                    } else {
                        registrations = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Registrations> call, @NonNull Throwable t) {
                    registrations = null;
                    t.printStackTrace();
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    registrations = null;
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
            if (registrations != null) {
                Toast.makeText(AppController.getInstance(), registrations.Message, Toast.LENGTH_SHORT).show();
                if (registrations.Code == 1)
                    loadItems(registrations.Registrations);
            } else if (i == 306)
                Toast.makeText(Calls.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(Calls.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(Calls.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}