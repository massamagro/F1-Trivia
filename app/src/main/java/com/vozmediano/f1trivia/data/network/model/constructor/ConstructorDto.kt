package com.vozmediano.f1trivia.data.network.model.constructor

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConstructorDto(
    @Json(name = "constructorId") val constructorId: String,
    @Json(name = "url") val url: String,
    @Json(name = "name") val name: String,
    @Json(name = "nationality") val nationality: String
)

