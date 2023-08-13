package com.atomikak.quizapp.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.atomikak.quizapp.R
import com.google.android.material.card.MaterialCardView

class DifficultyActivity : AppCompatActivity() {

    //textview
    private lateinit var d_quiz_category: TextView

    //material card view
    private lateinit var d_dif_easy: MaterialCardView
    private lateinit var d_dif_medium: MaterialCardView
    private lateinit var d_dif_hard: MaterialCardView

    //c_key
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)

        initializWidget()
        key = intent.getStringExtra("key").toString()
        d_quiz_category.setText(intent.getStringExtra("c_name"))

        //difficulty easy click events
        d_dif_easy.setOnClickListener {
            goToQuiz("Easy")
        }
        d_dif_easy.setOnLongClickListener {
            viewAlertDialog("Easy")
            return@setOnLongClickListener true
        }

        //difficulty medium click events
        d_dif_medium.setOnClickListener {
            goToQuiz("Medium")
        }
        d_dif_medium.setOnLongClickListener {
            viewAlertDialog("Medium")
            return@setOnLongClickListener true
        }

        //difficulty hard click events
        d_dif_hard.setOnClickListener {
            goToQuiz("Hard")
        }
        d_dif_hard.setOnLongClickListener {
            viewAlertDialog("Hard")
            return@setOnLongClickListener true
        }

    }

    private fun goToQuiz(s: String) {
        val intent = Intent(this@DifficultyActivity, QuizActivity::class.java)
        intent.putExtra("difficulty", s)
        intent.putExtra("key", key)
        startActivity(intent)
    }

    private fun viewAlertDialog(s: String) {
        val message: String = if (s == "Easy") {
            "In this difficulty you will face some easy and basic questions of ${d_quiz_category.text}"
        } else if (s == "Medium") {
            "In this difficulty you will face questions of ${d_quiz_category.text} with slightly high difficulty from 'Easy' quiz"
        } else {
            "In this difficulty you will face questions with complex logic of ${d_quiz_category.text}"
        }
        val alertDialog = AlertDialog.Builder(this@DifficultyActivity)
            .setPositiveButton(
                "Exit"
            ) { dialog, _ -> dialog!!.dismiss() }
            .setTitle(s)
            .setMessage(message)
            .create()
        alertDialog.show()
    }

    private fun initializWidget() {
        d_quiz_category = findViewById(R.id.d_quiz_category)
        d_dif_easy = findViewById(R.id.d_dif_easy)
        d_dif_medium = findViewById(R.id.d_dif_medium)
        d_dif_hard = findViewById(R.id.d_dif_hard)
    }
}