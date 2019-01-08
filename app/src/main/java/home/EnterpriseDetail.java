package home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class EnterpriseDetail extends BaseActivity {
    private ImageView enterprise_detail_back;
    private TextView enterprise_detail_companyname;
    private TextView enterprise_detail_establishmenttime;
    private TextView enterprise_detail_legalrepresentative;
    private TextView enterprise_detail_documentcode;
    private TextView enterprise_detail_economictype;
    private TextView enterprise_detail_majorindustryareas;
    private TextView enterprise_detail_competentdepartment;
    private TextView enterprise_detail_numberofemployees;
    private TextView enterprise_detail_registeredcapital;
    private TextView enterprise_detail_enterpriseaddress;
    private TextView enterprise_detail_risklevel;
    private TextView enterprise_detail_score;
    private Button enterprise_detail_map_btn;

    private String qyid;
    private int index = 0;

    private String longitude;
    private String latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        enterprise_detail_back = (ImageView) findViewById(R.id.enterprise_detail_back);
        enterprise_detail_companyname = (TextView) findViewById(R.id.enterprise_detail_companyname);
        enterprise_detail_establishmenttime = (TextView) findViewById(R.id.enterprise_detail_establishmenttime);
        enterprise_detail_legalrepresentative = (TextView) findViewById(R.id.enterprise_detail_legalrepresentative);
        enterprise_detail_documentcode = (TextView) findViewById(R.id.enterprise_detail_documentcode);
        enterprise_detail_economictype = (TextView) findViewById(R.id.enterprise_detail_economictype);
        enterprise_detail_majorindustryareas = (TextView) findViewById(R.id.enterprise_detail_majorindustryareas);
        enterprise_detail_competentdepartment = (TextView) findViewById(R.id.enterprise_detail_competentdepartment);
        enterprise_detail_numberofemployees = (TextView) findViewById(R.id.enterprise_detail_numberofemployees);
        enterprise_detail_registeredcapital = (TextView) findViewById(R.id.enterprise_detail_registeredcapital);
        enterprise_detail_enterpriseaddress = (TextView) findViewById(R.id.enterprise_detail_enterpriseaddress);
        enterprise_detail_risklevel = (TextView) findViewById(R.id.enterprise_detail_risklevel);
        enterprise_detail_score = (TextView) findViewById(R.id.enterprise_detail_score);
        enterprise_detail_map_btn = (Button) findViewById(R.id.enterprise_detail_map_btn);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.CompanyDetailInfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        Intent intent = getIntent();
        qyid = intent.getStringExtra("clickId");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(EnterpriseDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsb = new JSONObject(response);
                            Log.e("ces", jsb + "");
                            JSONArray jsar = jsb.getJSONArray("cells");
                            //赋值
                            enterprise_detail_companyname.setText(jsar.getJSONObject(index).getString("comname"));
                            enterprise_detail_establishmenttime.setText(jsar.getJSONObject(index).getString("setupdate"));
                            enterprise_detail_legalrepresentative.setText(jsar.getJSONObject(index).getString("legalperson"));
                            enterprise_detail_documentcode.setText(jsar.getJSONObject(index).getString("certificates"));
                            enterprise_detail_economictype.setText(jsar.getJSONObject(index).getString("jytypename"));
                            enterprise_detail_majorindustryareas.setText(jsar.getJSONObject(index).getString("mainfield"));
                            enterprise_detail_competentdepartment.setText(jsar.getJSONObject(index).getString("chargedeptname"));
                            enterprise_detail_numberofemployees.setText(jsar.getJSONObject(index).getString("staffnum"));
                            enterprise_detail_registeredcapital.setText(jsar.getJSONObject(index).getString("regcapital"));
                            enterprise_detail_enterpriseaddress.setText(jsar.getJSONObject(index).getString("zcaddress"));
                            enterprise_detail_risklevel.setText(jsar.getJSONObject(index).getString("risktypename"));
                            enterprise_detail_score.setText(jsar.getJSONObject(index).getString("riskscore"));
                            longitude = jsar.getJSONObject(index).getString("longitudecoord");
                            latitude = jsar.getJSONObject(index).getString("latitudecoord");
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        enterprise_detail_back.setOnClickListener(this);
        enterprise_detail_map_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterprise_detail_back:
                finish();
                break;
            case R.id.enterprise_detail_map_btn:
                Intent intent = new Intent(this, EnterpriseMap.class);
                intent.putExtra("qyname", enterprise_detail_companyname.getText().toString());
                intent.putExtra("lng", longitude);
                intent.putExtra("lat", latitude);
                startActivity(intent);
                break;
        }
    }
}
