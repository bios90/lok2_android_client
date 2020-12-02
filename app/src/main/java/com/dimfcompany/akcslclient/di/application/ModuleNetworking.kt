package com.dimfcompany.akcslclient.di.application

import com.grapesnberries.curllogger.CurlLoggerInterceptor
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.networking.MyInterceptor
import com.dimfcompany.akcslclient.networking.apis.ApiAuth
import com.dimfcompany.akcslclient.networking.apis.ApiCategories
import com.dimfcompany.akcslclient.networking.apis.ApiGlobal
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ModuleNetworking
{
    @Singleton
    @Provides
    fun provideMyInterceptor(): MyInterceptor
    {
        return MyInterceptor()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor
    {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun provideCurlInterceptor(): CurlLoggerInterceptor
    {
        return CurlLoggerInterceptor("****curl****")
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: MyInterceptor, log_interceptor: HttpLoggingInterceptor, curl_interceptor: CurlLoggerInterceptor): OkHttpClient
    {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(interceptor)
//        httpClientBuilder.addInterceptor(log_interceptor)
//        httpClientBuilder.addInterceptor(curl_interceptor)
        httpClientBuilder.callTimeout(120, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit
    {
        return Retrofit.Builder()
                .baseUrl(Constants.Urls.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(AppClass.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Singleton
    @Provides
    fun provideApiGlobal(retrofit: Retrofit): ApiGlobal
    {
        return retrofit.create(ApiGlobal::class.java)
    }

    @Singleton
    @Provides
    fun provideApiAuth(retrofit: Retrofit): ApiAuth
    {
        return retrofit.create(ApiAuth::class.java)
    }

    @Singleton
    @Provides
    fun provideApiCategories(retrofit: Retrofit): ApiCategories
    {
        return retrofit.create(ApiCategories::class.java)
    }
}