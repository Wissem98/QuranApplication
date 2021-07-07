package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ems.myapplication.R
import com.ems.myapplication.adapters.WordsAdapter
import com.ems.myapplication.database.WordDao
import com.ems.myapplication.database.QuranDatabase
import com.ems.myapplication.database.WordRepository
import com.ems.myapplication.models.Word
import kotlinx.android.synthetic.main.words_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordsFragment : Fragment(R.layout.words_fragment) {

    private lateinit var manager: LinearLayoutManager
    private lateinit var wordsAdapter: WordsAdapter
    private lateinit var textWatcher: TextWatcher
    private lateinit var quranDatabase: QuranDatabase
    private lateinit var wordDao: WordDao
    private lateinit var wordRepository: WordRepository
    private val args: WordsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeDatabase()

        manager = LinearLayoutManager(context)
        wordsAdapter = WordsAdapter { word -> onItemClicked(word)}

        initializeTextWatcher()
        wordSearch.editText?.addTextChangedListener(textWatcher)

        lifecycleScope.launch(Dispatchers.IO) {
            getWords(args.letter)
        }

        wordsRecycler.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = wordsAdapter
        }
    }


    private fun initializeDatabase() {
        quranDatabase = QuranDatabase.getDatabase(requireContext())
        wordDao = quranDatabase.wordDao()
        wordRepository = WordRepository(wordDao)
    }

    /*
    private suspend fun getWords() {
        withContext(Dispatchers.Main) {
            wordsProgressBar.visibility = ProgressBar.VISIBLE
        }
        val workbook = alphabetsFragment.workbook!!

        val sheet = workbook.getSheetAt(3)
        val formatter = DataFormatter()
        val words = mutableListOf<String>()

        for (i in 0 until 1800) {
            val currentRow = sheet.getRow(i)
            val currentCell = currentRow.getCell(1)
            if (formatter.formatCellValue(currentCell).isEmpty()) {
                break
            }
            val word = formatter.formatCellValue(currentCell)
            if (words.isNotEmpty() && !word.startsWith(args.letter)) {
                break
            } else if (word.startsWith(args.letter)) {
                Log.d("WordsFragment", word)
                words.add(word)
            }
        }
        withContext(Dispatchers.Main) {
            wordsProgressBar.visibility = ProgressBar.INVISIBLE
            wordsAdapter.differ.submitList(words)
            wordsAdapter.allWords = words
        }
    }

     */


    private fun getWords(firstLetter: String) {
        val words = wordRepository.getWords(firstLetter)
        val racineList = mutableListOf<String>()
        for (word in words) {
            racineList.add(word.word)
        }
        wordsAdapter.differ.submitList(words)
        wordsAdapter.allWords = words
    }


    private fun initializeTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wordsAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
    }


    private fun onItemClicked(word: Word) {
        val toVerses = WordsFragmentDirections.actionWordsFragmentToVersesFragment(word)
        findNavController().navigate(toVerses)
    }

}