package com.alohagoha.developerslife.model.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifDTO(
        @SerialName("id")
        val id: Int,
        @SerialName("description")
        val description: String?,
        @SerialName("gifURL")
        val gifUrl: String?
)
