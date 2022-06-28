/*
 * Copyright 2018-present KunMinX
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

package com.goldze.mvvmhabit.aioui.relax.music.play.bean;

import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord;
import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;

/**
 * Create by KunMinX at 19/10/31
 * <p>
 * bean，原始数据，只读
 * Java 我们通过移除 setter
 * kotlin 直接将字段设为 val 即可
 */
public class AIOAlbum extends BaseAlbumItem<AIOAlbum.AIOMusic, AIOAlbum.AIOArtist> {

    private String albumMid;

    public String getAlbumMid() {
        return albumMid;
    }

    public static class AIOMusic extends BaseMusicItem<AIOArtist> {

        private String songMid;

        private String desc;

        private MusicRecord record;

        public String getSongMid() {
            return songMid;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public MusicRecord getRecord() {
            return record;
        }

        public void setRecord(MusicRecord record) {
            this.record = record;
        }
    }

    public static class AIOArtist extends BaseArtistItem {

        private String birthday;

        public String getBirthday() {
            return birthday;
        }
    }
}

