package com.ems.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_word_table")
data class QuranWord(@PrimaryKey(autoGenerate = true)
                     val id: Int? = null,
                     val word: String = "",
                     val verseId: Int = 0,
                     val souratId: Int = 0)