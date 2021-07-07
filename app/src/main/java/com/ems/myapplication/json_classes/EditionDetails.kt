package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class EditionDetails(
        val code: Int,
        @SerializedName("data")
        val readerData: List<ReaderData>,
        val status: String
)