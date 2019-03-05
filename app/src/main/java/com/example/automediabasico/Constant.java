package com.example.automediabasico;

/**
 * Created by William_ST on 05/03/19.
 */

public class Constant {

    public static final String EMPTY = "";
    /**
     * Declares that ContentStyle is supported
     */
    public static final String CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED";

    /**
     * Bundle extra indicating the presentation hint for playable media items.
     */
    public static final String CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT";

    /**
     * Bundle extra indicating the presentation hint for browsable media items.
     */
    public static final String CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT";

    /**
     * Specifies the corresponding items should be presented as lists.
     */
    public static final int CONTENT_STYLE_LIST_ITEM_HINT_VALUE = 1;

    /**
     * Specifies that the corresponding items should be presented as grids.
     */
    public static final int CONTENT_STYLE_GRID_ITEM_HINT_VALUE = 2;


    /**
     * Bundle extra indicating that a song is explicit.
     */
    public static String EXTRA_IS_EXPLICIT = "android.media.IS_EXPLICIT";
    /**
     * Bundle extra indicating that a media item is available offline.
     * Same as MediaDescriptionCompat.EXTRA_DOWNLOAD_STATUS.
     */
    public static String EXTRA_IS_DOWNLOADED = "android.media.extra.DOWNLOAD_STATUS";
    /**
     * Bundle extra value indicating that an item should show the correspon
     * ding metadata.
     */
    public static long EXTRA_METADATA_ENABLED_VALUE = 1;
    /**
     * Bundle extra indicating the played state of long-form content (such as
     * podcast episodes or audiobooks).
     */
    public static String EXTRA_PLAY_COMPLETION_STATE = "android.media.extra.PLAYBACK_STATUS";
    /**
     * Value for EXTRA_PLAY_COMPLETION_STATE that indicates the media item has * not been played at all.
     */
    public static int STATUS_NOT_PLAYED = 0;
    /**
     * Value for EXTRA_PLAY_COMPLETION_STATE that indicates the media item has * been partially played (i.e. the current position is somewhere in the
     * middle).
     */
    public static int STATUS_PARTIALLY_PLAYED = 1;
    /**
     * Value for EXTRA_PLAY_COMPLETION_STATE that indicates the media item has * been completed.
     */
    public static int STATUS_FULLY_PLAYED = 2;
}
