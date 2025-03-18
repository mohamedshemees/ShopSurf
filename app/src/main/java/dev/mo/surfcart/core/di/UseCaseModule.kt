package dev.mo.surfcart.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mo.surfcart.core.repository.ProductRepository
import dev.mo.surfcart.home.usecase.GetFeaturedProductsUseCase
import dev.mo.surfcart.home.usecase.GetOnSaleProductsUseCase
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule
{
    @Provides
    fun provideGetCategoriesUseCase(repository: ProductRepository): GetParentCategoriesUseCase {
        return GetParentCategoriesUseCase(repository)
    }

    @Provides
    fun provideGetProuctsUseCase(repository: ProductRepository): GetFeaturedProductsUseCase {
        return GetFeaturedProductsUseCase(repository)
    }

    @Provides
    fun provideetProductsBySubCategoryUseCase(repository: ProductRepository): GetProductsBySubCategoryUseCase {
        return GetProductsBySubCategoryUseCase(repository)
    }

    @Provides
    fun provideGetSubCategoriesUseCase(repository: ProductRepository): GetSubCategoriesUseCase {
        return GetSubCategoriesUseCase(repository)
    }

    @Provides
    fun provideGetOnSaleProductsUseCase(repository: ProductRepository): GetOnSaleProductsUseCase {
        return GetOnSaleProductsUseCase(repository)
    }


}
