package io.github.sher1234.service.activity.admin;

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

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.AccountActivity;
import io.github.sher1234.service.activity.RegCallActivity;
import io.github.sher1234.service.activity.StartActivity;
import io.github.sher1234.service.adapter.UsersRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.UserX;
import io.github.sher1234.service.model.response.UsersX;
import io.github.sher1234.service.util.NavigationIconClickListener;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private View mProgressView;

    private boolean exit = false;

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

        findViewById(R.id.button0).setVisibility(View.GONE);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
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

        List<String> op = new ArrayList<>();
        op.add("Name");
        op.add("Pending Calls");
        op.add("Registrations");
        op.add("Visits");

        element1 = FormElementTextSingleLine.createInstance().setTitle("Search")
                .setHint("Enter Search Term").setTag(486901);
        element2 = FormElementPickerSingle.createInstance().setTitle("Sort by")
                .setOptions(op).setTag(486902);
        formItems.add(element1);
        formItems.add(element2);

        formBuilder = new FormBuilder(this, recyclerView);
        formBuilder.addFormElements(formItems);
    }

    private void startTask() {
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

    private void loadItems(List<UserX> users) {
        Toast.makeText(this, "Loading Calls...", Toast.LENGTH_SHORT).show();
        UsersRecycler usersRecycler = new UsersRecycler(this, users);
        recyclerView.setAdapter(usersRecycler);
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
                startActivity(new Intent(this, AdminActivity.class));
                finish();
                break;

            case R.id.button2:
                //CallsActivity
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
                resetFormValue(486901);
                resetFormValue(486902);
                formBuilder.getmFormAdapter().notifyDataSetChanged();
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

    private void resetFormValue(int i) {
        formBuilder.getFormElement(i).setValue("");
    }

    @NonNull
    @SuppressLint("SimpleDateFormat")
    private Query onFilter() {
        String s = "SELECT Email, Name, Phone, EmployeeID, " +
                "(SELECT COUNT(*) FROM RegisteredCallsX R WHERE (U.Email = R.Email)) as Regs, " +
                "(SELECT COUNT(*) FROM VisitedCallsX V WHERE (U.Email = V.Email)) as Visits, " +
                "(SELECT COUNT(*) FROM RegisteredCallsX R WHERE U.Email = R.Email AND R.IsCompleted = '0') as Pending " +
                "FROM UsersX U";
        Query query = new Query();
        query.setQuery(s);
        String search = getFormValue(486901);
        if (search != null && !search.isEmpty())
            search = " WHERE " +
                    "(Name LIKE '%" + search + "%'" +
                    " OR Email LIKE '%" + search + "%'" +
                    " OR EmployeeID LIKE '%" + search + "%')";
        else
            search = null;

        String sortBy = getFormValue(486902);
        String sort;
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("Name"))
                sort = " ORDER BY Name ASC";
            else if (sortBy.equalsIgnoreCase("Pending Calls"))
                sort = " ORDER BY Pending DESC";
            else if (sortBy.equalsIgnoreCase("Registrations"))
                sort = " ORDER BY Regs DESC";
            else if (sortBy.equalsIgnoreCase("Visits"))
                sort = " ORDER BY Visits DESC";
            else
                sort = null;
        } else
            sort = null;

        if (search != null && !search.isEmpty())
            query.setQuery(query.getQuery() + search);
        if (sort != null && !sort.isEmpty())
            query.setQuery(query.getQuery() + sort);

        query.setQuery(query.getQuery() + ";");
        query.setTable("Users");
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
        private UsersX usersX;
        private int i = 4869;

        QueryTask(Query query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            usersX = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<UsersX> call = api.GetUsersAdmin(query.getQueryMap());
            call.enqueue(new Callback<UsersX>() {
                @Override
                public void onResponse(@NonNull Call<UsersX> call, @NonNull Response<UsersX> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        usersX = response.body();
                        i = usersX.getResponse().getCode();
                    } else {
                        usersX = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UsersX> call, @NonNull Throwable t) {
                    usersX = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    usersX = null;
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
            if (usersX != null) {
                Toast.makeText(AppController.getInstance(), usersX.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (usersX.getResponse().getCode() == 1)
                    loadItems(usersX.getUsers());
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}