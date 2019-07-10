package com.munin.music.media.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;

import com.munin.library.log.Logger;


/**
 * @author M
 */
public class SoundHelper {
    private static final String TAG = "SoundHelper";
    private SoundPool mSoundPool;
    private SparseIntArray mSoundArray = new SparseIntArray();
    private Context mContext;

    public SoundHelper(@NonNull Context context, int maxStream) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStream)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(maxStream, AudioManager.STREAM_MUSIC, 0);
        }
    }

    public void loadSound(@NonNull Context context, int resId) {
        int soundId = mSoundPool.load(context, resId, 1);
        if (soundId != 0) {
            mSoundArray.put(resId, soundId);
        }
    }

    public void playSound(int resId, int repeatNum) {
        int soundId = mSoundArray.get(resId);
        if (soundId == 0) {
            Logger.w(TAG, "playSound: soundId is 0!");
            return;
        }
        AudioManager am = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeRatio = volumeCurrent / audioMaxVolume;
        mSoundPool.play(soundId, volumeRatio, volumeRatio, 1, repeatNum, 1);
    }

}
