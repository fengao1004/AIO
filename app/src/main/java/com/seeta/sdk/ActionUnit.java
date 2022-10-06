package com.seeta.sdk;

public class ActionUnit {
    static{
        System.loadLibrary("SeetaActionUnit600_java");
    }

    public long impl = 0;
    private native void construct(SeetaModelSetting seeting);
    public ActionUnit(SeetaModelSetting setting){
        this.construct(setting);
    }

    public native void dispose();
    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native int GetCropFaceWidth();
    public native int GetCropFaceHeight();
    public native int GetCropFaceChannels();
    public native boolean CropFace(SeetaImageData imageData, SeetaPointF[] points, SeetaImageData face);

    public native int GetExtractFeatureSize();

    public native boolean ExtractCroppedFace(SeetaImageData face, float[] features);
    public native boolean Extract(SeetaImageData image, SeetaPointF[] points, float[] features);
}
