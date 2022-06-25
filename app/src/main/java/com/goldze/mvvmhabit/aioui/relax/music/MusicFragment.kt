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
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.listener.AIOViewPagerOnTabSelectedListener
import com.goldze.mvvmhabit.aioui.relax.music.adapter.MusicViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.relax.music.play.PlayerManager
import com.goldze.mvvmhabit.aioui.relax.music.play.bean.AIOAlbum
import com.goldze.mvvmhabit.aioui.relax.music.play.notification.PlayerService
import com.goldze.mvvmhabit.aioui.relax.music.play.notification.PlayerService.NOTIFY_PAUSE
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicModel
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicViewPagerItemViewModel
import com.goldze.mvvmhabit.app.Injection
import com.goldze.mvvmhabit.databinding.FragmentMusicBinding
import com.google.android.material.tabs.TabLayout
import com.kunminx.player.helper.MediaPlayerHelper
import me.goldze.mvvmhabit.base.BaseFragment
import me.goldze.mvvmhabit.utils.ToastUtils


class MusicFragment : BaseFragment<FragmentMusicBinding, MusicModel>() {

    val TAG = "MusicFragment"

    private lateinit var mAudioManager: AudioManager
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
    }

    override fun initData() {
        super.initData()

        mAudioManager = getSystemService(context!!, AudioManager::class.java)!!

        binding.brRootView.setAppTitle("心理自助服务一体机")
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

        //模拟几个ViewPager页面
        for (i in 1..5) {
            val viewpagerItemViewModel = MusicViewPagerItemViewModel(
                viewModel,
                activity!!.application,
                Injection.provideDemoRepository()
            )
            viewModel.items.add(viewpagerItemViewModel)


            val tab = binding.tabs.newTab()
            val view = layoutInflater.inflate(R.layout.item_tablayout_topic, null)
            val tv = view.findViewById<TextView>(R.id.tvLabel)
            tv.text = "Tab标题"
            tab.customView = view
            binding.tabs.addTab(tab)
        }


        Thread {
            val source = "https://minio.xlxs.top/temp/银临 - 落英.mp3"
            val source2 = "https://minio.xlxs.top/temp/TAKA,野田洋次郎 - By My Side.mp3"
            val source3 = "https://minio.xlxs.top/temp/chaoxi.mp3"
            val aioMusic = AIOAlbum.AIOMusic().apply {
                url = source
            }
            val aioMusic2 = AIOAlbum.AIOMusic().apply {
                url = source2
            }
            val aioMusic3 = AIOAlbum.AIOMusic().apply {
                url = source3
            }

            val musicList = arrayListOf(aioMusic, aioMusic2, aioMusic3)
            val aioAlbum = AIOAlbum().apply {
                musics = musicList
            }

            PlayerManager.getInstance().loadAlbum(aioAlbum)
            PlayerManager.getInstance().playAudio()
        }.start()


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
                //获取总时长
                val duration = MediaPlayerHelper.getInstance().mediaPlayer.duration;
                val currentPosition: Int = MediaPlayerHelper.getInstance().mediaPlayer.currentPosition

                //发送数据给activity
                val message = Message.obtain()
                val bundle = Bundle()
                bundle.putInt("duration", duration)
                bundle.putInt("currentPosition", currentPosition)
                message.data = bundle
                handler.sendMessage(message)
            }
        }.start()
    }

    private fun initPlayControl() {
        binding.playerBack.setOnClickListener {
            sendBroadcast(PlayerService.NOTIFY_PREVIOUS)
            binding.playerPlayIcon.setImageResource(R.drawable.player_pause)

        }
        binding.playerForward.setOnClickListener {
            sendBroadcast(PlayerService.NOTIFY_NEXT)
            binding.playerPlayIcon.setImageResource(R.drawable.player_pause)

        }

        binding.playerPlayIcon.setOnClickListener {
            if (it is ImageView) {
                if (PlayerManager.getInstance().isPlaying) {
                    sendBroadcast(NOTIFY_PAUSE)
                    it.setImageResource(R.drawable.player_start)
                } else if (PlayerManager.getInstance().isPaused) {
                    sendBroadcast(PlayerService.NOTIFY_PLAY)
                    it.setImageResource(R.drawable.player_pause)
                }
            }
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
                binding.currentProgress.text = PlayerManager.getInstance().getTrackTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                PlayerManager.getInstance().setSeek(seekBar!!.progress)
            }
        })
    }

    override fun initViewObservable() {
        viewModel.itemClickEvent.observe(this) { text -> ToastUtils.showShort("position：$text") }
    }


    var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val data = msg.data
            val duration = data.getInt("duration")
            val currentPosition = data.getInt("currentPosition")
            binding.seekPlay.max = duration
            binding.seekPlay.progress = currentPosition

            binding.currentProgress.text = PlayerManager.getInstance().getTrackTime(currentPosition)
            binding.musicDuration.text = PlayerManager.getInstance().getTrackTime(duration)
        }
    }
}