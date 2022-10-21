package com.goldze.mvvmhabit.aioui.scan.qingxu.bean;

import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/9
 * Time: 3:32 下午
 */
public class SubmitBean {
    public String facePicUrl = "";
    public String serialNumber = "";

    public SubmitBean() {
        userFaces = new ArrayList<>();
    }

    public ResultDataBean resultData;
    public List<String> userFaces;
    public List<SrcDataBean> srcData;

    public static class ResultDataBean {
        public ResultDataBean() {
            blinkNum = 0;
            fps = 0;
            heartRate = 0;
            frames = 0;
            valid_frames = 0;
        }

        public Integer blinkNum;
        public Integer heartRate;
        public Integer fps;
        public Integer frames;
        public Integer valid_frames;
    }

    public static class SrcDataBean {
        public SrcDataBean() {
            microAction = new ArrayList<>();
            emotion = new ArrayList<>();
            faceInfo = new ArrayList<>();
            heartInfo = -1;
            eyeInfo = -1;
        }

        public List<Float> microAction;
        public List<Float> emotion;
        public List<Float> faceInfo;
        public Integer heartInfo;
        public Integer eyeInfo;
    }
}
