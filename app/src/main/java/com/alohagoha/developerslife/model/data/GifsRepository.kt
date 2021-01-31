package com.alohagoha.developerslife.model.data

import com.alohagoha.developerslife.model.entities.Gif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GifsRepository(private val apiService: DevelopersLifeAPI) {
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
        if (index > storeList.size - 1) {
            val newPage = getGifsPageByCategory(category, index / PAGECOUNT)
            if (newPage.isEmpty()) return null
            storeList.addAll(newPage)
        }
        return storeList[index]
    }

    companion object {
        private const val PAGECOUNT = 5
    }
}
