package com.goldze.mvvmhabit.aioui.http.impl

import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.http.ListRepository
import io.reactivex.Observable

class MeditationRepository : ListRepository() {
    override fun getCommonListData(bean: CommentRequestBean): Observable<CommonListResponseBean<*>> {
        return api.getMeditationPageList(bean).map {
            it as CommonListResponseBean<*>
        }
    }
}