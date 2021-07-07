package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class VerseDetails(
        val code: Int,
        @SerializedName("data")
    val `data`: Data,
        val status: String
)