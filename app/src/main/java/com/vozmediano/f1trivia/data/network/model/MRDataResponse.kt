package com.vozmediano.f1trivia.data.network.model

data class MRDataResponse(
    val series: String,
    val url: String,
    val limit: String,
    val offset: String,
    val total: String,
    val DriverTable: DriverTableResponse
)
