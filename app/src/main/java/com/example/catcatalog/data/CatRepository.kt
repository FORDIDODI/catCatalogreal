package com.example.catcatalog.data

import com.example.catcatalog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class CatRepository {
    private val api: CatApiService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", Constants.API_KEY)
                    .build()
                chain.proceed(request)
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(CatApiService::class.java)
    }

    suspend fun getCats(limit: Int = 10): Result<List<Cat>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cats = api.getCats(limit)
            Result.success(cats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 