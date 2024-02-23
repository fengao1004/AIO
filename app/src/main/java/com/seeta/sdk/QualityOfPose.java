package com.seeta.sdk;

public class QualityOfPose {
    static {
        System.loadLibrary("QualityAssessor300_java");
    }

    public enum QualityLevel
    {
        LOW ,//Quality level is low
        MEDIUM,//Quality level is medium
        HIGH,//Quality level is high
    }

    public long impl = 0;

    private native  void construct();
    public QualityOfPose(){
        this.construct();
    }

    public native void dispose();
    protected void finalize()throws Throwable{
        super.finalize();
        this.dispose();
    }

    /**
     *
     * @param imageData [input]image data
     * @param face [input] face location
     * @param landmarks [input] face landmarks
     * @param score [output] quality score
     * @return QualityLevel quality level sorted into "LOW" , "Medium" , "HIGH"
     */
    private native int checkCore(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score);
    public QualityLevel check(SeetaImageData imageData, SeetaRect face, SeetaPointF[] landmarks, float[] score){
        int index = this.checkCore(imageData, face, landmarks, score);

        QualityLevel level = QualityLevel.values()[index];
        return level;
    }
}