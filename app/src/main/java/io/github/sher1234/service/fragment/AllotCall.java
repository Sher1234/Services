package io.github.sher1234.service.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.adapter.UserRecycler;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllotCall extends BottomSheetDialogFragment implements View.OnClickListener, TextWatcher {


    private final Functions functions = new Functions();
    private TextInputEditText editText;
    private RecyclerView recyclerView;
    private Registration registration;
    private MaterialButton button;
    private View progressView;
    private List<User> users;
    private AllotTask task;
    private View formView;
    private User user;

    public static AllotCall newInstance(Registration registration) {
        AllotCall allotCall = new AllotCall();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, registration);
        allotCall.setArguments(bundle);
        return allotCall;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        User user = new User();
        assert getArguments() != null;
        registration = (Registration) getArguments().getSerializable(Strings.ExtraData);
        user.EmployeeID = registration != null ? registration.Email : null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_allot, container, false);
        view.findViewById(R.id.imageView).setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressView = view.findViewById(R.id.progressView);
        formView = view.findViewById(R.id.formView);
        editText = view.findViewById(R.id.editText);
        button = view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        editText.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new QueryTask(getQuery()).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                dismiss();
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new AllotTask(registration.CallNumber, user.EmployeeID);
                task.execute();
                break;
        }
    }

    public void onClickItem(@NotNull User user) {
        editText.setText(user.Name);
        button.setEnabled(true);
        this.user = user;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @NonNull
    private Query getQuery() {
        Query query = new Query();
        query.Query = "SELECT * FROM UsersX WHERE IsRegistered = 1";
        query.Table = "Users";
        return query;
    }

    private void loadItems(@NotNull List<User> users) {
        this.users = new ArrayList<>();
        for (User user : users)
            if (user.isExists() && user.isRegistered())
                this.users.add(user);
        UserRecycler userRecycler = new UserRecycler(this, this.users);
        recyclerView.setAdapter(userRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateItems(List<User> users) {
        if (recyclerView.getAdapter() != null) {
            UserRecycler userRecycler = (UserRecycler) recyclerView.getAdapter();
            userRecycler.setUsers(users);
            userRecycler.notifyDataSetChanged();
            return;
        }
        UserRecycler userRecycler = new UserRecycler(this, users);
        recyclerView.setAdapter(userRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        List<User> users = new ArrayList<>();
        for (User user : this.users) {
            if (user.Name.toLowerCase().contains(c.toString().toLowerCase()) ||
                    user.EmployeeID.toLowerCase().contains(c.toString().toLowerCase()))
                users.add(user);
        }
        button.setEnabled(false);
        updateItems(users);
        this.user = null;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @SuppressLint("StaticFieldLeak")
    private class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private final Query query;
        private Users usersX;
        private int i = 4869;

        QueryTask(Query query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView, formView);
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
            functions.showProgress(false, progressView, formView);
            if (usersX != null) {
                Toast.makeText(AppController.getInstance(), usersX.Message, Toast.LENGTH_SHORT).show();
                if (usersX.Code == 1)
                    loadItems(usersX.Users);
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AllotTask extends AsyncTask<Void, Void, Boolean> {

        private final String cn;
        private final String at;
        private int i = 4869;
        private Responded responded;

        AllotTask(String callNumber, String allottedTo) {
            at = allottedTo;
            cn = callNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView, formView);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call = api.AllotCall(cn, at);
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
            functions.showProgress(false, progressView, formView);
            if (responded != null) {
                Toast.makeText(getContext(), responded.Message, Toast.LENGTH_SHORT).show();
                if (responded.Code == 1) {
                    assert getActivity() != null;
                    ((ViewCall) getActivity()).onTapRefresh();
                }
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}