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

public class MyFavorActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static Circle sCurItem;
    private View back;
    private TextView title;
    private SwipeMenuListView mList;
    private MyTopicAdapter mAdapter;

    private String username[] = {"admin", "红领巾", "测试员", "小队长", "中队长", "大队长", "机器人", "小冰"};
    private String sendtime[] = {"2017-04-21 10:00:00", "2017-04-19 20:30:50", "2017-04-18 06:31:20", "2017-04-20 11:40:32", "2017-03-21 10:30:36", "2017-02-23 14:00:01", "2017-02-28 23:36:40", "2017-03-01 16:21:33"};
    private int image[] = {R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5, R.drawable.head6, R.drawable.head7, R.drawable.head8,};
    private String content[] = {"测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息息测试信息测试信息测试信息测试信息测试信息息测试信息测试信息测试信息测试信息测试信息", "今天天气真好啊", "真的是累啊累", "今天出去烧烤有人一起吗", "听，这里有人在唱歌", "风萧萧兮易水寒", "壮士一去兮不复还", "长使英雄泪满襟"};
    private int[] dz_arr = {10, 50, 23, 102, 32, 99, 78, 6};
    private int[] comment_arr = {50, 23, 13, 6, 110, 99, 74, 10};
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
        title.setText("我关注的话题");
        mList = (SwipeMenuListView) getWindow().getDecorView().findViewById(R.id.my_topic_list);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.MyFavorList();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        myname = SharedPrefsUtil.getValue(MyFavorActivity.this, "userInfo", "userid", "");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);

        mDatas = new ArrayList<>();
        /*for (int i = 0; i < username.length; i++) {
            Circle bean = new Circle();
            bean.setUsername(username[i]);
            bean.setTitle("关于涨价的通知");
            bean.setSendTime(DateUtils.getRencentTime(sendtime[i], "yyyy-MM-dd HH:mm:ss"));
            bean.setContent(content[i]);
            bean.setDzNum(dz_arr[i]);
            bean.setCommentNum(comment_arr[i]);
            bean.setHeadimage(image[i]);
            mDatas.add(bean);
        }*/

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
                        ShowToast.showToastNoWait(MyFavorActivity.this, R.string.network_error);
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
        in.putExtra("from", "MyFavor");
        startActivity(in);
    }
}
