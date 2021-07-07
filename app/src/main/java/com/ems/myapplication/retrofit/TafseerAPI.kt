package com.ems.myapplication.retrofit

import com.ems.myapplication.json_classes.Tafseer
import com.ems.myapplication.json_classes.VerseTafseer
import com.ems.myapplication.models.Verse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TafseerAPI {

    @GET("tafseer")
    suspend fun getTafseerList(): Response<List<Tafseer>>

    @GET("tafseer/{tafseerId}/{suraId}/{verseId}")
    suspend fun getTafseer(@Path("tafseerId") tafseerId: Int,
                           @Path("suraId") suraId: Int,
                           @Path("verseId") verseId: Int): Response<VerseTafseer>

}