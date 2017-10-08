package com.exaltead.sceneclassifier.jni;

class MfccJniHolder {

    private double[][] mFfts;
    private int mNumberOfFilters;

    public MfccJniHolder(double[][] ffts, int numberOfFilters){
        mFfts = ffts;
        mNumberOfFilters = numberOfFilters;
    }

    public double[][] getFfts(){
        return mFfts;
    }

    public int getNumberOfFilters(){
        return mNumberOfFilters;
    }
}
