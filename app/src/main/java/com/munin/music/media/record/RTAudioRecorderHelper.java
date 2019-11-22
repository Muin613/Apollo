package com.munin.music.media.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import com.munin.library.log.Logger;
import com.munin.library.thread.GlobalExecutor;
import com.munin.library.utils.IOUtils;
import com.flutter.mpeg.LameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author M
 */
public class RTAudioRecorderHelper {
    private static final String TAG = "RTAudioRecorderHelper";
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    private final static int AUDIO_SAMPLE_RATE = 44100;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSizeInBytes = 0;
    AudioRecord mAudioRecord;
    private String mFinalAudioPath;
    private boolean mIsRecord = true;
    private boolean mIsFinished = true;

    public RTAudioRecorderHelper(@NonNull String finalAudioPath) {
        this.mFinalAudioPath = finalAudioPath;
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        mAudioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, mBufferSizeInBytes);
        LameUtils.init(AUDIO_SAMPLE_RATE, 1, AUDIO_SAMPLE_RATE, 32);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            Logger.i(TAG, "running the recording!");
            writeData();
            Logger.i(TAG, "the recording is finished! handling file is now being processed");
            File file = new File(mFinalAudioPath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            mIsFinished = true;
        }
    };

    public void startRecord() {
        Logger.i(TAG, "startRecord");
        if (mIsFinished) {
            mIsFinished = false;
            mIsRecord = true;
            mAudioRecord.startRecording();
            GlobalExecutor.newInstance().execute(mTask);
        }
    }

    public void stopRecord() {
        if (mIsFinished) {
            return;
        }
        Logger.i(TAG, "stopRecord");
        mIsRecord = false;
        mAudioRecord.stop();
    }

    public void release() {
        mAudioRecord.release();
    }

    public void writeData() {
        short[] audioData = new short[mBufferSizeInBytes];
        FileOutputStream fos = null;
        int readSize = 0;
        try {
            File file = new File(mFinalAudioPath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            Logger.e(TAG, "writeData: " + e.getMessage());
        }
        byte[] mp3Buffer = new byte[(int) (7200 + (mBufferSizeInBytes * 2 * 1.25))];
        while (mIsRecord && fos != null) {
            Logger.e(TAG, "writeData: 开始");
            readSize = mAudioRecord.read(audioData, 0, mBufferSizeInBytes);
            if ( readSize>0) {
                int encodedSize = LameUtils.encode(audioData, audioData, readSize, mp3Buffer);
                Logger.e(TAG, "writeData: 转换完毕");
                if (encodedSize < 0) {
                    Logger.e(TAG, "writeData: 转换完毕1"+encodedSize);
                    continue;
                }
                try {
                    Logger.e(TAG, "writeData: 转换完毕2");
                    fos.write(mp3Buffer, 0, encodedSize);
                } catch (IOException e) {
                    Logger.e(TAG, "writeData: " + e.getMessage());
                }
            }
        }
        final int flushResult = LameUtils.flush(mp3Buffer);
        Logger.e(TAG, "writeData: 转换完毕3    "+flushResult);
        if (flushResult > 0) {
            try {
                fos.write(mp3Buffer, 0, flushResult);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        IOUtils.close(fos);
    }
}
