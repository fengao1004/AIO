package com.goldze.mvvmhabit.aioui.bean.list

data class ListData<T>(
    var current: String?,
    var orders: List<Any?>?,
    var pages: String?,
    var records: List<T>?,
    var searchCount: Boolean?,
    var size: String?,
    var total: String?,
)