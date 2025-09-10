package com.tripbuddy.app.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

public class MusicPlayerUtil {
    private static final String TAG = "MusicPlayerUtil";
    private static MediaPlayer mediaPlayer;
    private static boolean isPaused = false;
    private static AudioManager audioManager;
    private static AudioManager.OnAudioFocusChangeListener focusChangeListener;
    private static AudioFocusRequest audioFocusRequest;

    public static void playMusic(Context context, int resourceId) {
        try {
            if (mediaPlayer != null) {
                stopMusic();
            }

            // Request audio focus
            if (!requestAudioFocus(context)) {
                Log.w(TAG, "Audio focus not granted; attempting playback anyway");
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f, 1.0f);

            try {
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(resourceId);
                if (afd == null) {
                    Log.e(TAG, "AssetFileDescriptor is null for resId: " + resourceId);
                    fallbackCreate(context, resourceId);
                    return;
                }
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
            } catch (Exception e) {
                Log.w(TAG, "openRawResourceFd failed, falling back to MediaPlayer.create", e);
                fallbackCreate(context, resourceId);
                return;
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        mp.start();
                        isPaused = false;
                        Log.d(TAG, "Music started playing");
                    } catch (Exception startEx) {
                        Log.e(TAG, "Failed to start MediaPlayer after prepare", startEx);
                    }
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "MediaPlayer error. what=" + what + ", extra=" + extra);
                    stopMusic();
                    return true;
                }
            });

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "Error playing music", e);
            stopMusic();
        }
    }

    private static boolean requestAudioFocus(Context context) {
        try {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) return false;

            focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            pauseMusic();
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            resumeMusic();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            stopMusic();
                            break;
                    }
                }
            };

            int result;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
                        .setOnAudioFocusChangeListener(focusChangeListener)
                        .build();
                result = audioManager.requestAudioFocus(audioFocusRequest);
            } else {
                result = audioManager.requestAudioFocus(
                        focusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
            }
            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } catch (Exception e) {
            Log.w(TAG, "requestAudioFocus failed", e);
            return false;
        }
    }

    private static void abandonAudioFocus() {
        try {
            if (audioManager == null || focusChangeListener == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            } else {
                audioManager.abandonAudioFocus(focusChangeListener);
            }
        } catch (Exception e) {
            Log.w(TAG, "abandonAudioFocus failed", e);
        } finally {
            audioFocusRequest = null;
            focusChangeListener = null;
            audioManager = null;
        }
    }

    private static void fallbackCreate(Context context, int resourceId) {
        try {
            if (mediaPlayer != null) {
                try { mediaPlayer.release(); } catch (Exception ignore) {}
            }
            mediaPlayer = MediaPlayer.create(context, resourceId);
            if (mediaPlayer == null) {
                Log.e(TAG, "MediaPlayer.create returned null for resId: " + resourceId);
                return;
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.start();
            isPaused = false;
            Log.d(TAG, "Music started via fallback create()");
        } catch (Exception e) {
            Log.e(TAG, "fallbackCreate failed", e);
            stopMusic();
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPaused = true;
                    Log.d(TAG, "Music paused");
                }
            } catch (Exception e) {
                Log.w(TAG, "pauseMusic failed", e);
            }
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null && isPaused) {
            try {
                mediaPlayer.start();
                isPaused = false;
                Log.d(TAG, "Music resumed");
            } catch (Exception e) {
                Log.w(TAG, "resumeMusic failed", e);
            }
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                Log.w(TAG, "stop called in wrong state", e);
            }
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.w(TAG, "release failed", e);
            }
            mediaPlayer = null;
            isPaused = false;
            Log.d(TAG, "Music stopped");
        }
        abandonAudioFocus();
    }

    public static boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPaused() {
        return isPaused;
    }
}