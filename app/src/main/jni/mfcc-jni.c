#include <jni.h>
#include <android/log.h>
#include <malloc.h>

#include "mfcc.h"


jobjectArray createJavaArrayFromPrimitive2dArray(JNIEnv *env, double** primitive2DArray, int rows, int columns);
jobject createMfccHolder(JNIEnv *env, mel_filter melFilter, int rows, int columns);
double** allocate2dDoubleArray(int rows, int columns);

const int numberOfFilters = 10;
const int samplesPerFilter = 500;
const int minFrequency = 50;
const int maxFrequency = 9000;

JNIEXPORT jobject JNICALL
Java_com_exaltead_sceneclassifier_jni_MfccJni_initMfccHolder(JNIEnv *env, jobject instance, jint samplingRate) {

    const int columsOrN = 500;
    const int rowsORFreqBands = 10;
    mel_filter melFilter;
    melFilter.n_filters = rowsORFreqBands;
    //Initialize the object
    melFilter.filters = allocate2dDoubleArray(rowsORFreqBands, columsOrN);
    if(xtract_init_mfcc(columsOrN, samplingRate/2,XTRACT_EQUAL_GAIN,
                        50, 9000, melFilter.n_filters, melFilter.filters)
            != XTRACT_SUCCESS){
        __android_log_write(ANDROID_LOG_ERROR, "SceneJNI", "Failed to initialize ftt tables");
    }
    return createMfccHolder(env, melFilter, rowsORFreqBands, columsOrN);

}

jobject createMfccHolder(JNIEnv *env, mel_filter melFilter, int rows, int columns){
    jmethodID constructorString;
    double** mfcc2dArray;
    jclass holderClass = (*env) -> FindClass(env, "com/exaltead/sceneclassifier/jni/MfccJniHolder");
    mfcc2dArray = melFilter.filters;
    if(holderClass == NULL){
        __android_log_write(ANDROID_LOG_ERROR, "SceneJNI", "Could not find holder class");
    }
    constructorString = (*env) -> GetMethodID(env, holderClass, "<init>", "([[DI)V");
    if(constructorString == NULL){
        __android_log_write(ANDROID_LOG_ERROR, "SceneJNI", "Could not load method");
    }
    jobjectArray mfccJavaArray = createJavaArrayFromPrimitive2dArray(env, mfcc2dArray, rows, columns);
    return (*env) -> NewObject(env, holderClass, constructorString,mfccJavaArray, rows);
}

jobjectArray createJavaArrayFromPrimitive2dArray(JNIEnv *env, double** primitive2DArray, int rows, int columns){
    jclass  doubleArrayClass = (*env)->FindClass(env, "[D");
    if(doubleArrayClass == NULL){
        __android_log_write(ANDROID_LOG_ERROR, "SceneJNI", "Could not find double array class");
    }
    jobjectArray result = (*env) -> NewObjectArray(env, (jsize)rows, doubleArrayClass, NULL);

    for (int i = 0; i < rows; ++i) {
        jdoubleArray doubleArray = (*env) -> NewDoubleArray(env, columns);
        (*env)->SetDoubleArrayRegion(env, doubleArray, (jsize) 0, (jsize) columns, primitive2DArray[i]);
        (*env)->SetObjectArrayElement(env, result, (jsize) i, doubleArray);
        (*env)->DeleteLocalRef(env, doubleArray);
    }
    return result;
}

double** allocate2dDoubleArray(int rows, int columns){
    int i;
    double **arr = (double **)malloc(rows * sizeof(double *));
    for (i=0; i<rows; i++)
        arr[i] = (double *)malloc(columns * sizeof(double));
    return arr;
}

