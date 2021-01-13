package com.rafaelgalvezruiz.apptranslate.api


import com.rafaelgalvezruiz.apptranslate.dataclass.DetectionResponse
import com.rafaelgalvezruiz.apptranslate.dataclass.Language
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("/0.2/languages")
    suspend fun getLanguage(): Response<List<Language>>

    @Headers("Authorization: Bearer 1e34d05d84c0433b194ff5ec60c3fbcd")
    @FormUrlEncoded
    @POST("/0.2/detect")
    suspend fun getTextLanguage(@Field("q")text:String):Response<DetectionResponse>
}