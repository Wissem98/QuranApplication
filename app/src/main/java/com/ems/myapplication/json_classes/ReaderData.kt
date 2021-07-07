package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class ReaderData(
    val direction: String,
    @SerializedName("englishName")
    val englishName: String,
    val format: String,
    @SerializedName("identifier")
    val identifier: String,
    val language: String,
    val name: String,
    val type: String
)