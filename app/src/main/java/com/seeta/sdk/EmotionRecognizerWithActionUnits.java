package com.seeta.sdk;

public class EmotionRecognizerWithActionUnits {
    static{
        System.loadLibrary("SeetaEmotionRecognizerWithActionUnits600_java");
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
    public EmotionRecognizerWithActionUnits(SeetaModelSetting setting){
        this.construct(setting);
    }

    public native void dispose();
    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native int GetEmotionNum();
    public native int GetActionUnitsNum();

    public native boolean RecognizeEmotionAndActionUnitsWithCrop(SeetaImageData imageData, SeetaPointF[] points, float[] emotions, float[] actionUnits);

    public native void set(Property property, double value);
    public native double get(Property property);
}
