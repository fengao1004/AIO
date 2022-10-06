package com.seeta.sdk;

public class PoseEstimator {
    static{
        System.loadLibrary("PoseEstimation600_java");
    }

    public long impl = 0;
    private native void construct(String seetaModel);
    public PoseEstimator(String seetaModel){
        this.construct(seetaModel);
    }

    public native void dispose();
    protected void finalize()throws Throwable{
        super.finalize();
        this.dispose();
    }

    public native void Estimate(SeetaImageData image, SeetaRect face, float[] yaw, float[] pitch, float[] roll);
}
