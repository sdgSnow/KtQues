package com.sdg.api

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

abstract class BaseObserver<T>(context: Context?, hideLoading: Boolean) : Observer<Res<T>> {
    /**
     * hideLoading 是否隱藏等待框
     * 默認為true
     */
    private var hideLoading : Boolean = true

    private var context: Context? = null

    init {
        this.context = context
        this.hideLoading = hideLoading
    }

    override fun onSubscribe(d: Disposable) {
        ApiLoading.getInstance().show(context,false)
        onPrepare(d)
    }

    override fun onNext(t: Res<T>) {
        ApiLoading.getInstance().cancel()
        if(t != null){
            if(t.code == 200 && t.ResultObj != null){
                onSuccess(t)
            }else{
                onFaild(t.Msg)
            }
        }else{
            onFaild("数据返回异常")
        }
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

    protected abstract fun onSuccess(res: Res<T>?)

    protected abstract fun onFaild(error: String?)
}