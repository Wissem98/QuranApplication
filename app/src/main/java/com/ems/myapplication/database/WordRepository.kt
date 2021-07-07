package com.ems.myapplication.database

import com.ems.myapplication.models.Word

class WordRepository(private val wordDao: WordDao) {

    fun getWords(firstLetter: String): List<Word> {
        return wordDao.getWords(firstLetter)
    }

    fun insertWord(word: Word) {
        wordDao.insertWord(word)
    }

    fun getWordsCount(): Int {
        return wordDao.getWordsCount()
    }
}