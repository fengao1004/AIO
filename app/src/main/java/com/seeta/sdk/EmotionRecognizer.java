package com.seeta.sdk;

public class EmotionRecognizer {
    static{
        System.loadLibrary("SeetaEmotionRecognizer200_java");
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
    public EmotionRecognizer(SeetaModelSetting setting){
        this.construct(setting);
    }

    public native void dispose();
    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native int GetCropWidth();
    public native int GetCropHeight();
    public native int GetCropChannels();
    public native boolean CropFace(SeetaImageData imageData, SeetaPointF[] points, SeetaImageData face);

    public native int EmotionCount();

    public native boolean RecognizeEmotionWithCroppedFace(SeetaImageData face, float[] emotions);
    public native boolean RecognizeEmotion(SeetaImageData imageData, SeetaPointF[] points, float[] emotions);

    public native int RecognizeEmotion(SeetaImageData imageData, SeetaPointF[] points);

    public native void set(Property property, double value);
    public native double get(Property property);
}
