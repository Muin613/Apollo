package com.munin.music.media.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import com.munin.library.log.Logger;
import com.munin.library.thread.GlobalExecutor;
import com.munin.library.utils.IOUtils;
import com.munin.music.media.utils.AudioUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author M
 * PCM裸音数据转wav的音频文件
 */
public class NativeAudioRecorderHelper {
    private static final String TAG = "NativeAudioRecorderHelper";
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    private final static int AUDIO_SAMPLE_RATE = 16000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSizeInBytes = 0;
    AudioRecord mAudioRecord;
    private String mOriginalAudioPath;
    private String mFinalAudioPath;
    private boolean mIsRecord = true;
    private boolean mIsFinished = true;

    public NativeAudioRecorderHelper(@NonNull String originalAudioPath, @NonNull String finalAudioPath) {
        this.mOriginalAudioPath = originalAudioPath;
        this.mFinalAudioPath = finalAudioPath;
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
            copyWaveFile(mOriginalAudioPath, mFinalAudioPath);
            mIsFinished = true;
        }
    };

    public NativeAudioRecorderHelper() {
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        mAudioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, mBufferSizeInBytes);
    }

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
        byte[] audioData = new byte[mBufferSizeInBytes];
        FileOutputStream fos = null;
        int readSize = 0;
        try {
            File file = new File(mOriginalAudioPath);
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
        while (mIsRecord && fos != null) {
            readSize = mAudioRecord.read(audioData, 0, mBufferSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                try {
                    fos.write(audioData);
                } catch (IOException e) {
                    Logger.e(TAG, "writeData: " + e.getMessage());
                }
            }
        }
        IOUtils.close(fos);
    }


    public void copyWaveFile(String originalFile, String finalFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = 36;
        long longSampleRate = AUDIO_SAMPLE_RATE;
        int channels = 1;
        long byteRate = 16 * AUDIO_SAMPLE_RATE * channels / 8;
        byte[] data = new byte[mBufferSizeInBytes];
        try {
            in = new FileInputStream(originalFile);
            out = new FileOutputStream(finalFile);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            AudioUtils.writeWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
        } catch (Exception e) {
            Logger.e(TAG, "copyWaveFile: " + e.getMessage());
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }
    }
}
