package com.example.automediabasico;

import android.app.Application;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import java.io.LineNumberReader;

/**
 * Created by William_ST on 05/03/19.
 */

public class App extends Application {

    private final String TAG = App.class.getCanonicalName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Musica musica;

    private Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    private int currentPosition;

    private MediaMetadataCompat getMediaMetadataOfPistaAudio(PistaAudio pistaAudio) {
        MediaMetadataCompat mediaMetadata = null;
        if (pistaAudio != null) {
            mediaMetadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pistaAudio.getImage())
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, pistaAudio.getSource())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, pistaAudio.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pistaAudio.getTitle())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, pistaAudio.getArtist())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pistaAudio.getDuration() * 1000).build();
        }
        return mediaMetadata;
    }

    public MediaMetadataCompat getFirstMusic() {
        Log.d(TAG, "getFirstMusic called");
        currentPosition = 0;
        PistaAudio foundPistaAudio = getMusica().getMusica().get(currentPosition);
        MediaMetadataCompat mediaMetadata = null;
        if (foundPistaAudio != null) {
            mediaMetadata = getMediaMetadataOfPistaAudio(foundPistaAudio);
            Log.d(TAG, "getFirstMusic found: "+foundPistaAudio.getTitle());
        }
        return mediaMetadata;
    }

    public MediaMetadataCompat getMusicByMediaId(String mediaId) {
        Log.d(TAG, "getMusicByMediaId called");
        PistaAudio foundPistaAudio = null;
        for (int i = 0; i < getMusica().getMusica().size(); i++) {
            if (getMusica().getMusica().get(i).getId().equalsIgnoreCase(mediaId)) {
                foundPistaAudio = getMusica().getMusica().get(i);
                currentPosition = i;
                break;
            }
        }
        MediaMetadataCompat mediaMetadata = null;
        if (foundPistaAudio != null) {
            mediaMetadata = getMediaMetadataOfPistaAudio(foundPistaAudio);
            Log.d(TAG, "getMusicByMediaId found: "+foundPistaAudio.getTitle());
        }
        return mediaMetadata;
    }

    public MediaMetadataCompat getNextMusic() {
        Log.d(TAG, "getNextMusic called");
        MediaMetadataCompat mediaMetadata = null;
        Log.d(TAG, "getNextMusic condition: "+(currentPosition + 1 <= getMusica().getMusica().size() - 1));
        if (currentPosition + 1 <= getMusica().getMusica().size() - 1) {
            mediaMetadata = getMediaMetadataOfPistaAudio(getMusica().getMusica().get(currentPosition + 1));
            currentPosition = currentPosition + 1;
        } else {
            mediaMetadata = getMediaMetadataOfPistaAudio(getMusica().getMusica().get(0));
            currentPosition = 0;
        }
        return mediaMetadata;
    }

    public MediaMetadataCompat getLastMusic() {
        Log.d(TAG, "getLastMusic called");
        MediaMetadataCompat mediaMetadata = null;
        Log.d(TAG, "getLastMusic condition: "+(currentPosition - 1 != -1));
        if (currentPosition - 1 != -1) {
            mediaMetadata = getMediaMetadataOfPistaAudio(getMusica().getMusica().get(currentPosition - 1));
            currentPosition = currentPosition - 1;
        } else {
            mediaMetadata = getMediaMetadataOfPistaAudio(getMusica().getMusica().get(getMusica().getMusica().size()-1));
            currentPosition = getMusica().getMusica().size()-1;
        }
        return mediaMetadata;
    }
}
