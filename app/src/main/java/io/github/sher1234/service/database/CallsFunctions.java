package io.github.sher1234.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.Token;
import io.github.sher1234.service.model.base.User;

public class CallsFunctions {

    private final Context context;

    CallsFunctions(Context context) {
        this.context = context;
    }

    public void addPendingCalls(@NotNull List<Call> calls) {
        for (Call call : calls)
            addCall(call);
    }

    private void addCall(@NotNull Call call) {
        ContentValues values = new ContentValues();
        values.put("ID1", call.ID1);
        values.put("ID2", call.ID2);
        values.put("CallID", call.CallID);
        values.put("Warranty", call.Warranty);
        values.put("ConcernName", call.ConcernName);
        values.put("SiteDetails", call.SiteDetails);
        values.put("CustomerName", call.CustomerName);
        values.put("NatureOfSite", call.NatureOfSite);
        values.put("ConcernPhone", call.ConcernPhone);
        values.put("ComplaintType", call.ComplaintType);
        values.put("ProductDetail", call.ProductDetail);
        values.put("ProductNumber", call.ProductNumber);
        values.put("DateTime", call.getDateTimeServer());
        values.put("IsCompleted", call.getIsCompleted());
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.insert("Calls", null, values);
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
        ContentValues values = new ContentValues();
        values.put("Email", user.Email);
        values.put("Phone", user.Phone);
        values.put("UserID", user.UserID);
        values.put("Name", user.EmployeeID);
        values.put("IsAdmin", user.isAdmin());
        values.put("Password", user.Password);
        values.put("EmployeeID", user.EmployeeID);
        values.put("IsRegistered", user.isRegistered());
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
        database.insert("User", null, values);
    }

    private void addToken(@NotNull Token token) {
        ContentValues values = new ContentValues();
        values.put("Token", token.Token);
        values.put("Expiry", token.Expiry);
        values.put("Recent", token.Recent);
        values.put("UserID", token.UserID);
        SQLiteDatabase database = new OfflineBase(context).getWritableDatabase();
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
}