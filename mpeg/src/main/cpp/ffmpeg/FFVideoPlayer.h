#include <jni.h>

extern "C"
{
 void Java_com_flutter_mpeg_SimpleVideoView_playVideo(JNIEnv *env,jobject instance,jstring path_ ,jobject surface);
}