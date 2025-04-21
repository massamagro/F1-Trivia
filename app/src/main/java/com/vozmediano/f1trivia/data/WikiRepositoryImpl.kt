package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.ImageDao
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.WikiService
import com.vozmediano.f1trivia.domain.WikiRepository

class WikiRepositoryImpl(
    private val wikiService: WikiService,
    private val imageDao: ImageDao
) : WikiRepository{
    override suspend fun getImage(title: String): String {
        return try{
            val url = imageDao.getImage(title).imageUrls.first()
            Log.i("WikiRepositoryImpl", "url retrieved from database")
            Log.i("WikiRepositoryImpl", "url: $url")
            url
        } catch (e: Exception){
            Log.i("WikiRepositoryImpl","Error fetching url from database: ${e.message}")
            var url = ""
            try{
                url = wikiService.getAllImages(title).toDomain(title).imageUrls.first()
                Log.i("WikiRepositoryImpl", "url retrieved from API")
            }
            catch (e: Exception){
                Log.i("WikiRepositoryImpl","Error fetching url from API: ${e.message}")
            }
            Log.i("WikiRepositoryImpl", "url: $url")
            url
        }
    }
}