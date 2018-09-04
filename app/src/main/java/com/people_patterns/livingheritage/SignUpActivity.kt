package com.people_patterns.livingheritage

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.people_patterns.livingheritage.base.BaseActivity
import com.people_patterns.livingheritage.model.User

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setListeners()
    }

    private fun setListeners() {
        findViewById<Button>(R.id.btn_signup).setOnClickListener {
            var name = findViewById<EditText>(R.id.edt_username).text.toString()
            var email = findViewById<EditText>(R.id.edt_email).text.toString()
            var phone = findViewById<EditText>(R.id.edt_phone).text.toString()
            var password = findViewById<EditText>(R.id.edt_password).text.toString()
            var repassword = findViewById<EditText>(R.id.edt_repassword).text.toString()
//            if (!password.equals(repassword)) {
//                findViewById<EditText>(R.id.edt_repassword).setError("Enter matching passwords")
//                return@setOnClickListener
//            }

            //When its valid
            createUser(name, email, phone, password);
        }
    }

    private fun createUser(name: String, email: String, phone: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            Toast.makeText(this, it.result.user.email, Toast.LENGTH_SHORT)
            val newUser = User(name, email, phone, it.result.user.uid)
            saveUserToDb(newUser)
        }
    }

    private fun saveUserToDb(newUser: User) {
        createUser(newUser, OnCompleteListener { Toast.makeText(this, "User created", Toast.LENGTH_SHORT) })
    }
}
