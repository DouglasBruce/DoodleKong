package com.douglasbruce.data.models

data class BasicApiResponse(
    val successful: Boolean,
    val message: String? = null,
)
