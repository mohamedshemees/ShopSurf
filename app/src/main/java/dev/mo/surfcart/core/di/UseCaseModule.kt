package dev.mo.surfcart.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mo.surfcart.cart.CartRepository
import dev.mo.surfcart.cart.usecase.AddProductToCartUseCase
import dev.mo.surfcart.cart.usecase.DecreaseQuantityUseCase
import dev.mo.surfcart.cart.usecase.GetCartItemsUseCase
import dev.mo.surfcart.cart.usecase.IncreaseQuantityUseCase
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

@Provides
    fun addCartUseCase(repository: CartRepository): AddProductToCartUseCase {
        return AddProductToCartUseCase(repository)
    }
    @Provides
    fun increaseQuantityUseCase(repository: CartRepository): IncreaseQuantityUseCase {
        return IncreaseQuantityUseCase(repository)
    }
    @Provides
    fun decreaseQuantityUseCase(repository: CartRepository): DecreaseQuantityUseCase {
        return DecreaseQuantityUseCase(repository)
    }

    @Provides
    fun getCartItemsUseCase(repository: CartRepository): GetCartItemsUseCase {
        return GetCartItemsUseCase(repository)
    }

}
