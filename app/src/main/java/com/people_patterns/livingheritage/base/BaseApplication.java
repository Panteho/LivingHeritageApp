package com.people_patterns.livingheritage.base;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseApplication extends Application {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase ;

    @Override
    public void onCreate() {
        super.onCreate();
        initFireBase();
    }

    private void initFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    FirebaseDatabase getFirebaseDatabase(){
        return firebaseDatabase;
    }
}

