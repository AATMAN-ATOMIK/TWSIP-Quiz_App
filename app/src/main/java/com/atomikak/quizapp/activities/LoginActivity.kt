package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.atomikak.quizapp.R

class LoginActivity : AppCompatActivity() {

    //textview
    private lateinit var l_tv_register:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initWidgets()

        //register navigation
        l_tv_register.setOnClickListener{
            navigateToIntent(SignUpActivity::class.java)
        }
    }

    //function to initialize widgets
    private fun initWidgets() {
        l_tv_register = findViewById(R.id.l_tv_register)
    }


    //Navigation Function
    private fun navigateToIntent(toClass: Class<SignUpActivity>) {
        val intent = Intent(this@LoginActivity,toClass)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}