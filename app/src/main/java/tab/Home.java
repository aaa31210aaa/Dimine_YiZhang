package tab;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CommunityListAdapter;
import bean.Circle;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import home.Enterprise;
import home.Laws;
import home.MyNotification;
import okhttp3.Call;
import utils.BaseActivity;
import utils.BaseFragment;
import utils.LocalImageHolderView;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private View view;
    private RelativeLayout home_title;
    private TextView home_title_tv;
    private ListView home_list;

    private int lastPos = 0;
    private List<Circle> mDatas;  //文字内容集合

    private CommunityListAdapter adapter;


    private int headerHeight = 0;
    private SparseArray recordSp = new SparseArray(0);//设置容器大小，默认是10
    private int mIsShowTitleHeight;
    private int mCurrentfirstVisibleItem = 0;
    private View headerView;
    //企业信息
    private LinearLayout home_qyxx;
    //我的报表
    private LinearLayout home_report;
    //法律法规
    private LinearLayout home_laws;
    //通知公告
    private LinearLayout home_notification;

    private Intent intent;

    private ConvenientBanner home_banner;
    //本地图片
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    //翻页动画效果
    private ArrayList<String> transformerList = new ArrayList<String>();
    private Class cls;
    private ABaseTransformer transforemer;
    private String transforemerName;
    private String[] imageUrls;
    private List<String> networkImages;
    private String user_token;
    private String url;
    private String TopicListurl;

    private BGARefreshLayout home_refresh;
    private String[] images = {"http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg"
            , "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg", "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=793913307,1022041063&fm=11&gp=0.jpg"};


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_home, null);
            initView();

        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void loadData() {
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        home_title_tv = (TextView) view.findViewById(R.id.home_title_tv);
        home_title = (RelativeLayout) view.findViewById(R.id.home_title);
        home_list = (ListView) view.findViewById(R.id.home_list);
        home_refresh = (BGARefreshLayout) view.findViewById(R.id.home_refresh);
        MyRefreshStyle(home_refresh);
        headerView = View.inflate(getActivity(), R.layout.home_header, null);
        home_banner = (ConvenientBanner) headerView.findViewById(R.id.home_banner);
        home_qyxx = (LinearLayout) headerView.findViewById(R.id.home_qyxx);
        home_laws = (LinearLayout) headerView.findViewById(R.id.home_laws);
        home_report = (LinearLayout) headerView.findViewById(R.id.home_report);
        home_notification = (LinearLayout) headerView.findViewById(R.id.home_notification);
        home_list.addHeaderView(headerView);
        //设置banner
        setBanner();
    }

    @Override
    protected void initData() {
        url = PortIpAddress.Mobiletopic();
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);

        MonitorList();
        mOkhttp();

        TopicListurl = PortIpAddress.TopicList();
        initCommunity();
        initListView();
    }

    //获取话题列表
    private void initCommunity() {
        OkHttpUtils
                .post()
                .url(TopicListurl)
                .addParams("access_token", PortIpAddress.getToken(getActivity()))
                .addParams("industrytype", "")
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
                            Log.e(TAG, jo + "");
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
//                                    c.setHeadUrl(images[i]);
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


    /**
     * 监听
     */
    //监听listview渐变title
    private void MonitorList() {
        home_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                mIsShowTitleHeight = home_title.getHeight() + 500;
                View firstView = view.getChildAt(0);//获取当前最顶部的item
                if (firstView != null) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (itemRecord == null) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();//获取当前最顶部Item的高度
                    itemRecord.top = firstView.getTop();//获取对应item距离顶部的距离
                    recordSp.append(firstVisibleItem, itemRecord);//添加键值对设置值

                    int ScrollY = getScrollY();
                    if (ScrollY <= 0) {
                        home_title.setBackgroundColor(Color.argb(0, 255, 255, 255));
                        home_title_tv.setTextColor(Color.argb(255, 0, 0, 0));
                    } else if (ScrollY > 0 && ScrollY <= mIsShowTitleHeight) {
                        float scale = (float) ScrollY / mIsShowTitleHeight;
                        float alpha = (255 * scale);
                        home_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                        home_title_tv.setTextColor(Color.argb((int) alpha, 105, 105, 105));
                    } else {
                        //滑动距离大于mIsShowTitleHeight就设置为不透明
                        home_title.setBackgroundColor(Color.argb(255, 255, 255, 255));
                        home_title_tv.setTextColor(Color.argb(255, 0, 0, 0));
                    }
                }
            }
        });
    }


    /**
     * 设置listview
     */
    private void initListView() {
        mDatas = new ArrayList<>();

        adapter = new CommunityListAdapter(getActivity(), mDatas, this);

        home_list.setAdapter(adapter);
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "------");
                    }
                });
    }


    private void setBanner() {
        imageUrls = new String[]{
                "http://pic.sc.chinaz.com/files/pic/pic9/201410/apic6568.jpg",
                "http://pic.sc.chinaz.com/files/pic/pic9/201411/apic7677.jpg",
                "http://pic1.sc.chinaz.com/files/pic/pic9/201410/apic6867.jpg",
                "http://pic.sc.chinaz.com/files/pic/pic9/201411/apic7788.jpg",};


        for (int position = 0; position < 5; position++) {
            localImages.add(getResId("spanner" + position, R.drawable.class));
        }


        //自定义Holder
        home_banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        // 设置翻页的效果，不需要翻页效果可用不设
//                .setPageTransformer(Transformer.CubeIn);
//        convenientBanner.setManualPageable(false);//设置不能手动影响


        //加载网络图片
//        networkImages = Arrays.asList(imageUrls);
//        home_banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        }, networkImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);


        //各种翻页效果
        transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        transforemerName = transformerList.get(13);
        try {
            cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
            transforemer = (ABaseTransformer) cls.newInstance();
            home_banner.getViewPager().setPageTransformer(true, transforemer);

            //部分3D特效需要调整滑动速度
            if (transforemerName.equals("StackTransformer")) {
                home_banner.setScrollDuration(3500);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }


    //获取到listview滑动的垂直距离
    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }


    private class ItemRecod {
        int height = 0;
        int top = 0;
    }


    @Override
    protected void setOnClick() {
        headerView.setOnClickListener(this);
        home_qyxx.setOnClickListener(this);
        home_laws.setOnClickListener(this);
        home_notification.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_qyxx:
                intent = new Intent(getActivity(), Enterprise.class);
                startActivity(intent);
                break;
            case R.id.home_laws:
                intent = new Intent(getActivity(), Laws.class);
                startActivity(intent);
                break;

            case R.id.home_notification:
                intent = new Intent(getActivity(), MyNotification.class);
                startActivity(intent);
                break;

        }
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "首页onResume");
        //开始自动翻页
        home_banner.startTurning(4000);
//        initCommunity();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "首页onPause");
        //停止翻页
        home_banner.stopTurning();
    }

    // 停止自动翻页
    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "首页onStop");
        //停止翻页
        home_banner.stopTurning();
    }


    //获取发布图片
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
                                    Log.e(TAG, jo + "");
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

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
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
                    initCommunity();
                    home_refresh.endRefreshing();
                    ShowToast.showShort(getActivity(), R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(getActivity(), R.string.netcantuse);
            home_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
