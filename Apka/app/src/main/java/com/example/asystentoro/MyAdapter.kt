package com.example.asystentoro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemcard.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MyAdapter(private val exampleList: List<ItemCardView>):RecyclerView.Adapter<MyAdapter.ViewHolderCard>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCard {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemcard,
        parent, false)

        return ViewHolderCard(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderCard, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2


    }

    override fun getItemCount() = exampleList.size


    class ViewHolderCard(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageViewcard) //findbyid can be
        val textView1: TextView = itemView.text_view_card1
        val textView2: TextView = itemView.text_view_card2

    }
}


