package com.ems.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ems.myapplication.R
import com.ems.myapplication.models.Verse
import kotlinx.android.synthetic.main.verse_item.view.*

class VersesAdapter(private val onItemClick: (Verse) -> Unit)
    : RecyclerView.Adapter<VersesAdapter.VerseViewHolder>() {

    inner class VerseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<Verse>() {
        override fun areItemsTheSame(oldItem: Verse, newItem: Verse): Boolean {
            return oldItem.id!! == newItem.id!!
        }

        override fun areContentsTheSame(oldItem: Verse, newItem: Verse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.verse_item, parent, false)
        return VerseViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        holder.itemView.apply {
            verse.text = differ.currentList[position].verseText
            setOnClickListener {
                onItemClick(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}