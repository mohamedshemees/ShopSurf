package dev.mo.surfcart.core

import android.util.Log
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.UnknownRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun <T> safeSupabaseCall(
    apiCall: suspend () -> T
): T {
    return try {
        withContext(Dispatchers.IO) { apiCall() }
    } catch (e: Exception) {
        Log.e("safeSupabaseCall", "Exception caught: ${e.javaClass.simpleName}", e)
        throw when (e) {
            is UnknownRestException -> {
                if (e.message?.contains("duplicate key value violates unique constraint") == true) {
                    DuplicateEntryException(e.message ?: "Duplicate entry")
                } else {
                    UnknownException(e.message ?: "An unknown REST error occurred")
                }
            }
            is RestException -> {
                if (e.message?.contains("duplicate key value violates unique constraint") == true) {
                    DuplicateEntryException(e.message ?: "Duplicate entry")
                } else {
                    UnknownException(e.message ?: "An unknown REST error occurred")
                }
            }
            is HttpRequestTimeoutException, is io.ktor.client.network.sockets.SocketTimeoutException, is java.net.UnknownHostException, is IOException -> {
                NetworkException("Network error: ${e.message}")
            }
            else -> UnknownException("An unexpected error occurred: ${e.message}")
        }
    }
}
