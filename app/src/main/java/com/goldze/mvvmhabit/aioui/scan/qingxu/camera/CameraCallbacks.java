package com.goldze.mvvmhabit.aioui.scan.qingxu.camera;

import android.hardware.Camera;

@SuppressWarnings("deprecation")
public interface CameraCallbacks extends Camera.PreviewCallback{

    void onCameraUnavailable(int errorCode);
}
