package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class AudioData(
    @SerializedName("audio")
    val audio: String,
    val audioSecondary: List<String>,
    val edition: EditionX,
    val hizbQuarter: Int,
    val juz: Int,
    val manzil: Int,
    val number: Int,
    val numberInSurah: Int,
    val page: Int,
    val ruku: Int,
    val sajda: Boolean,
    val surah: SurahX,
    val text: String
)