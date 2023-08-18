package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.adapters.ScoreAdapter
import com.atomikak.quizapp.supportclasses.Score
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
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
    private var highScore: String? = "0"
    private lateinit var userName: String
    private var uid: String = FirebaseAuth.getInstance().uid.toString()
    private var totalQue: Int = 0
    private var db = Firebase.firestore

    //text view
    private lateinit var s_user_name: TextView
    private lateinit var tv_score: TextView
    private lateinit var tv_correct: TextView
    private lateinit var tv_incorrect: TextView

    //RecyclerView
    private lateinit var recv_scores: RecyclerView
    private lateinit var scoreAdapter: ScoreAdapter

    //lists
    private lateinit var scoreList: ArrayList<Score>

    //circular image view
    private lateinit var s_rImage: LottieAnimationView
    private lateinit var s_loader: LottieAnimationView

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
            s_rImage.setAnimation(R.raw.trophie)
        } else {
            s_rImage.setAnimation(R.raw.try_again)
        }



        db.collection("Quiz Category").document(key).collection("$difficulty Score")
            .document(uid).addSnapshotListener { value, error ->

                if (error != null) {
                    Log.d("DD: ", error.message.toString())
                }

                try {
                    highScore = value!!.get("highscore").toString()
                    if (highScore!!.toInt() < score.toInt()) {
                        val collectionRef = db.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener {
                            val myscore = Score(score, it.result.get("name").toString())
                            storeData(myscore)
                        }
                    }
                } catch (e: Exception) {
                    highScore = "0"
                    val collectionRef = db.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener {
                        val myscore = Score(score, it.result.get("name").toString())
                        storeData(myscore)
                    }
                }
            }
    }


    private fun storeData(myscore: Score) {
        val colleRef = db.collection("Quiz Category").document(key).collection("$difficulty Score")
        colleRef.document(uid).set(myscore)
            .addOnCompleteListener {
                val userRef = Firebase.firestore.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).collection("Score").document("MyScores").update(
                    mapOf(Pair("$QuizName $difficulty", score))).addOnFailureListener{
                    Toast.makeText(this@ScoreActivity, "hi,${it.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
            Log.d("DD: ", it.message.toString())
        }
        getScoreList()
    }

    private fun getScoreList() {
        scoreList.clear()
        val colleRef = db.collection("Quiz Category").document(key).collection("$difficulty Score")
            .orderBy("highscore", Query.Direction.DESCENDING)
            .get().addOnCompleteListener {
                for (score in it.result) {
                    scoreList.add(
                        Score(
                            score.getString("highscore").toString(),
                            score.get("c_name").toString(),
                        )
                    )
                }
                loadScores()
            }
            .addOnFailureListener {
                Log.d("DD: ", it.message.toString())
            }
    }

    private fun loadScores() {
        if (scoreList.isNotEmpty()) {
            scoreAdapter = ScoreAdapter(this@ScoreActivity, scoreList)
            recv_scores.layoutManager =
                LinearLayoutManager(this@ScoreActivity, LinearLayoutManager.VERTICAL, false)
            recv_scores.setHasFixedSize(true)
            recv_scores.adapter = scoreAdapter
            s_loader.visibility = LottieAnimationView.GONE
        }
    }

    private fun initializWidgets() {
        s_user_name = findViewById(R.id.s_user_name)
        tv_score = findViewById(R.id.score)
        tv_correct = findViewById(R.id.correct)
        tv_incorrect = findViewById(R.id.incorrect)
        recv_scores = findViewById(R.id.recv_scores)
        s_rImage = findViewById(R.id.s_rImage)
        s_loader = findViewById(R.id.s_loader)
        scoreList = ArrayList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ScoreActivity, MainActivity::class.java)
        startActivity(intent)
    }
}