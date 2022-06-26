package com.goldze.mvvmhabit.aioui.video.bean

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 2:46 下午
 */
class VideoBean(
    var url: String, //视频地址
    var title: String,  //页面标题
    var videoName: String, //视频标题
    var content: String, //文字内容
    var number: String, //点击量 如：点击量2821
): Serializable