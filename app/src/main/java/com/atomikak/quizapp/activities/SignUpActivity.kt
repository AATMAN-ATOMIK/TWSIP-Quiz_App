package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.atomikak.quizapp.R
import com.atomikak.quizapp.supportclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.remote.FirestoreChannel
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    //edittext
    private lateinit var su_ed_userNmae: EditText
    private lateinit var su_ed_email: EditText
    private lateinit var su_ed_pass: EditText

    //button
    private lateinit var su_btn_register: Button

    //label
    private lateinit var su_tv_login: TextView
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbUser = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initializeWidgets()

        //login redirect textview
        su_tv_login.setOnClickListener {
            navigateTo(LoginActivity::class.java)
        }

        //button register on click event
        su_btn_register.setOnClickListener {
            if (!(su_ed_userNmae.text.isNullOrEmpty() && su_ed_email.text.isNullOrEmpty() && su_ed_pass.text.isNullOrEmpty())) {
                if (su_ed_pass.text.length >= 6) {
                    firebaseAuth.createUserWithEmailAndPassword(
                        su_ed_email.text.toString(),
                        su_ed_pass.text.toString()
                    )
                        .addOnSuccessListener {
                            dbUser.collection("Users").document(it.user!!.uid).set(
                                User(
                                    name = su_ed_userNmae.text.toString(),
                                    email = su_ed_email.text.toString(),
                                    pass = su_ed_pass.text.toString()
                                )
                            ).addOnSuccessListener {
                                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener {
                            Log.d("DD: ", it.localizedMessage)
                        }
                }else{
                    su_ed_pass.error = "Password Must Be 6 Character Long."
                }
            } else {
                su_ed_userNmae.error = "Required"
                su_ed_email.error = "Required"
                su_ed_pass.error = "Required"
            }
        }
    }


    private fun initializeWidgets() {
        su_ed_userNmae = findViewById(R.id.su_ed_userNmae)
        su_ed_email = findViewById(R.id.su_ed_email)
        su_ed_pass = findViewById(R.id.su_ed_pass)
        su_btn_register = findViewById(R.id.su_btn_register)
        su_tv_login = findViewById(R.id.su_tv_login)
    }

    private fun navigateTo(toClass: Class<LoginActivity>) {
        val intent = Intent(this@SignUpActivity, toClass)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
