package dev.mo.surfcart.core.dto

import dev.mo.surfcart.core.entity.Category

object ParentCategoryMapper{
    fun CategoryDto.toCategory(): Category {
        return Category(
            id =id,
            name = name,
            parentId =parentId,
            categoryThumbnail = categoryThumbnail
        )
    }
}