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

import com.example.administrator.dimine_yizhang.MyNotificationDetail;
import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.NotificationAdapter;
import bean.TzGg;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class MyNotification extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView notification_back;
    private EditText notification_search;
    private ImageView notification_clear;
    private ListView notification_list;
    private List<TzGg> mDatas;
    private List<TzGg> searchDatas;
    private NotificationAdapter adapter;
    private String readUrl;
    private BGARefreshLayout notification_refresh;
    private BGARefreshLayout notification_nodatarefresh;
    private TzGg bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        notification_back = (ImageView) findViewById(R.id.notification_back);
        notification_search = (EditText) findViewById(R.id.notification_search);
        notification_clear = (ImageView) findViewById(R.id.notification_clear);
        notification_list = (ListView) findViewById(R.id.notification_list);
        notification_refresh = (BGARefreshLayout) findViewById(R.id.notification_refresh);
        notification_nodatarefresh = (BGARefreshLayout) findViewById(R.id.notification_nodatarefresh);
        MyRefreshStyle(notification_refresh);
        MyRefreshStyle(notification_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.MessageList();
        readUrl = PortIpAddress.MessageDetail();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", null);
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
                .addParams("userid", userid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(MyNotification.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsb = new JSONObject(response);
                            JSONArray jsonArray = jsb.getJSONArray("cells");
                            Log.e(TAG, jsb + "");
                            if (jsonArray.length() > 0) {
                                notification_refresh.setVisibility(View.VISIBLE);
                                notification_nodatarefresh.setVisibility(View.GONE);
                                mDatas = new ArrayList<TzGg>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new TzGg();
                                    bean.setTitle(jsonArray.optJSONObject(i).getString("noticetitle"));
                                    bean.setTzid(jsonArray.optJSONObject(i).getString("nid"));
                                    bean.setDate(jsonArray.optJSONObject(i).getString("sentdate"));
                                    bean.setUnread(jsonArray.optJSONObject(i).getString("readtype"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new NotificationAdapter(MyNotification.this, mDatas);
                                    notification_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                notification_refresh.setVisibility(View.GONE);
                                notification_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void MonitorList() {
        notification_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (TzGg) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyNotification.this, MyNotificationDetail.class);
                intent.putExtra("nid", bean.getTzid());
                ReadNotification(bean.getTzid());
                startActivity(intent);
            }
        });
    }

    private void ReadNotification(String nid) {
        OkHttpUtils
                .get()
                .url(readUrl)
                .addParams("nid", nid)
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
                    }
                });
    }


    private void MonitorEditext() {
        //监听edittext
        notification_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (notification_search.length() > 0) {
                        notification_clear.setVisibility(View.VISIBLE);
                        search(notification_search.getText().toString().trim());
                    } else {
                        notification_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (notification_search.length() > 0) {
                        notification_clear.setVisibility(View.VISIBLE);
                    } else {
                        notification_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<TzGg>();
            for (TzGg entity : mDatas) {
                try {
                    if (entity.getTitle().contains(str) || entity.getContent().contains(str) || entity.getDate().contains(str)) {
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
        notification_back.setOnClickListener(this);
        notification_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_back:
                finish();
                break;
            case R.id.notification_clear:
                notification_search.setText("");
                notification_clear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mOkhttp();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(this)) {
            BaseActivity.sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOkhttp();
                    notification_refresh.endRefreshing();
                    notification_nodatarefresh.endRefreshing();
                    ShowToast.showShort(MyNotification.this, R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(MyNotification.this, R.string.netcantuse);
            notification_refresh.endRefreshing();
            notification_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
