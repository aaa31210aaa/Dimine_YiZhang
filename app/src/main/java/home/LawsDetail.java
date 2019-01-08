package home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class LawsDetail extends BaseActivity {
    private ImageView laws_detail_back;
    //法律名称
    private TextView laws_detail_lrtitle;
    //所属行业
    private TextView laws_detail_industryname;
    //所属具体类别
    private TextView laws_detail_lrtypename;
    //文号
    private TextView laws_detail_docnumber;
    //颁布时间
    private TextView laws_detail_bbdate;
    //生效时间
    private TextView laws_detail_effdate;
    //具体内容
    private TextView laws_detail_lrdesc;
    private Intent intent;
    private String flfgid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        laws_detail_back = (ImageView) findViewById(R.id.laws_detail_back);
        laws_detail_lrtitle = (TextView) findViewById(R.id.laws_detail_lrtitle);
        laws_detail_industryname = (TextView) findViewById(R.id.laws_detail_industryname);
        laws_detail_lrtypename = (TextView) findViewById(R.id.laws_detail_lrtypename);
        laws_detail_docnumber = (TextView) findViewById(R.id.laws_detail_docnumber);
        laws_detail_bbdate = (TextView) findViewById(R.id.laws_detail_bbdate);
        laws_detail_effdate = (TextView) findViewById(R.id.laws_detail_effdate);
        laws_detail_lrdesc = (TextView) findViewById(R.id.laws_detail_lrdesc);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        flfgid = intent.getStringExtra("clickflfgid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetLawregulationsdetail())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("flfgid", flfgid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(LawsDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            String success = jsonObject.getString("success");
                            String error = jsonObject.getString("errormessage");
                            if (success.equals("true")) {
                                laws_detail_lrtitle.setText(jsonArray.optJSONObject(0).getString("lrtitle"));
                                laws_detail_industryname.setText("所属行业：" + jsonArray.optJSONObject(0).getString("industryname"));
                                laws_detail_lrtypename.setText("所属具体类别：" + jsonArray.optJSONObject(0).getString("lrtypename"));
                                laws_detail_docnumber.setText("文号：" + jsonArray.optJSONObject(0).getString("docnumber"));
                                laws_detail_bbdate.setText("颁布时间：" + jsonArray.optJSONObject(0).getString("bbdate"));
                                laws_detail_effdate.setText("生效时间：" + jsonArray.optJSONObject(0).getString("effdate"));
                                laws_detail_lrdesc.setText("法律法规信息：" + "\n" + jsonArray.optJSONObject(0).getString("lrdesc"));
                            } else {
                                ShowToast.showShort(LawsDetail.this, error);
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
        laws_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laws_detail_back:
                finish();
                break;
        }
    }
}
