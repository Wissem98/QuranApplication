package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class Tafseer(
    val author: String,
    val book_name: String,
    @SerializedName("id")
    val id: Int,
    val language: String,
    @SerializedName("name")
    val name: String
)