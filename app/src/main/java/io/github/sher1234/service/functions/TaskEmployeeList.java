package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Employees;
import io.github.sher1234.service.util.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskEmployeeList {

    private Task task;

    public TaskEmployeeList(AppCompatActivity activity) {
        User user = new TaskUser().getUser();
        if (user == null || !user.isAdmin()) onAccountError(activity);
        task = null;
    }

    public void onCallsRefresh(Query query, TaskUpdate taskUpdate) {
        if (task != null) task.cancel(true);
        task = new Task(taskUpdate, query);
        task.execute();
    }

    public void onNetworkError(final FragmentActivity context, final TaskUpdate taskUpdate, final Query query) {
        MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Network issue")
                .setDescription(R.string.offline_access)
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        onCallsRefresh(query, taskUpdate);
                        dialog.dismiss();
                    }
                })
                .negativeButton("Exit", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        context.finish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    private void onAccountError(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Account error")
                .setDescription("Unknown error occurred, login again to access the application.")
                .positiveButton("Sign out", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        new TaskUser().onLogout();
                        activity.finish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    public interface TaskUpdate {
        void onFetched(@Nullable Employees employees, int i);

        void fetch(Query query);

        void onFetch();
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate update;
        private final Query query;

        private int code = 0;
        private Employees employees;

        Task(TaskUpdate update, Query query) {
            this.update = update;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            update.onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Employees> call = api.getQueryEmployees(query.getMap());
            call.enqueue(new Callback<Employees>() {
                @Override
                public void onResponse(@NotNull Call<Employees> call, @NotNull Response<Employees> response) {
                    if (response.body() != null) {
                        employees = response.body();
                        code = employees.Code;
                    } else {
                        employees = null;
                        code = 306;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Employees> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    employees = null;
                    code = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    employees = null;
                    code = 308;
                    return true;
                }
                if (code != 0)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            update.onFetched(employees, code);
        }
    }
}