package io.github.sher1234.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.model.base.Token;
import io.github.sher1234.service.model.base.User;

public class LoginFunctions {

    private final Context context;

    public LoginFunctions() {
        this.context = AppController.getInstance();
    }

    public void onLogin(@NotNull User user, @NotNull Token token) {
        //Removing User
        onLogout();
        //Adding User
        addToken(token);
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
        values.put("Email", user.Email);
        values.put("Phone", user.Phone);
        values.put("UserID", user.UserID);
        values.put("Name", user.EmployeeID);
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
        values.put("Email", user.Email);
        values.put("Phone", user.Phone);
        values.put("Name", user.EmployeeID);
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

    @Nullable
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
}