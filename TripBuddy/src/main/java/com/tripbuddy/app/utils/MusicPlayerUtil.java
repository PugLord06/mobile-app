package com.tripbuddy.app.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MusicPlayerUtil {
    private static final String TAG = "MusicPlayerUtil";
    private static MediaPlayer mediaPlayer;
    private static boolean isPaused = false;

    public static void playMusic(Context context, int resourceId) {
        try {
            if (mediaPlayer != null) {
                stopMusic();
            }
            mediaPlayer = MediaPlayer.create(context, resourceId);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            isPaused = false;
            Log.d(TAG, "Music started playing");
        } catch (Exception e) {
            Log.e(TAG, "Error playing music", e);
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
            Log.d(TAG, "Music paused");
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.start();
            isPaused = false;
            Log.d(TAG, "Music resumed");
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                isPaused = false;
                Log.d(TAG, "Music stopped");
            } catch (Exception e) {
                Log.e(TAG, "Error stopping music", e);
            }
        }
    }

    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static boolean isPaused() {
        return isPaused;
    }
}