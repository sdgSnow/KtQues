package com.sdg.api

import android.content.Context
import android.os.Handler
import android.os.Process

class RetrofitManager {

    private var mMainThreadId = 0
    private var mHandler: Handler? = null
    var mContext: Context? = null

    /**
     * 连接超时时间，默认30秒
     */
    private var connectTimeout : Long = 30

    /**
     * 读取数据超时时间，默认30秒
     */
    private var readTimeout : Long = 30

    /**
     * 写入数据超时时间，默认30秒
     */
    private var writeTimeout : Long = 30

    /**
     * 失败后是否重连，默认false
     */
    private var retryOnConnectionFailure = false

    /**
     * api的地址
     */
    private var url: String? = null

    companion object {
        private var mRetrofitManager : RetrofitManager? = null
        fun get() : RetrofitManager{
            if(mRetrofitManager == null){
                synchronized(RetrofitManager::class.java){
                    if (mRetrofitManager == null){
                        mRetrofitManager = RetrofitManager()
                    }
                }
            }
            return mRetrofitManager!!
        }
    }

    fun <T> getApi(service: Class<T>): T {
        return OkHttpUtils.get().getRetrofit(url,connectTimeout,readTimeout,writeTimeout,retryOnConnectionFailure).create(service)
    }

    fun setConnectTimeout(connectTimeout: Long): RetrofitManager? {
        this.connectTimeout = connectTimeout
        return this
    }

    fun setReadTimeout(readTimeout: Long): RetrofitManager? {
        this.readTimeout = readTimeout
        return this
    }

    fun setWriteTimeout(writeTimeout: Long): RetrofitManager? {
        this.writeTimeout = writeTimeout
        return this
    }

    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): RetrofitManager? {
        this.retryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    fun setUrl(url: String?): RetrofitManager? {
        this.url = url
        return this
    }

    fun setContext(context: Context): RetrofitManager? {
        mContext = context
        // 获取主线程ID
        mMainThreadId = Process.myTid()
        // 获取Handler
        mHandler = Handler()
        return this
    }

    /**
     * 获取主线程ID
     */
    fun getMainThreadId(): Int {
        return mMainThreadId
    }

    /**
     * 获取Handler
     */
    fun getHandler(): Handler? {
        return mHandler
    }

    fun getContext(): Context? {
        return mContext
    }
}