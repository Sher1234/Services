package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.database.OfflineBase;
import io.github.sher1234.service.database.Queries;
import io.github.sher1234.service.model.base.Token;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.util.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskUser {

    private final Context context;
    private Password password;
    private Register register;
    private Update update;
    private Login login;

    public TaskUser() {
        this.context = AppController.getInstance();
    }

    public void onLogin(@NotNull User user, @NotNull Token token) {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.execSQL(Queries.DeleteToken);
        database.execSQL(Queries.DeleteUser);
        addToken(token);
        addUser(user);
    }

    public void onMigrateUser(User user) {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.execSQL(Queries.DeleteToken);
        database.execSQL(Queries.DeleteUser);
        addUser(user);
    }

    public void onRefresh(@NotNull User user, @NotNull Token token) {
        updateToken(token);
        updateUser(user);
    }

    public void onLogout() {
        deleteToken();
        deleteUser();
    }

    private void deleteUser() {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.execSQL(Queries.DeleteUser);
    }

    private void deleteToken() {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.execSQL(Queries.DeleteToken);
    }

    private void addUser(@NotNull User user) {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", user.Name);
        values.put("Email", user.Email);
        values.put("Phone", user.Phone);
        values.put("UserID", user.UserID);
        values.put("IsAdmin", user.isAdmin());
        values.put("Password", user.Password);
        values.put("EmployeeID", user.EmployeeID);
        values.put("IsRegistered", user.isRegistered());
        database.insert("User", null, values);
    }

    private void addToken(@NotNull Token token) {
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Token", token.Token);
        values.put("Expiry", token.Expiry);
        values.put("Recent", token.Recent);
        values.put("UserID", token.UserID);
        database.insert("Logins", null, values);
    }

    private void updateUser(@NotNull User user) {
        ContentValues values = new ContentValues();
        values.put("Name", user.Name);
        values.put("Email", user.Email);
        values.put("Phone", user.Phone);
        values.put("IsAdmin", user.isAdmin());
        values.put("Password", user.Password);
        values.put("EmployeeID", user.EmployeeID);
        values.put("IsRegistered", user.isRegistered());
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.update("User", values, "UserID = ?", new String[]{user.UserID});
    }

    private void updateToken(@NotNull Token token) {
        ContentValues values = new ContentValues();
        values.put("Token", token.Token);
        values.put("Expiry", token.Expiry);
        values.put("Recent", token.Recent);
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.update("Logins", values, "UserID = ?", new String[]{token.UserID});
    }

    public User getUser() {
        SQLiteDatabase database = new OfflineBase(context).getReadableDatabase();
        String[] projection = {"Name", "Email", "Phone", "UserID", "IsAdmin", "Password",
                "EmployeeID", "IsRegistered"};
        Cursor cursor = database.query("User", projection, null, null,
                null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.Name = cursor.getString(0);
            user.Email = cursor.getString(1);
            user.Phone = cursor.getString(2);
            user.UserID = cursor.getString(3);
            user.Password = cursor.getString(5);
            user.EmployeeID = cursor.getString(6);
            user.IsAdmin = Integer.parseInt(cursor.getString(4));
            user.IsRegistered = Integer.parseInt(cursor.getString(7));
        }
        cursor.close();
        return user;
    }

    @Nullable
    public Token getToken() {
        SQLiteDatabase database = new OfflineBase(context).getReadableDatabase();
        String[] projection = {"UserID", "Token", "Recent", "Expiry"};
        Cursor cursor = database.query("Logins", projection, null, null,
                null, null, null);
        Token token = null;
        if (cursor.moveToFirst()) {
            token = new Token();
            token.UserID = cursor.getString(0);
            token.Token = cursor.getString(1);
            token.Recent = cursor.getString(2);
            token.Expiry = cursor.getString(3);
        }
        cursor.close();
        return token;
    }

    public void onLoginUser(TaskUpdate taskUpdate, String s1, String s2) {
        if (login != null)
            login.cancel(true);
        if (update != null)
            update.cancel(true);
        if (register != null)
            register.cancel(true);
        if (password != null)
            password.cancel(true);
        login = null;
        login = new Login(taskUpdate, s1, s2);
        login.execute();
    }

    public void onRegisterUser(TaskUpdate taskUpdate, User user) {
        if (login != null)
            login.cancel(true);
        if (register != null)
            register.cancel(true);
        if (password != null)
            password.cancel(true);
        if (update != null)
            update.cancel(true);
        register = null;
        register = new Register(taskUpdate, user);
        register.execute();
    }

    public void onUpdateUser(TaskUpdate taskUpdate, User user) {
        if (login != null)
            login.cancel(true);
        if (update != null)
            update.cancel(true);
        if (password != null)
            password.cancel(true);
        if (register != null)
            register.cancel(true);
        update = null;
        update = new Update(taskUpdate, user);
        update.execute();
    }

    public void onChangePassword(TaskUpdate taskUpdate, Map<String, String> map) {
        if (login != null)
            login.cancel(true);
        if (update != null)
            update.cancel(true);
        if (password != null)
            password.cancel(true);
        if (register != null)
            register.cancel(true);
        password = null;
        password = new Password(taskUpdate, map);
        password.execute();
    }

    public void onAccountError(final FragmentActivity context, final TaskUpdate taskUpdate) {
        MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Account disabled")
                .setDescription(R.string.acc_disabled)
                .negativeButton("Sign Out", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        onLogout();
                        dialog.dismiss();
                    }
                })
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        taskUpdate.fetch();
                        dialog.dismiss();
                    }
                })
                .neutralButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        context.finish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    public void onNetworkError(final FragmentActivity context, final TaskUpdate taskUpdate) {
        MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Network issue")
                .setDescription(R.string.offline_access)
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        taskUpdate.fetch();
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

    public void onNetworkError2(@NotNull Context context, final TaskUpdate taskUpdate) {
        MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Network issue")
                .setDescription(R.string.offline_access)
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        taskUpdate.fetch();
                        dialog.dismiss();
                    }
                })
                .negativeButton(R.string.cancel, new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    public interface TaskUpdate {
        void onFetched(@Nullable AccountJson account, int i);

        void onFetch();

        void fetch();
    }

    @SuppressLint("StaticFieldLeak")
    private class Login extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate update;
        private final String pass;
        private final String id;

        private AccountJson account;
        private int code = 0;

        Login(TaskUpdate update, String id, String pass) {
            this.update = update;
            this.pass = pass;
            this.id = id;
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
            Call<AccountJson> call = api.onLoginUser(id, pass);
            call.enqueue(new Callback<AccountJson>() {
                @Override
                public void onResponse(@NotNull Call<AccountJson> call, @NotNull Response<AccountJson> response) {
                    if (response.body() != null) {
                        account = response.body();
                        code = account.Code;
                    } else {
                        account = null;
                        code = 306;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AccountJson> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    account = null;
                    code = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    account = null;
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
            update.onFetched(account, code);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Register extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate update;
        private final User user;

        private AccountJson account;
        private int code = 0;

        Register(TaskUpdate update, User user) {
            this.update = update;
            this.user = user;
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
            Call<AccountJson> call = api.onRegisterUser(user.getRegister());
            call.enqueue(new Callback<AccountJson>() {
                @Override
                public void onResponse(@NotNull Call<AccountJson> call, @NotNull Response<AccountJson> response) {
                    if (response.body() != null) {
                        account = response.body();
                        code = account.Code;
                    } else {
                        account = null;
                        code = 306;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AccountJson> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    account = null;
                    code = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    account = null;
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
            update.onFetched(account, code);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Update extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate update;
        private final User user;

        private AccountJson account;
        private int code = 0;

        Update(TaskUpdate update, User user) {
            this.update = update;
            this.user = user;
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
            Call<AccountJson> call = api.onUpdateUser(user.getEditMap());
            call.enqueue(new Callback<AccountJson>() {
                @Override
                public void onResponse(@NotNull Call<AccountJson> call, @NotNull Response<AccountJson> response) {
                    if (response.body() != null) {
                        account = response.body();
                        code = account.Code;
                    } else {
                        account = null;
                        code = 306;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AccountJson> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    account = null;
                    code = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    account = null;
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
            update.onFetched(account, code);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Password extends AsyncTask<Void, Void, Boolean> {

        private final Map<String, String> map;
        private final TaskUpdate update;

        private AccountJson account;
        private int code = 0;

        Password(TaskUpdate update, Map<String, String> map) {
            this.update = update;
            this.map = map;
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
            Call<AccountJson> call = api.onChangePassword(map);
            call.enqueue(new Callback<AccountJson>() {
                @Override
                public void onResponse(@NotNull Call<AccountJson> call, @NotNull Response<AccountJson> response) {
                    if (response.body() != null) {
                        account = response.body();
                        code = account.Code;
                    } else {
                        account = null;
                        code = 306;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AccountJson> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    account = null;
                    code = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    account = null;
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
            update.onFetched(account, code);
        }
    }
}