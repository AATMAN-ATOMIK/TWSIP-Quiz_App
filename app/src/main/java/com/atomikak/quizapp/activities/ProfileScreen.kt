package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.atomikak.quizapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileScreen : AppCompatActivity() {
    //textView
    private lateinit var p_userName:TextView

    //imageButton
    private lateinit var p_back:ImageButton
    private lateinit var p_logout:ImageButton

    //listView
    private lateinit var p_score_listView:ListView

    //database
    val userDb = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screem)

        initializeWidgets()
    }

    private fun initializeWidgets() {
        p_userName = findViewById(R.id.p_userName)
        p_back = findViewById(R.id.p_back)
        p_logout = findViewById(R.id.p_logout)
        p_score_listView = findViewById(R.id.p_score_listView)

        getUserName()

        p_back.setOnClickListener {
            val intent = Intent(this@ProfileScreen,MainActivity::class.java)
            startActivity(intent)
        }

        p_logout.setOnClickListener {
            val _auth = FirebaseAuth.getInstance()
            _auth.signOut()

            val intent = Intent(this@ProfileScreen,LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun getUserName() {
        userDb.document(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener {
            p_userName.text = it.result.get("name").toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ProfileScreen, MainActivity::class.java)
        startActivity(intent)
    }


}