/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goldze.mvvmhabit.aioui.relax.music.play;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.danikula.videocache.HttpProxyCacheServer;
import com.goldze.mvvmhabit.aioui.relax.music.play.bean.AIOAlbum;
import com.goldze.mvvmhabit.aioui.relax.music.play.helper.PlayerFileNameGenerator;
import com.goldze.mvvmhabit.aioui.relax.music.play.notification.PlayerService;
import com.kunminx.player.PlayerController;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;
import com.kunminx.player.contract.ICacheProxy;
import com.kunminx.player.contract.IPlayController;
import com.kunminx.player.contract.IServiceNotifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/10/31
 */
public class PlayerManager implements IPlayController<AIOAlbum, AIOAlbum.AIOMusic, AIOAlbum.AIOArtist> {

    private static final PlayerManager sManager = new PlayerManager();

    private final PlayerController<AIOAlbum, AIOAlbum.AIOMusic, AIOAlbum.AIOArtist> mController;

    private PlayerManager() {
        mController = new PlayerController<>();
    }

    public static PlayerManager getInstance() {
        return sManager;
    }

    private HttpProxyCacheServer mProxy;

    public void init(Context context) {
        init(context, null, null);
    }

    @Override
    public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
        Context applicationContext = context.getApplicationContext();

        mProxy = new HttpProxyCacheServer.Builder(applicationContext)
                .fileNameGenerator(new PlayerFileNameGenerator())
                .maxCacheSize(2147483648L) // 2GB
                .build();

        //添加额外的音乐格式
        List<String> extraFormats = new ArrayList<>();
        extraFormats.add(".flac");
        extraFormats.add(".ape");

        mController.init(
                applicationContext,
                extraFormats,
                startOrStop -> {
                    Intent intent = new Intent(applicationContext, PlayerService.class);
                    if (startOrStop) {
                        applicationContext.startService(intent);
                    } else {
                        applicationContext.stopService(intent);
                    }
                },
                url -> mProxy.getProxyUrl(url));
    }

    @Override
    public void loadAlbum(AIOAlbum musicAlbum) {
        mController.loadAlbum(musicAlbum);
    }

    @Override
    public void loadAlbum(AIOAlbum musicAlbum, int playIndex) {
        mController.loadAlbum(musicAlbum, playIndex);
    }

    @Override
    public void playAudio() {
        mController.playAudio();
    }

    @Override
    public void playAudio(int albumIndex) {
        mController.playAudio(albumIndex);
    }

    @Override
    public void playNext() {
        mController.playNext();
    }

    @Override
    public void playPrevious() {
        mController.playPrevious();
    }

    @Override
    public void playAgain() {
        mController.playAgain();
    }

    @Override
    public void pauseAudio() {
        mController.pauseAudio();
    }

    @Override
    public void resumeAudio() {
        mController.resumeAudio();
    }

    @Override
    public void clear() {
        if (mController != null) {
            try {
                mController.clear();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                Log.e("PlayManager", "clear error: " + e.getMessage());
            }
        }
    }

    @Override
    public void changeMode() {
        mController.changeMode();
    }

    @Override
    public boolean isPlaying() {
        return mController.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return mController.isPaused();
    }

    @Override
    public boolean isInit() {
        return mController.isInit();
    }

    @Override
    public void requestLastPlayingInfo() {
        mController.requestLastPlayingInfo();
    }

    @Override
    public void setSeek(int progress) {
        mController.setSeek(progress);
    }

    @Override
    public String getTrackTime(int progress) {
        return mController.getTrackTime(progress);
    }

    @Override
    public AIOAlbum getAlbum() {
        return mController.getAlbum();
    }

    @Override
    public List<AIOAlbum.AIOMusic> getAlbumMusics() {
        return mController.getAlbumMusics();
    }

    @Override
    public void setChangingPlayingMusic(boolean changingPlayingMusic) {
        mController.setChangingPlayingMusic(changingPlayingMusic);
    }

    @Override
    public int getAlbumIndex() {
        return mController.getAlbumIndex();
    }

    public LiveData<ChangeMusic<AIOAlbum, AIOAlbum.AIOMusic, AIOAlbum.AIOArtist>> getChangeMusicResult() {
        return mController.getChangeMusicResult();
    }

    public LiveData<PlayingMusic<AIOAlbum, AIOAlbum.AIOMusic, AIOAlbum.AIOArtist>> getPlayingMusicResult() {
        return mController.getPlayingMusicResult();
    }

    public LiveData<Boolean> getPauseResult() {
        return mController.getPauseResult();
    }

    @Override
    public LiveData<Enum<PlayingInfoManager.RepeatMode>> getPlayModeResult() {
        return mController.getPlayModeResult();
    }

    @Override
    public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
        return mController.getRepeatMode();
    }

    @Override
    public void togglePlay() {
        mController.togglePlay();
    }

    @Override
    public AIOAlbum.AIOMusic getCurrentPlayingMusic() {
        return mController.getCurrentPlayingMusic();
    }
}
