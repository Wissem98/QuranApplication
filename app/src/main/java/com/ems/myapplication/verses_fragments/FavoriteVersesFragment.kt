package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ems.myapplication.R
import com.ems.myapplication.adapters.VersesAdapter
import com.ems.myapplication.database.QuranDatabase
import com.ems.myapplication.database.VerseDao
import com.ems.myapplication.database.VerseRepository
import com.ems.myapplication.models.Verse
import kotlinx.android.synthetic.main.verses_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteVersesFragment : Fragment(R.layout.verses_fragment) {

    private lateinit var quranDatabase: QuranDatabase
    private lateinit var versesDao: VerseDao
    private lateinit var versesRepository: VerseRepository
    private lateinit var manager: LinearLayoutManager
    private lateinit var verseAdapter: VersesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeDatabase()
        manager = LinearLayoutManager(context)
        verseAdapter = VersesAdapter { verse -> onItemClicked(verse) }

        versesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = verseAdapter
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getFavoriteVerses()
        }

    }


    private fun initializeDatabase() {
        quranDatabase = QuranDatabase.getDatabase(requireContext())
        versesDao = quranDatabase.verseDao()
        versesRepository = VerseRepository(versesDao)
    }


    private fun getFavoriteVerses() {
        val verses = versesRepository.getFavoriteVerses()
        verseAdapter.differ.submitList(verses)
    }


    private fun onItemClicked(verse: Verse) {
        val toDetails = FavoriteVersesFragmentDirections
            .actionFavoriteVersesFragmentToFavoriteVerseDetails(verse)
        findNavController().navigate(toDetails)
    }

}