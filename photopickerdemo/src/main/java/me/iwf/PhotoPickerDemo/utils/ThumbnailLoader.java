package me.iwf.PhotoPickerDemo.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by xujichang on 2016/7/26.
 */
public class ThumbnailLoader extends CursorLoader {

    final String[] THUMBNAIL_PROJECTION = {
            MediaStore.Video.Thumbnails._ID,
            MediaStore.Video.Thumbnails.DATA,
    };

    /**
     * @param context
     * @param kind
     */
    public ThumbnailLoader(Context context, int kind, int videoId) {
        super(context);
        setProjection(THUMBNAIL_PROJECTION);
        setUri(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI);
        setSelection(MediaStore.Video.Thumbnails.VIDEO_ID + "=? and " + MediaStore.Video.Thumbnails.KIND + "=?");
        setSelectionArgs(new String[]{String.valueOf(videoId), String.valueOf(kind)});
        setSortOrder(null);
    }

    public ThumbnailLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
