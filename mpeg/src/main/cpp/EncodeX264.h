#include <jni.h>

#ifndef NELEM
# define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif

#define YUVBUFFER_IN_JAVA_OBJ_NAME   "mVideobuffer"

static const char* const kClassPathName = "com/flutter/mpeg/X264Helper";

extern "C"
{
static void Java_com_flutter_mpeg_X264Helper_initX264Encode(JNIEnv *env, jobject jobj,jint width, jint height, jint fps, jint bite);

static void Java_com_flutter_mpeg_X264Helper_encoderH264(JNIEnv *env, jobject jobj,jint lenght, jlong time);

static void Java_com_flutter_mpeg_X264Helper_releaseX264Encode(JNIEnv *env, jobject jobj);
}