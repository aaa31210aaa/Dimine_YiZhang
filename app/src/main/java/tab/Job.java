package tab;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.JobGridViewAdapter;
import adapter.JobListAdapter;
import bean.XcJcBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import job.TypeInfoDetail;
import job.TypeInfoList;
import okhttp3.Call;
import utils.BaseActivity;
import utils.BaseFragment;
import utils.CameraUtil;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Job extends BaseFragment implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private View view;
    //头像
//    private ImageView job_head_photo;
    //上级领导名字
//    private TextView job_leader_name;
//    //烟花爆竹
//    private LinearLayout job_yhbz;
//    //煤
//    private LinearLayout job_mei;
//    //非煤
//    private LinearLayout job_feimei;
//    //危化
//    private LinearLayout job_wh;
//    //商业工贸
//    private LinearLayout job_sygm;

    private GridView job_gridview;


    private Intent intent;

    //    private RadioGroup job_rg;
//    private RadioButton job_rb1;
//    private RadioButton job_rb2;
//    private RadioButton job_rb3;
//    private RadioButton job_rb4;
    private ListView job_listview;
    private List<XcJcBean> mDatas;
    private JobListAdapter adapter;

//    private String zgtype_arr[] = {"营业执照", "安全生产责任制", "营业执照", "安全生产责任制", "营业执照", "营业执照", "营业执照", "安全生产责任制", "营业执照", "营业执照"};
//    private String zgcontent_arr[] = {"1.有合法有效的营业执照", "1.有规定各级人员安全生产职责的制度和文件", "1.有合法有效的营业执照", "1.有合法有效的营业执照", "1.有合法有效的营业执照", "1.有合法有效的营业执照", "1.有规定各级人员安全生产职责的制度和文件", "1.有规定各级人员安全生产职责的制度和文件", "1.有合法有效的营业执照", "1.有合法有效的营业执照"};
//    private String zgdate_arr[] = {"01-12 12:03", "01-05 10:04", "01-12 10:05", "01-12 10:05", "01-12 12:03", "01-05 10:04", "01-12 10:05", "01-12 10:05", "01-12 12:03", "01-05 10:04"};

    private int NUM = 4; // 每行显示个数
    private int LIEWIDTH;//每列宽度
    private int LIE;//列数
    private List<String> tvLists;
    private JobGridViewAdapter jobAdapter;
    private List<String> industrytype;
    private BGARefreshLayout fragment_job_refresh;
    private XcJcBean bean;

    public Job() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_job, null);
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
        CameraUtil.init(getActivity());
//        job_head_photo = (ImageView) view.findViewById(R.id.job_head_photo);
//        job_leader_name = (TextView) view.findViewById(R.id.job_leader_name);
//        job_yhbz = (LinearLayout) view.findViewById(R.id.job_yhbz);
//        job_mei = (LinearLayout) view.findViewById(R.id.job_mei);
//        job_feimei = (LinearLayout) view.findViewById(R.id.job_feimei);
//        job_wh = (LinearLayout) view.findViewById(R.id.job_wh);
//        job_sygm = (LinearLayout) view.findViewById(R.id.job_sygm);
        job_gridview = (GridView) view.findViewById(R.id.job_gridview);


//        job_rg = (RadioGroup) view.findViewById(R.id.job_rg);
//        job_rb1 = (RadioButton) view.findViewById(R.id.job_rb1);
//        job_rb2 = (RadioButton) view.findViewById(R.id.job_rb2);
//        job_rb3 = (RadioButton) view.findViewById(R.id.job_rb3);
//        job_rb4 = (RadioButton) view.findViewById(R.id.job_rb4);
        job_listview = (ListView) view.findViewById(R.id.job_listview);
        fragment_job_refresh = (BGARefreshLayout) view.findViewById(R.id.fragment_job_refresh);
        MyRefreshStyle(fragment_job_refresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.GetIndustrylist();
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);
        mDatas = new ArrayList<>();
//        job_head_photo.setImageResource(R.drawable.simple_head);
//        job_rb1.setTextColor(Color.WHITE);

//      setRadiobutton();

        initGridView();
        initListView();
        MonitorList();
    }

    private void initGridView() {
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
                        Log.e(TAG, response + "---");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            tvLists = new ArrayList<String>();
                            industrytype = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvLists.add(jsonArray.optJSONObject(i).getString("industrytypename"));
                                industrytype.add(jsonArray.optJSONObject(i).getString("industrytype"));
                            }

                            job_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), TypeInfoList.class);
                                    intent.putExtra("typename", tvLists.get(position));
                                    intent.putExtra("typeid", industrytype.get(position));
                                    startActivity(intent);
                                }
                            });

                            setValue(tvLists);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        setValue();
                    }
                });
    }

    //自定义gridview
    private void setValue(List<String> lists) {
        LIEWIDTH = CameraUtil.screenWidth / NUM;
        JobGridViewAdapter adapter = new JobGridViewAdapter(getActivity(), lists);
        job_gridview.setAdapter(adapter);
        LayoutParams params = new LayoutParams(adapter.getCount() * LIEWIDTH,
                LayoutParams.WRAP_CONTENT);
        job_gridview.setLayoutParams(params);
        job_gridview.setColumnWidth(LIEWIDTH);
        job_gridview.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        job_gridview.setNumColumns(count);
    }


    /**
     * 监听整改按钮
     */
//    private void setRadiobutton() {
//        job_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == job_rb1.getId()) {
//                    job_rb1.setTextColor(Color.WHITE);
//                    job_rb2.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb3.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb4.setTextColor(Color.rgb(28, 134, 238));
//                    adapter.DataNotify(mDatas);
//                } else if (checkedId == job_rb2.getId()) {
//                    yzgDatas.clear();
//                    job_rb1.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb2.setTextColor(Color.WHITE);
//                    job_rb3.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb4.setTextColor(Color.rgb(28, 134, 238));
//                    for (ZgType entity : mDatas) {
//                        if (entity.getZgtag().contains("yzg")) {
//                            yzgDatas.add(entity);
//                            adapter.DataNotify(yzgDatas);
//                        }
//                    }
//
//                } else if (checkedId == job_rb3.getId()) {
//                    wzgDatas.clear();
//                    job_rb1.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb2.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb3.setTextColor(Color.WHITE);
//                    job_rb4.setTextColor(Color.rgb(28, 134, 238));
//                    for (ZgType entity : mDatas) {
//                        if (entity.getZgtag().contains("wzg")) {
//                            wzgDatas.add(entity);
//                            adapter.DataNotify(wzgDatas);
//                        }
//                    }
//
//                } else if (checkedId == job_rb4.getId()) {
//                    zgzDatas.clear();
//                    job_rb1.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb2.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb3.setTextColor(Color.rgb(28, 134, 238));
//                    job_rb4.setTextColor(Color.WHITE);
//                    for (ZgType entity : mDatas) {
//                        if (entity.getZgtag().contains("zgz")) {
//                            zgzDatas.add(entity);
//                            adapter.DataNotify(zgzDatas);
//                        }
//                    }
//                }
//            }
//        });
//    }


    /**
     * 初始化赋值listView
     */
    private void initListView() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetXccheckrecordlist())
                .addParams("access_token", user_token)
                .addParams("mainfield", "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            mDatas.clear();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new XcJcBean();
                                    bean.setImage(R.drawable.noimg);
                                    bean.setCrid(jsonArray.optJSONObject(i).getString("crid"));
                                    bean.setBjcdw(jsonArray.optJSONObject(i).getString("bjcdw"));
                                    bean.setStarttime(jsonArray.optJSONObject(i).getString("starttime"));
                                    bean.setRemark(jsonArray.optJSONObject(i).getString("remark"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new JobListAdapter(getActivity(), mDatas);
                                    job_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void MonitorList() {
        job_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (XcJcBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), TypeInfoDetail.class);
                intent.putExtra("crid", bean.getCrid());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initListView();
    }

    @Override
    protected void setOnClick() {
//        job_yhbz.setOnClickListener(this);
//        job_mei.setOnClickListener(this);
//        job_feimei.setOnClickListener(this);
//        job_wh.setOnClickListener(this);
//        job_sygm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.job_yhbz:
//                intent = new Intent(getActivity(), AddContent.class);
//                intent.putExtra("title", "烟花爆竹");
//                startActivity(intent);
//                break;
//            case R.id.job_mei:
//                intent = new Intent(getActivity(), AddContent.class);
//                intent.putExtra("title", "煤");
//                startActivity(intent);
//                break;
//            case R.id.job_feimei:
//                intent = new Intent(getActivity(), AddContent.class);
//                intent.putExtra("title", "非煤");
//                startActivity(intent);
//                break;
//            case R.id.job_wh:
//                intent = new Intent(getActivity(), AddContent.class);
//                intent.putExtra("title", "危化");
//                startActivity(intent);
//                break;
//            case R.id.job_sygm:
//                intent = new Intent(getActivity(), AddContent.class);
//                intent.putExtra("title", "商业工贸");
//                startActivity(intent);
//                break;


        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(getActivity())) {
            BaseActivity.sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initListView();
                    fragment_job_refresh.endRefreshing();
                    ShowToast.showShort(getActivity(), R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(getActivity(), R.string.netcantuse);
            fragment_job_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
