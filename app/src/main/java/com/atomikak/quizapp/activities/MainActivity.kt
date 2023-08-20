package com.atomikak.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.adapters.CategoryAdapter
import com.atomikak.quizapp.supportclasses.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //recyclerview
    private lateinit var m_recv_category: RecyclerView

    //lotti button
    private lateinit var m_btn_profile: LottieAnimationView
    private lateinit var loader: LottieAnimationView

    //textview
    private lateinit var m_user_name: TextView

    //adapters
    private lateinit var cAdapter: CategoryAdapter

    //list
    private lateinit var categoryList: ArrayList<Category>

    //database
    private val db = Firebase.firestore
    private val collectionRef = db.collection("Quiz Category")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializWidgets()

        val userSnapshot =
            db.collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get()
                .addOnSuccessListener {
                    m_user_name.text = it.get("name").toString()
                }



        getCategoryList()

    }

    private fun getCategoryList() {
        collectionRef.orderBy("c_id").get().addOnCompleteListener {
            for (category in it.result) {
                categoryList.add(
                    Category(
                        key=category.id,
                        c_id = category.getString("c_id")!!,
                        c_name = category.getString("c_name")!!,
                        c_desc = category.getString("c_desc")!!,
                        c_img = category.getString("c_img")!!,
                    )
                )
                Log.d("DD: ", category.id!!.toString())
            }
            loadCategory()
        }
    }

    private fun loadCategory() {
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show()
        } else {
            loader.visibility = LottieAnimationView.GONE
            cAdapter = CategoryAdapter(this@MainActivity, categoryList,m_user_name.text.toString())
            m_recv_category.layoutManager =
                GridLayoutManager(this@MainActivity, 2, GridLayoutManager.VERTICAL, false)
            m_recv_category.setHasFixedSize(true)
            m_recv_category.adapter = cAdapter

        }
    }

    private fun initializWidgets() {
        m_recv_category = findViewById(R.id.m_recv_category)
        m_btn_profile = findViewById(R.id.m_btn_profile)
        m_user_name = findViewById(R.id.m_user_name)
        loader = findViewById(R.id.loader)
        categoryList = ArrayList()

        m_btn_profile.setOnClickListener{
            val intent = Intent(this@MainActivity,ProfileScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}