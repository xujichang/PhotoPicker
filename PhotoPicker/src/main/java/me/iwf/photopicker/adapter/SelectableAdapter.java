package me.iwf.photopicker.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.BaseDirectory;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.entity.Video;
import me.iwf.photopicker.entity.VideoDirectory;
import me.iwf.photopicker.event.Selectable;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    protected List<? extends BaseDirectory> directories;
    protected List<Photo> selectedPhotos;
    private Video selectedVideo;

    /**
     * 初始进入时已选的照片 original selected photos
     */
    protected ArrayList<String> originalPhotos = null;

    public int currentDirectoryIndex = 0;


    public SelectableAdapter() {
        directories = new ArrayList<>();
        selectedPhotos = new ArrayList<>();
    }


    /**
     * 判断当前的额Item是否被选中
     *
     * @param photo
     * @return
     */
    @Override
    public boolean isSelected(Photo photo) {
        //把之前已经选中的图片添加当前
        if (originalPhotos != null && originalPhotos.contains(photo.getPath()) && !selectedPhotos.contains(photo)) {
            selectedPhotos.add(photo);
        }
        return getSelectedPhotos().contains(photo);
    }


    /**
     * 改变选中的状态 即如果选中则取消选中，如果未选中，则选中
     *
     * @param photo
     */
    @Override
    public void toggleSelection(Photo photo) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo);
            if (originalPhotos != null && originalPhotos.contains(photo.getPath())) {
                originalPhotos.remove(photo.getPath());
            }
        } else {
            selectedPhotos.add(photo);
        }
    }


    /**
     * 清除所有的选中的图片
     */
    @Override
    public void clearSelection() {
        selectedPhotos.clear();
    }


    /**
     * 返回当前选中图片的数量
     *
     * @return
     */
    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    /**
     * 当前选择的文件夹在文件夹集合中的position
     *
     * @param currentDirectoryIndex
     */
    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }

    /**
     * 根据Position,获取此文件夹中的图片集合
     *
     * @return
     */
    public List<Photo> getCurrentPhotos() {

        return ((PhotoDirectory) directories.get(currentDirectoryIndex)).getPhotos();
    }

    public List<Video> getCurrentVideos() {
        return ((VideoDirectory) directories.get(currentDirectoryIndex)).getVideos();
    }

    /**
     * 将本文件中的所有的Photo对象中提取处path 组成集合
     *
     * @return
     */
    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>();
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }

    /**
     * 返回 到目前为止 选中的图片
     *
     * @return
     */
    public List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

}