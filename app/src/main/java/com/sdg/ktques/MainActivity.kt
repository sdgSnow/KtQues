package com.sdg.ktques

import android.widget.Toast
import com.dimeno.commons.toolbar.impl.CommonToolbar
import com.dimeno.commons.toolbar.impl.Toolbar
import com.sdg.api.BaseObserver
import com.sdg.api.Res
import com.sdg.api.RetrofitManager
import com.sdg.api.RetrofitUtil
import com.sdg.common.BaseActivity
import com.sdg.ktques.api.AboutUsBean
import com.sdg.ktques.api.ApiImpl
import io.reactivex.disposables.Disposable

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {

    override fun getLayout(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun createToolbar(): Toolbar {
        return CommonToolbar(this, "Main")
    }

    override fun initViews() {
        Toast.makeText(mContext, "MainActivity", Toast.LENGTH_SHORT).show()
    }

    override fun initData() {
        RetrofitManager.get().setContext(this).setUrl("http://wm.dimenosys.com/");
        RetrofitUtil.get().request(
            ApiImpl.getAboutUs("weiduques"),
            object : BaseObserver<AboutUsBean>(mContext, false) {
                override fun onPrepare(d: Disposable) {

                }

                override fun onSuccess(res: Res<AboutUsBean>?) {
                    res?.let {
                        Toast.makeText(mContext, it.Msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFail(error: String?) {
                    Toast.makeText(mContext, error ?: "request fail", Toast.LENGTH_SHORT).show()
                }
            })
    }

}