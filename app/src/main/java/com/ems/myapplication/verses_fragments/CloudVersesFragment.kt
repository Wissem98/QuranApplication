package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ems.myapplication.R
import com.ems.myapplication.adapters.VersesAdapter
import com.ems.myapplication.models.Verse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.verses_fragment.*

class CloudVersesFragment : Fragment(R.layout.verses_fragment) {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var manager: LinearLayoutManager
    private lateinit var versesAdapter: VersesAdapter
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseRef = firebaseDatabase.reference

        manager = LinearLayoutManager(context)
        versesAdapter = VersesAdapter { verse -> onItemClicked(verse) }

        versesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = versesAdapter
        }

        getVersesFromCloud()

    }


    private fun getVersesFromCloud() {
        databaseRef.child(currentUser?.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val verses = mutableListOf<Verse>()
                snapshot.children.forEach {
                    val map = it.value as HashMap<*, *>
                    val verse = Verse(verseText = map["verseText"].toString(),
                        translation = map["translation"].toString(),
                        tafseer = map["tafseer"].toString(),
                        verseIdentifier = map["verseIdentifier"].toString().toInt(),
                        audioUrl = map["audioUrl"].toString(),
                        sourahId = map["sourahId"].toString().toInt())
                    verses.add(verse)
                }
                versesAdapter.differ.submitList(verses)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun onItemClicked(verse: Verse) {
        val toVerse = CloudVersesFragmentDirections
            .actionCloudVersesFragmentToCloudVerseDetailsFragment(verse)
        findNavController().navigate(toVerse)
    }

}