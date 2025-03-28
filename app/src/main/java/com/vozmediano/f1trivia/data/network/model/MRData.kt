package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MRData(
    @Json(name = "xmlns") val xmlns: String,
    @Json(name = "series") val series: String,
    @Json(name = "url") val url: String,
    @Json(name = "limit") val limit: String,
    @Json(name = "offset") val offset: String,
    @Json(name = "total") val total: String,
    @Json(name = "DriverTable") val driverTable: DriverTable
)