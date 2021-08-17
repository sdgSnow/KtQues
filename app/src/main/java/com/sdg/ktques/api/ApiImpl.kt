package com.sdg.ktques.api

import com.sdg.api.Res
import com.sdg.api.RetrofitManager
import io.reactivex.Observable

class ApiImpl {
    companion object {
        fun getAboutUs(procode: String?): Observable<Res<AboutUsBean?>?>? {
            return RetrofitManager.get().getApi(ApiService::class.java).getAboutUs(procode)
        }
    }

}