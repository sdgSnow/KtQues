package com.sdg.api

class Res<T> {

    /**
     * 状态吗
     */
    var code = 0

    /**
     * 状态吗
     */
    var Flag = 0

    /**
     * 数据
     */
    var ResultObj: T? = null

    var Msg: String? = null
}