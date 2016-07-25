package me.iwf.photopicker.entity;

import java.util.ArrayList;
import java.util.List;


public class VideoDirectory extends BaseDirectory {
    List<Video> videos = new ArrayList<>();

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<String> getVideoPath() {
        List<String> paths = new ArrayList<>(videos.size());
        for (Video video : videos) {
            paths.add(video.getPath());
        }
        return paths;
    }

    public void addVideo(int id, String path) {
        videos.add(new Video(id, path));
    }

    @Override
    void setDirectoryType() {
        directoryType = DIRESTORY_VIDEO;
    }
}
