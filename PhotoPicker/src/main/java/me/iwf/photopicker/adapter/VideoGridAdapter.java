package me.iwf.photopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import me.iwf.photopicker.R;
import me.iwf.photopicker.entity.Video;
import me.iwf.photopicker.entity.VideoDirectory;

/**
 * Created by xujichang on 2016/7/25.
 */
public class VideoGridAdapter extends SelectableAdapter<VideoGridAdapter.VideoViewHolder> {
    private RequestManager videoGide;
    private List<Video> videos;
    private Context context;

    public VideoGridAdapter(List<VideoDirectory> videoDirectories, RequestManager videoGide, Context context) {
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
        holder.name.setText(String.valueOf(video.getId()));
        holder.path.setText(video.getPath());
    }

    @Override
    public int getItemCount() {
        int size = directories.size() == 0 ? 0 : getCurrentVideos().size();
        return size;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView path;

        public VideoViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            path = (TextView) itemView.findViewById(R.id.path);
        }
    }
}
