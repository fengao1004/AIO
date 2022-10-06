package com.seeta.sdk;

public class SeetaHeartRateDetector {
    static {
        System.loadLibrary("SeetaHeartRateDetector600_java");
    }

    public long impl = 0;// native object pointer
    public native void construct();
    public SeetaHeartRateDetector() { this.construct(); }

    public native void dispose();
    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }

    public native void SetFrameNum(int num);

//    public native int GetSignal(SeetaImageData imageData, SeetaPointF[] points81);
//  set time in ms
    public native int GetSignal(SeetaImageData imageData, double time, SeetaPointF[] points81);

    public native boolean IsWaiting();

    public native double GetWaitingTime();

    public native double GetHeartRate();

    public native void Reset();

    public native void freeze_fps(double fps);

    public native void thaw_fps();

    public native double get_fps();
}
