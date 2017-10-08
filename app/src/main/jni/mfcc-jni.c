#include <jni.h>
#include <android/log.h>
#include <malloc.h>

#include "mfcc.h"

jobjectArray createJavaArrayFromPrimitive2dArray(JNIEnv *env, double** primitive2DArray, int rows, int columns);
jobject createMfccHolder(JNIEnv *env, double** mfcc2dArray, int rows, int columns);

JNIEXPORT jobject JNICALL
Java_com_exaltead_sceneclassifier_jni_MfccJni_initMfccHolder(JNIEnv *env, jobject instance) {

    int columsOrN = 500;
    int rowsORFreqBands = 10;
    int i, j;
    double **arr = (double **)malloc(rowsORFreqBands * sizeof(double *));
    for (i=0; i<rowsORFreqBands; i++)
        arr[i] = (double *)malloc(columsOrN * sizeof(double));

    //SET MOCK VALUE
    int count = 0;
    for (i = 0; i <  rowsORFreqBands; i++)
        for (j = 0; j < columsOrN; j++)
            arr[i][j] = ++count;

    return createMfccHolder(env, arr, rowsORFreqBands, columsOrN);

}

jobject createMfccHolder(JNIEnv *env, double** mfcc2dArray, int rows, int columns){
    jmethodID constructorString;
    jclass holderClass = (*env) -> FindClass(env, "com.exaltead.sceneclassifier.SceneJni/MfccJniHolder");
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
