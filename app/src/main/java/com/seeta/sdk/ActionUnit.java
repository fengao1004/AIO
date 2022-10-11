package com.seeta.sdk;

public class ActionUnit {
    static{
        System.loadLibrary("SeetaActionUnit600_java");
    }

    public enum Property {

        PROPERTY_NUMBER_THREADS(4),
        PROPERTY_ARM_CPU_MODE(5);

        private int value;
        private Property(int value) {
            this.value = value;
        }

        public int getValue(){
            return value;
        }
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

    public native void set(Property property, double value);
    public native double get(Property property);
}
