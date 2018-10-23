package com.people_patterns.livingheritage.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.people_patterns.livingheritage.OnGetTrees;
import com.people_patterns.livingheritage.UploadFileCallback;
import com.people_patterns.livingheritage.model.Tree;
import com.people_patterns.livingheritage.model.User;

import java.io.File;
import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog dialog;

    public void showProgress() {
        dialog.setTitle("Please wait");
        dialog.setMessage("This might take some time");
        dialog.show();
    }

    public void hideProgress() {
        dialog.dismiss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
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

    public FirebaseStorage getFirebaseStorage() {
        return ((BaseApplication)getApplication()).getFirebaseStorage();
    }

    public BaseApplication getbaseApp() {
        return ((BaseApplication)getApplication());
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

    public void createTree(Tree tree, OnCompleteListener<Void> callback) {
        DatabaseReference reference = getFirebaseDatabase().getReference("trees").push();
        reference.setValue(tree).addOnCompleteListener(callback);
    }

    public void getAllTrees(final OnGetTrees callback) {
        Query query = getFirebaseDatabase().getReference().child("trees");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Tree>> dt = new GenericTypeIndicator<HashMap<String, Tree>>(){};
                HashMap<String, Tree> trees = dataSnapshot.getValue(dt);
                if (trees != null) {
                    callback.onTreesFetched(trees.values());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void uploadImage(File file, Tree tree, final UploadFileCallback callback) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Name", tree.getName())
                .setCustomMetadata("LatLng", tree.getLat() + "," + tree.getLong())
                .setCustomMetadata("Description", tree.getDescription())
                .setCustomMetadata("Tagger", tree.getTaggerId())
                .setCustomMetadata("Guardian", tree.getGuardianId())
                .build();
        Uri uri = Uri.fromFile(file);
        StorageReference reference = getFirebaseStorage().getReference().child("images/" + uri.getLastPathSegment());
        reference.putFile(uri, metadata).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.uploadFailed();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.uploadSuccess();
            }
        });
    }

    public void getImageUrl(String key, OnCompleteListener<Uri> callback) {
        StorageReference reference = getFirebaseStorage().getReference().child("images/" + key);
        reference.getDownloadUrl().addOnCompleteListener(callback);
    }

    public String getLoggedInEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return "admin user";
    }
}
