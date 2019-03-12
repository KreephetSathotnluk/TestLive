package com.mfec.live.live.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.mfec.live.live.model.LiveModel
import com.mfec.live.manager.HttpManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LiveViewModel : ViewModel() {

    @SuppressLint("CheckResult")
    fun getListWowza(context: Context): MutableLiveData<LiveModel> {
        val mutableLiveData = MutableLiveData<LiveModel>()
        HttpManager(context) {
            Log.wtf("httpOk", it)
        }.service.getListWowza()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("ListWowza", it.toString())
                if (it.isSuccessful)
                    mutableLiveData.value = it.body()
                Log.wtf("ggwp",mutableLiveData.value.toString())
            }, {
                Log.wtf("Wowza", "get list wowza failed with ${it.message}")
            })
        return mutableLiveData
    }

}