package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.Api;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.sher1234.service.activity.Login.F_TAG;

public class Start extends Fragment {
    static final String ARG_USER = "param-email";

    private CheckUserTask checkUserTask;

    private EditText mEmail;
    private View mProgressView;

    public Start() {

    }

    @NonNull
    public static Start newInstance() {
        return new Start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_start, container, false);
        mEmail = view.findViewById(R.id.textInputEditText);
        mProgressView = view.findViewById(R.id.progressView);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
                if (getContext() != null) {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert inputMethodManager != null;
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                onCheckUser();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onCheckUser() {
        if (checkUserTask != null)
            return;
        mEmail.setError(null);
        String email = mEmail.getText().toString();
        boolean cancel = false;
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }

        if (cancel)
            mEmail.requestFocus();
        else {
            showProgress(true);
            checkUserTask = new CheckUserTask(email);
            checkUserTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(@NotNull String email) {
        return email.contains("@");
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private User user = null;
        private io.github.sher1234.service.model.base.Response response;
        private StringBuilder A = new StringBuilder();

        CheckUserTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.CheckUserEmail(mEmail);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        A.append("Message: ").append(response.body().getResponse().getMessage()).append("\n");
                        A.append("Code: ").append(response.body().getResponse().getCode()).append("\n");
                        user = null;
                        CheckUserTask.this.response = response.body().getResponse();
                        if (response.body().getUsers().size() < 1)
                            return;
                        user = response.body().getUsers().get(0);
                    } else {
                        CheckUserTask.this.response = null;
                        user = null;
                        A.append(response.toString()).append("\n").append("Error Processing Request");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    user = null;
                    response = null;
                    A.append(t.getMessage());
                }
            });
            while (true)
                if (!A.toString().isEmpty())
                    return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            checkUserTask = null;
            showProgress(false);
            Log.e("Start/Response/A", A.toString());
            if (user != null && response != null) {
                if (response.getCode() == 1 && !user.getEmail().isEmpty()) {
                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    Fragment outFragment = getFragmentManager().findFragmentByTag(F_TAG);
                    Fragment fragment = Login.newInstance(user);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (outFragment != null)
                        fragmentTransaction.remove(outFragment);
                    fragmentTransaction.replace(R.id.frameLayout, fragment, F_TAG);
                    fragmentTransaction.addToBackStack(F_TAG).commit();
                } else {
                    Toast.makeText(getContext(), "Email Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Email Not Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            checkUserTask = null;
            showProgress(false);
        }
    }
}
