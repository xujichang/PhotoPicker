package me.iwf.photopicker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donglua on 15/6/28.
 */
public class PhotoDirectory extends BaseDirectory {

    private List<Photo> photos = new ArrayList<>();

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    public void addPhoto(int id, String path) {
        photos.add(new Photo(id, path));
    }

    @Override
    void setDirectoryType() {
        directoryType = DIRESTORY_PHOTO;
    }
}
