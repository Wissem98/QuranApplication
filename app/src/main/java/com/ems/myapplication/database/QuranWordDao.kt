package com.ems.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ems.myapplication.models.QuranWord

@Dao
interface QuranWordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertQuranWord(quranWord: QuranWord)

    @Query("SELECT * FROM quran_word_table WHERE word = :word")
    fun getQuranWords(word: String): List<QuranWord>

    @Query("SELECT COUNT(*) FROM quran_word_table")
    fun getQuranWordCount(): Int
}