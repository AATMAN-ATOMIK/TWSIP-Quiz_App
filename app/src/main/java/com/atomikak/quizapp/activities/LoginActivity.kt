package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.supportclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //edittext
    private lateinit var l_ed_email: EditText
    private lateinit var l_ed_pass: EditText

    //button
    private lateinit var l_btn_login: Button
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //textview
    private lateinit var l_tv_register: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initWidgets()

        //register navigation
        l_tv_register.setOnClickListener {
            navigateToIntent(SignUpActivity::class.java)
        }

        //button login on click event
        l_btn_login.setOnClickListener {
            if (!(l_ed_email.text.isNullOrEmpty() && l_ed_pass.text.isNullOrEmpty())) {
                firebaseAuth.signInWithEmailAndPassword(
                    l_ed_email.text.toString(),
                    l_ed_pass.text.toString()
                )
                    .addOnSuccessListener {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener {
                        Log.d("DD: ", it.localizedMessage)
                    }
            } else {
                l_ed_email.setError("Required")
                l_ed_pass.setError("Required")
            }
        }
    }

    //function to initialize widgets
    private fun initWidgets() {
        l_tv_register = findViewById(R.id.l_tv_register)
        l_btn_login = findViewById(R.id.l_btn_login)
        l_ed_pass = findViewById(R.id.l_ed_pass)
        l_ed_email = findViewById(R.id.l_ed_email)
    }


    //Navigation Function
    private fun navigateToIntent(toClass: Class<SignUpActivity>) {
        val intent = Intent(this@LoginActivity, toClass)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}