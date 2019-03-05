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

import static com.example.automediabasico.Constant.CONTENT_STYLE_BROWSABLE_HINT;
import static com.example.automediabasico.Constant.CONTENT_STYLE_GRID_ITEM_HINT_VALUE;
import static com.example.automediabasico.Constant.CONTENT_STYLE_LIST_ITEM_HINT_VALUE;
import static com.example.automediabasico.Constant.CONTENT_STYLE_PLAYABLE_HINT;
import static com.example.automediabasico.Constant.CONTENT_STYLE_SUPPORTED;

public class ServicioMusica extends MediaBrowserServiceCompat {

    private final String TAG = ServicioMusica.class.getCanonicalName();

    private MediaSessionCompat mSession;
    private MediaPlayer mPlayer;
    private MediaMetadataCompat mCurrentTrack;
    private final String LIKE_ACTION = "like_action", NOT_LIKE_ACTION = "not_like_action";
    /*
    private MediaMetadataCompat getFirstMusic() {
        PistaAudio foundPistaAudio = ((App) getApplication()).getMusica().getMusica().get(0);
        MediaMetadataCompat mediaMetadata = null;
        if (foundPistaAudio != null) {
            mediaMetadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, foundPistaAudio.getSource())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, foundPistaAudio.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, foundPistaAudio.getTitle())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, foundPistaAudio.getArtist())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, foundPistaAudio.getDuration()*1000).build();
        }
        return mediaMetadata;
    }

    private MediaMetadataCompat getMusicByMediaId(String mediaId) {
        PistaAudio foundPistaAudio = null;
        for (PistaAudio pistaAudio : ((App) getApplication()).getMusica().getMusica()) {
            if (pistaAudio.getId().equalsIgnoreCase(mediaId)) {
                foundPistaAudio = pistaAudio;
                break;
            }
        }
        MediaMetadataCompat mediaMetadata = null;
        if (foundPistaAudio != null) {
            mediaMetadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, foundPistaAudio.getSource())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, foundPistaAudio.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, foundPistaAudio.getTitle())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, foundPistaAudio.getArtist())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, foundPistaAudio.getDuration()*1000).build();
        }
        return mediaMetadata;
    }
    */

    private boolean prepareMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        prepareMediaPlayer = false;
        mPlayer = new MediaPlayer();
        mSession = new MediaSessionCompat(this, "MiServicioMusical");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                Log.d(TAG, ">>> onPlayFromMediaId");
                MediaMetadataCompat mediaMetadataCompat = ((App) getApplication()).getMusicByMediaId(mediaId);
                if (mediaMetadataCompat != null) {
                    mCurrentTrack = mediaMetadataCompat;
                    handlePlay();
                }
            }

            @Override
            public void onPlay() {
                if (prepareMediaPlayer) {
                    Log.d(TAG, ">>> onPlay");
                    Log.d(TAG, ">>> mCurrentTrack == nul: " + (mCurrentTrack == null));
                    if (mCurrentTrack == null) {
                        mCurrentTrack = ((App) getApplication()).getFirstMusic();
                        Log.d(TAG, "handlePlay onPlay() method");
                        handlePlay();
                    } else {
                        mPlayer.start();
                        mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING));
                    }
                }
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                if (prepareMediaPlayer) {
                    mPlayer.reset();
                    mCurrentTrack = ((App) getApplication()).getLastMusic();
                    handlePlay();
                    Log.d(TAG, ">>> onSkipToPrevious");
                }
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                if (prepareMediaPlayer) {
                    mPlayer.reset();
                    mCurrentTrack = ((App) getApplication()).getNextMusic();
                    handlePlay();
                    Log.d(TAG, ">>> onSkipToNext");
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
                .addCustomAction(LIKE_ACTION, "Me gusta", R.drawable.ic_like)
                .addCustomAction(NOT_LIKE_ACTION, "Me gusta", R.drawable.ic_not_like)
                .setState(state, mPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime())
                .build();
    }

    private void handlePlay() {
        prepareMediaPlayer = false;
        mPlayer.seekTo(0);
        mSession.setPlaybackState(buildState(PlaybackStateCompat.STATE_BUFFERING));
        Log.d(TAG, "handlePlay 1");
        mSession.setMetadata(mCurrentTrack);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(ServicioMusica.this, Uri.parse(mCurrentTrack.getDescription().getMediaId()));
        } catch (IOException e) {
            Log.d(TAG, "handlePlay catch: " + e.toString());
        }
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareMediaPlayer = true;
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


                /*
                if (prepareMediaPlayer) {
                    mPlayer.reset();
                    mCurrentTrack = ((App) getApplication()).getNextMusic();
                    handlePlay();
                    Log.d(TAG, ">>> onSkipToNext");
                }
                */


            }
        });
        Log.d(TAG, "handlePlay 2");
        mPlayer.prepareAsync();
    }

    private final String ROOT_ID = "my_media_root_id";

    @Override
    public MediaBrowserServiceCompat.BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        Bundle extras = new Bundle();
        extras.putBoolean(CONTENT_STYLE_SUPPORTED, true);
        extras.putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_GRID_ITEM_HINT_VALUE);
        extras.putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_LIST_ITEM_HINT_VALUE);
        return new MediaBrowserServiceCompat.BrowserRoot(ROOT_ID, null);
    }

    private static TreeMapperMusic treeMapperMusic;
    private String lastMediaIdMusic = Constant.EMPTY;

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        if (ROOT_ID.equals(parentId)) {
            result.detach();
            GetMusicRepository getMusicRepository = new GetMusicRepository(this);
            getMusicRepository.setParseTree(new GetMusicRepository.ParseTree() {
                @Override
                public void parse(TreeMapperMusic treeMapperMusicParam) {
                    treeMapperMusic = treeMapperMusicParam;
                    result.sendResult(treeMapperMusic.getRootMenuBrowser());
                }
            });
            getMusicRepository.getRepositorioMusical();
        } else if (parentId.equalsIgnoreCase(TreeMapperMusic.MEDIA_ID_MUSICS_BY_GENRE)
                || parentId.equalsIgnoreCase(TreeMapperMusic.MEDIA_ID_MUSICS_BY_ALBUM)
                || parentId.equalsIgnoreCase(TreeMapperMusic.MEDIA_ID_MUSICS_BY_ARTIST)) {
            lastMediaIdMusic = parentId;
            result.sendResult(treeMapperMusic.getLoanChildMenu(parentId));
        } else {
            if (treeMapperMusic != null && parentId !=null && lastMediaIdMusic != null)
                result.sendResult(treeMapperMusic.getLoanChildPLayer(lastMediaIdMusic, parentId));
        }
    }

    @Override
    public void onDestroy() {
        mSession.release();
    }
}
