package com.seeta.sdk;

public class FaceLandmarker {
    static{
        System.loadLibrary("SeetaFaceLandmarker600_java");
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
    private native void construct(String model, String device, int id);

    public FaceLandmarker(SeetaModelSetting setting){
        this.construct(setting);
    }

    public FaceLandmarker(String model, String device, int id)
    {
        this.construct(model, device, id);
    }

    public native void dispose();
    protected void finalize()throws Throwable{
        super.finalize();
        this.dispose();
    }

    public native int number();

    public native void mark(SeetaImageData imageData, SeetaRect seetaRect, SeetaPointF[] pointFS);

    public native void mark(SeetaImageData imageData, SeetaRect seetaRect, SeetaPointF[] pointFS, int[] masks);
	
	public native void set(Property property, double value);
    public native double get(Property property);
}
