package com.flutter.mpeg;

import java.nio.ByteBuffer;

public class X264Helper {
    static {
        System.loadLibrary("x264lib");
    }


    public interface H264listener
    {
        void convertH264data(byte[] buffer, int length);
    }

    private H264listener _listener;

    public X264Helper(H264listener l){
        _listener = l;
    }

    private ByteBuffer mVideobuffer;


    public void PushOriStream(byte[] buffer, int length, long time)
    {
        if (mVideobuffer == null || mVideobuffer.capacity() < length) {
            mVideobuffer = ByteBuffer.allocateDirect(((length / 1024) + 1) * 1024);
        }
        mVideobuffer.rewind();
        mVideobuffer.put(buffer, 0, length);
        encoderH264(length, time);
    }

    public native void initX264Encode(int width, int height, int fps, int bite);

    public native int encoderH264(int length, long time);

    public native void CloseX264Encode();

    private void H264DataCallBackFunc(byte[] buffer, int length){
        _listener.convertH264data(buffer, length);
    }
}
