package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class VerseTafseer(
    val ayah_number: Int,
    val ayah_url: String,
    val tafseer_id: Int,
    val tafseer_name: String,
    @SerializedName("text")
    val text: String
)