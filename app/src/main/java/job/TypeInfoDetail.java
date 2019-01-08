package job;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import adapter.TypeInfoDetailAdapter;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class TypeInfoDetail extends BaseActivity {
    private ImageView typeinfo_detail_back;
    //整改情况
    private TextView typeinfo_detail_zgqk;
    //被检查单位
    private TextView typeinfo_detail_bjcdw;
    //地址
    private TextView typeinfo_detail_address;
    //法人代表
    private TextView typeinfo_detail_frdb;
    //职务
    private TextView typeinfo_detail_zw;
    //检查场所
    private TextView typeinfo_detail_jccs;
    //检查开始时间
    private TextView typeinfo_detail_startdate;
    //检查结束时间
    private TextView typeinfo_detail_enddate;
    //执法行政机关
    private TextView typeinfo_detail_zfxzjg;
    //主办人员
    private TextView typeinfo_detail_zbry;
    //主办人员证件号
    private TextView typeinfo_detail_zbryzjh;
    //协办人员
    private TextView typeinfo_detail_xbry;
    //协办人员证件号
    private TextView typeinfo_detail_xbryzjh;
    //检查描述
    private TextView typeinfo_detail_jcms;
    //是否整改
    private TextView typeinfo_detail_sfzg;
    //行政处罚分类
    private TextView typeinfo_detail_xzcffl;
    //检查单位
    private TextView typeinfo_detail_jcdw;

    private String crid;
    private RecyclerView typeinfo_detail_recycler;
    private String[] imagePaths;
    private TypeInfoDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_info_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        typeinfo_detail_back = (ImageView) findViewById(R.id.typeinfo_detail_back);
        typeinfo_detail_zgqk = (TextView) findViewById(R.id.typeinfo_detail_zgqk);
        typeinfo_detail_bjcdw = (TextView) findViewById(R.id.typeinfo_detail_bjcdw);
        typeinfo_detail_address = (TextView) findViewById(R.id.typeinfo_detail_address);
        typeinfo_detail_frdb = (TextView) findViewById(R.id.typeinfo_detail_frdb);
        typeinfo_detail_zw = (TextView) findViewById(R.id.typeinfo_detail_zw);
        typeinfo_detail_jccs = (TextView) findViewById(R.id.typeinfo_detail_jccs);
        typeinfo_detail_startdate = (TextView) findViewById(R.id.typeinfo_detail_startdate);
        typeinfo_detail_enddate = (TextView) findViewById(R.id.typeinfo_detail_enddate);
        typeinfo_detail_zfxzjg = (TextView) findViewById(R.id.typeinfo_detail_zfxzjg);
        typeinfo_detail_zbry = (TextView) findViewById(R.id.typeinfo_detail_zbry);
        typeinfo_detail_zbryzjh = (TextView) findViewById(R.id.typeinfo_detail_zbryzjh);
        typeinfo_detail_xbry = (TextView) findViewById(R.id.typeinfo_detail_xbry);
        typeinfo_detail_xbryzjh = (TextView) findViewById(R.id.typeinfo_detail_xbryzjh);
        typeinfo_detail_jcms = (TextView) findViewById(R.id.typeinfo_detail_jcms);
        typeinfo_detail_sfzg = (TextView) findViewById(R.id.typeinfo_detail_sfzg);
        typeinfo_detail_xzcffl = (TextView) findViewById(R.id.typeinfo_detail_xzcffl);
        typeinfo_detail_jcdw = (TextView) findViewById(R.id.typeinfo_detail_jcdw);
        typeinfo_detail_recycler = (RecyclerView) findViewById(R.id.typeinfo_detail_recycler);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        crid = intent.getStringExtra("crid");
        dialog = DialogUtil.createLoadingDialog(TypeInfoDetail.this, R.string.loading);
        mOkhttp();

    }

    private void mOkhttp() {
        OkHttpUtils
                .post()
                .url(PortIpAddress.GetXccheckrecorddetail())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("crid", crid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(TypeInfoDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            typeinfo_detail_bjcdw.setText(jsonArray.getJSONObject(0).getString("bjcdw"));
                            typeinfo_detail_address.setText(jsonArray.getJSONObject(0).getString("address"));
                            typeinfo_detail_frdb.setText(jsonArray.getJSONObject(0).getString("frdb"));
                            typeinfo_detail_zw.setText(jsonArray.getJSONObject(0).getString("zw"));
                            typeinfo_detail_jccs.setText(jsonArray.getJSONObject(0).getString("jccs"));
                            typeinfo_detail_startdate.setText(jsonArray.getJSONObject(0).getString("starttime"));
                            typeinfo_detail_enddate.setText(jsonArray.getJSONObject(0).getString("endtime"));
                            typeinfo_detail_zfxzjg.setText(jsonArray.getJSONObject(0).getString("zbjg"));
                            typeinfo_detail_zbry.setText(jsonArray.getJSONObject(0).getString("zbry"));
                            typeinfo_detail_zbryzjh.setText(jsonArray.getJSONObject(0).getString("zbjz"));
                            typeinfo_detail_xbry.setText(jsonArray.getJSONObject(0).getString("xbry"));
                            typeinfo_detail_xbryzjh.setText(jsonArray.getJSONObject(0).getString("xbzj"));
                            typeinfo_detail_jcms.setText(jsonArray.getJSONObject(0).getString("remark"));
                            typeinfo_detail_sfzg.setText(jsonArray.getJSONObject(0).getString("sfxyzg"));
                            typeinfo_detail_xzcffl.setText(jsonArray.getJSONObject(0).getString("xzcffl"));
                            typeinfo_detail_jcdw.setText(jsonArray.getJSONObject(0).getString("jcdwmc"));

                            JSONArray arrayFiles = jsonArray.getJSONObject(0).getJSONArray("files");
                            imagePaths = new String[arrayFiles.length()];
                            for (int i = 0; i < arrayFiles.length(); i++) {
                                String path = PortIpAddress.myUrl + arrayFiles.optJSONObject(i).getString("fileurl");
                                path = path.replaceAll("\\\\", "/");
                                imagePaths[i] = path;
                                Log.e(TAG, path);
                            }
                            typeinfo_detail_recycler.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
                            adapter = new TypeInfoDetailAdapter(TypeInfoDetail.this, imagePaths);
                            typeinfo_detail_recycler.setAdapter(adapter);

                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    protected void setOnClick() {
        typeinfo_detail_back.setOnClickListener(this);
        typeinfo_detail_zgqk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.typeinfo_detail_back:
                finish();
                break;
            case R.id.typeinfo_detail_zgqk:
                Intent intent = new Intent(TypeInfoDetail.this, WorkScore.class);
                intent.putExtra("crid", crid);
                startActivity(intent);
                break;
        }
    }
}
