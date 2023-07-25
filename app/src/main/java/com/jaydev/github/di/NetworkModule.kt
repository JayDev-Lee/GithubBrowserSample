package com.jaydev.github.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jaydev.github.network.NetworkConnectionInterceptor
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
	private const val TAG = "OkHttp"
	private const val connectTimeout = 20L
	private const val writeTimeout = 20L
	private const val readTimeout = 20L

	@Provides
	@Singleton
	fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
		return NetworkConnectionInterceptor(context)
	}

	@Provides
	@Singleton
	fun provideLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor(PrettyPrintLogger()).apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(
		networkConnectionInterceptor: NetworkConnectionInterceptor,
		loggingInterceptor: HttpLoggingInterceptor,
	): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(networkConnectionInterceptor)
			.addInterceptor(loggingInterceptor)
			.connectTimeout(connectTimeout, TimeUnit.SECONDS)
			.writeTimeout(writeTimeout, TimeUnit.SECONDS)
			.readTimeout(readTimeout, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		val contentType = "application/json".toMediaType()
		val json = Json {
			ignoreUnknownKeys = true
			prettyPrint = true
			isLenient = true
		}

		return Retrofit.Builder()
			.client(okHttpClient)
			.baseUrl("https://api.github.com")
			.addConverterFactory(json.asConverterFactory(contentType))
			.callFactory { okHttpClient.newCall(it) }
			.build()
	}

	private class PrettyPrintLogger : HttpLoggingInterceptor.Logger {
		override fun log(message: String) {
			Logger.t(TAG).run {
				try {
					val json = Json {
						prettyPrint = true
						ignoreUnknownKeys = true
						isLenient = true
					}
					json.decodeFromString<JsonElement>(message)
					json(message)
				} catch (ignore: SerializationException) {
					i(message)
				} catch (ignore: IllegalArgumentException) {
					i(message)
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
	}
}
