#include <jni.h>

extern "C"
{
void Java_com_flutter_mpeg_LameUtils_close(JNIEnv *env, jclass type);

jint Java_com_flutter_mpeg_LameUtils_encode(JNIEnv *env, jclass type, jshortArray buffer_l_,
                                             jshortArray buffer_r_, jint samples, jbyteArray mp3buf_);

jint Java_com_flutter_mpeg_LameUtils_flush(JNIEnv *env, jclass type, jbyteArray mp3buf_);

void Java_com_flutter_mpeg_LameUtils_initialize(JNIEnv *env, jclass type, jint inSampleRate,
                                                   jint outChannel, jint outSampleRate, jint outBitrate, jint quality);
}