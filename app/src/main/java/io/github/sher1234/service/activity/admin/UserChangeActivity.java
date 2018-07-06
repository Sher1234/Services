package io.github.sher1234.service.activity.admin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.UsersRecyclerX;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.UserChangeClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserChangeActivity extends AppCompatActivity implements UserChangeClick {

    private RecyclerView recyclerView;
    private ChangeTask changeTask;
    private View mProgressView;
    private QueryTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_calls);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
        mProgressView = findViewById(R.id.progressView);
        recyclerView = findViewById(R.id.recyclerView);
        startTask();
    }

    private void startTask() {
        if (changeTask != null)
            changeTask.cancel(true);
        if (task != null)
            task.cancel(true);
        task = null;
        changeTask = null;
        task = new QueryTask(getQuery());
        task.execute();
    }

    private void loadItems(List<User> users) {
        Toast.makeText(this, "Loading Users...", Toast.LENGTH_SHORT).show();
        UsersRecyclerX usersRecycler = new UsersRecyclerX(this, users);
        recyclerView.setAdapter(usersRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                startTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    private Query getQuery() {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        String e = preferences.getString(Strings.Email, null);
        String s = "SELECT * FROM UsersX WHERE Email <> '" + e + "'";
        Query query = new Query();
        query.setQuery(s);
        query.setQuery(query.getQuery() + ";");
        query.setTable("Users");
        return query;
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
    public void onAdminChange(@NotNull String email, int value) {
        if (changeTask != null)
            changeTask.cancel(true);
        if (task != null)
            task.cancel(true);
        task = null;
        changeTask = null;
        changeTask = new ChangeTask(email, value, 0);
        changeTask.execute();
    }

    @Override
    public void onAccountStateChange(@NotNull String email, int value) {
        if (changeTask != null)
            changeTask.cancel(true);
        if (task != null)
            task.cancel(true);
        task = null;
        changeTask = null;
        changeTask = new ChangeTask(email, value, 1);
        changeTask.execute();
    }

    @Override
    public void onAccountDelete(@NotNull String email) {
        if (changeTask != null)
            changeTask.cancel(true);
        if (task != null)
            task.cancel(true);
        task = null;
        changeTask = null;
        changeTask = new ChangeTask(email, -1);
        changeTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private final Query query;
        private Users usersX;
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
            Call<Users> call = api.GetUsers(query.getQueryMap());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        usersX = response.body();
                        i = usersX.getResponse().getCode();
                    } else {
                        usersX = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
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

    @SuppressLint("StaticFieldLeak")
    class ChangeTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String value;
        private final int type;
        private int i = 4869;
        private Users users;

        ChangeTask(String email, int value, int type) {
            this.type = type;
            this.email = email;
            this.value = value + "";
        }

        ChangeTask(String email, int type) {
            this.type = type;
            this.value = null;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call;
            if (type == 0)
                call = api.ChangeAccountPrivilege(email, value);
            else if (type == 1)
                call = api.ChangeAccountState(email, value);
            else
                call = api.DeleteAccount(email);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        users = response.body();
                        i = users.getResponse().getCode();
                    } else {
                        users = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    users = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    users = null;
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
            if (users != null) {
                Toast.makeText(AppController.getInstance(), users.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again...", Toast.LENGTH_SHORT).show();
            }
            startTask();
        }
    }
}