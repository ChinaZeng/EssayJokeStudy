LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    :=bspatch
LOCAL_SRC_FILES :=bspatch.c
LOCAL_LOLIBS := -ljnigraphics -llog
include $(BUILD_SHARED_LIBRARY)

