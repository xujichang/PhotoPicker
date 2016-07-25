package me.iwf.photopicker.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Video.Media;
import android.support.v4.content.CursorLoader;

/**
 * Created by xujichang on 2016/7/25.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoDirectoryLoader extends CursorLoader {
    final String[] VIDEO_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED,
    };

    public VideoDirectoryLoader(Context context) {
        super(context);
        setProjection(VIDEO_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");
//        setSelection(
//                MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? ");
//        String[] selectionArgs = new String[]{"video/3gpp", "video/mp4", "video/x-msvideo"};
//        setSelectionArgs(new String[]{"1=1"});
        setSelection(null);
        setSelectionArgs(null);
    }

    public VideoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
