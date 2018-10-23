package com.people_patterns.livingheritage

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.people_patterns.livingheritage.base.BaseActivity
import android.content.pm.PackageManager




class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if(firebaseAuth.currentUser != null) {
            gotoHomepage()
            finish()
        }
        askForPermission()
    }

    private fun askForPermission() {
        val array = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, array, 1);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this@SignInActivity, "Permission denied to we need to exit", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    fun gotoSignUp(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun gotoHomepage(){
        val intent = Intent(this, TreeMapActivity::class.java)
        startActivity(intent)
    }

    fun signInUser(view: View){
        var email = findViewById<EditText>(R.id.edt_username).text.toString()
        var password = findViewById<EditText>(R.id.edt_password).text.toString()
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        gotoHomepage()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@SignInActivity, "Please enter correct details", Toast.LENGTH_SHORT).show();
                    }

                })

    }


}
