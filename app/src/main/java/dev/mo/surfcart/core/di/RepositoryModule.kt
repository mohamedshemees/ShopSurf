package dev.mo.surfcart.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mo.surfcart.cart.CartRepository
import dev.mo.surfcart.cart.CartRepositoryImpl
import dev.mo.surfcart.core.repository.ProductRepositoryImpl
import dev.mo.surfcart.core.repository.ProductRepository
import dev.mo.surfcart.registration.data.AuthRepoImpl
import dev.mo.surfcart.registration.data.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        postgres:Postgrest,
        ): ProductRepository{
        return ProductRepositoryImpl(
            postgres)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        postgres:Auth,
    ): AuthRepository {
        return AuthRepoImpl(
            postgres
        )
    }
    @Provides
    @Singleton
    fun provideCartRepository(
        postgres:Postgrest,
    ): CartRepository {
        return CartRepositoryImpl(
            postgres
        )
    }

}