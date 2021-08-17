package com.sdg.common

interface BasePresenter<in V : BaseView> {
    /**
     * 绑定View
     */
    fun attachView(baseView: V)

    /**
     * 解绑View
     */
    fun detachView()

}