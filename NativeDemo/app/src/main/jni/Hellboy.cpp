//
// Created by tlvan on 1/31/2018.
//
#include "hb_nativedemo_MyNDk.h"

int fib(int n);

JNIEXPORT jstring JNICALL Java_hb_nativedemo_MyNDk_getMyString
        (JNIEnv *env, jobject) {
    return (*env).NewStringUTF("Hellboy here");
}

JNIEXPORT jint JNICALL Java_hb_nativedemo_MyNDk_fibonacy
        (JNIEnv *env, jobject, jint v) {
    return fib(v);
}

JNIEXPORT jint JNICALL Java_hb_nativedemo_MyNDk_add

        (JNIEnv *env, jobject, jint a, jint b) {
    return a + b;
}

int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    return (fib(n - 1) + fib(n - 2));
}