package com.example.automediabasico;

import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.List;

import static com.example.automediabasico.Constant.CONTENT_STYLE_BROWSABLE_HINT;
import static com.example.automediabasico.Constant.CONTENT_STYLE_GRID_ITEM_HINT_VALUE;
import static com.example.automediabasico.Constant.CONTENT_STYLE_LIST_ITEM_HINT_VALUE;
import static com.example.automediabasico.Constant.CONTENT_STYLE_PLAYABLE_HINT;

/**
 * Created by William_ST on 05/03/19.
 */

public class TreeMapperMusic {

    public static String MEDIA_ID_MUSICS_BY_GENRE = "GENRE";
    private String MEDIA_TITLE_MUSICS_BY_GENRE = "GENERO";
    public static String MEDIA_ID_MUSICS_BY_ALBUM = "ALBUM";
    private String MEDIA_TITLE_MUSICS_BY_ALBUM = "ALBÚM";
    public static String MEDIA_ID_MUSICS_BY_ARTIST = "ARTIST";
    private String MEDIA_TITLE_MUSICS_BY_ARTIST = "ARTISTA";

    private List<PistaAudio> pistaAudioList;

    private List<MediaBrowserCompat.MediaItem> rootMenuBrowser;

    private List<MediaBrowserCompat.MediaItem> genderList;
    private List<MediaBrowserCompat.MediaItem> albumList;
    private List<MediaBrowserCompat.MediaItem> artistList;

    public TreeMapperMusic(Musica musica) {
        rootMenuBrowser = new ArrayList<>();
        rootMenuBrowser.add(createMediaItem(MEDIA_ID_MUSICS_BY_GENRE, MEDIA_TITLE_MUSICS_BY_GENRE, null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
        rootMenuBrowser.add(createMediaItem(MEDIA_ID_MUSICS_BY_ALBUM, MEDIA_TITLE_MUSICS_BY_ALBUM, null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
        rootMenuBrowser.add(createMediaItem(MEDIA_ID_MUSICS_BY_ARTIST, MEDIA_TITLE_MUSICS_BY_ARTIST, null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));

        genderList = new ArrayList<>();
        albumList = new ArrayList<>();
        artistList = new ArrayList<>();

        pistaAudioList = musica.getMusica();

        for (PistaAudio pistaAudio : pistaAudioList) {
            if (!search(genderList, pistaAudio.getGenre()).equalsIgnoreCase(Constant.EMPTY)) {
                genderList.add(createMediaItem(pistaAudio.getGenre(), pistaAudio.getGenre(), null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
            }

            if (!search(albumList, pistaAudio.getAlbum()).equalsIgnoreCase(Constant.EMPTY)) {
                albumList.add(createMediaItem(pistaAudio.getAlbum(), pistaAudio.getAlbum(), null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
            }

            if (!search(artistList, pistaAudio.getArtist()).equalsIgnoreCase(Constant.EMPTY)) {
                artistList.add(createMediaItem(pistaAudio.getArtist(), pistaAudio.getArtist(), null, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
            }
        }

    }

    private String search(List<MediaBrowserCompat.MediaItem> list, String searchItem) {
        for (MediaBrowserCompat.MediaItem item : list) {
            if (item.getMediaId().equalsIgnoreCase(searchItem))
                return Constant.EMPTY;
        }
        return searchItem;
    }

    public List<MediaBrowserCompat.MediaItem> getLoanChildMenu(String key) {
        if (key.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_GENRE)) {
            return genderList;
        } else if (key.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_ALBUM)) {
            return albumList;
        } else if (key.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_ARTIST)) {
            return artistList;
        } else {
            return new ArrayList<>();
        }
    }

    public List<MediaBrowserCompat.MediaItem> getLoanChildPLayer(String filterCategory, String key) {
        List<MediaBrowserCompat.MediaItem> items =  new ArrayList<>();
        for (PistaAudio pistaAudio : pistaAudioList) {
            if (filterCategory.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_GENRE)) {
                if (pistaAudio.getGenre().equalsIgnoreCase(key)) {
                    items.add(createMediaItem(items.size()+"", pistaAudio.getTitle(), Uri.parse(pistaAudio.getSource()), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
                }
            } else if (filterCategory.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_ALBUM)) {
                if (pistaAudio.getAlbum().equalsIgnoreCase(key)) {
                    items.add(createMediaItem(items.size()+"", pistaAudio.getTitle(), Uri.parse(pistaAudio.getSource()), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
                }
            } else if (filterCategory.equalsIgnoreCase(MEDIA_ID_MUSICS_BY_ARTIST)) {
                if (pistaAudio.getArtist().equalsIgnoreCase(key)) {
                    items.add(createMediaItem(items.size()+"", pistaAudio.getTitle(), Uri.parse(pistaAudio.getSource()), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
                }
            }
        }

        return items;
    }

    public List<MediaBrowserCompat.MediaItem> getRootMenuBrowser() {
        return rootMenuBrowser;
    }

    private MediaBrowserCompat.MediaItem createMediaItem(String mediaId, String folderName, Uri iconUri, int flag) {
        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(mediaId);
        mediaDescriptionBuilder.setTitle(folderName);
        mediaDescriptionBuilder.setIconUri(iconUri);
        return new MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), flag);
    }
}
