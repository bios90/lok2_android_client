package com.dimfcompany.akcslclient.networking

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response
    {
        val request = chain.request()
        val url = request.url().newBuilder()
        val builded_url = url.build()
        val newRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .url(builded_url)

        //Todo make adding user_id

        return chain.proceed(newRequest.build())
    }
}