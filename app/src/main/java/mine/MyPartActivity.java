package mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyTopicAdapter;
import bean.Circle;
import community.HTDetailActivity;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class MyPartActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static Circle sCurItem;
    private View back;
    private TextView title;
    private SwipeMenuListView mList;
    private MyTopicAdapter mAdapter;

    private String myname;
    private List<Circle> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topic);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        back = getWindow().getDecorView().findViewById(R.id.modify_password_back);
        title = (TextView) getWindow().getDecorView().findViewById(R.id.win_title);
        title.setText("我参与的话题");
        mList = (SwipeMenuListView) getWindow().getDecorView().findViewById(R.id.my_topic_list);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.WeJoinlist();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        myname = SharedPrefsUtil.getValue(MyPartActivity.this, "userInfo", "userid", "");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);

        mDatas = new ArrayList<>();

        mAdapter = new MyTopicAdapter(this, mDatas);
        mList.setAdapter(mAdapter);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("releasename", myname)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(MyPartActivity.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.e(TAG, jo + "");
                            String success = jo.getString("success");
                            String error = jo.getString("errormessage");
                            if (success.equals("true")) {
                                mDatas.clear();
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
                                    c.setHeadUrl(PortIpAddress.host.replace("mobile/", "") +cell.getString("headimageurl").replace("\\","/"));
                                    c.setDz_tag(cell.getInt("isfabulous") == 0 ? false : true);
                                    c.setFv_tag(cell.getInt("isfollow") == 0 ? false : true);
                                    mDatas.add(c);
                                }
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(context, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast.showShort(context, "返回数据错误");
                        }
                    }
                });

    }

    @Override
    protected void setOnClick() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_password_back:
                finish();
                break;
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent in = new Intent(context, HTDetailActivity.class);
        sCurItem = mDatas.get(position);
        in.putExtra("from", "MyPart");
        startActivity(in);
    }
}
