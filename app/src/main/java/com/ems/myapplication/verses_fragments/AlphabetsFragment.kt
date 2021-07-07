package com.ems.myapplication.verses_fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ems.myapplication.R
import com.ems.myapplication.adapters.AlphabetsAdapter
import com.ems.myapplication.database.*
import com.ems.myapplication.models.Verse
import com.ems.myapplication.models.Word
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.alphabets_fragment.*
import kotlinx.android.synthetic.main.verse_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory

class AlphabetsFragment : Fragment(R.layout.alphabets_fragment) {

    private val alphabet = arrayOf('ي', 'و', 'ه', 'ن', 'م', 'ل', 'ك', 'ق', 'ف', 'غ',
            'ع', 'ظ', 'ط', 'ض', 'ص', 'ش', 'س', 'ز', 'ر', 'ذ', 'د', 'خ', 'ح', 'ج', 'ث', 'ت',
            'ب', 'أ')

    private lateinit var manager: LinearLayoutManager
    private lateinit var alphabetsAdapter: AlphabetsAdapter
    private lateinit var quranDatabase: QuranDatabase
    private lateinit var wordDao: WordDao
    private lateinit var wordRepository: WordRepository
    private lateinit var verseDao: VerseDao
    private lateinit var verseRepository: VerseRepository
    var excelFile: POIFSFileSystem = POIFSFileSystem()
    private lateinit var workbook: Workbook
    private val TAG = "AlphabetsFragment"
    private val mAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeDatabase()

        manager = LinearLayoutManager(context)
        alphabetsAdapter = AlphabetsAdapter(alphabet.reversedArray()) { char -> onItemClicked(char)}

        alphabetRecycler.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = alphabetsAdapter
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getWords()
        }

        favoriteVersesBtn.setOnClickListener {
            val toFavorites = AlphabetsFragmentDirections.actionAlphabetsFragmentToFavoriteVersesFragment()
            findNavController().navigate(toFavorites)
        }

        logoutBtn.setOnClickListener {
            mAuth.signOut()
            requireActivity().finish()
        }

        cloudBtn.setOnClickListener {
            val toCloudVerses = AlphabetsFragmentDirections.actionAlphabetsFragmentToCloudVersesFragment()
            findNavController().navigate(toCloudVerses)
        }

    }


    private fun initializeDatabase() {
        quranDatabase = QuranDatabase.getDatabase(requireContext())
        wordDao = quranDatabase.wordDao()
        wordRepository = WordRepository(wordDao)
        verseDao = quranDatabase.verseDao()
        verseRepository = VerseRepository(verseDao)
    }


    private fun onItemClicked(char: Char) {
        Log.d(TAG, char.toString())
        val toWords = AlphabetsFragmentDirections.actionAlphabetsFragmentToWordsFragment(char.toString())
        findNavController().navigate(toWords)
    }


    private suspend fun getExcelFile() {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "fetching data in database", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "this will take up to 1 minute", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "getting excel file")
        try {
            val assets = requireContext().assets
            val file = assets.open("quran_database.xls")
            excelFile = POIFSFileSystem(file)
            workbook = WorkbookFactory.create(excelFile)
            saveWordsInDatabase()
            saveVersesInDatabase()
        } catch (e: Exception) {
            Log.d(TAG, e.message ?: "error getting excel")
        }

    }


    private fun showProgressBar() {
        alphabetProgressBar.visibility = ProgressBar.VISIBLE
        alphabetRecycler.visibility = RecyclerView.INVISIBLE
    }


    private fun hideProgressBar() {
        alphabetProgressBar.visibility = ProgressBar.INVISIBLE
        alphabetRecycler.visibility = RecyclerView.VISIBLE
    }


    private suspend fun saveWordsInDatabase() {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "saving data...", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "saving words in db")
        val sheet = workbook.getSheetAt(3)
        val formatter = DataFormatter()
        val rows = sheet.physicalNumberOfRows

        for (i in 0 until rows) {
            val currentRow = sheet.getRow(i)
            val currentCell = currentRow.getCell(1)
            val firstLetterCell = currentRow.getCell(2)
            val secondLetterCell = currentRow.getCell(3)
            val thirdLetterCell = currentRow.getCell(4)
            val fourthLetterCell = currentRow.getCell(5)
            if (formatter.formatCellValue(currentCell).isEmpty()) {
                break
            }
            val racineWord = formatter.formatCellValue(currentCell)
            val firstLetter = formatter.formatCellValue(firstLetterCell)
            val secondLetter = formatter.formatCellValue(secondLetterCell)
            val thirdLetter = formatter.formatCellValue(thirdLetterCell)
            val fourthLetter = formatter.formatCellValue(fourthLetterCell)
            val word = Word(word = racineWord, firstLetter = firstLetter, secondLetter = secondLetter,
                    thirdLetter = thirdLetter, fourthLetter = fourthLetter)
            wordRepository.insertWord(word)
        }
        Log.d(TAG, "words saved")
    }

    private suspend fun saveVersesInDatabase() {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Almost Done ...", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "saving verses in db")
        val sheet = workbook.getSheetAt(1)
        val formatter = DataFormatter()
        val rows = sheet.physicalNumberOfRows

        for (i in 1 until rows) {
            val currentRow = sheet.getRow(i)
            val verseCell = currentRow.getCell(3)
            val verseIdCell = currentRow.getCell(2)
            val sourahCell = currentRow.getCell(1)
            if (formatter.formatCellValue(verseCell).isEmpty()) {
                break
            }
            val verseText = formatter.formatCellValue(verseCell)
            val verseId = formatter.formatCellValue(verseIdCell)
            val sourahId = formatter.formatCellValue(sourahCell)
            val verse = Verse(verseText = verseText, verseIdentifier = verseId.toInt(),
                    sourahId = sourahId.toInt())
            verseRepository.insertVerse(verse)
        }
        Log.d(TAG, "verses saved")
    }


    private suspend fun getWords() {
        Log.d(TAG, "starting database call")
        withContext(Dispatchers.Main) {
            showProgressBar()
        }
        Log.d(TAG, "started database call")
        val words = wordRepository.getWordsCount()
        val verses = verseRepository.getVersesCount()
        if (words == 0 || verses == 0) {
            getExcelFile()
        }
        withContext(Dispatchers.Main) {
            hideProgressBar()
        }
    }

}