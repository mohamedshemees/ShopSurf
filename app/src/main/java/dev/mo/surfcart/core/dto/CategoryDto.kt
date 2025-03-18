package dev.mo.surfcart.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(

    @SerialName("category_id")
    val id: Int,

    @SerialName("category_name")
    val name: String,

    @SerialName("parent_category_id")
    val parentId: Long?,

    @SerialName("category_thumbnail")
    val categoryThumbnail: String,
)
