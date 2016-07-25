package me.iwf.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;


public class VideoPicker {
    public static final int REQUEST_CODE = 234;

    public static VideoPickerBuilder builder() {
        return new VideoPickerBuilder();
    }

    public static class VideoPickerBuilder {
        /**
         * 选择视频时的参数携带
         */
        private Bundle mVideoOptionsBundle;
        private Intent mVideoIntent;

        public VideoPickerBuilder() {
            mVideoOptionsBundle = new Bundle();
            mVideoIntent = new Intent();
        }


        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment, int requestCode) {
            fragment.startActivityForResult(getIntent(context), requestCode);
        }

        public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), REQUEST_CODE);
        }

        public void start(@NonNull Activity activity) {
            start(activity, REQUEST_CODE);
        }

        /**
         * 组装Intent
         *
         * @param context
         * @return
         */
        private Intent getIntent(Context context) {
            mVideoIntent.setClass(context, VideoPickerActivity.class);
            mVideoIntent.putExtras(mVideoOptionsBundle);
            return mVideoIntent;
        }

    }
}
