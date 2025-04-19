package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikiResponse(
    @Json(name = "items") val items: List<WikiItem>
)

@JsonClass(generateAdapter = true)
data class WikiItem(
    @Json(name = "srcset") val srcset: List<WikiSrc>
)

@JsonClass(generateAdapter = true)
data class WikiSrc(
    @Json(name = "src") val src: String
)
