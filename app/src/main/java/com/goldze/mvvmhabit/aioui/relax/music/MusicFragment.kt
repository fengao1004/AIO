package com.goldze.mvvmhabit.aioui.relax.music

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.listener.AIOViewPagerOnTabSelectedListener
import com.goldze.mvvmhabit.aioui.http.impl.MusicRepository
import com.goldze.mvvmhabit.aioui.relax.music.adapter.MusicRecyclerViewBindingAdapter
import com.goldze.mvvmhabit.aioui.relax.music.adapter.MusicViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.relax.music.play.PlayerManager
import com.goldze.mvvmhabit.aioui.relax.music.play.bean.AIOAlbum
import com.goldze.mvvmhabit.aioui.relax.music.play.notification.PlayerService
import com.goldze.mvvmhabit.aioui.relax.music.play.notification.PlayerService.NOTIFY_PAUSE
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicModel
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicViewPagerItemViewModel
import com.goldze.mvvmhabit.databinding.FragmentMusicBinding
import com.goldze.mvvmhabit.utils.ImageUtil
import com.google.android.material.tabs.TabLayout
import com.kunminx.player.helper.MediaPlayerHelper
import me.goldze.mvvmhabit.base.BaseFragment
import me.goldze.mvvmhabit.utils.ToastUtils


class MusicFragment : BaseFragment<FragmentMusicBinding, MusicModel>() {

    val TAG = "MusicFragment"
    private lateinit var mAudioManager: AudioManager
    val sPlayerManager = PlayerManager.getInstance()
    var mActiveTabPosition = -1
    private var keepTrue = true

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_music
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        keepTrue = false
        if (sPlayerManager.isPlaying) {
            sendBroadcast(PlayerService.NOTIFY_CLOSE)
        }
    }

    override fun initData() {
        super.initData()

        mAudioManager = getSystemService(context!!, AudioManager::class.java)!!

        binding.brRootView.setPageTitle("音乐放松")
        binding.brRootView.backIv.setOnClickListener {
            activity?.finish()
        }

        initVolumeBar()
        initPlayBar()
        updateSeekBar()
        initPlayControl()


        //给ViewPager设置adapter
        binding.adapter = MusicViewPagerBindingAdapter()
        // viewpager tl 关联
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(AIOViewPagerOnTabSelectedListener(binding.viewPager))
    }

    private fun updateSeekBar() {


        // 开启一个线程循环更新进度
        Thread {
            while (keepTrue) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (!sPlayerManager.isPlaying) continue

                //获取总时长
                val duration = MediaPlayerHelper.getInstance().mediaPlayer.duration;
                val currentPosition: Int = MediaPlayerHelper.getInstance().mediaPlayer.currentPosition

                //发送数据给activity
                val message = Message.obtain()
                val bundle = Bundle()
                bundle.putInt("duration", duration)
                bundle.putInt("currentPosition", currentPosition)
                message.data = bundle
                mUpdateSeekBarHandler.sendMessage(message)
            }
        }.start()
    }

    private fun initPlayControl() {
        binding.playerBack.setOnClickListener {
            val musics = sPlayerManager.album?.musics
            if (musics != null && musics.isNotEmpty()) {
                sendBroadcast(PlayerService.NOTIFY_PREVIOUS)
                binding.playerPlayIcon.setImageResource(R.drawable.player_pause)

                var currentIndex = sPlayerManager.albumIndex
                val nextIndex = if (--currentIndex <= 0) sPlayerManager.albumMusics.size - 1 else currentIndex
                syncControlUi(sPlayerManager.albumMusics[nextIndex].record)
                syncAllRecyclerView(nextIndex, mActiveTabPosition)
            } else {
                ToastUtils.showShort("播放列表没有音乐")
            }
        }
        binding.playerForward.setOnClickListener {
            val musics = sPlayerManager.album?.musics
            if (musics != null && musics.isNotEmpty()) {
                sendBroadcast(PlayerService.NOTIFY_NEXT)
                binding.playerPlayIcon.setImageResource(R.drawable.player_pause)

                var currentIndex = sPlayerManager.albumIndex
                val nextIndex = if (++currentIndex >= sPlayerManager.albumMusics.size) 0 else currentIndex
                syncControlUi(sPlayerManager.albumMusics[nextIndex].record)
                syncAllRecyclerView(nextIndex, mActiveTabPosition)
            } else {
                ToastUtils.showShort("播放列表没有音乐")
            }

        }

        binding.playerPlayIcon.setOnClickListener {
            if (it is ImageView) {
                dealWithPauseOrPlay(it)
            }
        }
    }

    private fun dealWithPauseOrPlay(imageView: ImageView) {
        val musics = sPlayerManager.album?.musics
        if (musics == null || musics.isEmpty()) {
            ToastUtils.showShort("播放列表没有音乐")
            return
        }

        if (sPlayerManager.isPlaying) {
            sendBroadcast(NOTIFY_PAUSE)
            imageView.setImageResource(R.drawable.player_start)
            syncAllRecyclerView(0, -1)
        } else {
            sendBroadcast(PlayerService.NOTIFY_PLAY)
            imageView.setImageResource(R.drawable.player_pause)
            val currentIndex = sPlayerManager.albumIndex
            syncAllRecyclerView(currentIndex, mActiveTabPosition)
        }
    }

    private fun sendBroadcast(action: String) {
        context!!.sendBroadcast(Intent(action).setPackage(context!!.packageName))
    }

    private fun initVolumeBar() {
        // 初始化进度条为音量大小
        val max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        Log.i(TAG, "initVolumeBar: max:$max current:$current")
        binding.volumeBar.progress = (current / max.toFloat() * 100).toInt()
        binding.volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged: $progress")
                // 改变音量
                mAudioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (progress / 100f * max).toInt(),
                    AudioManager.FLAG_PLAY_SOUND
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun initPlayBar() {
        binding.seekPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.currentProgress.text = sPlayerManager.getTrackTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sPlayerManager.setSeek(seekBar!!.progress)
            }
        })
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            Log.i("fengao_xiaomi", "initViewObservable: ${entity.toString()}")
            if (entity is MusicRecord) {
                // 是否为暂停
                if (viewModel.currentTabPosition == mActiveTabPosition
                    && entity.itemPosition == sPlayerManager.albumIndex
                ) {
                    dealWithPauseOrPlay(binding.playerPlayIcon)
                    return@observe
                }


                // 更新播放列表
                val musicList = viewModel.items[viewModel.currentTabPosition].observableList.map {
                    val musicRecord = it.entity.get() as MusicRecord
                    AIOAlbum.AIOMusic().apply {
                        coverImg = musicRecord.faceImage ?: ""
                        title = musicRecord.name ?: ""
                        desc = musicRecord.musicDescribe ?: ""
                        url = musicRecord.resourcesUrl ?: ""
                        record = musicRecord
                    }
                }
                val aioAlbum = AIOAlbum().apply {
                    musics = musicList
                }
                sPlayerManager.loadAlbum(aioAlbum)
                val itemPosition = entity.itemPosition
                if (itemPosition >= sPlayerManager.albumMusics.size) {
                    return@observe
                }
                sPlayerManager.loadAlbum(aioAlbum, itemPosition)

                syncControlUi(entity)
                syncAllRecyclerView(itemPosition)
            }
        }

        viewModel.tabLoadComplete.observe(this) {
            for (itemDatum in viewModel.tabItemData) {
                val tab = binding.tabs.newTab()
                val view = layoutInflater.inflate(R.layout.item_tablayout_topic, null)
                val tv = view.findViewById<TextView>(R.id.tvLabel)
                tv.text = itemDatum.name
                tab.customView = view
                binding.tabs.addTab(tab)

                val viewpagerItemViewModel = MusicViewPagerItemViewModel(
                    viewModel,
                    activity!!.application,
                    MusicRepository(),
                )
                viewpagerItemViewModel.tabBean = itemDatum
                viewModel.items.add(viewpagerItemViewModel)
            }

            // 请求第一页数据
            if (viewModel.items.size > 0) {
                viewModel.items[0].onRefreshCommand.execute()
            }
        }

        viewModel.loadTabsData()
    }

    private fun syncControlUi(entity: MusicRecord?) {
        if (entity == null) {
            return
        }
        // 控制部分 UI 更新
        binding.title.text = entity.name ?: ""
        binding.desc.text = entity.brief ?: ""
        ImageUtil.display(
            entity.faceImage,
            binding.musicMainIcon,
            0
        )
        binding.playerPlayIcon.setImageResource(R.drawable.player_pause)
    }

    private fun syncAllRecyclerView(itemPosition: Int, activeTabPosition: Int = binding.viewPager.currentItem) {
        // 同步所有 tab 下的 RV
        val viewPagerBindingAdapter = binding.viewPager.adapter as MusicViewPagerBindingAdapter
        for (entry in viewPagerBindingAdapter.mAdapterMap) {
            syncOtherRvAdapter(entry.value)
        }

        // 同步当前 tab 下的 RV
        val currentRvAdapter = viewPagerBindingAdapter.mAdapterMap[activeTabPosition] ?: return
        mActiveTabPosition = activeTabPosition
        syncCurrentRvAdapter(currentRvAdapter, itemPosition)
    }

    // 同步当前 ViewPager Rv 项的状态
    private fun syncCurrentRvAdapter(
        currentRvAdapter: MusicRecyclerViewBindingAdapter?,
        itemPosition: Int
    ) {
        if (currentRvAdapter == null) {
            return
        }
        for (i in 0 until currentRvAdapter.itemCount) {
            currentRvAdapter.mBindingMap[i]?.controlImage?.setImageResource(R.drawable.player_start)
        }
        currentRvAdapter.mBindingMap[itemPosition]?.controlImage?.setImageResource(R.drawable.player_pause)
    }

    // 同步当前 ViewPager Rv 项的状态
    private fun syncOtherRvAdapter(rvAdapter: MusicRecyclerViewBindingAdapter?) {
        if (rvAdapter == null) {
            return
        }
        for (i in 0 until rvAdapter.itemCount) {
            rvAdapter.mBindingMap[i]?.controlImage?.setImageResource(R.drawable.player_start)
        }
    }

    private var mUpdateSeekBarHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (!sPlayerManager.isPlaying) {
                return
            }
            val data = msg.data
            val duration = data.getInt("duration")
            val currentPosition = data.getInt("currentPosition")
            binding.seekPlay.max = duration
            binding.seekPlay.progress = currentPosition

            binding.currentProgress.text = sPlayerManager.getTrackTime(currentPosition)
            binding.musicDuration.text = sPlayerManager.getTrackTime(duration)

            initVolumeBar()
        }
    }
}