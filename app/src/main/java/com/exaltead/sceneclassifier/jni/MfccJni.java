package com.exaltead.sceneclassifier.jni;

import android.util.Log;

public class MfccJni {

    private MfccJniHolder mMfccInit;
    public native MfccJniHolder initMfccHolder(int samplingRate);
    static {
        System.loadLibrary("mfcc-jni");
    }

    public void init(int samplingRate){
        if(mMfccInit == null){
            mMfccInit = initMfccHolder(samplingRate);
            Log.d("SceneJni", "MfccInitialized");
        }
    }
}
