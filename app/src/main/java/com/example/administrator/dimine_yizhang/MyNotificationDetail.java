package com.example.administrator.dimine_yizhang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class MyNotificationDetail extends BaseActivity {
    private ImageView notification_detail_back;
    //消息详情标题
    private TextView notification_detail_tilte;
    //消息详情时间
    private TextView notification_detail_time;
    //消息详情编号
    private TextView notification_detail_noticecoid;
    //信息详情类型
    private TextView notification_detail_noticetypename;

    //消息详情内容
    private TextView notification_detail_content;
    private String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        notification_detail_back = (ImageView) findViewById(R.id.notification_detail_back);
        notification_detail_tilte = (TextView) findViewById(R.id.notification_detail_tilte);
        notification_detail_time = (TextView) findViewById(R.id.notification_detail_time);
        notification_detail_noticecoid = (TextView) findViewById(R.id.notification_detail_noticecoid);
        notification_detail_noticetypename = (TextView) findViewById(R.id.notification_detail_noticetypename);
        notification_detail_content = (TextView) findViewById(R.id.notification_detail_noticecontent);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.MessageDetail();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        Intent intent = getIntent();
        nid = intent.getStringExtra("nid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("nid", nid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(MyNotificationDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            notification_detail_tilte.setText(jsonObject.getString("noticetitle"));
                            notification_detail_time.setText(jsonObject.getString("addtime"));
                            notification_detail_noticecoid.setText(jsonObject.getString("noticecode"));
                            notification_detail_noticetypename.setText(jsonObject.getString("noticetypename"));
                            notification_detail_content.setText(jsonObject.getString("noticecontent"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
    }


    @Override
    protected void setOnClick() {
        notification_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_detail_back:
                finish();
                break;
        }
    }
}
