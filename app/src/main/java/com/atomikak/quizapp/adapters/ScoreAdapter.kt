package com.atomikak.quizapp.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.supportclasses.Score

class ScoreAdapter(val context:Context,val scoreList:ArrayList<Score>) :RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scores_list_item,parent,false)
        return ScoreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.username.text = scoreList[position].c_name.toString()
        holder.score.text = scoreList[position].highscore.toString()
    }

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.sl_userName)
        val score = itemView.findViewById<TextView>(R.id.sl_score)
    }
}
