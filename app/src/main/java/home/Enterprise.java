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

import adapter.EnterpriseAdapter;
import bean.QyXx;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Enterprise extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView enterprise_back;
    private EditText enterprise_search;
    private ImageView enterprise_clear;
    private ListView enterprise_list;
    private List<QyXx> mDatas;
    private List<QyXx> searchDatas;
    private EnterpriseAdapter adapter;
    private String url;
    private String user_token;
    private List<String> qynames;
    private BGARefreshLayout enterprise_refresh;
    private BGARefreshLayout enterprise_nodatarefresh;
    private QyXx bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        enterprise_back = (ImageView) findViewById(R.id.enterprise_back);
        enterprise_search = (EditText) findViewById(R.id.enterprise_search);
        enterprise_clear = (ImageView) findViewById(R.id.enterprise_clear);
        enterprise_list = (ListView) findViewById(R.id.enterprise_list);
        enterprise_refresh = (BGARefreshLayout) findViewById(R.id.enterprise_refresh);
        enterprise_nodatarefresh = (BGARefreshLayout) findViewById(R.id.enterprise_nodatarefresh);
        MyRefreshStyle(enterprise_refresh);
        MyRefreshStyle(enterprise_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.Companyinfo();
        dialog = DialogUtil.createLoadingDialog(Enterprise.this, R.string.loading);
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
                        ShowToast.showToastNoWait(Enterprise.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsar = jsonObject.getJSONArray("cells");
                            if (jsar.length() > 0) {
                                enterprise_refresh.setVisibility(View.VISIBLE);
                                enterprise_nodatarefresh.setVisibility(View.GONE);
                                mDatas = new ArrayList<QyXx>();
                                qynames = new ArrayList<String>();
                                for (int i = 0; i < jsar.length(); i++) {
                                    bean = new QyXx();
                                    bean.setQyId(jsar.optJSONObject(i).getString("qyid"));
                                    bean.setQyName(jsar.optJSONObject(i).getString("comname"));
                                    bean.setJgBm(jsar.optJSONObject(i).getString("chargedeptname"));
                                    bean.setQyAddress(jsar.optJSONObject(i).getString("zcaddress"));
                                    qynames.add(bean.getQyName());
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new EnterpriseAdapter(Enterprise.this, mDatas);
                                    enterprise_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                enterprise_refresh.setVisibility(View.GONE);
                                enterprise_nodatarefresh.setVisibility(View.VISIBLE);
                            }

                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * list子项点击事件
     */
    private void MonitorList() {
        enterprise_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (QyXx) parent.getItemAtPosition(position);
                Intent intent = new Intent(Enterprise.this, EnterpriseDetail.class);
                intent.putExtra("clickId", bean.getQyId());
                startActivity(intent);
            }
        });
    }

    /**
     * 监听Editext
     */
    private void MonitorEditext() {
        //监听edittext
        enterprise_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (enterprise_search.length() > 0) {
                        enterprise_clear.setVisibility(View.VISIBLE);
                        search(enterprise_search.getText().toString().trim());
                    } else {
                        enterprise_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (enterprise_search.length() > 0) {
                        enterprise_clear.setVisibility(View.VISIBLE);
                    } else {
                        enterprise_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<QyXx>();
            for (QyXx entity : mDatas) {
                try {
                    if (entity.getQyName().contains(str) || entity.getJgBm().contains(str) || entity.getQyAddress().contains(str)) {
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
        enterprise_back.setOnClickListener(this);
        enterprise_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterprise_back:
                finish();
                break;
            case R.id.enterprise_clear:
                enterprise_search.setText("");
                enterprise_clear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(this)) {
            BaseActivity.sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOkhttp();
                    enterprise_refresh.endRefreshing();
                    enterprise_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Enterprise.this, R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(Enterprise.this, R.string.netcantuse);
            enterprise_refresh.endRefreshing();
            enterprise_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
