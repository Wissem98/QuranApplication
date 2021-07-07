package com.ems.myapplication.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "word_table")
data class Word(@PrimaryKey(autoGenerate = true)
                val id: Int? = null,
                val word: String,
                val firstLetter: String,
                val secondLetter: String = "",
                val thirdLetter: String = "",
                val fourthLetter: String = ""): Parcelable