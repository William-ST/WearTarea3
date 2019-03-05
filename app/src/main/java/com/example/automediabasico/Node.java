package com.example.automediabasico;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;
import java.util.Map;

/**
 * Created by William_ST on 05/03/19.
 */

public class Node {

    private Map<String, List<MediaBrowserCompat.MediaItem>> mediaItems;

    public Map<String, List<MediaBrowserCompat.MediaItem>> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(Map<String, List<MediaBrowserCompat.MediaItem>> mediaItems) {
        this.mediaItems = mediaItems;
    }
}
