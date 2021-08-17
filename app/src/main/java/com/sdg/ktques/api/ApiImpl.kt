package com.sdg.ktques.api

import com.sdg.api.RetrofitManager

class ApiImpl {
    companion object {
        fun getAboutUs(code: String) =
            RetrofitManager.get().getApi(ApiService::class.java).getAboutUs(code)
    }

}