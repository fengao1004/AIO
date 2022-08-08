package com.goldze.mvvmhabit.aioui.kepu.content

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/28
 * Time: 6:34 上午
 */
data class KepuItemBean(
    val code: String,
    val data: KepuItemBeanData,
    val message: String,
    val success: Boolean
)

data class KepuItemBeanData(
    val current: String,
    val orders: List<Any>,
    val pages: String,
    val records: List<KepuItemBeanRecord>,
    val searchCount: Boolean,
    val size: String,
    val total: String
)

data class KepuItemBeanRecord(
    val brief: String,
    val contentList: List<KepuItemBeanContent>,
    val createTime: Any,
    val id: String,
    val isDel: Int,
    val name: String,
    val status: Int,
    val themeId: String,
    val updateTime: Any
)

data class KepuItemBeanContent(
    val createTime: Any,
    val id: String,
    val isDel: Int,
    val resourcesId: String,
    val resourcesName: String,
    val status: Int,
    val sysModuleCode: String,
    val sysModuleId: Int,
    val themeId: String,
    val themeItemId: String,
    val updateTime: Any,
    val code:String
)