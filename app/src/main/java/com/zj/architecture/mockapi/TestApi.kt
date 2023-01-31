package com.zj.architecture.mockapi

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.zj.architecture.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TestApi {

    @GET("banner/json")
    suspend fun getBanner(): BaseData<List<Banner>>

    companion object {
        // Please do not follow this code as this has been
        // modified to intercept API calls with mock response.
        fun create(): TestApi {
            val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(OkHttpProfilerInterceptor())
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TestApi::class.java)
        }
    }
}