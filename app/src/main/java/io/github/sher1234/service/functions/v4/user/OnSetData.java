package io.github.sher1234.service.functions.v4.user;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.firebase.FireApi;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;

import static android.os.AsyncTask.Status.FINISHED;
import static android.os.AsyncTask.Status.RUNNING;

public class OnSetData extends AsyncTask<Void, Void, Boolean> implements FireApi {

    private FirebaseFirestore store;
    private OnEvent<User> onEvent;
    private int code = 0;
    private User user;


    public OnSetData(OnEvent<User> onEvent) {
        this.store = FirebaseFirestore.getInstance();
        this.onEvent = onEvent;
    }

    public OnSetData onSetData(@NotNull User user) {
        if (getStatus() == RUNNING || getStatus() == FINISHED)
            return new OnSetData(onEvent).onSetData(user);
        this.user = user;
        this.execute();
        return this;
    }

    @Override
    protected void onPreExecute() {
        onEvent.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        store.collection(USERS).document(user.uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w("DataSave", "dataFetch:success");
                        code = 1;
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
            if (isCancelled()) return false;
            if (code == -1) return false;
            if (code == 1) return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            new OnOfflineData().onSetData(user);
            onEvent.onPostExecute(user);
        } else onEvent.onPostExecute(null);
        super.onPostExecute(aBoolean);
    }
}