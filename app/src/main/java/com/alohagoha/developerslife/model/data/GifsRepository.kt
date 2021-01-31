package com.alohagoha.developerslife.model.data

import com.alohagoha.developerslife.model.entities.Gif
import com.alohagoha.developerslife.model.entities.GifStoreKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GifsRepository(private val apiService: DevelopersLifeAPI) {
    private val storeMap: MutableMap<GifStoreKey, Gif> = mutableMapOf()
    private val storeList: MutableList<Gif> = mutableListOf()

    private suspend fun getGifsPageByCategory(category: String, page: Int): List<Gif> =
        withContext(Dispatchers.IO) {
            apiService.getGifs(category, page, true)?.result?.map { gifDTO ->
                Gif(
                    id = gifDTO.id,
                    description = gifDTO.description.orEmpty(),
                    gifUrl = gifDTO.gifUrl.orEmpty()
                )
            }.orEmpty()
        }

    suspend fun getGifByCategory(category: String, index: Int): Gif? {
        if (storeMap[GifStoreKey(category, index)] == null) {
            val newPage = getGifsPageByCategory(category, index / PAGECOUNT)
            if (newPage.isEmpty()) return null
            newPage.mapIndexed { id, gif -> storeMap[GifStoreKey(category, index + id)] = gif }
        }
        return storeMap[GifStoreKey(category, index)]
//        return storeList[index]
    }

    companion object {
        private const val PAGECOUNT = 5
    }
}
