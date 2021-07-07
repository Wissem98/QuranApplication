package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class Reader(@SerializedName("identifier")
                  val readerId: String,
                  @SerializedName("englishName")
                  val name: String)