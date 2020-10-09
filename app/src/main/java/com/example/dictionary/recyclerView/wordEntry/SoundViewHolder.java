package com.example.dictionary.recyclerView.wordEntry;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dictionary.R;
import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.databinding.RecyclerviewItemHomeSoundBinding;
import com.example.dictionary.utils.FileManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class SoundViewHolder extends AbstractViewHolder {

    private RecyclerviewItemHomeSoundBinding binding;

    private static final boolean OREO_OR_NEWER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = i -> {
        switch (i)
        {
            case AudioManager.AUDIOFOCUS_GAIN :
            {
                mediaPlayer.start();
                break;
            }

            case AudioManager.AUDIOFOCUS_LOSS :
            {
                mediaPlayer.pause();
                releaseAudioFocus();
                releaseMediaPlayer();
                break;
            }

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT : case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
            {
                mediaPlayer.pause();
                break;
            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = mediaPlayer -> {
        releaseAudioFocus();
        releaseMediaPlayer();
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = mediaPlayer -> {
        play();
    };

    public SoundViewHolder(@NonNull RecyclerviewItemHomeSoundBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


    @Override
    public void bindView(WordEntry wordEntry) {
        binding.textViewDictionaryName.setText(wordEntry.getDictionaryName());

        binding.buttonPlay.setOnClickListener(view -> {
            releaseAudioFocus();
            releaseMediaPlayer();
            startMediaPlayer(wordEntry.getSoundName());

        });

    }

    public void play()
    {
        int focusRequest;
        if(OREO_OR_NEWER)
        {
            focusRequest = audioManager.requestAudioFocus(audioFocusRequest);
        }
        else
        {
            focusRequest = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        switch (focusRequest)
        {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED: {
                Snackbar.make(binding.getRoot(), R.string.audio_focus_request_failed, Snackbar.LENGTH_SHORT).show();
            }

            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED: {
                mediaPlayer.start();
            }
        }
    }


    public void startMediaPlayer(String soundName)
    {
        Context context = binding.buttonPlay.getContext();
        FileManager fileManager = new FileManager(context);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if(OREO_OR_NEWER)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                    .build();
        }

        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileManager.getFilePath(soundName));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnPreparedListener(onPreparedListener);
        }
        catch (IOException ioexception)
        {
            Snackbar.make(binding.getRoot(), R.string.audio_preparing_error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, view1 -> startMediaPlayer(soundName)).show();
        }
    }

    public void releaseAudioFocus()
    {
        if(audioManager != null)
        {
            if(audioFocusRequest != null)
            {
                if(OREO_OR_NEWER)
                {
                    audioManager.abandonAudioFocusRequest(audioFocusRequest);
                }
                else
                {
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
            }
        }

        audioManager = null;
    }

    public void releaseMediaPlayer()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
        }

        mediaPlayer = null;
    }

}
