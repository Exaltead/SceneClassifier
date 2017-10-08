LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := mfcc-jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/../cpp/mfcc.h $(LOCAL_PATH)/../cpp/mfcc.cpp mfcc-jni.c
LOCAL_LDLIBS := -llog -Wall -Wextra -v
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../cpp
include $(BUILD_SHARED_LIBRARY)