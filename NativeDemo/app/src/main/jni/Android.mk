LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Hellboy
LOCAL_SRC_FILES := Hellboy.cpp

include $(BUILD_SHARED_LIBRARY)