package com.atomikak.quizapp.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.activities.QuizActivity
import com.atomikak.quizapp.listeners.RecvClickListener
import com.atomikak.quizapp.supportclasses.Question

class QuizAdapter(
    val quizActivity: QuizActivity,
    val quizList: ArrayList<Question>,
    val recvClickListener: RecvClickListener
) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_layout, parent, false)
        return QuizViewHolder(view, recvClickListener)
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.ql_question.text = quizList[position].question
        quizList[position].options.shuffle()
        holder.ql_option1.text = quizList[position].options[0]
        holder.ql_option2.text = quizList[position].options[1]
        holder.ql_option3.text = quizList[position].options[2]
        holder.ql_option4.text = quizList[position].options[3]

        holder.ql_option1.setOnClickListener {
            holder.ql_option4.isEnabled = false
            holder.ql_option2.isEnabled = false
            holder.ql_option3.isEnabled = false
            recvClickListener.onItemClick(it,position,quizList[position].options[0])
        }
        holder.ql_option2.setOnClickListener {
            holder.ql_option1.isEnabled = false
            holder.ql_option4.isEnabled = false
            holder.ql_option3.isEnabled = false
            recvClickListener.onItemClick(it,position,quizList[position].options[1])
        }
        holder.ql_option3.setOnClickListener {
            holder.ql_option1.isEnabled = false
            holder.ql_option2.isEnabled = false
            holder.ql_option4.isEnabled = false
            recvClickListener.onItemClick(it,position,quizList[position].options[2])
        }
        holder.ql_option4.setOnClickListener {
            holder.ql_option1.isEnabled = false
            holder.ql_option2.isEnabled = false
            holder.ql_option3.isEnabled = false
            recvClickListener.onItemClick(it,position,quizList[position].options[3])
        }
    }

    class QuizViewHolder(itemView: View, val recvClickListener: RecvClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val ql_question: TextView = itemView.findViewById(R.id.ql_question)
        val ql_option1: TextView = itemView.findViewById(R.id.ql_option1)
        val ql_option2: TextView = itemView.findViewById(R.id.ql_option2)
        val ql_option3: TextView = itemView.findViewById(R.id.ql_option3)
        val ql_option4: TextView = itemView.findViewById(R.id.ql_option4)
    }
}