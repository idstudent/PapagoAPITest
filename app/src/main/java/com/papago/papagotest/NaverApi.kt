package com.papago.papagotest

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NaverApi {
    // 요청변수 https://developers.naver.com/docs/nmt/reference/#1--%EC%A4%80%EB%B9%84%EC%82%AC%ED%95%AD
    @FormUrlEncoded
    @POST("v1/papago/n2mt")
    fun transferPapago(
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<NaverPapagoResponse>

    companion object {
        private const val BASE_URL_NAVER_API = "https://openapi.naver.com/"
        // api 요청하고 받은 클라언트 아이디, 비밀번호 넣어줘야 됨 깃허브에 올리려고 지웠음
        private const val CLIENT_ID = "clientID"
        private const val CLIENT_SECRET = "clientSecret"

        fun create(): NaverApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Naver-Client-Id", CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_NAVER_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NaverApi::class.java)
        }
    }
}