package com.ems.myapplication.database

import androidx.room.*
import com.ems.myapplication.models.Verse

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVerse(verse: Verse)

    @Query("SELECT * FROM verse_table WHERE verseText LIKE '%' || :first || '_' || :second || '_' || :third || '%'")
    fun getVerses(first: String, second: String, third: String): List<Verse>

    @Query("SELECT COUNT(*) FROM VERSE_TABLE")
    fun getVersesCount(): Int

    @Query("SELECT * FROM verse_table WHERE isFavorite = 1")
    fun getFavoriteVerses(): List<Verse>

    @Query("SELECT * FROM verse_table WHERE sourahId = :sourahId AND verseIdentifier = :verseId ")
    fun getVerse(sourahId: Int, verseId: Int): Verse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateVerse(verse: Verse)
}