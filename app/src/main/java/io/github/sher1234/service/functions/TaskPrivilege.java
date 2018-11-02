package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskPrivilege {

    private Changing changing;
    private Listing listing;

    public TaskPrivilege() {
        changing = null;
        listing = null;
    }

    @NonNull
    private Query getQuery() {
        User user = new TaskUser().getUser();
        String s = "SELECT * FROM User WHERE UserID <> '" + user.UserID + "'";
        Query query = new Query();
        query.Table = "Users";
        query.Query = s;
        return query;
    }

    public void onListRefresh(TaskUpdate taskUpdate) {
        if (listing != null)
            listing.cancel(true);
        if (changing != null)
            changing.cancel(true);
        listing = null;
        listing = new Listing(taskUpdate, getQuery());
        listing.execute();
    }

    public void onDeleteAccount(TaskUpdate taskUpdate, String id, String email) {
        if (listing != null)
            listing.cancel(true);
        if (changing != null)
            changing.cancel(true);
        changing = null;
        changing = new Changing(taskUpdate, email, id);
        changing.execute();
    }

    private void onChangeAccount(TaskUpdate taskUpdate, String userID, int value, int type) {
        if (listing != null)
            listing.cancel(true);
        if (changing != null)
            changing.cancel(true);
        changing = null;
        changing = new Changing(taskUpdate, userID, value, type);
        changing.execute();
    }

    public void onChangeAdministrator(TaskUpdate taskUpdate, String userID, int value) {
        onChangeAccount(taskUpdate, userID, value, 0);
    }

    public void onChangeAccountState(TaskUpdate taskUpdate, String userID, int value) {
        onChangeAccount(taskUpdate, userID, value, 1);
    }

    public void onNetworkError(final FragmentActivity context, final TaskUpdate taskUpdate) {
        final MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Network issue")
                .setDescription(R.string.offline_access)
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        taskUpdate.fetch();
                        dialog.dismiss();
                    }
                })
                .negativeButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    public interface TaskUpdate {
        void onFetched(@Nullable Users users, int i, boolean b);

        void onFetch();

        void fetch();
    }

    @SuppressLint("StaticFieldLeak")
    private class Listing extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final Query query;
        private Users users;
        private int i = 4869;

        Listing(TaskUpdate taskUpdate, Query query) {
            this.taskUpdate = taskUpdate;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskUpdate.onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.getQueryUsers(query.getMap());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null) {
                        users = response.body();
                        i = users.Code;
                    } else {
                        users = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
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
            taskUpdate.onFetched(users, i, false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Changing extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final int value, type;
        private final String userID;
        private Responded responded;
        private String email;
        private int i = 4869;

        Changing(TaskUpdate taskUpdate, String userID, int value, int type) {
            this.taskUpdate = taskUpdate;
            this.type = type;
            this.value = value;
            this.userID = userID;
        }

        Changing(TaskUpdate taskUpdate, String email, String userID) {
            this(taskUpdate, userID, 0, -1);
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskUpdate.onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call;
            if (type == 0)
                call = api.setUserPrevilige(userID, value + "");
            else if (type == 1)
                call = api.setUserRegisteration(userID, value + "");
            else
                call = api.onDeleteUser(userID, email);
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
            taskUpdate.onFetched(null, 1, true);
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.Message, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AppController.getInstance(), "Error updating account", Toast.LENGTH_SHORT).show();
            taskUpdate.fetch();
        }
    }
}