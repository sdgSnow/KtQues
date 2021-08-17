package com.sdg.ktques

import android.util.Log
import android.widget.Toast
import com.dimeno.commons.toolbar.impl.CommonToolbar
import com.dimeno.commons.toolbar.impl.Toolbar
import com.sdg.api.BaseObserver
import com.sdg.api.Res
import com.sdg.api.RetrofitUtil
import com.sdg.common.BaseActivity
import com.sdg.ktques.api.AboutUsBean
import com.sdg.ktques.api.ApiImpl
import io.reactivex.disposables.Disposable

class MainActivity : BaseActivity<MainView,MainPresenter>(),MainView{

    override fun getLayout(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter? = MainPresenter()

    override fun createToolbar(): Toolbar? {
        return CommonToolbar(this,"Main")
    }

    override fun initViews() {
        Toast.makeText(mContext,"MainActivity",Toast.LENGTH_SHORT).show()
    }

    override fun initData() {

        RetrofitUtil.get().request(ApiImpl.getAboutUs("weiduques"), object : BaseObserver<AboutUsBean?>(mContext, false) {
            override fun onPrepare(d: Disposable?) {
                Log.i("api-tag","onPrepare")
            }

            override fun onSuccess(res: Res<AboutUsBean?>?) {
                Log.i("api-tag","onSuccess")
            }

            override fun onFaild(error: String?) {
                Log.i("api-tag","onFaild")
            }
        })
    }

}