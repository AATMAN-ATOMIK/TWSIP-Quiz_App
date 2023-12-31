package com.atomikak.quizapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
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
    private var correct = 0
    private var incorrect = 0

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

        q_quiz_category.text = intent.getStringExtra("c_name").toString()

    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                q_timer.setText("Time Ramain ${millisUntilFinished / 1000}s")
            }

            override fun onFinish() {
                incorrect++
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

            val category = intent.getStringExtra("c_name")
            val diff = intent.getStringExtra("difficulty").toString()
            val difColReference = intent.getStringExtra("key").toString()
            val userName = intent.getStringExtra("userName").toString()

            val intent = Intent(this@QuizActivity, ScoreActivity::class.java)
            intent.putExtra("score",score)
            intent.putExtra("correct",correct)
            intent.putExtra("incorrect",incorrect)
            intent.putExtra("QuizName",category)
            intent.putExtra("difficulty",diff)
            intent.putExtra("key",difColReference)
            intent.putExtra("userName",userName)
            startActivity(intent)
        }
    }

    private fun getQuestions() {
        collectionRef.limit(10).addSnapshotListener { value, e ->
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
    override fun onItemClick(view: View, position: Int, answer: String) {
        if (quizList[position].correct == answer) {
            view.setBackgroundResource(R.drawable.positive_button)
            score++
            correct++
        } else {
            view.setBackgroundResource(R.drawable.negative_button)
            incorrect++
        }

        countDownTimer.cancel()
        Handler().postDelayed({
            view.setBackgroundResource(R.drawable.button_quiz_option)
            goToNextQuestion()
        }, 1200)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@QuizActivity, MainActivity::class.java)
        startActivity(intent)
    }
}