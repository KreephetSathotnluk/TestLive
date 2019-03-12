package com.mfec.live.manager

import com.mfec.live.live.model.LiveModel
import io.reactivex.Single
import retrofit2.Response

import retrofit2.http.POST

interface ApiService {
    @POST("content/public/wowza/getListLiveStream")
    fun getListWowza(): Single<Response<LiveModel>>
}