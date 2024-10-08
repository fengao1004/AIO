package com.goldze.mvvmhabit.aioui.video

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.WebSettings.ZoomDensity
import android.widget.ImageView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean
import com.goldze.mvvmhabit.databinding.ActivityVideoBinding
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import me.goldze.mvvmhabit.base.BaseActivity


class VideoActivity : BaseActivity<ActivityVideoBinding, VideoModel>() {
    private val url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
    var videoBean: VideoBean? = null

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_video
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder? {
        //内置封面可参考SampleCoverVideo
        val imageView = ImageView(this)
//        loadCover(imageView, url)
        return GSYVideoOptionBuilder()
            .setThumbImageView(imageView)
            .setUrl(url)
            .setCacheWithPlay(true)
            .setVideoTitle(" ")
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(true) //打开动画
            .setNeedLockFull(true)
            .setSeekRatio(1f)
    }

    override fun initData() {
        super.initData()
        videoBean = intent.getSerializableExtra("videoBean") as VideoBean
        binding.tvTitle.text = videoBean?.videoName
        binding.tvNum.text = videoBean?.number
        binding.webview.setInitialScale(250)
        binding.webview.loadDataWithBaseURL(null,
            videoBean?.content, "text/html", "utf-8",null)
        binding.brRootView.setPageTitle(videoBean!!.title)
        binding.scvVideo.setUp(videoBean?.url, false, "")
        binding.scvVideo.setLockLand(false)
        binding.scvVideo.setShowFullAnimation(true)//打开动画
        binding.scvVideo.setNeedLockFull(true)
        binding.scvVideo.startPlayLogic()
        binding.scvVideo.setLooping(true)
        binding.scvVideo.getFullscreenButton()
            .setOnClickListener(View.OnClickListener { //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                binding.scvVideo.startWindowFullscreen(this@VideoActivity, true, true)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}