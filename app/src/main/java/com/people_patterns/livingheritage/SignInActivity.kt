package com.people_patterns.livingheritage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.people_patterns.livingheritage.base.BaseActivity


class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
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
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        gotoHomepage()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@SignInActivity, "Please enter correct details", Toast.LENGTH_SHORT);
                    }

                })

    }


}
