package io.github.sher1234.service.functions.v4.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class OnSignIn extends AsyncTask<Void, Void, FirebaseUser> {

    private OnEvent<User> onEvent;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private int code = 0;
    private String pass;
    private String id;

    public OnSignIn(OnEvent<User> onEvent) {
        this.mAuth = FirebaseAuth.getInstance();
        this.onEvent = onEvent;
    }

    public OnSignIn onSignIn(@NotNull String id, @NotNull String pass) {
        if (getStatus() == RUNNING || getStatus() == FINISHED)
            return new OnSignIn(onEvent).onSignIn(id, pass);
        this.pass = pass;
        this.id = id;
        execute();
        return this;
    }

    public void onResetPassword(@NotNull String id, final Context context) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(id)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        Log.d("SignIn", "passwordResetRequest:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Unable to reset password, try again.", Toast.LENGTH_SHORT).show();
                        Log.d("SignIn", "passwordResetRequest:failure", e);
                    }
                });
    }

    @Override
    protected void onPreExecute() {
        onEvent.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected FirebaseUser doInBackground(Void... voids) {
        mAuth.signInWithEmailAndPassword(id, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = authResult.getUser();
                        code = 1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("SignIn", "signInWithEmail:failure", e);
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
        if (user != null) new OnGetData(onEvent).onGetData(user.getUid());
        else onEvent.onPostExecute(null);
        super.onPostExecute(user);
    }
}