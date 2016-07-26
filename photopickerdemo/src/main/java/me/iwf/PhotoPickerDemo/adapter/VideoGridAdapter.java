package me.iwf.PhotoPickerDemo.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.entity.Video;
import me.iwf.PhotoPickerDemo.entity.VideoDirectory;

/**
 * Created by xujichang on 2016/7/25.
 */
public class VideoGridAdapter extends SelectableAdapter<VideoGridAdapter.VideoViewHolder> {
    private RequestManager videoGide;
    private List<Video> videos;
    private FragmentActivity context;

    public VideoGridAdapter(List<VideoDirectory> videoDirectories, RequestManager videoGide, FragmentActivity context) {
        this.directories = videoDirectories;
        this.videoGide = videoGide;
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.picker_item_video, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        List<Video> videos = getCurrentVideos();
        Video video = videos.get(position);
        new myAsyncTask(holder.thumb_image, video.getPath()).execute();
//        ThumbnailUtils.createVideoThumbnail(video.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
//        MediaStoreHelper.getVideoThumbnail(context, video, holder.thumb_image, new MediaStoreHelper.ThumbnailResultCallback() {
//            @Override
//            public void onResultCallback(Photo photo, ImageView imageView) {
//                 videoGide.load(new File(photo.getPath()))
//                        .centerCrop()
//                        .dontAnimate()
//                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
//                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
//                        .into(imageView);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int size = directories.size() == 0 ? 0 : getCurrentVideos().size();
        return size;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        //        private TextView name;
//        private TextView path;
        private ImageView thumb_image;

        public VideoViewHolder(View itemView) {
            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.name);
//            path = (TextView) itemView.findViewById(R.id.path);
            thumb_image = (ImageView) itemView.findViewById(R.id.thumb_image);
        }
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {
        private ImageView imageView;
        private String path;
        private Bitmap bitmap;

        public myAsyncTask(ImageView imageView, String path) {
            this.imageView = imageView;
            this.path = path;
        }

        @Override
        protected Void doInBackground(Void... params) {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
