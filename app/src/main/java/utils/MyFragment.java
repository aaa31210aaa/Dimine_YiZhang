package utils;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Field;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class MyFragment extends Fragment{
    public FragmentActivity mActivity;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    public String TAG = getClass().getSimpleName();
    public Dialog dialog;
    public String url;
    public String user_token;

    /**
     * 刷新样式
     */
    protected void MyRefreshStyle(BGARefreshLayout refreshLayout) {
        refreshLayout.setDelegate((BGARefreshLayout.BGARefreshLayoutDelegate) this);
//        BGAMoocStyleRefreshViewHolder refreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
//        refreshViewHolder.setOriginalImage(R.drawable.refresh_ks);
//        refreshViewHolder.setUltimateColor(R.color.refresh_img_ks);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }


    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


}
