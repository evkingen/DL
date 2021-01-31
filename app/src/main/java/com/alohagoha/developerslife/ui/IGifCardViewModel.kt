package com.alohagoha.developerslife.ui

import androidx.lifecycle.LiveData

interface IGifCardViewModel {
    val isFirstGif: LiveData<Boolean>
    val currentGif: LiveData<GifResult>

    fun fetchStartGif(category: String)
    fun fetchPrevGif(category: String)
    fun fetchNextGif(category: String)

}
