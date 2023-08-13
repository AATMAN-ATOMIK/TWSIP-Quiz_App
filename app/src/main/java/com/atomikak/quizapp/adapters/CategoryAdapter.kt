package com.atomikak.quizapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.atomikak.quizapp.R
import com.atomikak.quizapp.activities.DifficultyActivity
import com.atomikak.quizapp.supportclasses.Category
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(val context: Context, val categoryList: ArrayList<Category>,val userName: String) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.categroy_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.cat_name.setText(categoryList[position].c_name)
        Glide.with(context).load(categoryList[position].c_img.toUri()).into(holder.cat_image)

        //click event
        holder.my_category_card.setOnClickListener {
            val intent = Intent(context,DifficultyActivity::class.java)
            intent.putExtra("c_name",categoryList[position].c_name.toString())
            intent.putExtra("key",categoryList[position].key.toString())
            intent.putExtra("userName",userName)
            context.startActivity(intent)
        }

        //long click event
        holder.my_category_card.setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle(holder.cat_name.text.toString())
                .setMessage(categoryList[position].c_desc.toString())
                .setPositiveButton("Exit"
                ) { dialog, _ -> dialog!!.dismiss() }
                .create()
            alertDialog.show()
            return@setOnLongClickListener true
        }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val my_category_card: MaterialCardView = itemView.findViewById(R.id.my_category_card)
        val cat_image: CircleImageView = itemView.findViewById(R.id.cat_image)
        val cat_name: TextView = itemView.findViewById(R.id.cat_name)
    }
}
