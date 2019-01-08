package job;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.TypeInfoListAdapter;
import bean.XcJcBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.NetUtils;
import utils.PortIpAddress;
import utils.ShowToast;

public class TypeInfoList extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView typeinfo_back;
    private TextView typeinfo_name;
    private ListView typeinfo_list;
    private List<XcJcBean> mDatas;
    private String typename;
    private String typeid;
    private TypeInfoListAdapter adapter;
    private ImageView typeinfo_add;
    private boolean first = true;
    private BGARefreshLayout typeinfo_refresh;
    private XcJcBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_info);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        typeinfo_back = (ImageView) findViewById(R.id.typeinfo_back);
        typeinfo_name = (TextView) findViewById(R.id.typeinfo_name);
        typeinfo_list = (ListView) findViewById(R.id.typeinfo_list);
        typeinfo_add = (ImageView) findViewById(R.id.typeinfo_add);
        typeinfo_refresh = (BGARefreshLayout) findViewById(R.id.typeinfo_refresh);
        MyRefreshStyle(typeinfo_refresh);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        typename = intent.getStringExtra("typename");
        typeid = intent.getStringExtra("typeid");
        typeinfo_name.setText(typename);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<XcJcBean>();
        mOkhttp();
        MonitorList();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetXccheckrecordlist())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("mainfield", typeid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(TypeInfoList.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, response + "---" + typeid);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            mDatas.clear();
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
                                adapter = new TypeInfoListAdapter(TypeInfoList.this, mDatas);
                                typeinfo_list.setAdapter(adapter);
                            } else {
                                adapter.DataNotify(mDatas);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void MonitorList() {
        typeinfo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (XcJcBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(TypeInfoList.this, TypeInfoDetail.class);
                intent.putExtra("crid", bean.getCrid());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void setOnClick() {
        typeinfo_back.setOnClickListener(this);
        typeinfo_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.typeinfo_back:
                finish();
                break;
            case R.id.typeinfo_add:
                Intent intent = new Intent(this, AddType.class);
                intent.putExtra("typename", typename);
                startActivityForResult(intent, 10);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, data + "---" + resultCode);
        if (resultCode == RESULT_OK) {
            mOkhttp();
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
                    typeinfo_refresh.endRefreshing();
                    ShowToast.showShort(TypeInfoList.this, R.string.refreshed);
                }
            }, BaseActivity.LOADING_REFRESH);

        } else {
            ShowToast.showShort(TypeInfoList.this, R.string.netcantuse);
            typeinfo_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
