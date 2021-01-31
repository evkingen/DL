package com.alohagoha.developerslife.model.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDTO(
        @SerialName("result")
        val result: List<GifDTO>
)
