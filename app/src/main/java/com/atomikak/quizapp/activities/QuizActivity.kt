package com.atomikak.quizapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.adapters.QuizAdapter
import com.atomikak.quizapp.listeners.RecvClickListener
import com.atomikak.quizapp.supportclasses.Question
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity(), RecvClickListener {

    //recyclerview
    private lateinit var recv_questions: RecyclerView

    //lottie button
    private lateinit var q_btn_profile: LottieAnimationView

    //textview
    private lateinit var q_quiz_category: TextView
    private lateinit var q_que_counter: TextView
    private lateinit var q_timer: TextView

    //list
    private lateinit var quizList: ArrayList<Question>

    //adapter
    private lateinit var quizAdapter: QuizAdapter

    //database
    private lateinit var collectionRef: CollectionReference

    //question index
    private var qIndex: Int = 0

    //score
    private var score = 0

    //timer
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        qIndex = 0
        collectionRef = Firebase.firestore.collection("Quiz Category")
            .document(intent.getStringExtra("key").toString())
            .collection(intent.getStringExtra("difficulty").toString())

        initializWidgets()
        Log.d("DD: ", intent.getStringExtra("key").toString())
        getQuestions()

        startTimer()

    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                q_timer.setText("Time Ramain ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                goToNextQuestion()
            }
        }

        countDownTimer.start()
    }

    private fun goToNextQuestion() {
        qIndex++
        if (qIndex < quizList.size) {
            q_que_counter.setText("Question ${qIndex!! + 1} / ${quizList.size}")
            recv_questions.scrollToPosition(qIndex)
            startTimer()
        } else {
            val intent = Intent(this@QuizActivity, ScoreActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getQuestions() {
        collectionRef.addSnapshotListener { value, e ->
            if (e != null) {
                Log.d("DD: ", e.message.toString())
            }

            for (question in value!!) {
                quizList.add(
                    Question(
                        question = question.getString("question")!!,
                        options = question.get("options")!! as ArrayList<String>,
                        correct = question.getString("correct")!!
                    )
                )
                Log.d("DD: ", question.toString())
            }
            if (quizList.isNotEmpty()) {
                quizAdapter = QuizAdapter(this@QuizActivity, quizList, this)
                recv_questions.layoutManager = object : LinearLayoutManager(this@QuizActivity) {
                    override fun canScrollHorizontally() = false
                    override fun canScrollVertically() = false
                }
                q_que_counter.setText("Question ${qIndex!! + 1} / ${quizList.size}")
                recv_questions.setHasFixedSize(true)
                recv_questions.stopScroll()
                recv_questions.isScrollContainer = false
                recv_questions.adapter = quizAdapter
            }
        }
    }

    private fun initializWidgets() {
        recv_questions = findViewById(R.id.recv_questions)
        q_btn_profile = findViewById(R.id.q_btn_profile)
        q_quiz_category = findViewById(R.id.q_quiz_category)
        q_que_counter = findViewById(R.id.q_que_counter)
        q_timer = findViewById(R.id.q_timer)
        quizList = ArrayList()
    }

    @SuppressLint("ResourceAsColor")
    override fun onItemClick(it: View, position: Int, answer: String) {
        if (quizList[position].correct == answer) {
            it.setBackgroundResource(R.drawable.positive_button)
            score++
        } else {
            it.setBackgroundResource(R.drawable.negative_button)
        }

        countDownTimer.cancel()
        Handler().postDelayed({
            goToNextQuestion()
        }, 2000)

    }
}