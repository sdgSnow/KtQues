package com.sdg.api

import com.dimeno.commons.utils.T
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RetrofitUtil {

    companion object {
        private var mRetrofitUtil : RetrofitUtil? = null
        fun get() : RetrofitUtil{
            if(mRetrofitUtil == null){
                synchronized(RetrofitUtil::class.java){
                    if (mRetrofitUtil == null){
                        mRetrofitUtil = RetrofitUtil()
                    }
                }
            }
            return mRetrofitUtil!!
        }
    }

    fun request(o: Observable<Res<T>>, s: Observer<Res<T>>?) {
        o.doOnSubscribe { }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s!!)
    }

}