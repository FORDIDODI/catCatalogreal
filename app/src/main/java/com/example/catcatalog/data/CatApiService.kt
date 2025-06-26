package com.example.catcatalog.data

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatApiService {
    @Headers("x-api-key: ") // Nanti diisi dari Interceptor
    @GET("images/search")
    suspend fun getCats(
        @Query("limit") limit: Int = 10
    ): List<Cat>
} 