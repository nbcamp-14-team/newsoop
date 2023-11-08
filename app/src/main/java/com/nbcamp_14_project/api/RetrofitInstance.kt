package com.nbcamp_14_project.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

/**
 * 레트로핏 인스턴스입니다.
 */
object RetrofitInstance {
//    val api: NewsCollector get() = retrofit.create(NewsCollector::class.java)
    private val okHttpClient by lazy{
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
//            .addInterceptor(
//                HttpLoggingInterceptor()
//                .apply { level = HttpLoggingInterceptor.Level.BODY }
//            )
            .build()
    }
    private val retrofit: Retrofit
        private get(){
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(NewsCollector.NEWS_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(GsonConverterFactory.create(getDateFormatGsonBuilder()))
                .addConverterFactory(ScalarsConverterFactory.create()) //String 처리 할때 사용
                .build()
        }
    private fun getDateFormatGsonBuilder() = GsonBuilder()
        .setDateFormat("EEE, DD MMM YYYY hh:mm:ss")
        .create()
    /**
     * 검색 Retrofit입니다.
     */
    val search:NewsCollector by lazy{
        retrofit.create(NewsCollector::class.java)
    }

}