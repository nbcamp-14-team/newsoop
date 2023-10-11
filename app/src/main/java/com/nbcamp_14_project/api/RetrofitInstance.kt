package com.nbcamp_14_project.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object RetrofitInstance {
    val api: NewsCollector get() = retrofit.create(NewsCollector::class.java)

    private val retrofit: Retrofit
        private get(){
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(NewsCollector.NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create()) //String 처리 할때 사용
                .build()
        }
}