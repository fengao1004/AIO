package com.goldze.mvvmhabit.aioui.bean.list

open class BaseRecord {
    var brief: String? = null
    var clickCount: Int? = null
    var createTime: String? = null
    var deptId: Int? = null
    var id: Long = 0
    var isDel: Int = 0
    var name: String? = null
    var status: Int? = null
    var updateTime: String? = null
    var content: String? = null
    var resourcesUrl: String? = null

    override fun toString(): String {
        return "BaseRecord(brief=$brief, clickCount=$clickCount, createTime=$createTime, deptId=$deptId, id=$id, isDel=$isDel, name=$name, status=$status, updateTime=$updateTime, content=$content, resourcesUrl=$resourcesUrl)"
    }
}