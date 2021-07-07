package com.ems.myapplication.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "verse_table")
data class Verse (@PrimaryKey(autoGenerate = true)
                  var id: Int? = null,
                  val verseText: String = "",
                  @SerializedName("text")
                  var translation: String = "",
                  var tafseer: String = "",
                  val verseIdentifier: Int = 0,
                  var audioUrl: String = "",
                  var isFavorite: Boolean = false,
                  val sourahId: Int = 0) : Parcelable