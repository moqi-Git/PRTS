//
// Created by reol on 11/30/20.
//
#include <jni.h>
#include <android/log.h>
#include <assert.h>
#include <opencv2/opencv.hpp>

#define LOG_TAG "ptilopsis"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG, __VA_ARGS__)

static jstring get_opencv_version(JNIEnv *env, jclass thiz){
    using namespace cv;
    return env->NewStringUTF(CV_VERSION);
}
