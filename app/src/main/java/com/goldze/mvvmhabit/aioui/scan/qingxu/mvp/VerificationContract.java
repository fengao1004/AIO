package com.goldze.mvvmhabit.aioui.scan.qingxu.mvp;

import android.graphics.Bitmap;
import android.view.TextureView;

import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.SubmitResponseBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.camera.CameraPreview2;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public interface VerificationContract {

    interface View {

        void drawFaceRect(Rect faceRect);

        void drawFaceImage(Bitmap faceBmp);

        void toastMessage(String msg);

        void showCameraUnavailableDialog(int errorCode);

        void setStatus(int status, Mat matBgr, Rect faceRect);

        void setName(String name, Mat matBgr, Rect faceRect);

        void FaceRegister(String tip);

        void showSimpleTip(String tip);

        void setBestImage(Bitmap bitmap);

        void setPresenter(Presenter presenter);

        void setContent(String content);

        void updateFaceUi(int x, int y, int width, int height);

        void updateUi(float[] action, float[] emotion, int blink, double heart);

        CameraPreview2 getTextureView();

        boolean isActive();

        void jump(SubmitResponseBean bean);

        void exit();
    }

    interface Presenter {

        void detect(byte[] data, int width, int height, int rotation);

        void destroy();

        void submit();
    }
}
