package com.ems.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ems.myapplication.models.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertWord(word: Word)

    @Query("SELECT * FROM word_table WHERE firstLetter = :firstLetter")
    fun getWords(firstLetter: String): List<Word>

    @Query("SELECT COUNT(*) FROM word_table")
    fun getWordsCount(): Int
}