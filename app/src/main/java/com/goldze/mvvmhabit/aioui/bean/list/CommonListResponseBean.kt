package com.goldze.mvvmhabit.aioui.bean.list

data class CommonListResponseBean<T>(
    var code: String,
    var `data`: ListData<T>,
    var message: String,
    var success: Boolean
)