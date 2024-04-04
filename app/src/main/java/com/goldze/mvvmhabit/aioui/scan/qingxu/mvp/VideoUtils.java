package com.goldze.mvvmhabit.aioui.scan.qingxu.mvp;

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2024/3/8
 * Time: 5:39 PM
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.goldze.mvvmhabit.aioui.http.Api;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.UploadResponseBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.VideoSubmitBean;
import com.goldze.mvvmhabit.utils.RetrofitClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VideoUtils {
    static Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            startRotateVideo();
            return false;
        }
    });
    public static boolean rotateing = false;
    static String dirPath = "/sdcard/rlsb/";
    static Api api = RetrofitClient.getInstance().create(Api.class);

    public static void start() {
        handler.sendEmptyMessage(0);
    }

    public static void startRotateVideo() {
        if (rotateing) {
            return;
        } else {
            synchronized (VideoUtils.class){
                File file = new File(dirPath);
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.getName().startsWith("face_") && !f.getName().equals("face_90.mp4")) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                File file1 = new File("/sdcard/rlsb/face_90.mp4");
                                if (file1.exists()) {
                                    file1.delete();
                                }
                                String name = f.getName();
                                String data = name.replace("face_","");
                                data = data.replace(".mp4","");
                                rotateVideo(f.getAbsolutePath(), "/sdcard/rlsb/face_90.mp4", data, 270);
                            }
                        }.start();
                        return;
                    }
                }
            }
        }
    }

    public static void rotateVideo(String inputPath, String outputPath, String data, int rotation) {
        synchronized (VideoUtils.class) {
            rotateing = true;
            Log.i("fengao_xiaomi", "rotateVideo:  start " + data);
            File file = new File(outputPath);
            if (file.exists()) {
                file.delete();
            }
            String[] cmd;
            switch (rotation) {
                case 90:
                    cmd = new String[]{"-i", inputPath, "-vf", "transpose=1", "-c:a", "copy", outputPath};
                    break;
                case 180:
                    cmd = new String[]{"-i", inputPath, "-vf", "transpose=2,transpose=2", "-c:a", "copy", outputPath};
                    break;
                case 270:
                    cmd = new String[]{"-i", inputPath, "-vf", "transpose=2", "-c:a", "copy", outputPath};
                    break;
                default:
                    cmd = new String[]{"-i", inputPath, "-c", "copy", outputPath};
                    break;
            }
            Log.i("fengao_xiaomi", "rotateVideo: ");
            int execute = FFmpeg.execute(cmd);
            Log.i("fengao_xiaomi", "rotateVideo execute:  " + execute);
            uploadVideo("/sdcard/rlsb/face_90.mp4", data);
            Log.i("fengao_xiaomi", "rotateVideo:  end " + data);
            File file1 = new File(inputPath);
            file1.delete();
            rotateing = false;
            start();
        }
    }

    public static void uploadVideo(String filePath, String data) {
        Log.i("fengao_xiaomi", "uploadVideo: ");
        File file = new File(filePath);
        if (!file.exists() || file.length() < 1) {
            return;
        }
        Log.i("fengao_xiaomi", "uploadVideo: 开始上传");
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), body)
                .setType(MultipartBody.FORM)
                .build();
        api.updateFacePic(multipartBody.parts())
                .flatMap(uploadResponseBean -> {
                    Log.i("fengao_xiaomi", "uploadVideo: flatMap" + uploadResponseBean);
                    if (uploadResponseBean.success) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String faceVideoEndTime = sdf.format(date);
                        Date dateStart = new Date(date.getTime() - 1000 * 20);
                        String faceVideoStartTime = sdf.format(dateStart);
                        String url = uploadResponseBean.data;
                        String infoId = data;
                        VideoSubmitBean videoSubmitBean = new VideoSubmitBean(url, infoId, faceVideoStartTime, faceVideoEndTime);
                        return api.saveVideo(videoSubmitBean);
                    } else {
                        throw new RuntimeException("");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("fengao_xiaomi", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(UploadResponseBean bean) {
                        Log.i("fengao_xiaomi", "onNext: " + bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("fengao_xiaomi", "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

