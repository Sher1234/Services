package io.github.sher1234.service.functions.v4.user;

import com.google.firebase.auth.FirebaseAuth;

import io.github.sher1234.service.firebase.model.user.User;

public class FireUser {

    private final FirebaseAuth mAuth;

    public FireUser() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signOut() {
        if (isLoggedIn()) mAuth.signOut();
    }

    public User getUser() {
        return new OnOfflineData().onGetData();
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }
}