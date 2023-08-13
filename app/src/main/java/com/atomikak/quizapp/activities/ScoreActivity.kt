package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.atomikak.quizapp.R
import com.atomikak.quizapp.supportclasses.Score
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ScoreActivity : AppCompatActivity() {

    //variables
    private lateinit var score: String
    private lateinit var correct: String
    private lateinit var incorrect: String
    private lateinit var key: String
    private lateinit var difficulty: String
    private lateinit var QuizName: String
    private var highScore: String?="0"
    private lateinit var userName: String
    private var uid: String = FirebaseAuth.getInstance().uid.toString()
    private var totalQue: Int = 0
    private var db = Firebase.firestore

    //text view
    private lateinit var s_user_name: TextView
    private lateinit var tv_score: TextView
    private lateinit var tv_correct: TextView
    private lateinit var tv_incorrect: TextView

    //circular image view
    private lateinit var s_rImage: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        score = intent.getIntExtra("score", 0).toString()
        correct = intent.getIntExtra("correct", 0).toString()
        incorrect = intent.getIntExtra("incorrect", 0).toString()
        key = intent.getStringExtra("key").toString()
        difficulty = intent.getStringExtra("difficulty").toString()
        QuizName = intent.getStringExtra("QuizName").toString()
        userName = intent.getStringExtra("userName").toString()
        totalQue = incorrect.toInt() + correct.toInt()

        Log.d("DD: ", "$score ,$correct ,$incorrect ,$key ,$difficulty ,$QuizName ,$totalQue")

        initializWidgets()

        tv_score.setText("Score : $score")
        tv_correct.setText("Correct : $correct")
        tv_incorrect.setText("Incorrect : $incorrect")

        if ((correct.toInt() * 100) / totalQue.toInt() > 40) {
            Glide.with(this@ScoreActivity).load(R.drawable.success).into(s_rImage)
        } else {
            Glide.with(this@ScoreActivity).load(R.drawable.defeat).into(s_rImage)
        }



        db.collection("Quiz Category").document(key).collection("$difficulty Score")
            .document(uid).addSnapshotListener { value, error ->

                if (error != null) {
                    Log.d("DD: ", error.message.toString())
                }

                if(value!!.get("highscore").toString()==""){
                    highScore = "0"
                    val myscore = Score(score, userName)
                    storeData(myscore)
                }else{
                    highScore = value.get("highscore").toString()
                    if (highScore!!.toInt() < score.toInt()) {
                        val myscore = Score(score, userName)
                        storeData(myscore)
                    }
                }
            }

    }


    private fun storeData(myscore: Score) {
        Toast.makeText(this, "ho", Toast.LENGTH_SHORT).show()
        val colleRef = db.collection("Quiz Category").document(key).collection("$difficulty Score")
        colleRef.document(uid).set(myscore).addOnFailureListener {
            Log.d("DD: ", it.message.toString())
        }
    }

    private fun initializWidgets() {
        s_user_name = findViewById(R.id.s_user_name)
        tv_score = findViewById(R.id.score)
        tv_correct = findViewById(R.id.correct)
        tv_incorrect = findViewById(R.id.incorrect)
        s_rImage = findViewById(R.id.s_rImage)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ScoreActivity,MainActivity::class.java)
        startActivity(intent)
    }
}