package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class AudioDetails(
        val code: Int,
        @SerializedName("data")
        val audioData: AudioData,
        val status: String
)