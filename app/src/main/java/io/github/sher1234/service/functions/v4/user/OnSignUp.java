package io.github.sher1234.service.functions.v4.user;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;

import static android.os.AsyncTask.Status.FINISHED;
import static android.os.AsyncTask.Status.RUNNING;

public class OnSignUp extends AsyncTask<Void, Void, FirebaseUser> {

    private OnEvent<User> onEvent;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private int code = 0;
    private String pass;
    private String id;
    private User data;


    public OnSignUp(OnEvent<User> onEvent) {
        this.mAuth = FirebaseAuth.getInstance();
        this.onEvent = onEvent;
    }

    public OnSignUp onSignUp(@NotNull User data, @NotNull String pass) {
        if (getStatus() == RUNNING || getStatus() == FINISHED)
            return new OnSignUp(onEvent).onSignUp(data, pass);
        this.id = data.email;
        this.data = data;
        this.pass = pass;
        this.execute();
        return this;
    }

    @Override
    protected void onPreExecute() {
        onEvent.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected FirebaseUser doInBackground(Void... voids) {
        mAuth.createUserWithEmailAndPassword(id, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.w("SignUp", "signUpWithEmail:success");
                        user = authResult.getUser();
                        code = 1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("SignUp", "signUpWithEmail:failure", e);
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
    protected void onPostExecute(FirebaseUser user) {
        if (user != null) {
            data.uid = user.getUid();
            new OnSetData(onEvent).onSetData(data);
        } else onEvent.onPostExecute(null);
        super.onPostExecute(user);
    }
}