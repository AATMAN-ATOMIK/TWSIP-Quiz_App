package com.atomikak.quizapp.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.adapters.ScoreListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileScreen : AppCompatActivity() {
    //textView
    private lateinit var p_userName: TextView

    //imageButton
    private lateinit var p_back: ImageButton
    private lateinit var p_logout: ImageButton

    //listView
    private lateinit var p_score_listView: ListView

    //list
    private lateinit var scoreList: ArrayList<Pair<String, Any?>>

    //database
    val userDb = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screem)
        initializeWidgets()
        getUserName()
        getScoreList()
    }

    private fun initializeWidgets() {
        p_userName = findViewById(R.id.p_userName)
        p_back = findViewById(R.id.p_back)
        p_logout = findViewById(R.id.p_logout)
        p_score_listView = findViewById(R.id.p_score_listView)
        scoreList = ArrayList()

        p_back.setOnClickListener {
            val intent = Intent(this@ProfileScreen, MainActivity::class.java)
            startActivity(intent)
        }

        p_logout.setOnClickListener {
            val _auth = FirebaseAuth.getInstance()
            _auth.signOut()

            val intent = Intent(this@ProfileScreen, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun getScoreList() {
        Firebase.firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString())
            .collection("Score").document("MyScores")
            .get().addOnCompleteListener {
               if(it.result.exists()){
                   for(score in it.result.data!!.toList())
                   {
                       scoreList.add(score)
                   }

                   loadScoreList()
               }
            }
    }

    private fun loadScoreList() {
        if(!scoreList.isNullOrEmpty()){
            p_score_listView.adapter = ScoreListAdapter(this@ProfileScreen,scoreList)
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