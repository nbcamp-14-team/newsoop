package com.nbcamp_14_project.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor:Interceptor {
    /**
     * API검색을 시도할 때 마다 Header를 추가합니다
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(
                "X-Naver-Client-Id",
                "XFBo9h4LO1TqscFAuCKO"
            )
            .addHeader(
                "X-Naver-Client-Secret",
                "hi8mfNOW0V"
            ).build()
        return chain.proceed(newRequest)
    }

}