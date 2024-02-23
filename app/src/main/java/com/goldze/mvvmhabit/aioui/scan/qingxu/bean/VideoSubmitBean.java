package com.goldze.mvvmhabit.aioui.scan.qingxu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/9
 * Time: 3:32 下午
 */
public class VideoSubmitBean {
    public String faceVideoUrl = "";
    public String reportId = "";
    public String faceVideoStartTime = "";
    public String faceVideoEndTime = "";

    public VideoSubmitBean(String facePicUrl, String infoId, String faceVideoStartTime, String faceVideoEndTime) {
        this.faceVideoUrl = facePicUrl;
        this.reportId = infoId;
        this.faceVideoStartTime = faceVideoStartTime;
        this.faceVideoEndTime = faceVideoEndTime;
    }
}
