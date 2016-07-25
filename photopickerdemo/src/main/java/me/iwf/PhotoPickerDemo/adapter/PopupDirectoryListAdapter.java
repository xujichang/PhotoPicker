package me.iwf.PhotoPickerDemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.entity.BaseDirectory;
import me.iwf.PhotoPickerDemo.entity.PhotoDirectory;
import me.iwf.PhotoPickerDemo.entity.VideoDirectory;


/**
 * 图片目录选择的ListView的Adapter
 */
public class PopupDirectoryListAdapter extends BaseAdapter {
    //
    private List<? extends BaseDirectory> directories = new ArrayList<>();
    private RequestManager glide;

    public PopupDirectoryListAdapter(RequestManager glide, List<? extends BaseDirectory> directories) {
        this.directories = directories;
        this.glide = glide;
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public BaseDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.__picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //加载数据的方法被封装到了Holder本身
        holder.bindData(directories.get(position));

        return convertView;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView) rootView.findViewById(R.id.tv_dir_count);
        }

        public void bindData(BaseDirectory directory) {
            //加载图片
            glide.load(directory.getCoverPath())
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(ivCover);
            //显示文件名
            tvName.setText(directory.getName());
            //显示文件夹中图片的数量
            tvCount.setText(tvCount.getContext().getString(R.string.__picker_image_count, getSize(directory)));
        }

        private int getSize(BaseDirectory directory) {
            if (directory.getDirectoryType() == BaseDirectory.DIRESTORY_PHOTO) {
                return ((PhotoDirectory) directory).getPhotos().size();
            } else if (directory.getDirectoryType() == BaseDirectory.DIRESTORY_VIDEO) {
                return ((VideoDirectory) directory).getVideos().size();
            } else {
                return 0;
            }
        }

    }
}