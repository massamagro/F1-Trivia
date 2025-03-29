package com.vozmediano.f1trivia.data.network.model.constructor

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConstructorTable(
    @Json(name = "season") val season: String? = null,
    @Json(name = "Constructors") val constructorDtos: List<ConstructorDto>? = null,
    @Json(name = "constructorId") val constructorId: String? = null
)
