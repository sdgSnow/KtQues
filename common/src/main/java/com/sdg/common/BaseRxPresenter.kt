package com.sdg.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseRxPresenter<V : BaseView> : BasePresenter<V>{

    var mView : V? = null
    private var compositeDisposable: CompositeDisposable? = null

    override fun attachView(baseView: V) {
        this.mView = baseView
    }

    override fun detachView() {
        mView = null
        if(!compositeDisposable!!.isDisposed){
            compositeDisposable!!.clear()
        }
    }

    fun addDisposable(disposable: Disposable){
        if(compositeDisposable == null){
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }
}