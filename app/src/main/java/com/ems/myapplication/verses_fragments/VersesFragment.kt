package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ems.myapplication.R
import com.ems.myapplication.adapters.VersesAdapter
import com.ems.myapplication.database.QuranDatabase
import com.ems.myapplication.database.VerseDao
import com.ems.myapplication.database.VerseRepository
import com.ems.myapplication.models.Verse
import com.ems.myapplication.models.Word
import kotlinx.android.synthetic.main.verses_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VersesFragment : Fragment(R.layout.verses_fragment) {

    private lateinit var quranDatabase: QuranDatabase
    private lateinit var verseDao: VerseDao
    private lateinit var verseRepository: VerseRepository
    private lateinit var manager: LinearLayoutManager
    private lateinit var versesAdapter: VersesAdapter
    private val TAG = "VersesFragment"
    private val args: VersesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeDatabase()

        manager = LinearLayoutManager(context)
        versesAdapter = VersesAdapter { verse -> onItemClicked(verse) }

        versesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = versesAdapter
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getVerses(args.word)
        }

    }


    private fun initializeDatabase() {
        quranDatabase = QuranDatabase.getDatabase(requireContext())
        verseDao = quranDatabase.verseDao()
        verseRepository = VerseRepository(verseDao)
    }


    private fun getVerses(word: Word) {
        val verses = verseRepository.getVerses(word.firstLetter, word.secondLetter, word.thirdLetter)
        versesAdapter.differ.submitList(verses)
    }


    private fun onItemClicked(verse: Verse) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
        val toVerseDetails = VersesFragmentDirections.actionVersesFragmentToVerseDetailsFragment(verse)
        findNavController().navigate(toVerseDetails)
    }

}