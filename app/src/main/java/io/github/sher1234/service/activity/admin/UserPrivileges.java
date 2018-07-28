package io.github.sher1234.service.activity.admin;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.PrivilegeRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.UserChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserPrivileges extends AppCompatActivity implements UserChangeListener, View.OnLongClickListener, View.OnClickListener {

    private final Functions functions = new Functions();
    private RecyclerView recyclerView;
    private ChangeTask changeTask;
    private View mProgressView;
    private QueryTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.imageView2).setOnClickListener(this);
        findViewById(R.id.imageView1).setOnLongClickListener(this);
        findViewById(R.id.imageView2).setOnLongClickListener(this);
        mProgressView = findViewById(R.id.progressView);
        recyclerView = findViewById(R.id.recyclerView);
        onRefresh();
    }

    private void onRefresh() {
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
        Toast.makeText(this, "Loading User...", Toast.LENGTH_SHORT).show();
        PrivilegeRecycler privilegeRecycler = new PrivilegeRecycler(this, users);
        recyclerView.setAdapter(privilegeRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_dash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                onRefresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    private Query getQuery() {
        User user = AppController.getUserFromPrefs();
        String s = "SELECT * FROM UsersX WHERE Email <> '" + user.Email + "'";
        Query query = new Query();
        query.Table = "Users";
        query.Query = s;
        return query;
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
        changeTask = new ChangeTask(email);
        changeTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (changeTask != null)
            changeTask.cancel(true);
        if (task != null)
            task.cancel(true);
        task = null;
        changeTask = null;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.imageView1) {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (view.getId() == R.id.imageView2) {
            Toast.makeText(this, R.string.refresh, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView1)
            finish();
        if (view.getId() == R.id.imageView2)
            onRefresh();
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
            functions.showProgress(true, mProgressView);
            usersX = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.GetQueryUsers(query.getQueryMap());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null) {
                        usersX = response.body();
                        i = usersX.Code;
                    } else {
                        usersX = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    usersX = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
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
            functions.showProgress(false, mProgressView);
            if (usersX != null) {
                Toast.makeText(AppController.getInstance(), usersX.Message, Toast.LENGTH_SHORT).show();
                if (usersX.Code == 1)
                    loadItems(usersX.Users);
            } else if (i == 306)
                Toast.makeText(UserPrivileges.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(UserPrivileges.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(UserPrivileges.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ChangeTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String value;
        private final int type;
        private Responded responded;
        private int i = 4869;

        ChangeTask(String email, int value, int type) {
            this.type = type;
            this.email = email;
            this.value = value + "";
        }

        ChangeTask(String email) {
            this(email, 0, -1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, mProgressView);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call;
            if (type == 0)
                call = api.ChangeAccountPrivilege(email, value);
            else if (type == 1)
                call = api.ChangeAccountState(email, value);
            else
                call = api.DeleteAccount(email);
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    responded = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    responded = null;
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
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.Message, Toast.LENGTH_SHORT).show();
            } else if (i == 306)
                Toast.makeText(UserPrivileges.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(UserPrivileges.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(UserPrivileges.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
            onRefresh();
        }
    }
}