package me.iwf.PhotoPickerDemo.entity;

import java.util.ArrayList;
import java.util.List;


public class VideoDirectory extends BaseDirectory {
    //视频文件的集合
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
        Video video = new Video(id, path);
        videos.add(video);

    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    @Override
    void setDirectoryType() {
        directoryType = DIRESTORY_VIDEO;
    }
}
