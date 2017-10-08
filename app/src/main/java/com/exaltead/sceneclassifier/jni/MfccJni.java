package com.exaltead.sceneclassifier.jni;

public class MfccJni {

    private MfccJniHolder mMfccInit;
    public native MfccJniHolder initMfccHolder();
    static {
        System.loadLibrary("mfcc-jni");
    }

    public void init(){
        mMfccInit = initMfccHolder();
    }
}
