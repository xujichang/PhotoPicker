package me.iwf.PhotoPickerDemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import me.iwf.PhotoPickerDemo.R;
import me.iwf.PhotoPickerDemo.adapter.PopupDirectoryListAdapter;
import me.iwf.PhotoPickerDemo.adapter.VideoGridAdapter;
import me.iwf.PhotoPickerDemo.entity.VideoDirectory;
import me.iwf.PhotoPickerDemo.utils.MediaStoreHelper;


public class VideoPickerFragment extends Fragment {
    private Context mContext;
    private RequestManager videoGlide;
    private List<VideoDirectory> directories;
    private VideoGridAdapter videoAdpter;
    private PopupDirectoryListAdapter listAdapter;

    public static VideoPickerFragment newInstance() {

        Bundle args = new Bundle();

        VideoPickerFragment fragment = new VideoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //初始化RequestManager
        videoGlide = Glide.with(mContext);
        //初始化视频文件集合
        directories = new ArrayList<>();
        //初始化Adapter
        videoAdpter = new VideoGridAdapter(directories, videoGlide, getActivity());
        //初始化 directoryAdapter
        listAdapter = new PopupDirectoryListAdapter(videoGlide, directories);

        Bundle mediaStoreArgs = new Bundle();
        MediaStoreHelper.getVideoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.VideosResultCallback() {
                    @Override
                    public void onResultCallback(List<VideoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        videoAdpter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.__picker_fragment_photo_picker, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
        Button btSwitchDirectory = (Button) rootView.findViewById(R.id.button);

        recyclerView.setAdapter(videoAdpter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }
}
