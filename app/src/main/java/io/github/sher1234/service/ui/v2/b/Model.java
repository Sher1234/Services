package io.github.sher1234.service.ui.v2.b;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.firebase.FireApi;
import io.github.sher1234.service.firebase.model.request.Request;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.firebase.model.visit.Visit;
import io.github.sher1234.service.functions.v4.OnEvent;
import io.github.sher1234.service.functions.v4.user.FireUser;

public class Model extends ViewModel {

    public MutableLiveData<List<Request>> requests(OnEvent<Void> onEvent) {
        if (requests == null) requests = new MutableLiveData<>();
        if (visits == null) visits = new MutableLiveData<>();
        this.onEvent = onEvent;
        return requests;
    }
    MutableLiveData<List<Request>> requests;
    MutableLiveData<List<Visit>> visits;
    private OnEvent<Void> onEvent;
    private DataTask task;
    private User user;
    public Model() {
        requests = new MutableLiveData<>();
        visits = new MutableLiveData<>();
        user = new FireUser().getUser();
    }

    void refresh() {
        this.stop();
        task = new DataTask();
        task.execute();
    }

    void stop() {
        if (task != null) task.cancel(true);
        task = null;
    }

    @SuppressLint("StaticFieldLeak")
    private class DataTask extends AsyncTask<Void, Void, Boolean> {

        private FirebaseFirestore store;
        private List<Request> requests;
        private List<Visit> visits;
        private int a = 0, b = 0;

        @Override
        protected void onPreExecute() {
            store = FirebaseFirestore.getInstance();
            requests = new ArrayList<>();
            visits = new ArrayList<>();
            onEvent.onPreExecute();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            CollectionReference rRef = store.collection(FireApi.Requests);
            Query rQuery = user.admin?rRef:rRef.whereArrayContains("access", user.uid);
            rQuery = rQuery.orderBy("time", Query.Direction.DESCENDING);
            rQuery.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot snapshot) {
                            Log.w("Dashboard", "dataFetch-requests:success");
                            for (DocumentSnapshot doc: snapshot.getDocuments())
                                requests.add(doc.toObject(Request.class));
                            a = 1;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Dashboard", "dataFetch-requests:failure", e);
                            a = -1;
                        }
                    });
            CollectionReference vRef = store.collection(FireApi.Visits);
            Query vQuery = user.admin?vRef:vRef.whereArrayContains("uid", user.uid);
            vQuery = vQuery.orderBy("start", Query.Direction.DESCENDING);
            vQuery.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot snapshot) {
                            Log.w("Dashboard", "dataFetch-visits:success");
                            for (DocumentSnapshot doc: snapshot.getDocuments())
                                visits.add(doc.toObject(Visit.class));
                            b = 1;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Dashboard", "dataFetch-visits:failure", e);
                            b = -1;
                        }
                    });
            while (true) {
                if (a != 0 && b!= 0) return true;
                if (isCancelled()) return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            onEvent.onPostExecute(null);
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Model.this.requests.setValue(requests);
                Model.this.visits.setValue(visits);
            } else {
                Model.this.requests.setValue(null);
                Model.this.visits.setValue(null);
            }
        }
    }
}
