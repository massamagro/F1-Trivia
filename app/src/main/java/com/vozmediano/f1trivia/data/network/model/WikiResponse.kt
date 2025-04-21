package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikiResponse(
    @Json(name = "items") val items: List<WikiItem>
)

@JsonClass(generateAdapter = true)
data class WikiItem(
    @Json(name = "type") val type: String,
    @Json(name = "srcset") val srcset: List<WikiSrc>?,
    @Json(name = "leadImage") val leadImage: Boolean
)

@JsonClass(generateAdapter = true)
data class WikiSrc(
    @Json(name = "src") val src: String
)
