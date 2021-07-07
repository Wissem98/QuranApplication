package com.ems.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ems.myapplication.R
import kotlinx.android.synthetic.main.word_item.view.*

class AlphabetsAdapter(private val alphabets: Array<Char>, private val onItemClicked: (Char) -> Unit) :
        RecyclerView.Adapter<AlphabetsAdapter.AlphabetViewHolder>() {

    inner class AlphabetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return AlphabetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        holder.itemView.apply {
            word.text = alphabets[position].toString()
            setOnClickListener {
                onItemClicked(alphabets[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return alphabets.size
    }
}