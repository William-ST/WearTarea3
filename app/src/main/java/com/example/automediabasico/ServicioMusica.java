package com.example.automediabasico;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicioMusica extends MediaBrowserServiceCompat {

    private final String TAG = ServicioMusica.class.getCanonicalName();

    private MediaSessionCompat mSession;
    private List<MediaMetadataCompat> mMusic;
    private MediaPlayer mPlayer;
    private MediaMetadataCompat mCurrentTrack;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusic = new ArrayList<MediaMetadataCompat>();
        //Añadimos 3 canciones desde la librería de audio de youtube
        mMusic.add(new MediaMetadataCompat.Builder().putString(MediaMetadata.METADATA_KEY_MEDIA_ID,
                "https://www.youtube.com/audiolibrary_download?vid=f5cfb6bd8c048b98")
                .putString(MediaMetadata.METADATA_KEY_TITLE, "Primera canción")
                .putString(MediaMetadata.METADATA_KEY_ARTIST, "Artista 1")
                .putLong(MediaMetadata.METADATA_KEY_DURATION, 109000).build());
        mMusic.add(new MediaMetadataCompat.Builder().putString(MediaMetadata.METADATA_KEY_MEDIA_ID,
                "https://www.youtube.com/audiolibrary_download?vid=ac7a38f4a568229c")
                .putString(MediaMetadata.METADATA_KEY_TITLE, "Segunda canción")
                .putString(MediaMetadata.METADATA_KEY_ARTIST, "Artista 2")
                .putLong(MediaMetadata.METADATA_KEY_DURATION, 65000).build());
        mMusic.add(new MediaMetadataCompat.Builder().putString(MediaMetadata.METADATA_KEY_MEDIA_ID,
                "https://www.youtube.com/audiolibrary_download?vid=456229530454affd")
                .putString(MediaMetadata.METADATA_KEY_TITLE, "Tercera canción")
                .putString(MediaMetadata.METADATA_KEY_ARTIST, "Artista 3")
                .putLong(MediaMetadata.METADATA_KEY_DURATION, 121000).build());

        mPlayer = new MediaPlayer();
        mSession = new MediaSessionCompat(this, "MiServicioMusical");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                Log.d(TAG, ">>> onPlayFromMediaId");
                for (MediaMetadataCompat item : mMusic) {
                    if (item.getDescription().getMediaId().equals(mediaId)) {
                        mCurrentTrack = item;
                        break;
                    }
                }
                Log.d(TAG, "handlePlay onPlayFromMediaId");
                handlePlay();
            }

            @Override
            public void onPlay() {
                Log.d(TAG, ">>> onPlay");
                Log.d(TAG, ">>> mCurrentTrack == nul: "+(mCurrentTrack == null));
                if (mCurrentTrack == null) {
                    mCurrentTrack = mMusic.get(0);
                    Log.d(TAG, "handlePlay onPlay() method");
                    handlePlay();
                } else {
                    mPlayer.start();
                    mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING));
                }
            }

            @Override
            public void onStop() {
                super.onStop();
                Log.d(TAG, ">>> onStop");
            }

            @Override
            public void onPause() {
                mPlayer.pause();
                mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_PAUSED));
            }
        });
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setActive(true);
        setSessionToken(mSession.getSessionToken());
    }


    private PlaybackStateCompat buildState(int state) {
        return new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_PAUSE)
                .setState(state, mPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime())
                .build();
    }

    private void handlePlay() {
        mPlayer.seekTo(0);
        mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_BUFFERING));
        Log.d(TAG, "handlePlay 1");
        mSession.setMetadata(mCurrentTrack);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(ServicioMusica.this, Uri.parse(mCurrentTrack.getDescription().getMediaId()));
        } catch (IOException e) {
            Log.d(TAG, "handlePlay catch: "+e.toString());
        }
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, ">>> onPrepared");
                        mediaPlayer.start();
                        mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING));
                    }
                }, 3000);
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion");
                mp.seekTo(0);
                mp.pause();
                mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_STOPPED));
                //mp.reset();
            }
        });
        Log.d(TAG, "handlePlay 2");
        mPlayer.prepareAsync();
    }

    @Override
    public MediaBrowserServiceCompat.BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        return new MediaBrowserServiceCompat.BrowserRoot("ROOT", null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        List<MediaBrowserCompat.MediaItem> list = new ArrayList<MediaBrowserCompat.MediaItem>();
        for (MediaMetadataCompat m : mMusic) {
            list.add(new MediaBrowserCompat.MediaItem(m.getDescription(),
                    MediaBrowser.MediaItem.FLAG_PLAYABLE));
        }
        result.sendResult(list);
    }

    @Override
    public void onDestroy() {
        mSession.release();
    }
}
