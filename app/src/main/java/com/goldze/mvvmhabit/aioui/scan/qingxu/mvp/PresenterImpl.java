package com.goldze.mvvmhabit.aioui.scan.qingxu.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.goldze.mvvmhabit.aioui.Util;
import com.goldze.mvvmhabit.aioui.http.Api;
import com.goldze.mvvmhabit.aioui.http.HttpRepository;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.SubmitBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.SubmitResponseBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.config.AppConfig;
import com.goldze.mvvmhabit.aioui.scan.qingxu.utils.FileUtils;
import com.goldze.mvvmhabit.utils.RetrofitClient;
import com.seeta.sdk.EmotionRecognizerWithActionUnits;
import com.seeta.sdk.EyeStateDetector;
import com.seeta.sdk.FaceDetector;
import com.seeta.sdk.FaceLandmarker;
import com.seeta.sdk.SeetaDevice;
import com.seeta.sdk.SeetaHeartRateDetector;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaModelSetting;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PresenterImpl implements VerificationContract.Presenter {

    static {
        System.loadLibrary("opencv_java3");
    }

    boolean isSubmit = false;

    Api api = RetrofitClient.getInstance().create(Api.class);
    Bitmap faceBitmap = null;

    private List<SubmitBean.SrcDataBean> srcData = new ArrayList<>();
    SubmitBean.ResultDataBean resultData = new SubmitBean.ResultDataBean();
    private static final String TAG = "PresenterImpl";

    private VerificationContract.View mView;

    static String fdModel = "/sdcard/seeta/SeetaFaceDetector6.0.IPC.sta";
    public static FaceDetector faceDetector = null;
//    public static EmotionRecognizer emotionRecognizer = null;
//    public static ActionUnit actionUnit = null;
    public static SeetaHeartRateDetector seetaHeartRateDetector = null;
    public static EyeStateDetector eyeStateDetector = null;
    public static EmotionRecognizerWithActionUnits emotionRecognizerWithActionUnits = null;

    private static int WIDTH = AppConfig.IMAGE_WIDTH;
    private static int HEIGHT = AppConfig.IMAGE_HEIGHT;
    public SeetaImageData imageData = new SeetaImageData(WIDTH, HEIGHT, 3);

    private float thresh = 0.70f;

    static String pdModel = "/sdcard/seeta/SeetaFaceLandmarker5.0.tsm.sta";
    public static FaceLandmarker faceLandmarker = null;
    public static FaceLandmarker faceLandmarker2 = null;

    static String frModel = "/sdcard/seeta/SeetaFaceRecognizer.RN30.light.tsm.sta";

    public static class TrackingInfo {
        public long time;
        public int personId;
        public Mat matBgr;
        public Mat matGray;
        public SeetaRect faceInfo = new SeetaRect();
        public Rect faceRect = new Rect();
        public float score;
        public long birthTime;
        public long lastProccessTime;
        public static Map<String, float[]> name2feats = new HashMap<>();
    }

    private HandlerThread mFaceTrackThread;
    private HandlerThread mFasThread;

    {
        mFaceTrackThread = new HandlerThread("FaceTrackThread", Process.THREAD_PRIORITY_MORE_FAVORABLE);
        mFasThread = new HandlerThread("FasThread", Process.THREAD_PRIORITY_MORE_FAVORABLE);
        mFaceTrackThread.start();
        mFasThread.start();
    }

    private Mat matNv21 = new Mat(AppConfig.CAMERA_PREVIEW_HEIGHT + AppConfig.CAMERA_PREVIEW_HEIGHT / 2,
            AppConfig.CAMERA_PREVIEW_WIDTH, CvType.CV_8UC1);

    public PresenterImpl(Context context, VerificationContract.View view) {
        mView = view;
        mView.setPresenter(this);

        File cacheDir = getInternalCacheDirectory(context, null);
        String modelPath = cacheDir.getAbsolutePath();
        Log.d("cacheDir", "" + modelPath);

        String fdModel = "SeetaFaceDetector6.0.IPC.sta";
//        String erModel = "SeetaEmotionRecognizer.v1.sta";
        String pdModel = "SeetaFaceLandmarker5.0.tsm.sta";
        String pd2Model = "SeetaFaceLandmarker5.0.pts81.tsm.sta";
//        String actionModel = "SeetaActionUnit1.0.0.ext.sta";
        String eyeModel = "SeetaEyeBlink.squeezenet.4class.214000.1010.sta";
        String actionAndEr = "EmotionAU_emotions_action_units-fast.sta";
        // String key = "key.dat";
        if (!isExists(modelPath, fdModel)) {
            File fdFile = new File(cacheDir + "/" + fdModel);
            FileUtils.copyFromAsset(context, fdModel, fdFile, false);
        }
        if (!isExists(modelPath, pdModel)) {
            File pdFile = new File(cacheDir + "/" + pdModel);
            FileUtils.copyFromAsset(context, pdModel, pdFile, false);
        }
//        if (!isExists(modelPath, erModel)) {
//            File erFile = new File(cacheDir + "/" + erModel);
//            FileUtils.copyFromAsset(context, erModel, erFile, false);
//        }
//        if (!isExists(modelPath, actionModel)) {
//            File erFile = new File(cacheDir + "/" + actionModel);
//            FileUtils.copyFromAsset(context, actionModel, erFile, false);
//        }
        if (!isExists(modelPath, pd2Model)) {
            File erFile = new File(cacheDir + "/" + pd2Model);
            FileUtils.copyFromAsset(context, pd2Model, erFile, false);
        }
        if (!isExists(modelPath, eyeModel)) {
            File erFile = new File(cacheDir + "/" + eyeModel);
            FileUtils.copyFromAsset(context, eyeModel, erFile, false);
        }
        if (!isExists(modelPath, actionAndEr)) {
            File aAeFile = new File(cacheDir + "/" + actionAndEr);
            FileUtils.copyFromAsset(context, actionAndEr, aAeFile, false);
        }
        // if(!isExists("/sdcard", key))
        // {
        // File keyFile = new File("/sdcard/" + key);
        // FileUtils.copyFromAsset(context, key, keyFile, false);
        // }

        String rootPath = cacheDir + "/";
        try {
            if (faceDetector == null || faceLandmarker == null) {
                faceDetector = new FaceDetector(new SeetaModelSetting(0, new String[]{rootPath + fdModel}, SeetaDevice.SEETA_DEVICE_AUTO));
//                emotionRecognizer = new EmotionRecognizer((new SeetaModelSetting(0, new String[]{rootPath + erModel}, SeetaDevice.SEETA_DEVICE_AUTO)));
                faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, new String[]{rootPath + pdModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                faceLandmarker2 = new FaceLandmarker(new SeetaModelSetting(0, new String[]{rootPath + pd2Model}, SeetaDevice.SEETA_DEVICE_AUTO));
//                actionUnit = new ActionUnit(new SeetaModelSetting(0, new String[]{rootPath + actionModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                eyeStateDetector = new EyeStateDetector(new SeetaModelSetting(0, new String[]{rootPath + eyeModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                emotionRecognizerWithActionUnits = new EmotionRecognizerWithActionUnits(new SeetaModelSetting(0, new String[]{rootPath + actionAndEr}, SeetaDevice.SEETA_DEVICE_AUTO));
                seetaHeartRateDetector = new SeetaHeartRateDetector();
                seetaHeartRateDetector.SetFrameNum(10);
            }
            faceDetector.set(FaceDetector.Property.PROPERTY_MIN_FACE_SIZE, 40);
        } catch (Exception e) {
            Log.e(TAG, "init exception:" + e.toString());
        }

    }

    public boolean isExists(String path, String modelName) {
        File file = new File(path + "/" + modelName);
        if (file.exists()) return true;

        return false;
    }

    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Log.e("getInternalDirectory", "getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    boolean hasSave = false;
    int frequency;
    private Handler mFaceTrackingHandler = new Handler(mFaceTrackThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            resultData.frames++;
            final TrackingInfo trackingInfo = (TrackingInfo) msg.obj;
            trackingInfo.matBgr.get(0, 0, imageData.data);
            long startTime = System.currentTimeMillis();
            faceDetector.set(FaceDetector.Property.PROPERTY_NUMBER_THREADS, 2);
            SeetaRect[] faces = faceDetector.Detect(imageData);
            long endTime = System.currentTimeMillis();
            Log.i("time_tag", "人脸检测: " + (endTime - startTime) +"人脸数："+faces.length);
            //设一个初始值
            trackingInfo.faceInfo.x = (int) 0;
            trackingInfo.faceInfo.y = 0;
            trackingInfo.faceInfo.width = 0;
            trackingInfo.faceInfo.height = 0;
            if (faces.length != 0) {
                int maxIndex = 0;
                double maxWidth = 0;
                for (int i = 0; i < faces.length; ++i) {
                    if (faces[i].width > maxWidth) {
                        maxIndex = i;
                        maxWidth = faces[i].width;
                    }
                }
                trackingInfo.faceInfo = faces[maxIndex];

                trackingInfo.faceRect.x = (int) faces[maxIndex].x;
                trackingInfo.faceRect.y = (int) faces[maxIndex].y;
                trackingInfo.faceRect.width = (int) faces[maxIndex].width;
                trackingInfo.faceRect.height = (int) faces[maxIndex].height;
                mView.updateFaceUi(trackingInfo.faceRect.x, trackingInfo.faceRect.y, trackingInfo.faceRect.width, trackingInfo.faceRect.height);
                trackingInfo.lastProccessTime = System.currentTimeMillis();

//                保存图片 只保存一次
                if (!hasSave) {
                    faceBitmap = mView.getTextureView().getBitmap();
                    hasSave = true;
                }

                mFasHandler.removeMessages(0);
                mFasHandler.obtainMessage(0, trackingInfo).sendToTarget();

            } else {
                mView.drawFaceRect(null);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
//                        mView.setStatus(0, null, null);
                    }
                });
            }
        }
    };


    EyeStateDetector.EYE_STATE eye_state_left = EyeStateDetector.EYE_STATE.EYE_UNKNOWN;
    EyeStateDetector.EYE_STATE eye_state_right = EyeStateDetector.EYE_STATE.EYE_UNKNOWN;
    private Handler mFasHandler = new Handler(mFasThread.getLooper()) {

        @Override
        public void handleMessage(Message msg) {
            SubmitBean.SrcDataBean srcDataBean = new SubmitBean.SrcDataBean();
            final TrackingInfo trackingInfo = (TrackingInfo) msg.obj;
            Log.i("fengao_xiaomi", "handleMessage: start " + trackingInfo.time + "    " + System.currentTimeMillis());
            trackingInfo.matGray = new Mat();
            final Rect faceRect = trackingInfo.faceRect;
            trackingInfo.matBgr.get(0, 0, imageData.data);
            float[] f = new float[7];
            float[] f1 = new float[19];

            long startTime = System.currentTimeMillis();

            long endTime;

            //特征点检测
            SeetaPointF[] points = new SeetaPointF[5];
            faceLandmarker.set(FaceLandmarker.Property.PROPERTY_NUMBER_THREADS, 2);
            faceLandmarker.set(FaceLandmarker.Property.PROPERTY_ARM_CPU_MODE, 0);
            faceLandmarker.mark(imageData, trackingInfo.faceInfo, points);
            endTime = System.currentTimeMillis();
            Log.i("time_tag", "5点定位: " + (endTime - startTime));
            //特征点检测
            startTime = System.currentTimeMillis();
            SeetaPointF[] points2 = new SeetaPointF[81];
            faceLandmarker2.set(FaceLandmarker.Property.PROPERTY_NUMBER_THREADS, 2);
            faceLandmarker2.set(FaceLandmarker.Property.PROPERTY_ARM_CPU_MODE, 0);
            faceLandmarker2.mark(imageData, trackingInfo.faceInfo, points2);
            endTime = System.currentTimeMillis();
            Log.i("time_tag", "81点定位: " + (endTime - startTime));


            startTime = System.currentTimeMillis();
            EyeStateDetector.EYE_STATE[] f2 = new EyeStateDetector.EYE_STATE[2];
            eyeStateDetector.set(EyeStateDetector.Property.PROPERTY_NUMBER_THREADS, 2);
            eyeStateDetector.Detect(imageData, points, f2);
            endTime = System.currentTimeMillis();
            Log.i("time_tag", "眼部检测: " + (endTime - startTime));
            srcDataBean.eyeInfo = f2[0].ordinal();

//            startTime = System.currentTimeMillis();
//            actionUnit.set(ActionUnit.Property.PROPERTY_NUMBER_THREADS, 4);
//            actionUnit.set(ActionUnit.Property.PROPERTY_ARM_CPU_MODE, 0);
//            actionUnit.Extract(imageData, points, f1);
//            endTime = System.currentTimeMillis();
//            Log.i("time_tag", "动作检测: " + (endTime - startTime));

            startTime = System.currentTimeMillis();
//            emotionRecognizer.set(EmotionRecognizer.Property.PROPERTY_NUMBER_THREADS, 4);
//            emotionRecognizer.set(EmotionRecognizer.Property.PROPERTY_ARM_CPU_MODE, 0);
//            emotionRecognizer.RecognizeEmotion(imageData, points, f);
            emotionRecognizerWithActionUnits.set(EmotionRecognizerWithActionUnits.Property.PROPERTY_NUMBER_THREADS, 2);
            emotionRecognizerWithActionUnits.set(EmotionRecognizerWithActionUnits.Property.PROPERTY_ARM_CPU_MODE, 0);
            emotionRecognizerWithActionUnits.RecognizeEmotionAndActionUnitsWithCrop(imageData,points,f,f1);
            endTime = System.currentTimeMillis();
            Log.i("time_tag", "表情+动作 检测: " + (endTime - startTime));


            startTime = System.currentTimeMillis();
            seetaHeartRateDetector.GetSignal(imageData, System.currentTimeMillis(), points2);
            double heart = seetaHeartRateDetector.GetHeartRate();
            endTime = System.currentTimeMillis();
            Log.i("time_tag", "心率检测: " + (endTime - startTime));

            int blink = 0;
            if (eye_state_left == EyeStateDetector.EYE_STATE.EYE_CLOSE && f2[0] == EyeStateDetector.EYE_STATE.EYE_OPEN) {
                blink++;
            }
            if (eye_state_right == EyeStateDetector.EYE_STATE.EYE_CLOSE && f2[1] == EyeStateDetector.EYE_STATE.EYE_OPEN) {
                blink++;
            }
            eye_state_left = f2[0];
            eye_state_right = f2[1];
            srcDataBean.heartInfo = Double.valueOf(heart).intValue();

            srcDataBean.faceInfo.add(0.0f);
            srcDataBean.faceInfo.add(0.0f);
            srcDataBean.faceInfo.add(0.0f);
            for (int i = 0; i < f.length; i++) {
                srcDataBean.emotion.add(f[i]);
            }

            for (int i = 0; i < f1.length; i++) {
                if (i == 10) {
                    continue;
                }
                srcDataBean.microAction.add(f1[i]);
            }
            srcData.add(srcDataBean);
            resultData.heartRate = Double.valueOf(heart).intValue();
            resultData.blinkNum += blink;
            resultData.valid_frames++;
            mView.updateUi(f1, f, blink, heart);
        }
    };

    @Override
    public void detect(byte[] data, int width, int height, int rotation) {
        if (isSubmit) {
            return;
        }
        TrackingInfo trackingInfo = new TrackingInfo();
        trackingInfo.time = System.currentTimeMillis();
        matNv21.put(0, 0, data);
        trackingInfo.matBgr = new Mat(AppConfig.CAMERA_PREVIEW_HEIGHT, AppConfig.CAMERA_PREVIEW_WIDTH, CvType.CV_8UC3);
        trackingInfo.matGray = new Mat();

        Imgproc.cvtColor(matNv21, trackingInfo.matBgr, Imgproc.COLOR_YUV2BGR_NV21);
        Core.transpose(trackingInfo.matBgr, trackingInfo.matBgr);
        if (!HttpRepository.isTest){
            Core.rotate(trackingInfo.matBgr, trackingInfo.matBgr, Core.ROTATE_180);
        }
        Core.flip(trackingInfo.matBgr, trackingInfo.matBgr, 0);
        Core.flip(trackingInfo.matBgr, trackingInfo.matBgr, 1);

        Imgproc.cvtColor(trackingInfo.matBgr, trackingInfo.matGray, Imgproc.COLOR_BGR2GRAY);

        trackingInfo.birthTime = System.currentTimeMillis();
        trackingInfo.lastProccessTime = System.currentTimeMillis();

        mFaceTrackingHandler.removeMessages(1);
        mFaceTrackingHandler.obtainMessage(1, trackingInfo).sendToTarget();
    }

    public void saveImgage(Bitmap bitmap, String path, String imageName) {
        File f = new File(path, imageName);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        mFaceTrackThread.quitSafely();
        mFasThread.quitSafely();
    }

    @Override
    public void submit() {
        final ProgressDialog dialog = new ProgressDialog(mView.getTextureView().getContext());
        dialog.setCancelable(false);
        dialog.setMessage("分析中。。。");
        dialog.show();
        isSubmit = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i("fengao_xiaomi", "run: 开始保存");
                if (faceBitmap != null) {
                    saveImgage(faceBitmap, "/sdcard/rlsb", "face.jpg");
                }
                if (faceBitmap != null) {
                    faceBitmap.recycle();
                }
                faceBitmap = null;
                Log.i("fengao_xiaomi", "run: 开始结束");
                File file = new File("/sdcard/rlsb/face.jpg");
                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody multipartBody = new MultipartBody.Builder()
                        .addFormDataPart("file", "face.jpg", body)
                        .setType(MultipartBody.FORM)
                        .build();
                resultData.fps = resultData.frames / 20;
                SubmitBean bean = new SubmitBean();
                bean.serialNumber = Util.INSTANCE.getSerialNumber();
                bean.resultData = resultData;
                bean.srcData = srcData;
                api.updateFacePic(multipartBody.parts())
                        .flatMap(uploadResponseBean -> {
                            if (uploadResponseBean.success) {
                                bean.facePicUrl = uploadResponseBean.data;
                                return api.faceSubmit(bean);
                            } else {
                                throw new RuntimeException("");
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<SubmitResponseBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(SubmitResponseBean bean) {
                                dialog.dismiss();
                                if (bean.success) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            File file1 = new File("/sdcard/rlsb/face.mp4");
                                            File file2 = new File("/sdcard/rlsb/face_"+ bean.data +".mp4");
                                            file1.renameTo(file2);
                                            VideoUtils.start();
                                        }
                                    }.start();
                                    mView.jump(bean);
                                } else {
                                    ToastUtils.showShort("发生了一些错误，请重新尝试" + bean.message);
                                    mView.exit();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                ToastUtils.showShort("发生了一些错误，请重新尝试");
                                mView.exit();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }.start();

    }

}

class VideoUtil {


}
