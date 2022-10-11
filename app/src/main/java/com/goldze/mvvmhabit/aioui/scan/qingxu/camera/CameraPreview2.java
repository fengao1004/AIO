package com.goldze.mvvmhabit.aioui.scan.qingxu.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;


import androidx.annotation.Nullable;

import com.goldze.mvvmhabit.aioui.scan.qingxu.constants.ErrorCode;
import com.goldze.mvvmhabit.aioui.scan.qingxu.mvp.exceptions.CameraUnavailableException;

import java.io.IOException;
import java.util.List;

public class CameraPreview2 extends TextureView implements TextureView.SurfaceTextureListener {

    private static final int CAMERA_ID = 0;

    private static final String TAG = "CameraPreview";
    @Nullable
    private Camera mCamera;
    @Nullable
    private Camera.CameraInfo mCameraInfo;
    private CameraCallbacks mCallbacks;
    private int mRotation;

    public CameraPreview2(Context context) {
        this(context, null);
    }

    public CameraPreview2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreview2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraPreview2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSurfaceTextureListener(this);
//        paint.setStrokeWidth(6);
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.parseColor("#ff0000"));
    }

    public void setCameraCallbacks(CameraCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            openCamera();
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, info);
//            int rotation = Surface.ROTATION_0;
//            if (getContext() instanceof Activity) {
//                rotation = ((Activity) getContext())
//                        .getWindowManager().getDefaultDisplay().getRotation();
//            }
//
//            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
//                rotation = info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ? 360 - info.orientation : info.orientation;
//                Log.d(TAG, "orientation: portrait rotation="+rotation);
//            } else {
//                rotation = 90;
//                Log.d(TAG, "orientation: landscape");
//            }
            setCamera(mCamera, info, 90);
            startPreview(surface);
        } catch (Exception e) {
            e.printStackTrace();
            if (mCallbacks != null) {
                mCallbacks.onCameraUnavailable(ErrorCode.CAMERA_UNAVAILABLE_ERROR);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        stopPreviewAndFreeCamera();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void startPreview(SurfaceTexture surface) throws IOException {
        // The Surface has been created, now tell the camera where to draw the preview.
        if (mCamera == null || mCameraInfo == null) {
            return;
        }
        try {
            mCamera.setPreviewTexture(surface);
            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            for (Camera.Size size : sizes) {
                Log.i("fengao_xiaomi", "width: " + size.width + " height:  " + size.height);
            }
            Camera.Size expected = sizes.get(sizes.size() - 1);
            for (Camera.Size size : sizes) {
                if (size.width == 800 && size.height == 600) {
                    expected = size;
                    break;
                }
            }
            Log.i(TAG, "Preview size is w:" + expected.width + " h:" + expected.height);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(expected.width, expected.height);
            //parameters.setPreviewFpsRange();
            mCamera.setParameters(parameters);
            // Start camera preview when id scanned. Del by linhx 20170428 begin
            mCamera.startPreview();
            Log.i(TAG, "Camera preview started.");
            if (mCallbacks != null) {
                mCamera.setPreviewCallback(mCallbacks);
            }
            // Start camera preview when id scanned. Del by linhx 20170428 end
        } catch (Exception e) {
            Log.i(TAG, "Error setting camera preview: " + e.getMessage());
            if (mCallbacks != null) {
                mCallbacks.onCameraUnavailable(ErrorCode.CAMERA_UNAVAILABLE_PREVIEW);
            }
        }
    }

    public void pausePreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

    public void restartPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            if (mCallbacks != null) {
                mCamera.setPreviewCallback(mCallbacks);
            }
        }
    }

    private void openCamera() throws CameraUnavailableException {
        if (Camera.getNumberOfCameras() > 0) {
            try {
                mCamera = Camera.open(CAMERA_ID);
                assert mCamera != null;
            } catch (Exception e) {
                throw new CameraUnavailableException(e);
            }
        } else {
            throw new CameraUnavailableException();
        }
    }

    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void setCamera(Camera camera, Camera.CameraInfo cameraInfo,
                           int displayOrientation) {
        mCamera = camera;
        mCameraInfo = cameraInfo;
        mCamera.setDisplayOrientation(displayOrientation);
        mRotation = displayOrientation;
    }

    public int getCameraRotation() {
        return mRotation;
    }

    @Override
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(600, 800, Bitmap.Config.RGB_565);
        return super.getBitmap(bitmap);
    }

//
//    float faceX = 0.0f;
//    float faceY = 0.0f;
//    float faceWidth = 0.0f;
//    float faceHeight = 0.0f;
//    Paint paint = new Paint();
//
//    public void updateFace(int x, int y, int faceWidth, int faceHeight, float scale) {
//        this.faceX = x * scale;
//        this.faceY = y * scale;
//        this.faceWidth = faceWidth * scale;
//        this.faceHeight = faceHeight * scale;
//        invalidate();
//    }


//    onDraw(canvas:Canvas?) {
//        super.onDraw(canvas)
//        canvas ?.drawColor(Color.TRANSPARENT);
//        if (faceHeight * faceHeight > 0) {
//            Log.i("fengao_xiaomi", "onDraw: $x $y $faceHeight $faceWidth")
//            var rect = RectF()
//            rect.left = x;
//            rect.right = (x + faceWidth);
//            rect.top = y;
//            rect.bottom = (y + faceHeight);
//            canvas ?.drawRect(rect, paint);
//        }
//    }
}
