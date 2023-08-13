package com.atomikak.quizapp.listeners

import android.view.View

interface RecvClickListener {
    fun onItemClick(view:View,position:Int,answer:String)
}