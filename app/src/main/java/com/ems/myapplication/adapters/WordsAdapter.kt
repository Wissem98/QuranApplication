package com.ems.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ems.myapplication.R
import com.ems.myapplication.models.Word
import kotlinx.android.synthetic.main.word_item.view.*

class WordsAdapter(private val onItemClick: (Word) -> Unit)
    : RecyclerView.Adapter<WordsAdapter.WordViewHolder>(), Filterable {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id!! == newItem.id!!
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    var allWords = differ.currentList
    private var filteredWords = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.itemView.apply {
            word.text = differ.currentList[position].word
            setOnClickListener {
                onItemClick(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchStr = constraint?.toString()!!
                val filterResults = FilterResults()
                filteredWords = if (searchStr.isEmpty()) {
                    allWords
                } else {
                    val filteredList = mutableListOf<Word>()
                    for (word in differ.currentList) {
                        if (word.word.contains(searchStr, ignoreCase = true)) {
                            filteredList.add(word)
                        }
                    }
                    filteredList
                }
                filterResults.values = filteredWords
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredWords = results?.values as List<Word>
                differ.submitList(filteredWords)
            }
        }
    }

}