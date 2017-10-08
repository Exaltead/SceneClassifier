LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := mfcc-jni
LOCAL_SRC_FILES := mfcc-jni.c
LOCAL_LDLIBS := -llog
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../cpp
include $(BUILD_SHARED_LIBRARY)