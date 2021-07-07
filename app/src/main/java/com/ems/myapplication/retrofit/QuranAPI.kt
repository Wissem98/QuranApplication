package com.ems.myapplication.retrofit

import com.ems.myapplication.json_classes.*
import com.ems.myapplication.models.Verse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranAPI {

    @GET("ayah/{sourat}:{ayah}/en.asad")
    suspend fun getVerseTranslation(@Path("sourat") sourat: Int,
                                    @Path("ayah") ayah: Int): Response<VerseDetails>

    @GET("edition?format=audio&language=ar")
    suspend fun getReaders(): Response<EditionDetails>

    @GET("ayah/{sourat}:{ayah}/{readerId}")
    suspend fun getVerseAudio(@Path("sourat") sourat: Int,
                              @Path("ayah") ayah: Int,
                              @Path("readerId") reader: String): Response<AudioDetails>

}