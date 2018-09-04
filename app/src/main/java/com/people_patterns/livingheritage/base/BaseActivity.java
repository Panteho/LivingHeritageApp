package com.people_patterns.livingheritage.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.people_patterns.livingheritage.model.User;

public class BaseActivity extends AppCompatActivity {

    public void showNothing() {

    }


    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public FirebaseAuth getFirebaseAuth() {
        return ((BaseApplication)getApplication()).getFirebaseAuth();
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return ((BaseApplication)getApplication()).getFirebaseDatabase();
    }

    public void addToDatabase(String key, int value){
        getFirebaseDatabase();
        DatabaseReference myRef = getFirebaseDatabase().getReference(key);
        myRef.setValue(value);
    }

    public void createUser(User user, OnCompleteListener<Void> callback) {
        DatabaseReference reference = getFirebaseDatabase().getReference("users").push();
        reference.setValue(user).addOnCompleteListener(callback);
    }
}
