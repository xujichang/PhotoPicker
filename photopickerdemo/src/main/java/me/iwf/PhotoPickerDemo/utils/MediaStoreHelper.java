package me.iwf.PhotoPickerDemo.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.iwf.PhotoPickerDemo.PhotoPicker;
import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.entity.Photo;
import me.iwf.PhotoPickerDemo.entity.PhotoDirectory;
import me.iwf.PhotoPickerDemo.entity.Video;
import me.iwf.PhotoPickerDemo.entity.VideoDirectory;

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

    public static void getVideoThumbnail(FragmentActivity activity, Video video, ImageView imageView, ThumbnailResultCallback resultCallback) {
        activity.getSupportLoaderManager().initLoader(0, null, new ThumbnailLoaderCallbacks(activity, video, imageView, resultCallback));

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

    public interface ThumbnailResultCallback {
        void onResultCallback(Photo photo, ImageView imageView);
    }

    private static class ThumbnailLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private Video video;
        private Photo photo;
        private WeakReference<Context> context;
        private ImageView imageView;
        private ThumbnailResultCallback resultCallback;

        public ThumbnailLoaderCallbacks(FragmentActivity activity, Video video, ImageView imageView, ThumbnailResultCallback resultCallback) {
            this.video = video;
            this.context = new WeakReference<Context>(activity);
            this.imageView = imageView;
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new ThumbnailLoader(context.get(), MediaStore.Video.Thumbnails.MINI_KIND, video.getId());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (null == data) {
                return;
            }
            while (data.moveToNext()) {
                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                photo = new Photo(imageId, path);
                video.setThumbnail(photo);
            }
            if (resultCallback != null) {
                resultCallback.onResultCallback(photo, imageView);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private static class VideoDirLoaderCallBacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private FragmentActivity activity;
        private WeakReference<Context> context;
        private VideosResultCallback resultCallback;

        public VideoDirLoaderCallBacks(FragmentActivity activity, VideosResultCallback resultCallback) {
            this.activity = activity;
            this.context = new WeakReference<Context>(activity);
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
                //根据获取的信息创建文件夹对象
                VideoDirectory directory = new VideoDirectory();
                directory.setId(bucketId);
                directory.setName(name);
                Video video = new Video(videoId, path);
//                getVideoThumbnail(activity, video);
                //判断在文件夹集合了；里面是否已经有过此文件夹 已经重写过equals
                if (!videoDirectories.contains(directory)) {
                    //如果集合中不存在文件夹的信息，则设置此文件夹的信息
                    directory.setCoverPath(path);
                    directory.addVideo(video);
                    directory.setDateAdded(dateAdded);
                    //添加到文件夹的集合
                    videoDirectories.add(directory);
                } else {
                    //如果文件夹已存在，则将此视频添加到指定的文件夹中
                    videoDirectories.get(videoDirectories.indexOf(directory)).addVideo(videoId, path);
                }
                //所有视频的文件夹当然也要添加
                videoDirectoryAll.addVideo(video);
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
