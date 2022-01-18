package com.example.samplenaverlogin.di

import com.example.samplenaverlogin.BuildConfig
import com.example.samplenaverlogin.MovieApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://nid.naver.com/oauth2.0/"

val networkModule = module {
    factory { provideRetrofit(get()) }
    factory { providesOkHttpClient(get() as Interceptor) }
    factory { keyHeaderInterceptor() }
    factory { provideTimesApi(get()) }
}

private fun provideTimesApi(retrofit: Retrofit) = retrofit.create(MovieApp::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun keyHeaderInterceptor(): Interceptor {
    return Interceptor { chain ->
        val originRequest = chain.request()
        val builder = originRequest.url.newBuilder()
            .addQueryParameter("client_key", BuildConfig.NAVER_CLIENT_KEY).build()
        val newBuilder = originRequest.newBuilder().url(builder).build()
        chain.proceed(newBuilder)
    }
}

fun providesOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(interceptor)
        .build()
}