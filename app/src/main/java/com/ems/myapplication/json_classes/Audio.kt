package com.ems.myapplication.json_classes

import com.google.gson.annotations.SerializedName

data class Audio(@SerializedName("audio")
                 val audioUrl: String)