package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.AdminActivity;
import io.github.sher1234.service.activity.admin.UsersActivity;
import io.github.sher1234.service.adapter.RegisterRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.response.Registrations;
import io.github.sher1234.service.util.NavigationIconClickListener;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerDate;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallsActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private View mProgressView;

    private boolean exit = false;
    private boolean isAdmin;
    private String email;

    private QueryTask task;

    private FormBuilder formBuilder;

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


        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

        mProgressView = findViewById(R.id.progressView);
        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.navButton1).setOnClickListener(this);
        findViewById(R.id.navButton2).setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        RecyclerView recyclerView = findViewById(R.id.navRecyclerView);
        onCreateFilter(recyclerView);
        startTask();
    }

    private void onCreateFilter(RecyclerView recyclerView) {
        List<BaseFormElement> formItems = new ArrayList<>();
        BaseFormElement element1, element2;

        List<String> op1 = new ArrayList<>();
        op1.add("Complaint");
        op1.add("New Commissioning");

        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");
        op2.add("To be checked");

        List<String> op3 = new ArrayList<>();
        op3.add("All");
        op3.add("Opened");
        op3.add("Closed");

        element1 = FormElementTextSingleLine.createInstance().setTitle("Search")
                .setHint("Enter Search Term").setTag(486901);
        element2 = FormElementPickerDate.createInstance().setTitle("From Date")
                .setHint("Enter Date").setTag(486902);
        formItems.add(element1);
        formItems.add(element2);

        element1 = FormElementPickerDate.createInstance().setTitle("To Date")
                .setHint("Enter Date").setTag(486903);
        element2 = FormElementPickerSingle.createInstance().setTitle("Status")
                .setOptions(op3).setTag(486904).setValue("All");
        formItems.add(element1);
        formItems.add(element2);

        element1 = FormElementPickerSingle.createInstance().setTitle("Nature of Site")
                .setOptions(op1).setHint("Select One").setTag(486905);
        element2 = FormElementPickerSingle.createInstance().setTitle("Warranty")
                .setOptions(op2).setHint("Select One").setTag(486906);
        formItems.add(element1);
        formItems.add(element2);

        formBuilder = new FormBuilder(this, recyclerView);
        formBuilder.addFormElements(formItems);
    }

    private void startTask() {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        isAdmin = preferences.getBoolean("IsAdmin", false);
        if (isAdmin)
            findViewById(R.id.button0).setVisibility(View.VISIBLE);
        email = preferences.getString("Email", "");
        if (task != null)
            task.cancel(true);
        task = null;
        task = new QueryTask(onFilter());
        task.execute();
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
        Toast.makeText(this, "Loading Calls...", Toast.LENGTH_SHORT).show();
        RegisterRecycler registerRecycler = new RegisterRecycler(this, registrations);
        recyclerView.setAdapter(registerRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTask();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                if (isAdmin)
                    startActivity(new Intent(this, DashboardActivity.class));
                else
                    startActivity(new Intent(this, AdminActivity.class));
                finish();
                break;

            case R.id.button0:
                startActivity(new Intent(this, UsersActivity.class));
                finish();
                break;

            case R.id.button3:
                startActivity(new Intent(this, RegCallActivity.class));
                break;

            case R.id.button4:
                startActivity(new Intent(this, AccountActivity.class));
                finish();
                break;

            case R.id.button5:
                //About App
                break;

            case R.id.button6:
                onLogoutUser();
                break;

            case R.id.navButton1:
                resetFormValue(486901, "");
                resetFormValue(486902, "");
                resetFormValue(486903, "");
                resetFormValue(486905, "");
                resetFormValue(486906, "");
                resetFormValue(486904, "All");
                formBuilder.getFormAdapter().notifyDataSetChanged();
                break;

            case R.id.navButton2:
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(GravityCompat.END);
                startTask();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calls, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                startTask();
                return true;

            case R.id.menuFilter:
                DrawerLayout drawer = findViewById(R.id.drawerLayout);
                drawer.openDrawer(GravityCompat.END);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getFormValue(int i) {
        return formBuilder.getFormElement(i).getValue();
    }

    private void resetFormValue(int i, String s) {
        formBuilder.getFormElement(i).setValue(s);
    }

    @NonNull
    @SuppressLint("SimpleDateFormat")
    private Query onFilter() {
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateFormatServer);
        Query query = new Query();
        query.setQuery("SELECT * FROM RegisteredCallsX WHERE ");
        String search = getFormValue(486901);
        if (search != null && !search.isEmpty()) {
            i = 1;
            search = "(CustomerName LIKE '%" + search + "%'" +
                    " OR ProductDetail LIKE '%" + search + "%'" +
                    " OR ComplaintType LIKE '%" + search + "%'" +
                    " OR SiteDetails LIKE '%" + search + "%'" +
                    " OR ConcernName LIKE '%" + search + "%')";
        } else {
            search = null;
        }

        String fromDateString = getFormValue(486902);
        String fromDate;
        if (fromDateString != null && !fromDateString.isEmpty()) {
            i = 1;
            fromDate = dateFormat.format
                    (((FormElementPickerDate) formBuilder.getFormElement(486902)).getDate());
            fromDateString = "DateTime >= '" + fromDate + "'";
        } else {
            fromDateString = null;
        }

        String toDateString = getFormValue(486903);
        String toDate;
        if (toDateString != null && !toDateString.isEmpty()) {
            i = 1;
            toDate = dateFormat.format
                    (((FormElementPickerDate) formBuilder.getFormElement(486903)).getDate());
            toDateString = "DateTime <= '" + toDate + "'";
        } else {
            toDateString = null;
        }

        String statusString = getFormValue(486904);
        if (statusString != null && !statusString.isEmpty()) {
            i = 1;
            if (statusString.equalsIgnoreCase("Opened"))
                statusString = "IsCompleted = '0'";
            else if (statusString.equalsIgnoreCase("Closed"))
                statusString = "IsCompleted = '1'";
            else
                statusString = "(IsCompleted = '0' OR IsCompleted = '1')";
        } else {
            statusString = "IsCompleted = '0'";
        }

        String siteNature = getFormValue(486905);
        if (siteNature != null && !siteNature.isEmpty()) {
            i = 1;
            siteNature = "NatureOfSite = '" + siteNature + "'";
        } else {
            siteNature = null;
        }

        String warrantyStatus = getFormValue(486906);
        if (warrantyStatus != null && !warrantyStatus.isEmpty()) {
            i = 1;
            warrantyStatus = "WarrantyStatus = '" + warrantyStatus + "'";
        } else {
            warrantyStatus = null;
        }

        if (i == 1) {
            if (search != null)
                query.setQuery(query.getQuery() + search + " AND ");
            if (fromDateString != null)
                query.setQuery(query.getQuery() + fromDateString + " AND ");
            if (toDateString != null)
                query.setQuery(query.getQuery() + toDateString + " AND ");
            query.setQuery(query.getQuery() + statusString + " AND ");
            if (siteNature != null)
                query.setQuery(query.getQuery() + siteNature + " AND ");
            if (warrantyStatus != null)
                query.setQuery(query.getQuery() + warrantyStatus);
            if (query.getQuery().endsWith(" AND "))
                query.setQuery(query.getQuery().substring(0, query.getQuery().length() - 5));
            query.setQuery(query.getQuery() + " AND Email = '" + email + "' ORDER BY IsCompleted " +
                    "ASC, DateTime ASC;");
        } else {
            query.setQuery("SELECT * FROM RegisteredCallsX WHERE Email = '" + email + "' ORDER " +
                    "BY IsCompleted ASC, DateTime ASC;");
        }
        query.setTable("Registrations");
        Log.e("log/query", query.getQuery());
        return query;
    }

    @SuppressLint("CommitPrefEdits")
    private void onLogoutUser() {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("status", false);
        editor.putBoolean("exists", false);
        editor.remove("EmployeeID");
        editor.remove("Password");
        editor.remove("IsAdmin");
        editor.remove("Email");
        editor.remove("Phone");
        editor.remove("Name");
        editor.apply();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = AppController.getInstance().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
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
            showProgress(true);
            registrations = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Registrations> call = api.GetRegistrationsUser(query.getQueryMap());
            call.enqueue(new Callback<Registrations>() {
                @Override
                public void onResponse(@NonNull Call<Registrations> call, @NonNull Response<Registrations> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        registrations = response.body();
                        i = registrations.getResponse().getCode();
                    } else {
                        registrations = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Registrations> call, @NonNull Throwable t) {
                    registrations = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
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
            showProgress(false);
            if (registrations != null) {
                Toast.makeText(AppController.getInstance(), registrations.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (registrations.getResponse().getCode() == 1)
                    loadItems(registrations.getRegistrations());
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}