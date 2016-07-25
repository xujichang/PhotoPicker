package me.iwf.PhotoPickerDemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.entity.Photo;
import me.iwf.PhotoPickerDemo.entity.PhotoDirectory;
import me.iwf.PhotoPickerDemo.event.OnItemCheckListener;
import me.iwf.PhotoPickerDemo.event.OnPhotoClickListener;
import me.iwf.PhotoPickerDemo.utils.MediaStoreHelper;

/**
 * 浏览图片的Adapter
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private LayoutInflater inflater;
    private RequestManager glide;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;
    private View.OnClickListener onCameraClickListener = null;

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private final static int COL_NUMBER_DEFAULT = 3;

    private boolean hasCamera = true;
    private boolean previewEnable = true;

    private int imageSize;
    private int columnNumber = COL_NUMBER_DEFAULT;

    /**
     * @param context
     * @param requestManager
     * @param photoDirectories
     */
    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories) {
        this.directories = photoDirectories;
        this.glide = requestManager;
        inflater = LayoutInflater.from(context);
        setColumnNumber(context, columnNumber);
    }

    /**
     * 可以设置列数 以及具有已选图片
     *
     * @param context          上下文
     * @param requestManager
     * @param photoDirectories 文件夹的
     * @param orginalPhotos
     * @param colNum
     */
    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories, ArrayList<String> orginalPhotos, int colNum) {
        this(context, requestManager, photoDirectories);
        setColumnNumber(context, colNum);
        setOriginalPhotos(orginalPhotos);
    }

    private void setOriginalPhotos(ArrayList<String> originalPhotos) {
        this.originalPhotos = originalPhotos;
    }

    /**
     * 根据列数 计算每张图片的大小
     *
     * @param context
     * @param columnNumber
     */
    private void setColumnNumber(Context context, int columnNumber) {
        this.columnNumber = columnNumber;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        if (viewType == ITEM_TYPE_CAMERA) {
            holder.vSelected.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCameraClickListener != null) {
                        onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            List<Photo> photos = getCurrentPhotos();
            final Photo photo;

            if (showCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }
            //使用Glide框架加载图片
            glide.load(new File(photo.getPath()))
                    .centerCrop()
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .override(imageSize, imageSize)
                    .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);

            final boolean isChecked = isSelected(photo);

            holder.vSelected.setSelected(isChecked);
            holder.ivPhoto.setSelected(isChecked);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        //如果是可预览的 则去预览，不可预览则触发选中事件
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos, showCamera());
                        } else {
                            //代码触发指定控件的点击事件
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;

                    if (onItemCheckListener != null) {
                        isEnable = onItemCheckListener.OnItemCheck(pos, photo, isChecked,
                                getSelectedPhotos().size());
                    }
                    if (isEnable) {
                        toggleSelection(photo);
                        notifyItemChanged(pos);
                    }
                }
            });

        } else {
            holder.ivPhoto.setImageResource(R.drawable.__picker_camera);
        }
    }


    @Override
    public int getItemCount() {
        int photosCount =
                directories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
        }
    }

    /**
     * 设置Item的点击事件
     *
     * @param onItemCheckListener
     */
    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    /**
     * 设置图片的点击事件
     *
     * @param onPhotoClickListener
     */
    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    /**
     * 设置拍照标志的点击事件
     *
     * @param onCameraClickListener
     */
    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    /**
     * huoqu 选中图片的Path
     *
     * @return
     */
    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>();

        for (Photo photo : selectedPhotos) {
            selectedPhotoPaths.add(photo.getPath());
        }

        return selectedPhotoPaths;
    }


    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    /**
     * 判断是否显示拍照标志 显示标志&&全部照片
     *
     * @return
     */
    public boolean showCamera() {
        return (hasCamera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL);
    }

    /**
     * 当RecyclerView的item 被删除的时候，Glide也需要删除，释放内存
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
        Glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
