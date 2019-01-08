package utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import tab.Home;

/**
 * Created by Administrator on 2017-03-28.
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    private Home home = new Home();
    private Context context;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        //glide加载出图片，data是传过来的图片地址，
        Glide.with(context).load(data).into(imageView);
    }
}
