package com.sdg.api

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

abstract class BaseNormalObserver<T> : Observer<T> {
    /**
     * hideLoading 是否隱藏等待框
     * 默認為true
     */
    private var hideLoading : Boolean = true

    private var context: Context? = null

    open fun BaseObserver(context: Context?, hideLoading: Boolean) {
        this.context = context
        this.hideLoading = hideLoading
    }

    override fun onSubscribe(d: Disposable) {
        ApiLoading.getInstance().show(context,false)
        onPrepare(d)
    }

    override fun onNext(t: T) {
        ApiLoading.getInstance().cancel()
        onSuccess(t)

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if(e is HttpException){
            val code = e.code()
            onFaild(e.message())
            if(code == 404){
                Toast.makeText(context, "抱歉~你要查看的页面不存在", Toast.LENGTH_SHORT).show()
            }else if(code == 500){
                Toast.makeText(context, "服务器异常，请稍后重试...", Toast.LENGTH_SHORT).show()
            }else if(code == 502){
                Toast.makeText(context, "网络连接不可用，请稍后重试", Toast.LENGTH_SHORT).show()
            }else if(code == 504){
                Toast.makeText(context, "网络连接不可用，请稍后重试", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        if (hideLoading) {
            //TODO 停止接口的加载时动画
            ApiLoading.getInstance().cancel()
        }
    }

    override fun onComplete() {
        if (hideLoading){
            ApiLoading.getInstance().cancel()
        }
    }

    protected abstract fun onPrepare(d: Disposable?)

    protected abstract fun onSuccess(res: T?)

    protected abstract fun onFaild(error: String?)
}