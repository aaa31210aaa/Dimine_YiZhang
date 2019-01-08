package home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.LawsAdapter;
import bean.LawsBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Laws extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView laws_back;
    private ListView laws_listview;
    private List<LawsBean> mDatas;
    private List<LawsBean> searchDatas;
    private LawsAdapter adapter;
    private String url;
    private String user_token;
    private EditText laws_search;
    private ImageView laws_clear;
    private BGARefreshLayout laws_refresh;
    private BGARefreshLayout laws_nodatarefresh;
    private LawsBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        laws_back = (ImageView) findViewById(R.id.laws_back);
        laws_search = (EditText) findViewById(R.id.laws_search);
        laws_clear = (ImageView) findViewById(R.id.laws_clear);
        laws_listview = (ListView) findViewById(R.id.laws_listview);
        laws_refresh = (BGARefreshLayout) findViewById(R.id.laws_refresh);
        laws_nodatarefresh  = (BGARefreshLayout) findViewById(R.id.laws_nodatarefresh);
        MyRefreshStyle(laws_refresh);
        MyRefreshStyle(laws_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.GetLawregulations();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
        MonitorList();
        MonitorEditext();
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
                        dialog.dismiss();
                        ShowToast.showToastNoWait(Laws.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                laws_refresh.setVisibility(View.VISIBLE);
                                laws_nodatarefresh.setVisibility(View.GONE);
                                mDatas = new ArrayList<LawsBean>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new LawsBean();
//                                  bean.setIndustry(jsonArray.optJSONObject(i).getString(""));
                                    bean.setLrtitle(jsonArray.optJSONObject(i).getString("lrtitle"));
                                    bean.setFlfgid(jsonArray.optJSONObject(i).getString("flfgid"));
                                    bean.setIndustry(jsonArray.optJSONObject(i).getString("industryname"));
                                    bean.setLrtypename(jsonArray.optJSONObject(i).getString("lrtypename"));
                                    bean.setLrdesc(jsonArray.optJSONObject(i).getString("lrdesc"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new LawsAdapter(Laws.this, mDatas);
                                    laws_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                laws_refresh.setVisibility(View.GONE);
                                laws_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        laws_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (LawsBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(Laws.this, LawsDetail.class);
                intent.putExtra("clickflfgid", bean.getFlfgid());
                startActivity(intent);
            }
        });
    }


    private void MonitorEditext() {
        //监听edittext
        laws_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (laws_search.length() > 0) {
                        laws_clear.setVisibility(View.VISIBLE);
                        search(laws_search.getText().toString().trim());
                    } else {
                        laws_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (laws_search.length() > 0) {
                        laws_clear.setVisibility(View.VISIBLE);
                    }else{
                        laws_clear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //搜索框
    private void search(String str) {
        if (mDatas != null) {
            searchDatas = new ArrayList<LawsBean>();
            for (LawsBean entity : mDatas) {
                try {
                    if (entity.getLrtitle().contains(str) || entity.getIndustry().contains(str) || entity.getLrtypename().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.DataNotify(searchDatas);
            }
        }
    }


    @Override
    protected void setOnClick() {
        laws_back.setOnClickListener(this);
        laws_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laws_back:
                finish();
                break;
            case R.id.laws_clear:
                laws_search.setText("");
                laws_clear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(this)) {
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOkhttp();
                    laws_refresh.endRefreshing();
                    laws_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Laws.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            laws_refresh.endRefreshing();
            laws_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
