package com.sdg.common

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dimeno.commons.toolbar.ToolbarActivity
import com.wangzhen.statusbar.DarkStatusBar

abstract class BaseActivity<in V : BaseView,P : BasePresenter<V>> : ToolbarActivity(), BaseView {

    lateinit var mContext : Context
    lateinit var mAppCompatActivity : AppCompatActivity
    var mPresenter : P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("base", "onCreate")
        mContext = this
        mAppCompatActivity = this
        ActivityManager.getAppManager().addActivity(this)
        setContentView(getLayout())
        mPresenter = createPresenter()
        if(mPresenter == null) mPresenter!!.attachView(this as V)

        initViews()

        initData()
    }

    abstract fun getLayout(): Int

    abstract fun createPresenter(): P?

    abstract fun initViews()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        ActivityManager.getAppManager().removeActivityStack(this)
    }

    open fun fitDarkStatusBar(dark: Boolean) {
        val statusBar = DarkStatusBar.get()
        if (dark) statusBar.fitDark(this)
    }

}