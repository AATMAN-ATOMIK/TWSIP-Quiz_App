package com.atomikak.quizapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.atomikak.quizapp.R

class ScoreListAdapter(val context: Context, val scoreList:ArrayList<Pair<String,Any?>>): BaseAdapter() {
    override fun getCount(): Int {
        return scoreList.size
    }

    override fun getItem(position: Int): Any {
        return scoreList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.profile_listview_item,parent,false)

        val p_quiz_name: TextView = view.findViewById(R.id.p_quiz_name)
        val p_score: TextView = view.findViewById(R.id.p_score)

        p_quiz_name.text = scoreList[position].first.toString()
        p_score.text = scoreList[position].second.toString()

        return view
    }
}