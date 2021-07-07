package com.ems.myapplication.database

import com.ems.myapplication.models.Verse

class VerseRepository(private val verseDao: VerseDao) {

    fun insertVerse(verse: Verse) {
        verseDao.insertVerse(verse)
    }

    fun getVerses(first: String, second: String, third: String): List<Verse> {
        return verseDao.getVerses(first, second, third)
    }

    fun getVersesCount(): Int {
        return verseDao.getVersesCount()
    }

    fun updateVerse(verse: Verse) {
        verseDao.updateVerse(verse)
    }

    fun getFavoriteVerses(): List<Verse> {
        return verseDao.getFavoriteVerses()
    }

    fun getVerse(sourahId: Int, verseId: Int): Verse {
        return verseDao.getVerse(sourahId, verseId)
    }
}