package dev.mo.surfcart.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mo.surfcart.account.data.dto.AccountRepositoryImpl
import dev.mo.surfcart.account.data.dto.SettingsRepositoryImpl
import dev.mo.surfcart.account.domain.repository.AccountRepository
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import dev.mo.surfcart.cart.CartRepository
import dev.mo.surfcart.cart.CartRepositoryImpl
import dev.mo.surfcart.core.repository.ProductRepository
import dev.mo.surfcart.core.repository.ProductRepositoryImpl
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
        postgres: Postgrest,
    ): ProductRepository =ProductRepositoryImpl(postgres)

    @Provides
    @Singleton
    fun provideAccountRepository(
        postgres: Postgrest,
    ): AccountRepository = AccountRepositoryImpl( postgres)

    @Provides
    @Singleton
    fun provideAuthRepository(
        postgres: Auth,
    ): AuthRepository = AuthRepoImpl(postgres)

    @Provides
    @Singleton
    fun provideCartRepository(
        postgres: Postgrest,
    ): CartRepository = CartRepositoryImpl(postgres)

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository =SettingsRepositoryImpl(dataStore)

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "settings"
    )
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}