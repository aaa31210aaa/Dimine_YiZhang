package community;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CommunityListAdapter;
import bean.Circle;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Yhbz extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    public static Circle sCurItem;
    public String id;
    private View view;
    private ListView yhbz_list;
    private CommunityListAdapter adapter;
    private BGARefreshLayout fragment_yhbz_refresh;

    private List<Circle> mDatas = new ArrayList<>(100);  //文字内容集合

    private Map<Integer, String[]> imageMap = new HashMap<>(100);
    private int lastPos = 0;
    private List<String[]> paths;

    public void setId(String id) {
        this.id = id;
    }

//    @Override
//    public View makeView() {
//        if (view == null) {
//            view = View.inflate(getActivity(), R.layout.fragment_yhbz, null);
//        }
//
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//        }
//        return view;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_yhbz, null);
            loadData();
        }

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    //    @Override
    protected void loadData() {
        initView();
        initData();
        setOnClick();
    }

    //    @Override
    protected void initView() {
        yhbz_list = (ListView) view.findViewById(R.id.yhbz_list);
        yhbz_list.setEmptyView(view.findViewById(R.id.empty));
        fragment_yhbz_refresh = (BGARefreshLayout) view.findViewById(R.id.fragment_yhbz_refresh);
        MyRefreshStyle(fragment_yhbz_refresh);
    }

    /**
     * 刷新样式
     */
    protected void MyRefreshStyle(BGARefreshLayout refreshLayout) {
        refreshLayout.setDelegate((BGARefreshLayout.BGARefreshLayoutDelegate) this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }


    //    @Override
    protected void initData() {
        String url = PortIpAddress.TopicList();
        OkHttpUtils
                .post()
                .url(url)
                .addParams("access_token", PortIpAddress.getToken(getActivity()))
                .addParams("industrytype", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showShort(getContext(), "获取话题失败，请稍后重试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.e("ces", jo + "");
                            String success = jo.getString("success");
                            String error = jo.getString("errormessage");
                            if (success.equals("true")) {
                                mDatas.clear();
                                lastPos = 0;
                                JSONArray cells = jo.getJSONArray("cells");
                                for (int i = 0; i < cells.length(); i++) {
                                    JSONObject cell = (JSONObject) cells.get(i);
                                    Circle c = new Circle();
                                    c.setId(cell.getString("tid"));
                                    c.setTitle(cell.getString("topicname"));
                                    c.setContent(cell.getString("topiccontent"));
                                    c.setDzNum(cell.getInt("fabulouscount"));
                                    c.setCommentNum(cell.getInt("reviewcount"));
                                    c.setSendTime(cell.getString("releasedate"));
                                    c.setUid(cell.getString("releasename"));
                                    c.setUsername(cell.getString("releasenamename"));
                                    if (cell.getString("headimageurl").equals("")) {
                                        c.setHeadUrl("");
                                    } else {
                                        c.setHeadUrl(PortIpAddress.host.replace("mobile/", "") + cell.getString("headimageurl").replace("\\", "/"));
                                    }
                                    c.setDz_tag(cell.getInt("isfabulous") == 0 ? false : true);
                                    c.setFv_tag(cell.getInt("isfollow") == 0 ? false : true);
                                    mDatas.add(c);
                                }
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(getActivity(), error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast.showShort(getContext(), "获取话题失败，请稍后重试");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置listview
     */
    private void initListView() {
        if (adapter == null) {
            adapter = new CommunityListAdapter(getActivity(), mDatas, this);
            yhbz_list.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void getImageList() {
        if (mDatas == null || mDatas.size() <= 0 || lastPos >= mDatas.size())
            return;
        for (; lastPos < mDatas.size(); lastPos++) {
            final Circle c = (Circle) adapter.getItem(lastPos);
            if (c.getImageUrls() == null) {
                String url = PortIpAddress.TopicImage();
                OkHttpUtils
                        .get()
                        .url(url)
                        .addParams("access_token", PortIpAddress.getToken(getActivity()))
                        .addParams("tid", c.getId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                int i = 0;
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    Log.e("ces", jo + "");
                                    String success = jo.getString("success");
                                    String error = jo.getString("errormessage");
                                    if (success.equals("true")) {
                                        JSONArray cells = jo.getJSONArray("cells");
                                        String[] imgs = new String[cells.length()];
                                        for (int i = 0; i < cells.length(); i++) {
                                            JSONObject cell = (JSONObject) cells.get(i);
                                            imgs[i] = PortIpAddress.host.replace("mobile/", "") + cell.getString("fileurl").replace("\\", "/");
                                        }
                                        c.setImageUrls(imgs);
                                        Message message = handler.obtainMessage();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                    } else {
                                        ShowToast.showShort(getActivity(), error);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            }
        }
    }

    //    @Override
    protected void setOnClick() {

    }


    //    @Override
    public void onClick(View v) {

    }

    public void refresh() {
        initData();
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    initListView();
                    getImageList();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(getActivity())) {
            BaseActivity.sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                    fragment_yhbz_refresh.endRefreshing();
                    ShowToast.showShort(getActivity(), R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(getActivity(), R.string.netcantuse);
            fragment_yhbz_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
