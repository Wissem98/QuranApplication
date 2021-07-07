package com.ems.myapplication.verses_fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ems.myapplication.R
import com.ems.myapplication.database.QuranDatabase
import com.ems.myapplication.database.VerseDao
import com.ems.myapplication.database.VerseRepository
import com.ems.myapplication.models.Verse
import com.ems.myapplication.retrofit.RetrofitInstance
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.verse_details_fragment.*
import kotlinx.coroutines.*
import java.lang.Exception

class VerseDetailsFragment : Fragment(R.layout.verse_details_fragment) {

    private lateinit var readersId: MutableList<String>
    private lateinit var currentReader: String
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioUrl: String
    private lateinit var quranDatabase: QuranDatabase
    private lateinit var verseDao: VerseDao
    private lateinit var verseRepository: VerseRepository
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val args: VerseDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseRef = firebaseDatabase.reference
        readersId = mutableListOf()
        currentReader = "ar.alafasy"
        audioUrl = ""

        checkIfVerseIsFavorite()

        initializeDatabase()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgressBars()
            }
            getTafseerList()
            getReadersList()
            getTafseer(1, args.verse.sourahId, args.verse.verseIdentifier)
            getVerseTranslation(args.verse.sourahId, args.verse.verseIdentifier)
            getAudio(args.verse.sourahId, args.verse.verseIdentifier, currentReader)
            initializeMediaPlayer()
            withContext(Dispatchers.Main) {
                hideProgressBars()
            }
        }

        (tafseerType.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            lifecycleScope.launch(Dispatchers.Main) {
                getTafseer(position + 1, args.verse.sourahId, args.verse.verseIdentifier)
            }
        }

        (verseReader.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            currentReader = readersId[position]
            lifecycleScope.launch(Dispatchers.IO) {
                mediaPlayer.release()
                withContext(Dispatchers.Main) {
                    playBtn.isClickable = false
                    playBtn.setBackgroundResource(R.drawable.ic_play)
                }
                getAudio(args.verse.sourahId, args.verse.verseIdentifier, currentReader)
                initializeMediaPlayer()
                withContext(Dispatchers.Main) {
                    playBtn.isClickable = true
                }
            }
        }

        playBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    withContext(Dispatchers.Main) {
                        playBtn.setBackgroundResource(R.drawable.ic_pause)
                    }
                } else {
                    mediaPlayer.pause()
                    withContext(Dispatchers.Main) {
                        playBtn.setBackgroundResource(R.drawable.ic_play)
                    }
                }
            }
        }

        mushafBtn.setOnClickListener {
            val toMushaf = VerseDetailsFragmentDirections
                .actionVerseDetailsFragmentToMoushafFragment(args.verse)
            findNavController().navigate(toMushaf)
        }

        favBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                updateVerse(args.verse)
            }
        }

        uploadBtn.setOnClickListener {
            val map = hashMapOf<String, Any>()
            map["verseText"] = args.verse.verseText
            map["translation"] = args.verse.translation
            map["tafseer"] = args.verse.tafseer
            map["verseIdentifier"] = args.verse.verseIdentifier
            map["audioUrl"] = args.verse.audioUrl
            map["sourahId"] = args.verse.sourahId
            uploadVerse(map)
        }
    }


    private fun initializeDatabase() {
        quranDatabase = QuranDatabase.getDatabase(requireContext())
        verseDao = quranDatabase.verseDao()
        verseRepository = VerseRepository(verseDao)
    }


    private suspend fun getVerseTranslation(sourat: Int, ayah: Int) {
        supervisorScope {
            try {
                val task = async { RetrofitInstance.quranApi.getVerseTranslation(sourat, ayah) }
                val result = task.await()
                if (result.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        verseTranslation.text = result.body()!!.data.text
                        args.verse.translation = result.body()!!.data.text
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun getTafseerList() {
        supervisorScope {
            try {
                val task = async { RetrofitInstance.tafseerApi.getTafseerList() }
                val result = task.await()
                if (result.isSuccessful) {
                    val tafseerList = result.body()!!
                    val items = mutableListOf<String>()
                    for (tafseer in tafseerList) {
                        items.add(tafseer.name)
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
                    withContext(Dispatchers.Main) {
                        (tafseerType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun getTafseer(tafseerId: Int, suraId: Int, verseId: Int) {
        supervisorScope {
            try {
                val task = async { RetrofitInstance.tafseerApi.getTafseer(tafseerId, suraId, verseId) }
                val result = task.await()
                if (result.isSuccessful) {
                    val tafseer = result.body()!!
                    args.verse.tafseer = tafseer.text
                    withContext(Dispatchers.Main) {
                        verseExplanation.text = tafseer.text
                        args.verse.tafseer = tafseer.text
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun getReadersList() {
        supervisorScope {
            try {
                val task = async { RetrofitInstance.quranApi.getReaders() }
                val result = task.await()
                if (result.isSuccessful) {
                    val readersList = result.body()!!.readerData
                    val items = mutableListOf<String>()
                    for (reader in readersList) {
                        items.add(reader.englishName)
                        readersId.add(reader.identifier)
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
                    withContext(Dispatchers.Main) {
                        (verseReader.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun getAudio(sourahId: Int, ayah: Int, readerId: String) {
        supervisorScope {
            try {
                val task = async { RetrofitInstance.quranApi.getVerseAudio(sourahId, ayah, readerId) }
                val result = task.await()
                if (result.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        args.verse.audioUrl = result.body()!!.audioData.audio
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun initializeMediaPlayer() {
        val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(attributes)
            setDataSource(args.verse.audioUrl)
            prepare()
        }
    }


    private fun showProgressBars() {
        playBtn.visibility = Button.INVISIBLE
        verseReader.visibility = TextInputLayout.INVISIBLE
        explanationScroll.visibility = ScrollView.INVISIBLE
        translationScroll.visibility = ScrollView.INVISIBLE
        explanationProgressBar.visibility = ProgressBar.VISIBLE
        translationProgressBar.visibility = ProgressBar.VISIBLE
        readerProgressBar.visibility = ProgressBar.VISIBLE
    }


    private fun hideProgressBars() {
        playBtn.visibility = Button.VISIBLE
        verseReader.visibility = TextInputLayout.VISIBLE
        explanationScroll.visibility = ScrollView.VISIBLE
        translationScroll.visibility = ScrollView.VISIBLE
        explanationProgressBar.visibility = ProgressBar.INVISIBLE
        translationProgressBar.visibility = ProgressBar.INVISIBLE
        readerProgressBar.visibility = ProgressBar.INVISIBLE
    }


    private fun checkIfVerseIsFavorite() {
        if (args.verse.isFavorite) {
            favBtn.setBackgroundResource(R.drawable.ic_bookmark)
        } else {
            favBtn.setBackgroundResource(R.drawable.ic_empty_bookmark)
        }
    }


    private suspend fun updateVerse(verse: Verse) {
        getVerse(args.verse.sourahId, args.verse.verseIdentifier)
        args.verse.isFavorite = !args.verse.isFavorite
        verseRepository.updateVerse(verse)
        withContext(Dispatchers.Main) {
            checkIfVerseIsFavorite()
        }
    }


    private fun getVerse(sourahId: Int, verseId: Int) {
        val verse = verseRepository.getVerse(sourahId, verseId)
        args.verse.id = verse.id
    }


    private fun uploadVerse(map: HashMap<String, Any>) {
        databaseRef.child(currentUser?.uid.toString()).child(map["sourahId"].toString()
                + ":" + map["verseIdentifier"].toString()).updateChildren(map).apply {

            addOnCompleteListener {
                Toast.makeText(context, "verse saved in the cloud", Toast.LENGTH_SHORT).show()
            }

            addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}