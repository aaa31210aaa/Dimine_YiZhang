package community;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class CommunityPl extends BaseActivity {
    private ImageView home_pl_back;
    private TextView home_pl_submit;
    private EditText home_pl_etv;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_pl);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        home_pl_back = (ImageView) findViewById(R.id.home_pl_back);
        home_pl_submit = (TextView) findViewById(R.id.home_pl_submit);
        home_pl_etv = (EditText) findViewById(R.id.home_pl_etv);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void setOnClick() {
        home_pl_back.setOnClickListener(this);
        home_pl_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_pl_back:
                finish();
                break;
            case R.id.home_pl_submit:
                if (home_pl_etv.getText().toString().trim().equals("")) {
                    ShowToast.showShort(CommunityPl.this, "请您填写一点宝贵的评论");
                } else {
                    user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", "");
                    String user_id = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
                    OkHttpUtils
                            .post()
                            .url(PortIpAddress.Comment())
                            .addParams("access_token", user_token)
                            .addParams("tid", id)
                            .addParams("reviewid", user_id)
                            .addParams("reviewcontent", home_pl_etv.getText().toString().trim())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    ShowToast.showShort(context, "评论失败，请稍后重试");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jo = new JSONObject(response);
                                        Log.e(TAG, jo + "");
                                        String success = jo.getString("success");
                                        String error = jo.getString("errormessage");
                                        if (success.equals("true")) {
                                            Message message = handler.obtainMessage();
                                            message.what = 1;
                                            handler.sendMessage(message);
                                        } else {
                                            ShowToast.showShort(context, error);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ShowToast.showShort(context, "评论失败，请稍后重试");
                                    }
                                }
                            });
                }

                break;

        }
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
