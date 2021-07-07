package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class Data(
        val edition: Edition,
        val hizbQuarter: Int,
        val juz: Int,
        val manzil: Int,
        val number: Int,
        val numberInSurah: Int,
        val page: Int,
        val ruku: Int,
        val sajda: Boolean,
        val surah: Surah,
        @SerializedName("text")
        val text: String
)