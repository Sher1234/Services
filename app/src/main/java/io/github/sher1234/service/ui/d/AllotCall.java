package io.github.sher1234.service.ui.d;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.AllotAdapter;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskCall;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.Strings;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllotCall extends AppCompatActivity
        implements View.OnClickListener, TaskCall.TaskUpdate, AllotAdapter.ItemClick, TextWatcher {

    private final Common common = new Common();
    private TaskCall taskCall;
    private QueryTask task;

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private MaterialButton button;
    private List<User> users;
    private String call;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_2);
        Call call = (Call) getIntent().getSerializableExtra(Strings.ExtraData);
        if (call == null) {
            Toast.makeText(this, "Invalid Call ID", Toast.LENGTH_SHORT).show();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((AppCompatTextView) findViewById(R.id.textView1)).setText(call.CallID);
        TextInputEditText editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        editText.addTextChangedListener(this);
        textView = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        taskCall = new TaskCall();
        this.call = call.CallID;
        textView.setText(null);
        onReloadUsers();
        setResult(102);
    }

    private void onReloadUsers() {
        Query query = new Query();
        query.Query = "SELECT * FROM User WHERE IsRegistered = 1";
        query.Table = "Users";
        if (task != null) task.cancel(true);
        task = new QueryTask(query);
        task.execute();
    }

    private void onLoadUsers(List<User> users, boolean b) {
        AllotAdapter adapter = (AllotAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.onUpdateUsers(users);
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter = new AllotAdapter(this, users);
            recyclerView.setAdapter(adapter);
        }
        if (b) this.users = users;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) fetch();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(recyclerView, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) taskCall.onCallAllotted(this);
        } else {
            if (i == 306)
                Snackbar.make(recyclerView, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(recyclerView, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(recyclerView, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskCall.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
        setResult(100);
    }

    @Override
    public void fetch() {
        if (user != null) taskCall.onAllotCall(this, call, user.UserID);
        else Snackbar.make(recyclerView, "Invalid User", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(User user, View view) {
        String s = user.Name + "\n" + user.Email;
        button.setEnabled(true);
        textView.setText(s);
        this.user = user;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence c, int start, int before, int count) {
        String s = String.valueOf(c).toLowerCase();
        List<User> uL = new ArrayList<>();
        for (User u : this.users)
            if (u.Name.toLowerCase().contains(s) || u.EmployeeID.toLowerCase().contains(s))
                uL.add(u);
        onLoadUsers(uL, false);
        button.setEnabled(false);
        textView.setText(null);
        user = null;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @SuppressLint("StaticFieldLeak")
    private class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private final Query query;
        private int i = 4869;
        private Users users;

        QueryTask(Query query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            users = null;
            onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            retrofit2.Call<Users> call = api.getQueryUsers(query.getMap());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null) {
                        users = response.body();
                        i = users.Code;
                    } else {
                        users = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Users> call, @NonNull Throwable t) {
                    users = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
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
            common.dismissProgressDialog();
            if (users != null) {
                Snackbar snackbar = Snackbar.make(recyclerView, users.Message, Snackbar.LENGTH_SHORT);
                if (users.Code == 1) onLoadUsers(users.Users, true);
                else snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReloadUsers();
                    }
                }).setDuration(BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            } else if (i == 306)
                Snackbar.make(recyclerView, "Content parse error", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReloadUsers();
                    }
                }).show();
            else if (i == 307)
                Snackbar.make(recyclerView, "Network failure", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReloadUsers();
                    }
                }).show();
            else if (i == 308)
                Snackbar.make(recyclerView, "Request cancelled", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReloadUsers();
                    }
                }).show();
        }
    }
}