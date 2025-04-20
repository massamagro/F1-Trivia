package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.ImageEntity
import com.vozmediano.f1trivia.data.network.model.WikiItem
import com.vozmediano.f1trivia.data.network.model.WikiResponse
import com.vozmediano.f1trivia.domain.model.wiki.Image

fun WikiResponse.toDomain(title:String) = Image(
    title = title,
    imageUrls = getUrls(items)
    //imageUrls = this.items.filter{ it.leadImage }.first().srcset.first().src
)



fun Image.toDatabase() = ImageEntity(
    title = title,
    imageUrls = imageUrls
)

fun ImageEntity.toDomain() = Image(
    title = title,
    imageUrls = imageUrls
)


fun getUrls(items: List<WikiItem>): List<String> {
    val urls = mutableListOf<String>()
    items.filter { it.type == "image" }.forEach{ urls.add( it.srcset.first().src )}
    return urls
}