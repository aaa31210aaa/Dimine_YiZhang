package job;

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
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.SearchQyAdapter;
import bean.SearchQyBean;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class SearchQy extends BaseActivity {
    private ImageView search_qy_back;
    private TextView search_qy_submit;
    private EditText search_qy_etv;
    private ImageView search_qy_clear;
    private ListView search_qy_list;
    private List<SearchQyBean> mDatas;
    private SearchQyAdapter adapter;
    private SearchQyBean bean;
    public static int RESULT_SEARCHQY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qy);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        search_qy_back = (ImageView) findViewById(R.id.search_qy_back);
        search_qy_submit = (TextView) findViewById(R.id.search_qy_submit);
        search_qy_etv = (EditText) findViewById(R.id.search_qy_etv);
        search_qy_clear = (ImageView) findViewById(R.id.search_qy_clear);
        search_qy_list = (ListView) findViewById(R.id.search_qy_list);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        MonitorEtv();
    }

    private void MonitorEtv() {
        search_qy_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    search_qy_clear.setVisibility(View.VISIBLE);
                } else {
                    search_qy_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void mOkhttp(String qyname) {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetSelectQyname())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("qyname", qyname)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(SearchQy.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            String success = jsonObject.getString("success");
                            String error = jsonObject.getString("errormessage");
                            mDatas.clear();
                            if (success.equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new SearchQyBean();
                                    bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new SearchQyAdapter(SearchQy.this, mDatas);
                                    search_qy_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                                search_qy_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        bean = (SearchQyBean) parent.getItemAtPosition(position);
                                        Intent intent = new Intent();
                                        intent.putExtra("qyname", bean.getQyname());
                                        intent.putExtra("qyid", bean.getQyid());
                                        setResult(RESULT_SEARCHQY, intent);
                                        finish();
                                    }
                                });

                            } else {
                                ShowToast.showShort(SearchQy.this, error);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        search_qy_back.setOnClickListener(this);
        search_qy_submit.setOnClickListener(this);
        search_qy_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_qy_back:
                finish();
                break;
            case R.id.search_qy_submit:
                dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
                mOkhttp(search_qy_etv.getText().toString());
                break;
            case R.id.search_qy_clear:
                search_qy_etv.setText("");
                search_qy_clear.setVisibility(View.GONE);
                break;
        }
    }
}
