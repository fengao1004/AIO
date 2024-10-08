package com.goldze.mvvmhabit.aioui.bean.list

class MusicRecord : BaseRecord() {
    var faceImage: String? = null
    var homeImage: String? = null
    var musicDescribe: String? = null

    // 业务字段
    var itemPosition = -1
    override fun toString(): String {
        return super.toString()+"MusicRecord(faceImage=$faceImage, homeImage=$homeImage, musicDescribe=$musicDescribe, itemPosition=$itemPosition)"
    }


}