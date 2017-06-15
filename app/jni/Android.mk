LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    :=libjpeg
LOCAL_SRC_FILES :=libjpeg.so
include $(PREBUILT_SHARED_LIBRARY)
include $(CLEAR_VARS)
LOCAL_MODULE    :=compressimg
LOCAL_SRC_FILES :=compress_image.cpp
LOCAL_SHARED_LIBRARIES :=libjpeg
LOCAL_LDLIBS := -ljnigraphics -llog  
include $(BUILD_SHARED_LIBRARY)