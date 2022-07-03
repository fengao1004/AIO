package com.goldze.mvvmhabit.aioui.clazz.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzResponseBeanData
import com.goldze.mvvmhabit.databinding.ActivityClazzContentBinding
import com.shuyu.gsyvideoplayer.GSYVideoManager
import me.goldze.mvvmhabit.base.BaseActivity

class ClazzContentActivity : BaseActivity<ActivityClazzContentBinding, ClazzContentModel>() {
    lateinit var bean: ClazzResponseBeanData

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_clazz_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        var tvList = arrayListOf<TextView>()
        var id = intent.getIntExtra("id", 0).toString()
        var name = intent.getStringExtra("name")
        binding.brRootView.setPageTitle(name)
        viewModel.loadData(id ?: "")
        viewModel.beanLiveData.observe(this) {
            bean = it
            binding.webContent.loadDataWithBaseURL(
                null,
                bean.courseDescribe,
                "text/html",
                "utf-8",
                null
            )
            bean.itemList.forEachIndexed { index, it ->
                var view: TextView = LayoutInflater.from(this)
                    .inflate(R.layout.clazz_item_text, binding.llItemRoot, false) as TextView
                view.text = it.name
                view.setOnClickListener { tv ->
                    tvList.forEach {
                        it.setTextColor(it.context.resources.getColor(R.color.textGray))
                    }
                    view.setTextColor(tv.context.resources.getColor(R.color.app_blue))
                    playVideo(it.resourcesUrl)
                }
                binding.llItemRoot.addView(view)
                if (index == 0) {
                    view.setTextColor(view.context.resources.getColor(R.color.app_blue))
                }
                tvList.add(view)
            }
            playVideo(bean.itemList[0].resourcesUrl)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    private fun playVideo(url: String) {
        binding.scvVideo.setUp(url, false, "")
        binding.scvVideo.setLockLand(false)
        binding.scvVideo.setShowFullAnimation(true)//打开动画
        binding.scvVideo.setNeedLockFull(true)
        binding.scvVideo.startPlayLogic()
        binding.scvVideo.setLooping(true)
        binding.scvVideo.getFullscreenButton()
            .setOnClickListener(View.OnClickListener { //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                binding.scvVideo.startWindowFullscreen(this@ClazzContentActivity, true, true)
            })
    }
}