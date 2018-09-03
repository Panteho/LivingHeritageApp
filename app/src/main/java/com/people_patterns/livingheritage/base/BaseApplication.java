package com.people_patterns.livingheritage.base;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class BaseApplication extends Application {
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        initFireBase();
    }

    private void initFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
