package me.iwf.PhotoPickerDemo.entity;

/**
 * Created by xujichang on 2016/7/25.
 */
public class Video {
    private int id;
    private String path;
    private Photo thumbnail;

    public Video(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Video(int id, String path, Photo thumbnail) {
        this.id = id;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Photo getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Photo thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        return id == video.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
