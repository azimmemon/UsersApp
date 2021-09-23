package com.example.gitusers.network

import com.example.gitusers.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceFactory {

    fun getApiInterface(): ApiInterface = buildRetrofitClient().create(ApiInterface::class.java)

    private fun buildRetrofitClient(): Retrofit {
        val okHttpClient =
            getOkHttpClient()
        return buildApiService(
            okHttpClient
        )
    }


    private fun buildApiService(okHttpClient: OkHttpClient):
            Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .build()

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(makeHttpLoggingInterceptor())

        return okHttpClientBuilder
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    }

    private fun makeHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

}