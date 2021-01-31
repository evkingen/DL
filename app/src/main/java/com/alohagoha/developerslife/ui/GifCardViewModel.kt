package com.alohagoha.developerslife.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alohagoha.developerslife.model.data.GifsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GifCardViewModel(private val repository: GifsRepository) : ViewModel(), IGifCardViewModel {
    private var job: Job? = null
    private var currentIndex: Int = 0
    override val isFirstGif: MutableLiveData<Boolean> = MutableLiveData()
    override val currentGif: MutableLiveData<GifResult> = MutableLiveData()

    init {
        currentGif.value = Loading
    }

    override fun fetchStartGif(category: String) = fetchGif(category, currentIndex)
    override fun fetchNextGif(category: String) = fetchGif(category, currentIndex++)
    override fun fetchPrevGif(category: String) = fetchGif(category, currentIndex--)

    private fun fetchGif(category: String, index: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                isFirstGif.value = currentIndex == 0
                val gif = repository.getGifByCategory(category, currentIndex)
                currentGif.value = gif?.let { ValidResult(it) } ?: EmptyResult
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    currentGif.value = ErrorResult
                }
            }
        }
    }
}
