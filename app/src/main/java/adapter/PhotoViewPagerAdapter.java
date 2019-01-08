package adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.R;

import uk.co.senab.photoview.PhotoView;


public class PhotoViewPagerAdapter extends PagerAdapter {
    private AppCompatActivity activity;
    private String[] images;

    public PhotoViewPagerAdapter(AppCompatActivity activity, String[] images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = images[position];
        PhotoView photoView = new PhotoView(activity);
        //加载图片
        Glide
                .with(activity)
                .load(url)
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
//                .centerCrop()
//                .crossFade()
                .into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        return images != null ? images.length : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
