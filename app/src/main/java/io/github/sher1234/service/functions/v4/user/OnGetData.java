package io.github.sher1234.service.functions.v4.user;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.github.sher1234.service.firebase.FireApi;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;

public class OnGetData extends AsyncTask<Void, Void, User> {

    private FirebaseFirestore store;
    private OnEvent<User> onEvent;
    @Nullable private String id;
    private int code = 0;
    private User user;


    public OnGetData(OnEvent<User> onEvent) {
        this.store = FirebaseFirestore.getInstance();
        this.onEvent = onEvent;
    }

    public void onGetData(@Nullable String id) {
        if (id == null) {
            onEvent.onPostExecute(null);
            return;
        }
        this.id = id;
        execute();
    }

    @Override
    protected void onPreExecute() {
        onEvent.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(Void... voids) {
        if (id == null) return null;
        Map<String, Date> map = new HashMap<>();
        map.put("recent", new Date());
        store.collection(FireApi.Users).document(id).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w("DataFetch", "dateUpdate:success");
                        code = 1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DataFetch", "dateUpdate:failure", e);
                        code = -1;
                    }
                });
        while (true) if (isCancelled() || code != 0) break;
        store.collection(FireApi.Users).whereEqualTo("uid", id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        Log.w("DataFetch", "dataFetch:success");
                        if (snapshot.getDocuments().size() == 1) {
                            user = snapshot.getDocuments().get(0).toObject(User.class);
                            code = 1;
                        } else {
                            user = null;
                            code = 2;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DataFetch", "dataFetch:failure", e);
                        user = null;
                        code = -1;
                    }
                });
        while (true) {
            if (isCancelled()) return null;
            if (code != 0) return user;
        }
    }

    @Override
    protected void onPostExecute(@Nullable User user) {
        if (user != null) {
            new OnOfflineData().onSetData(user);
            onEvent.onPostExecute(user);
        } else onEvent.onPostExecute(null);
        super.onPostExecute(user);
    }
}