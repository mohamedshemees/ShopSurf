package dev.mo.surfcart.core.entity

data class Category(
    val id: Int,
    val name: String,
    val parentId: Long?,
    val categoryThumbnail: String,

    )
