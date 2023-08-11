package com.atomikak.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.atomikak.quizapp.activities.LoginActivity
import com.atomikak.quizapp.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(
            {
                if(FirebaseAuth.getInstance().currentUser!=null){
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)

                }else{
                    val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                    startActivity(intent)
                }
            }, 4000
        )

    }
}