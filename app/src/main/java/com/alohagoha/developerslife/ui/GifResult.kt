package com.alohagoha.developerslife.ui

import com.alohagoha.developerslife.model.entities.Gif

sealed class GifResult
object Loading : GifResult()
class ValidResult(val gif: Gif) : GifResult()
object EmptyResult : GifResult()
object ErrorResult : GifResult()
