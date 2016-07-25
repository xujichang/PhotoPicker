package me.iwf.photopicker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.iwf.photopicker.fragment.VideoPickerFragment;

/**
 * Created by xujichang on 2016/7/25.
 */
public class VideoPickerActivity extends AppCompatActivity {
    private VideoPickerFragment pickerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picker_activity_video_picket);
        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Video");

        ActionBar actionBar = getSupportActionBar();
        //断言 java 中的关键词
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果支持阴影效果
            actionBar.setElevation(25);
        }

        pickerFragment = (VideoPickerFragment) getSupportFragmentManager().findFragmentByTag("VideoPickerFragment");
        if (null == pickerFragment) {
            pickerFragment = VideoPickerFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, pickerFragment, "PhotoPickerFragment")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }
}
