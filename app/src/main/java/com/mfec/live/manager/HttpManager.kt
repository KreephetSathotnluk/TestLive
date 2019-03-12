package com.mfec.live.manager

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HttpManager(context: Context?, callback: ((String) -> Unit)?) {
    val service: ApiService

    init {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create()
        val retrofit = Retrofit.Builder().baseUrl(BaseServer.BASE_SERVER)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(SelfSigningClientBuilder.getUnsafeOkHttpClient(context, callback))
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}