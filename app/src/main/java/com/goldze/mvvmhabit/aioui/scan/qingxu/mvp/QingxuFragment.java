package com.goldze.mvvmhabit.aioui.scan.qingxu.mvp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.aioui.http.HttpRepository;
import com.goldze.mvvmhabit.aioui.scan.qingxu.adapter.ActionAdapter;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.ActionBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.SubmitResponseBean;
import com.goldze.mvvmhabit.aioui.scan.qingxu.camera.CameraCallbacks;
import com.goldze.mvvmhabit.aioui.scan.qingxu.camera.CameraPreview2;
import com.goldze.mvvmhabit.aioui.scan.qingxu.view.FaceView;
import com.goldze.mvvmhabit.aioui.webview.WebViewFromUrlActivityA;
import com.seeta.sdk.SeetaImageData;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.utils.ToastUtils;


@SuppressWarnings("deprecation")
public class QingxuFragment extends Fragment
        implements VerificationContract.View {

    public static final String TAG = "MainFragment";

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    CameraPreview2 mCameraPreview;
    RecyclerView recyclerView;
    TextView tv_blink;
    TextView tv_qx;
    TextView tv_timer;
    TextView tv_heart;
    TextView tv_ziran_num;
    TextView tv_fennu_num;
    TextView tv_yanwu_num;
    TextView tv_kongju_num;
    TextView tv_gaoxing_num;
    TextView tv_shangxin_num;
    TextView tv_jingya_num;
    ImageView iv_ziran;
    ImageView iv_fennu;
    ImageView iv_yanwu;
    ImageView iv_kongju;
    ImageView iv_gaoxing;
    ImageView iv_shangxin;
    ImageView iv_jingya;
    protected SurfaceView mOverlap;
    String qingxu = "";

    // Show compared score and start tip. Add by linhx 20170428 end
    private VerificationContract.Presenter mPresenter;
    private AlertDialog mCameraUnavailableDialog;
    private Camera.Size mPreviewSize;

    private Paint mFaceRectPaint = null;
    private Paint mFaceNamePaint = null;
    private List<ActionBean> actionList = new ArrayList<>();

    public boolean needFaceRegister = false;//是否需要注册人脸
    public String registeredName = "";//注册的名称

    public String recognizedName = "";

    public SeetaImageData imageData = new SeetaImageData(600, 800, 3);

    private CameraCallbacks mCameraCallbacks = new CameraCallbacks() {
        @Override
        public void onCameraUnavailable(int errorCode) {
            Log.e(TAG, "camera unavailable, reason=%d" + errorCode);
            showCameraUnavailableDialog(errorCode);
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mPreviewSize == null) {
                mPreviewSize = camera.getParameters().getPreviewSize();
            }
            mPresenter.detect(data, mPreviewSize.width, mPreviewSize.height,
                    mCameraPreview.getCameraRotation());
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        mFaceRectPaint = new Paint();
        mFaceRectPaint.setColor(Color.argb(255, 255, 0, 0));
        mFaceRectPaint.setStrokeWidth(5);
        mFaceRectPaint.setStyle(Paint.Style.STROKE);

        mFaceNamePaint = new Paint();
        mFaceNamePaint.setColor(Color.argb(255, 255, 0, 0));
        mFaceNamePaint.setTextSize(50);
        mFaceNamePaint.setStyle(Paint.Style.FILL);

    }


    private SurfaceHolder mOverlapHolder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_qingxu, container, false);
        initActionList();
        mCameraPreview = view.findViewById(R.id.camera_preview);
        recyclerView = view.findViewById(R.id.rv_action);
        tv_blink = view.findViewById(R.id.tv_blink);
        tv_qx = view.findViewById(R.id.tv_qx);
        tv_timer = view.findViewById(R.id.tv_timer);
        tv_heart = view.findViewById(R.id.tv_heart);
        tv_ziran_num = view.findViewById(R.id.tv_ziran_num);
        tv_fennu_num = view.findViewById(R.id.tv_fennu_num);
        tv_yanwu_num = view.findViewById(R.id.tv_yanwu_num);
        mOverlap = view.findViewById(R.id.surfaceViewOverlap);
        tv_kongju_num = view.findViewById(R.id.tv_kongju_num);
        tv_gaoxing_num = view.findViewById(R.id.tv_gaoxing_num);
        tv_shangxin_num = view.findViewById(R.id.tv_shangxin_num);
        tv_jingya_num = view.findViewById(R.id.tv_jingya_num);
        iv_ziran = view.findViewById(R.id.iv_ziran);
        iv_fennu = view.findViewById(R.id.iv_fennu);
        iv_yanwu = view.findViewById(R.id.iv_yanwu);
        iv_kongju = view.findViewById(R.id.iv_kongju);
        iv_gaoxing = view.findViewById(R.id.iv_gaoxing);
        iv_shangxin = view.findViewById(R.id.iv_shangxin);
        iv_jingya = view.findViewById(R.id.iv_jingya);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ActionAdapter(actionList));
        startCountDown();
        initAnim();
        mOverlap.setZOrderOnTop(true);
        mOverlap.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mOverlapHolder = mOverlap.getHolder();

        return view;
    }


    Disposable timerDisposable;

    private void startCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        //aLong从0开始
                        return 20 - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        timerDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        tv_timer.setText(value + "s");
                        Log.d("Timer", "" + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mPresenter.submit();
                    }
                });
    }


    private void initActionList() {
        actionList.add(new ActionBean(0, "内眉上扬"));
        actionList.add(new ActionBean(0, "外眉上扬"));
        actionList.add(new ActionBean(0, "眉毛下压"));
        actionList.add(new ActionBean(0, "上眼睑上扬"));
        actionList.add(new ActionBean(0, "脸颊抬起"));
        actionList.add(new ActionBean(0, "眼睑收紧"));
        actionList.add(new ActionBean(0, "鼻子蹙皱"));
        actionList.add(new ActionBean(0, "上唇抬起"));
        actionList.add(new ActionBean(0, "嘴角上扬"));
        actionList.add(new ActionBean(0, "收缩嘴角"));
        actionList.add(new ActionBean(0, "嘴角下拉"));
        actionList.add(new ActionBean(0, "下巴缩紧"));
        actionList.add(new ActionBean(0, "噘嘴"));
        actionList.add(new ActionBean(0, "嘴唇舒展"));
        actionList.add(new ActionBean(0, "嘴唇收缩"));
        actionList.add(new ActionBean(0, "嘴唇压紧"));
        actionList.add(new ActionBean(0, "上下唇分开"));
        actionList.add(new ActionBean(0, "下颚下拉"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mCameraPreview.setCameraCallbacks(mCameraCallbacks);
    }


    @WorkerThread
    @Override
    public void drawFaceRect(org.opencv.core.Rect faceRect) {
//        if (!isActive()) {
//            return;
//        }
//        Canvas canvas = mOverlapHolder.lockCanvas();
//        if (canvas == null) {
//            return;
//        }
//        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
//
//        if (faceRect != null) {
//            faceRect.x *= mPreviewScaleX;
//            faceRect.y *= mPreviewScaleY;
//            faceRect.width *= mPreviewScaleX;
//            faceRect.height *= mPreviewScaleY;
//
//            focusRect.left = faceRect.x;
//            focusRect.right = faceRect.x + faceRect.width;
//            focusRect.top = faceRect.y;
//            focusRect.bottom = faceRect.y + faceRect.height;
//
//            canvas.drawRect(focusRect, mFaceRectPaint);
//        }
//
//
//        mOverlapHolder.unlockCanvasAndPost(canvas);
    }

    @WorkerThread
    @Override
    public void drawFaceImage(Bitmap faceBmp) {

    }

    @Override
    public void toastMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {

        }
    }

    @Override
    public void showCameraUnavailableDialog(int errorCode) {
        if (mCameraUnavailableDialog == null) {
            mCameraUnavailableDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("摄像头不可用")
                    .setMessage(getContext().getString(R.string.please_restart_device_or_app, errorCode))
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().recreate();
                                }
                            });
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().finish();
                                }
                            });
                        }
                    })
                    .create();
        }
        if (!mCameraUnavailableDialog.isShowing()) {
            mCameraUnavailableDialog.show();
        }
    }

    @Override
    public void setStatus(int status, Mat matBgr, org.opencv.core.Rect faceRect) {
        Log.i(TAG, "setStatus " + status);

    }

    @Override
    public void setName(String name, Mat matBgr, org.opencv.core.Rect faceRect) {

    }

    @Override
    public void showSimpleTip(String tip) {
        needFaceRegister = false;
        registeredName = "";
        Toast.makeText(getContext(), tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void FaceRegister(String tip) {
    }

    @Override
    public void setBestImage(Bitmap bitmap) {

    }

    @Override
    public void setPresenter(VerificationContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setContent(String content) {

    }

    RectF faceRect = new RectF();

    @Override
    public void updateFaceUi(int x, int y, int width, int height) {
        Log.i("fengao_xiaomi", "updateFaceUi: ");
        if (!isActive()) {
            return;
        }
        Canvas canvas = mOverlapHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (faceRect != null) {
            faceRect.left = 1080 - ((float) (x * 1.8f));
            faceRect.right = 1080 - ((x + width) * 1.8f);
            faceRect.top = (1440 - (float) (y * 1.8f)) - 180;
            faceRect.bottom = (1440 - ((y + height) * 1.8f)) - 180;
            canvas.drawRect(faceRect, mFaceRectPaint);
        }
        if (faceRect != null && (faceRect.bottom < 0 || faceRect.right < 0 || faceRect.left > 1080 || faceRect.top > 560)) {
            canvas.drawText("请调整与摄像头的距离", 300, 300, mFaceNamePaint);
        } else {
            int maxIndex = 0;
            for (int i = 0; i < targetf1.length; i++) {
                if (targetf1[i] > targetf1[maxIndex]) {
                    maxIndex = i;
                }
            }
            String text = "";
            switch (maxIndex) {
                case 0: {
                    text = "自然";
                    break;
                }
                case 1: {
                    text = "愤怒";
                    break;
                }
                case 2: {
                    text = "厌恶";
                    break;
                }
                case 3: {
                    text = "恐惧";
                    break;
                }
                case 4: {
                    text = "高兴";
                    break;
                }
                case 5: {
                    text = "伤心";
                    break;
                }
                case 6: {
                    text = "惊讶";
                    break;
                }
            }
            text = text + Float.valueOf(tempf1[maxIndex] * 100).intValue();
            canvas.drawText(text, (faceRect.left + faceRect.right) / 2 - 125, faceRect.top + 50, mFaceNamePaint);
        }
        mOverlapHolder.unlockCanvasAndPost(canvas);
    }

    int blink = 0;
    double heart = -1.0f;
    ValueAnimator animation = ValueAnimator.ofFloat(0.0f, 1.0f);


    @Override
    public void updateUi(float[] action, float[] emotion, int blinkNum, double heartNum) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                updateUiOnMain(action, emotion, blinkNum, heartNum);

            }
        });

    }

    float[] originalf1 = new float[7];
    float[] originalf2 = new float[19];
    float[] targetf1 = new float[7];
    float[] targetf2 = new float[19];
    float[] tempf1 = new float[7];
    float[] tempf2 = new float[19];

    void updateUiOnAnim(float[] action, float[] emotion) {
        for (int i = 0; i < action.length; i++) {
            if (i < 10) {
                int progress = Float.valueOf((action[i] * 100.0f)).intValue();
                actionList.get(i).progress = progress;
            } else if (i > 10) {
                int progress = Float.valueOf((action[i - 1] * 100.0f)).intValue();
                actionList.get(i - 1).progress = progress;
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        int ziran = Float.valueOf((emotion[0] * 100.0f)).intValue();
        int fennu = Float.valueOf((emotion[1] * 100.0f)).intValue();
        int yanwu = Float.valueOf((emotion[2] * 100.0f)).intValue();
        int kongju = Float.valueOf((emotion[3] * 100.0f)).intValue();
        int gaoxing = Float.valueOf((emotion[4] * 100.0f)).intValue();
        int shangxin = Float.valueOf((emotion[5] * 100.0f)).intValue();
        int jingya = Float.valueOf((emotion[6] * 100.0f)).intValue();

        LinearLayout.LayoutParams lp_ziran = ((LinearLayout.LayoutParams) tv_ziran_num.getLayoutParams());
        lp_ziran.weight = 30 + (100 - ziran);
        tv_ziran_num.setLayoutParams(lp_ziran);

        LinearLayout.LayoutParams lp_iv_ziran = ((LinearLayout.LayoutParams) iv_ziran.getLayoutParams());
        lp_iv_ziran.weight = ziran;
        iv_ziran.setLayoutParams(lp_iv_ziran);

        ((LinearLayout.LayoutParams) tv_fennu_num.getLayoutParams()).weight = 30 + (100 - fennu);
        ((LinearLayout.LayoutParams) iv_fennu.getLayoutParams()).weight = fennu;

        ((LinearLayout.LayoutParams) tv_yanwu_num.getLayoutParams()).weight = 30 + (100 - yanwu);
        ((LinearLayout.LayoutParams) iv_yanwu.getLayoutParams()).weight = yanwu;

        ((LinearLayout.LayoutParams) tv_kongju_num.getLayoutParams()).weight = 30 + (100 - kongju);
        ((LinearLayout.LayoutParams) iv_kongju.getLayoutParams()).weight = kongju;

        ((LinearLayout.LayoutParams) tv_gaoxing_num.getLayoutParams()).weight = 30 + (100 - gaoxing);
        ((LinearLayout.LayoutParams) iv_gaoxing.getLayoutParams()).weight = gaoxing;

        ((LinearLayout.LayoutParams) tv_shangxin_num.getLayoutParams()).weight = 30 + (100 - shangxin);
        ((LinearLayout.LayoutParams) iv_shangxin.getLayoutParams()).weight = shangxin;

        ((LinearLayout.LayoutParams) tv_jingya_num.getLayoutParams()).weight = 30 + (100 - jingya);
        ((LinearLayout.LayoutParams) iv_jingya.getLayoutParams()).weight = jingya;
        tv_ziran_num.setText(ziran + "");
        tv_fennu_num.setText(fennu + "");
        tv_yanwu_num.setText(yanwu + "");
        tv_kongju_num.setText(kongju + "");
        tv_gaoxing_num.setText(gaoxing + "");
        tv_shangxin_num.setText(shangxin + "");
        tv_jingya_num.setText(jingya + "");
    }

    private void initAnim() {
        animation.setDuration(2000)
                .addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    for (int i = 0; i < tempf1.length; i++) {
                        float o = originalf1[i];
                        float t = targetf1[i];
                        float temp = o + ((t - o) * value);
                        tempf1[i] = temp;
                    }

                    for (int i = 0; i < tempf2.length; i++) {
                        float o = originalf2[i];
                        float t = targetf2[i];
                        float temp = o + ((t - o) * value);
                        tempf2[i] = temp;
                    }
                    updateUiOnAnim(tempf2, tempf1);
                });
    }

    public void updateUiOnMain(float[] action, float[] emotion, int blinkNum, double heartNum) {

        blink += blinkNum;
        if (blink == 0) {
            tv_blink.setText("检测中");
        } else {
            tv_blink.setText(blink + "次");
        }
        if (heartNum > 0.0f) {
            heart = heartNum;
        }
        if (heart == -1.0f) {
            tv_heart.setText("检测中");
        } else {
            tv_heart.setText((heartNum + "0000000").substring(0, 4));
        }
        int ziran = Float.valueOf((emotion[0] * 100.0f)).intValue();
        int fennu = Float.valueOf((emotion[1] * 100.0f)).intValue();
        int yanwu = Float.valueOf((emotion[2] * 100.0f)).intValue();
        int kongju = Float.valueOf((emotion[3] * 100.0f)).intValue();
        int gaoxing = Float.valueOf((emotion[4] * 100.0f)).intValue();
        int shangxin = Float.valueOf((emotion[5] * 100.0f)).intValue();
        int jingya = Float.valueOf((emotion[6] * 100.0f)).intValue();
        int zhengxiang = gaoxing;
        int zhongxing = ziran + jingya;
        int fumian = shangxin + fennu + yanwu + kongju;
        if (zhongxing > fumian && zhongxing > zhengxiang) {
            tv_qx.setText("中性");
        }
        if (fumian > zhongxing && fumian > zhengxiang) {
            tv_qx.setText("负面");
        }
        if (zhengxiang > fumian && zhengxiang > zhongxing) {
            tv_qx.setText("正面");
        }
        originalf1 = targetf1;
        originalf2 = targetf2;

        targetf1 = emotion;
        targetf2 = action;
        animation.cancel();
        animation.start();
    }

    @Override
    public boolean isActive() {
        return getView() != null && isAdded() && !isDetached();
    }

    @Override
    public void jump(SubmitResponseBean bean) {
        Intent intent = new Intent(this.getActivity(), WebViewFromUrlActivityA.class);
        String url = HttpRepository.Companion.getQXH5base() + "/emotion/report?reportId=" + bean.data;
//        String url = HttpRepository.Companion.getQXH5base() + "/emotion/report?reportId=" + 1578392827360108546L;


        intent.putExtra("url", url);
        intent.putExtra("title", "情绪分析");
        intent.putExtra("id", bean.data);
//        intent.putExtra("id", 1578392827360108546L);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void exit() {
        getActivity().finish();
    }

    @Override
    public CameraPreview2 getTextureView() {
        return mCameraPreview;
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        super.onDestroyView();
        if (timerDisposable != null) {
            timerDisposable.dispose();
        }
        animation.cancel();
    }

    @SuppressWarnings("unused")
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            getActivity().recreate();
        }
    }

}
