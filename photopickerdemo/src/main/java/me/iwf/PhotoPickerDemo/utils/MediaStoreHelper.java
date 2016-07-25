package me.iwf.PhotoPickerDemo.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.entity.PhotoDirectory;
import me.iwf.PhotoPickerDemo.entity.VideoDirectory;
import me.iwf.PhotoPickerDemo.PhotoPicker;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

/**
 * Created by donglua on 15/5/31.
 */
public class MediaStoreHelper {

    public final static int INDEX_ALL = 0;

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    public static void getVideoDirs(FragmentActivity activity, Bundle args, VideosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new VideoDirLoaderCallBacks(activity, resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(PhotoPicker.EXTRA_SHOW_GIF, false));
        }

        //数据加载完成的回调
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null)
                return;
            List<PhotoDirectory> directories = new ArrayList<>();
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.setName(context.get().getString(R.string.__picker_all_image));
            photoDirectoryAll.setId("ALL");

            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));

                PhotoDirectory photoDirectory = new PhotoDirectory();
                photoDirectory.setId(bucketId);
                photoDirectory.setName(name);

                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(path);
                    photoDirectory.addPhoto(imageId, path);
                    photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                }

                photoDirectoryAll.addPhoto(imageId, path);
            }
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            directories.add(INDEX_ALL, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

    public interface VideosResultCallback {
        void onResultCallback(List<VideoDirectory> directories);
    }

    private static class VideoDirLoaderCallBacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private WeakReference<Context> context;
        private VideosResultCallback resultCallback;

        public VideoDirLoaderCallBacks(Context context, VideosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new VideoDirectoryLoader(context.get());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (null == data) {
                return;
            }
            List<VideoDirectory> videoDirectories = new ArrayList<>();
            //先创建全部的
            VideoDirectory videoDirectoryAll = new VideoDirectory();
            videoDirectoryAll.setName("全部视频");
            videoDirectoryAll.setId("全部");
            Log.v("VIDEO_DATA", "count :" + data.getCount());
            while (data.moveToNext()) {
                int videoId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long dateAdded = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));

                VideoDirectory directory = new VideoDirectory();
                directory.setId(bucketId);
                directory.setName(name);

                if (!videoDirectories.contains(directory)) {
                    directory.setCoverPath(path);
                    directory.addVideo(videoId, path);
                    directory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    videoDirectories.add(directory);
                } else {
                    videoDirectories.get(videoDirectories.indexOf(directory)).addVideo(videoId, path);
                }
                videoDirectoryAll.addVideo(videoId, path);
            }

            if (videoDirectoryAll.getVideoPath().size() > 0) {
                videoDirectoryAll.setCoverPath(videoDirectoryAll.getVideoPath().get(0));
            }
            videoDirectories.add(INDEX_ALL, videoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(videoDirectories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
