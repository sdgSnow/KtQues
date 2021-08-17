package com.sdg.api

import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OkHttpUtils {

    /**
     * okhttp
     */
    private var okHttpClient: OkHttpClient? = null

    /**
     * Retrofit
     */
    private var retrofit: Retrofit? = null

    /**
     * 设缓存有效期为两天
     */
    val sCACHE_STALE_SEC = 60 * 60 * 24 * 2.toLong()

    companion object {
        private var okHttpUtils : OkHttpUtils? = null
        fun get() : OkHttpUtils {
            if(okHttpUtils == null){
                synchronized(OkHttpUtils::class.java){
                    if (okHttpUtils == null){
                        okHttpUtils = OkHttpUtils()
                    }
                }
            }
            return okHttpUtils!!
        }
    }

    /**
     * 获取Retrofit的实例
     *
     * @return retrofit
     */
    fun getRetrofit(url:String?,connectTimeout:Long,readTimeout:Long,writeTimeout:Long,retryOnConnectionFailure:Boolean) : Retrofit{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(connectTimeout, readTimeout, writeTimeout, retryOnConnectionFailure))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getOkHttpClient(connectTimeout: Long, readTimeout: Long, writeTimeout: Long, retryOnConnectionFailure: Boolean): OkHttpClient {
        if(okHttpClient == null){
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(connectTimeout,TimeUnit.SECONDS)
                .readTimeout(readTimeout,TimeUnit.SECONDS)
                .writeTimeout(writeTimeout,TimeUnit.SECONDS)
                .retryOnConnectionFailure(retryOnConnectionFailure)
                .addInterceptor(mLoggingInterceptor)
                .addInterceptor(mRewriteCacheControlInterceptor)
            okHttpClient = builder.build()
        }
        return okHttpClient!!
    }

    private val mLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        Log.i("url:", String.format("发送请求: %s%n%s", request.url(), chain.connection(), request.headers()))
        val response = chain.proceed(request)
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
        val responseBody = response.peekBody(1024 * 1024.toLong())
        response
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private val mRewriteCacheControlInterceptor =
        Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtils.isConnected(RetrofitManager.get().getContext())) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val originalResponse = chain.proceed(request)
            if (NetworkUtils.isConnected(RetrofitManager.get().getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                val cacheControl = request.cacheControl().toString()
                originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build()
            } else {
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + sCACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build()
            }
        }
}