//
// Created by reol on 11/30/20.
//
#include <jni.h>
#include <android/log.h>
#include <assert.h>
#include "include/opencv2/core/version.hpp"

#define LOG_TAG "ptilopsis"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT jstring JNICALL
Java_com_moqi_prts_ptilopsis_PtilopsisEyes_getOpenCVVersion(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(CV_VERSION);
}


