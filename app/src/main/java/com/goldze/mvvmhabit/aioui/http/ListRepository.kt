package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import io.reactivex.Observable

abstract class ListRepository : HttpRepository() {

    abstract fun getCommonListData(bean: CommentRequestBean): Observable<CommonListResponseBean<*>>
}