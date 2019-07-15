package com.munin.music.media.sound;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import com.munin.library.utils.ContextUtils;
import com.munin.library.utils.IOUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author M
 */
public class NativeAudioTrackHelper {

    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    private final static int AUDIO_SAMPLE_RATE = 16000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSizeInBytes = 0;
    AudioTrack mAudioTrack;

    public NativeAudioTrackHelper() {
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            AudioFormat audioFormat = new AudioFormat.Builder()
                    .setChannelMask(AUDIO_CHANNEL)
                    .setEncoding(AUDIO_ENCODING)
                    .setSampleRate(AUDIO_SAMPLE_RATE)
                    .build();
            mAudioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(attributes)
                    .setAudioFormat(audioFormat)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build();
        } else {
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, mBufferSizeInBytes, AudioTrack.MODE_STREAM);
        }
    }

    private DataInputStream dataInputStream;

    public void play() {
        InputStream inputStream = null;
        try {
            inputStream = ContextUtils.getApplicationContext().getAssets().open("love.raw");
            dataInputStream = new DataInputStream(inputStream);
            int readCount = 0;
            byte[] tempBuffer = new byte[mBufferSizeInBytes];
            while (dataInputStream.available() > 0) {
                readCount = dataInputStream.read(tempBuffer);
                mAudioTrack.play();
                mAudioTrack.write(tempBuffer, 0, readCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(dataInputStream);
        }
    }
}
