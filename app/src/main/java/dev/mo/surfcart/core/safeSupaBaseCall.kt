package dev.mo.surfcart.core

import android.util.Log
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeSupabaseCall(
    apiCall: suspend () -> T
): T {
   return try {
       withContext(Dispatchers.IO) {apiCall()}
    } catch (e: RestException) {
       throw e

    } catch (e: HttpRequestTimeoutException) {
        // Network timeout
        Log.e("ERROR_CAUGHT", "Network HttpRequestTimeoutException: ${e.message}", e)
       throw e

    } catch (e: io.ktor.client.network.sockets.SocketTimeoutException) {
        Log.e("ERROR_CAUGHT", "Ktor SocketTimeoutException: ${e.message}", e)
       throw e
    } catch (e: java.net.UnknownHostException) {
        // No network connectivity or DNS resolution issue
        Log.e("ERROR_CAUGHT", "UnknownHostException: No network or DNS issue. ${e.message}", e)
       throw e
    } catch (e: java.io.IOException) {
        // Broader category for network issues
        Log.e("ERROR_CAUGHT", "IOException: Network issue. ${e.message}", e)
       throw e
    } catch (e: Exception) {
       // Catch-all for any other unexpected errors
       Log.e("ERROR_CAUGHT", "Generic Exception during Supabase call: ${e.message}", e)
       throw e
   }
}