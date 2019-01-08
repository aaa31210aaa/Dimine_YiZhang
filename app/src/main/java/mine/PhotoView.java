package mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import adapter.PhotoViewPagerAdapter;
import utils.AppUtils;
import utils.BaseActivity;

public class PhotoView extends BaseActivity {
    private PhotoViewPager photo_viewPager;
    //点的是哪一张图片
    private int currentPosition;
    //数量显示
    private TextView photo_num;
    private PhotoViewPagerAdapter adapter;
    //图片集合
    private String[] images;
    private TextView photo_view_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        photo_viewPager = (PhotoViewPager) findViewById(R.id.photo_viewPager);
        photo_num = (TextView) findViewById(R.id.photo_num);
        photo_view_back = (TextView) findViewById(R.id.photo_view_back);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", 0);
        images = intent.getStringArrayExtra("urls");
        Log.e(TAG, images + "");
        adapter = new PhotoViewPagerAdapter(this, images);
        photo_viewPager.setAdapter(adapter);
        photo_viewPager.setCurrentItem(currentPosition);
        photo_num.setText(currentPosition + 1 + "/" + images.length);
        photo_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                photo_num.setText(currentPosition + 1 + "/" + images.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void setOnClick() {
        photo_view_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_view_back:
                finish();
                break;
        }
    }
}
