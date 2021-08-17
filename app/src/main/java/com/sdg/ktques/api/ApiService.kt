package com.sdg.ktques.api

import com.sdg.api.Res
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /**
     * ‘关于我们’信息
     *
     * @param code
     * @return
     */
    @GET("api/Que/GetAboutUs")
    fun getAboutUs(@Query("ProCode") code: String): Observable<Res<AboutUsBean>>
}