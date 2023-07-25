package com.jaydev.github.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.jaydev.github.domain.interactor.NetworkConnectException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(context: Context) : Interceptor {
    private val applicationContext = context.applicationContext

    private val isConnected: Boolean
        get() {
            val result: Boolean
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            result = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result
        }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected) {
            val newRequest = chain.request().newBuilder()
            return chain.proceed(newRequest.build())
        } else {
            throw NetworkConnectException()
        }
    }
}
