package com.goldze.mvvmhabit.aioui.scan.qingxu.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.goldze.mvvmhabit.aioui.scan.qingxu.config.AppConfig;
import com.goldze.mvvmhabit.aioui.scan.qingxu.utils.FileUtils;
import com.seeta.sdk.ActionUnit;
import com.seeta.sdk.EmotionRecognizer;
import com.seeta.sdk.EyeStateDetector;
import com.seeta.sdk.FaceDetector;
import com.seeta.sdk.FaceLandmarker;
import com.seeta.sdk.FaceRecognizer;
import com.seeta.sdk.SeetaDevice;
import com.seeta.sdk.SeetaHeartRateDetector;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaModelSetting;
import com.seeta.sdk.SeetaPointF;
import com.seeta.sdk.SeetaRect;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PresenterImpl implements VerificationContract.Presenter {

    static {
        System.loadLibrary("opencv_java3");
    }

    private static final String TAG = "PresenterImpl";

    private VerificationContract.View mView;

    static String fdModel = "/sdcard/seeta/SeetaFaceDetector6.0.IPC.sta";
    public static FaceDetector faceDetector = null;
    public static EmotionRecognizer emotionRecognizer = null;
    public static ActionUnit actionUnit = null;
    public static SeetaHeartRateDetector seetaHeartRateDetector = null;
    public static EyeStateDetector eyeStateDetector = null;

    private static int WIDTH = AppConfig.IMAGE_WIDTH;
    private static int HEIGHT = AppConfig.IMAGE_HEIGHT;
    public SeetaImageData imageData = new SeetaImageData(WIDTH, HEIGHT, 3);

    private float thresh = 0.70f;

    static String pdModel = "/sdcard/seeta/SeetaFaceLandmarker5.0.tsm.sta";
    public static FaceLandmarker faceLandmarker = null;
    public static FaceLandmarker faceLandmarker2 = null;

    static String frModel = "/sdcard/seeta/SeetaFaceRecognizer.RN30.light.tsm.sta";
    public static FaceRecognizer faceRecognizer = null;

    public static class TrackingInfo {
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
        String erModel = "SeetaEmotionRecognizer.v1.sta";
        String pdModel = "SeetaFaceLandmarker5.0.tsm.sta";
        String pd2Model = "SeetaFaceLandmarker5.0.pts81.tsm.sta";
        String frModel = "SeetaFaceRecognizer.RN30.light.tsm.sta";
        String actionModel = "SeetaActionUnit1.0.0.ext.sta";
        String eyeModel = "SeetaEyeBlink.squeezenet.4class.214000.1010.sta";
        // String key = "key.dat";
        if (!isExists(modelPath, fdModel)) {
            File fdFile = new File(cacheDir + "/" + fdModel);
            FileUtils.copyFromAsset(context, fdModel, fdFile, false);
        }
        if (!isExists(modelPath, pdModel)) {
            File pdFile = new File(cacheDir + "/" + pdModel);
            FileUtils.copyFromAsset(context, pdModel, pdFile, false);
        }
        if (!isExists(modelPath, frModel)) {
            File frFile = new File(cacheDir + "/" + frModel);
            FileUtils.copyFromAsset(context, frModel, frFile, false);
        }
        if (!isExists(modelPath, erModel)) {
            File erFile = new File(cacheDir + "/" + erModel);
            FileUtils.copyFromAsset(context, erModel, erFile, false);
        }
        if (!isExists(modelPath, actionModel)) {
            File erFile = new File(cacheDir + "/" + actionModel);
            FileUtils.copyFromAsset(context, actionModel, erFile, false);
        }
        if (!isExists(modelPath, pd2Model)) {
            File erFile = new File(cacheDir + "/" + pd2Model);
            FileUtils.copyFromAsset(context, pd2Model, erFile, false);
        }
        if (!isExists(modelPath, eyeModel)) {
            File erFile = new File(cacheDir + "/" + eyeModel);
            FileUtils.copyFromAsset(context, eyeModel, erFile, false);
        }
        // if(!isExists("/sdcard", key))
        // {
        // File keyFile = new File("/sdcard/" + key);
        // FileUtils.copyFromAsset(context, key, keyFile, false);
        // }

        String rootPath = cacheDir + "/";
        try {
            if (faceDetector == null || faceLandmarker == null || faceRecognizer == null) {
                faceDetector = new FaceDetector(new SeetaModelSetting(0, new String[]{rootPath + fdModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                emotionRecognizer = new EmotionRecognizer((new SeetaModelSetting(0, new String[]{rootPath + erModel}, SeetaDevice.SEETA_DEVICE_AUTO)));
                faceLandmarker = new FaceLandmarker(new SeetaModelSetting(0, new String[]{rootPath + pdModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                faceLandmarker2 = new FaceLandmarker(new SeetaModelSetting(0, new String[]{rootPath + pd2Model}, SeetaDevice.SEETA_DEVICE_AUTO));
                faceRecognizer = new FaceRecognizer(new SeetaModelSetting(0, new String[]{rootPath + frModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                actionUnit = new ActionUnit(new SeetaModelSetting(0, new String[]{rootPath + actionModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                eyeStateDetector = new EyeStateDetector(new SeetaModelSetting(0, new String[]{rootPath + eyeModel}, SeetaDevice.SEETA_DEVICE_AUTO));
                seetaHeartRateDetector = new SeetaHeartRateDetector();
            }
            faceDetector.set(FaceDetector.Property.PROPERTY_MIN_FACE_SIZE, 80);
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

    int frequency;
    private Handler mFaceTrackingHandler = new Handler(mFaceTrackThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            long t = System.currentTimeMillis();
            final TrackingInfo trackingInfo = (TrackingInfo) msg.obj;

            trackingInfo.matBgr.get(0, 0, imageData.data);

            //测试输入到算法中的图像数据
//            Mat inputDataMat = new Mat(imageData.height, imageData.width, CvType.CV_8UC3);
//            inputDataMat.put(0, 0, imageData.data);
//            saveImgage(trackingInfo.matBgr, "/sdcard/rlsb_mi8", "l-" + frequency++ + ".jpg");

            SeetaRect[] faces = faceDetector.Detect(imageData);

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
                trackingInfo.lastProccessTime = System.currentTimeMillis();

                int limitX = trackingInfo.faceRect.x + trackingInfo.faceRect.width;
                int limitY = trackingInfo.faceRect.y + trackingInfo.faceRect.height;
                if (limitX < WIDTH && limitY < HEIGHT) {
                    Mat faceMatBGR = new Mat(trackingInfo.matBgr, trackingInfo.faceRect);
                    Imgproc.resize(faceMatBGR, faceMatBGR, new Size(200, 240));
                    Mat faceMatBGRA = new Mat();
                    Imgproc.cvtColor(faceMatBGR, faceMatBGRA, Imgproc.COLOR_BGR2RGBA);
                    Bitmap faceBmp = Bitmap.createBitmap(faceMatBGR.width(), faceMatBGR.height(),
                            Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(faceMatBGRA, faceBmp);
                    mView.drawFaceImage(faceBmp);

//                    saveImgage(faceMatBGR, "/sdcard/rlsb", "l-" + frequency++ + ".jpg");
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

    private Handler mFasHandler = new Handler(mFasThread.getLooper()) {

        @Override
        public void handleMessage(Message msg) {
            final TrackingInfo trackingInfo = (TrackingInfo) msg.obj;
            trackingInfo.matGray = new Mat();
            final Rect faceRect = trackingInfo.faceRect;
            trackingInfo.matBgr.get(0, 0, imageData.data);

            String targetName = "unknown";

            //注册人脸
            QingxuFragment qingxuFragment = (QingxuFragment) mView;
//            float[] f = new float[7];
//            emotionRecognizer.RecognizeEmotionWithCroppedFace(imageData, f);
//            float[] f1 = new float[19];
//            actionUnit.ExtractCroppedFace(imageData, f1);
//            String content = "自然: " + f[0] + "\r\n" +
//                    "愤怒: " + f[1] + "\r\n" +
//                    "厌恶: " + f[2] + "\r\n" +
//                    "恐惧: " + f[3] + "\r\n" +
//                    "高兴: " + f[4] + "\r\n" +
//                    "伤心: " + f[5] + "\r\n" +
//                    "惊讶: " + f[6] + "\r\n";
//
//            content += "内眉上扬: " + f1[0] + "\r\n" +
//                    "外眉上扬: " + f1[1] + "\r\n" +
//                    "眉毛下压: " + f1[2] + "\r\n" +
//                    "上眼睑上扬: " + f1[3] + "\r\n" +
//                    "脸颊抬起: " + f1[4] + "\r\n" +
//                    "眼睑收紧: " + f1[5] + "\r\n" +
//                    "鼻子蹙皱: " + f1[6] + "\r\n" +
//                    "上唇抬起: " + f1[7] + "\r\n" +
//                    "嘴角上扬: " + f1[8] + "\r\n" +
//                    "收缩嘴角: " + f1[9] + "\r\n" +
//                    "嘴角下拉: " + f1[10] + "\r\n" +
//                    "？？: " + f1[11] + "\r\n" +
//                    "下巴缩紧: " + f1[12] + "\r\n" +
//                    "噘嘴: " + f1[13] + "\r\n" +
//                    "嘴唇舒展: " + f1[14] + "\r\n" +
//                    "嘴唇收缩: " + f1[15] + "\r\n" +
//                    "嘴唇压紧: " + f1[16] + "\r\n" +
//                    "上下唇分开: " + f1[17] + "\r\n" +
//                    "下颚下拉: " + f1[18] + "\r\n";
//            mView.setContent(content);

            boolean canRegister = true;
            float[] feats = new float[faceRecognizer.GetExtractFeatureSize()];

            //特征点检测
            SeetaPointF[] points = new SeetaPointF[5];
            faceLandmarker.mark(imageData, trackingInfo.faceInfo, points);

            //特征点检测
            SeetaPointF[] points2 = new SeetaPointF[81];
            faceLandmarker2.mark(imageData, trackingInfo.faceInfo, points2);
//
//            //特征提取
//            faceRecognizer.Extract(imageData, points, feats);


//            EyeStateDetector.EYE_STATE[] f2 = new EyeStateDetector.EYE_STATE[2];
//            eyeStateDetector.Detect(imageData,points,f2);
//            Log.i("fengao_xiaomi", "handleMessage: f2 0 " + f2[0].name());
//            Log.i("fengao_xiaomi", "handleMessage: f2 1 " + f2[1].name());

            float[] f1 = new float[19];
            actionUnit.Extract(imageData, points, f1);
            String content = "内眉上扬: " + f1[0] + "\r\n" +
                    "外眉上扬: " + f1[1] + "\r\n" +
                    "眉毛下压: " + f1[2] + "\r\n" +
                    "上眼睑上扬: " + f1[3] + "\r\n" +
                    "脸颊抬起: " + f1[4] + "\r\n" +
                    "眼睑收紧: " + f1[5] + "\r\n" +
                    "鼻子蹙皱: " + f1[6] + "\r\n" +
                    "上唇抬起: " + f1[7] + "\r\n" +
                    "嘴角上扬: " + f1[8] + "\r\n" +
                    "收缩嘴角: " + f1[9] + "\r\n" +
                    "嘴角下拉: " + f1[10] + "\r\n" +
                    "？？: " + f1[11] + "\r\n" +
                    "下巴缩紧: " + f1[12] + "\r\n" +
                    "噘嘴: " + f1[13] + "\r\n" +
                    "嘴唇舒展: " + f1[14] + "\r\n" +
                    "嘴唇收缩: " + f1[15] + "\r\n" +
                    "嘴唇压紧: " + f1[16] + "\r\n" +
                    "上下唇分开: " + f1[17] + "\r\n" +
                    "下颚下拉: " + f1[18] + "\r\n";
            mView.setContent(content);
            seetaHeartRateDetector.SetFrameNum(10);
            seetaHeartRateDetector.GetSignal(imageData, System.currentTimeMillis(), points2);
            Log.i("fengao_xiaomi", "handleMessage: " + seetaHeartRateDetector.GetHeartRate());
//            Log.i("fengao_xiaomi", "handleMessage: " + seetaHeartRateDetector.GetHeartRate());
//            if (mainFragment.needFaceRegister) {
//                String registeredName = "";
//                boolean canRegister = true;
//                float[] feats = new float[faceRecognizer.GetExtractFeatureSize()];
//
//                if (trackingInfo.faceInfo.width != 0) {
//                    //特征点检测
//                    SeetaPointF[] points = new SeetaPointF[5];
//                    faceLandmarker.mark(imageData, trackingInfo.faceInfo, points);
//
//                    //特征提取
//                    faceRecognizer.Extract(imageData, points, feats);
//
//                    if ("".equals(mainFragment.registeredName)) {
//                        canRegister = false;
//                        final String tip = "注册名称不能为空";
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mView.showSimpleTip(tip);
//                            }
//                        });
//                    }
//
//                    for (String key : trackingInfo.name2feats.keySet()) {
//                        if (key.equals(mainFragment.registeredName)) {
//                            canRegister = false;
//                            final String tip = mainFragment.registeredName + "已经注册";
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mView.showSimpleTip(tip);
//                                }
//                            });
//                        }
//                    }
//                }
//
//                //进行人脸的注册
//                if (canRegister) {
//                    trackingInfo.name2feats.put(mainFragment.registeredName, feats);
//                    final String tip = mainFragment.registeredName + "名称已经注册成功";
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mView.FaceRegister(tip);
//                        }
//                    });
//                }
//            }

            //进行人脸识别
//            if (trackingInfo.faceInfo.width != 0) {
//                //特征点检测
//                SeetaPointF[] points = new SeetaPointF[5];
//                faceLandmarker.mark(imageData, trackingInfo.faceInfo, points);
//
//                //特征提取
//                if (!trackingInfo.name2feats.isEmpty()) {//不空进行特征提取，并比对
//                    float[] feats = new float[faceRecognizer.GetExtractFeatureSize()];
//                    faceRecognizer.Extract(imageData, points, feats);
//
//                    int galleryNum = trackingInfo.name2feats.size();
//                    float maxSimilarity = 0.0f;
//
//                    for (String name : trackingInfo.name2feats.keySet()) {
//                        float sim = faceRecognizer.CalculateSimilarity(feats, trackingInfo.name2feats.get(name));
//                        if (sim > maxSimilarity && sim > thresh) {
//                            maxSimilarity = sim;
//                            targetName = name;
//                        }
//                    }
//                }
//            }

            final String pickedName = targetName;
            Log.e("recognized name:", pickedName);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mView.setName(pickedName, trackingInfo.matBgr, faceRect);
                }
            });
        }
    };

    @Override
    public void detect(byte[] data, int width, int height, int rotation) {
        TrackingInfo trackingInfo = new TrackingInfo();

        matNv21.put(0, 0, data);
        trackingInfo.matBgr = new Mat(AppConfig.CAMERA_PREVIEW_HEIGHT, AppConfig.CAMERA_PREVIEW_WIDTH, CvType.CV_8UC3);
        trackingInfo.matGray = new Mat();

        Imgproc.cvtColor(matNv21, trackingInfo.matBgr, Imgproc.COLOR_YUV2BGR_NV21);

        Core.transpose(trackingInfo.matBgr, trackingInfo.matBgr);
        Core.flip(trackingInfo.matBgr, trackingInfo.matBgr, 0);
        Core.flip(trackingInfo.matBgr, trackingInfo.matBgr, 1);

        Imgproc.cvtColor(trackingInfo.matBgr, trackingInfo.matGray, Imgproc.COLOR_BGR2GRAY);

        trackingInfo.birthTime = System.currentTimeMillis();
        trackingInfo.lastProccessTime = System.currentTimeMillis();

        mFaceTrackingHandler.removeMessages(1);
        mFaceTrackingHandler.obtainMessage(1, trackingInfo).sendToTarget();
    }

    public void saveImgage(Mat bgr, String path, String imageName) {
        Mat rgba = bgr.clone();
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_BGR2RGBA);

        Bitmap mBitmap = null;
        mBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba, mBitmap);

        File f = new File(path, imageName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        mFaceTrackThread.quitSafely();
        mFasThread.quitSafely();
    }
}
