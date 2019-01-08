package job;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.WorkScoreListAdapter;
import bean.WorkScoreBean;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class WorkScore extends BaseActivity {
    private ImageView work_score_back;
    //    private StarView job_start_view;
    //选择的星星数量
    private int select_starnum;
    private static final int A = 10;
    private ListView work_score_listview;
    private List<WorkScoreBean> mDatas;
    private WorkScoreListAdapter adapter;
    private String crid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_score);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        work_score_back = (ImageView) findViewById(R.id.work_score_back);
//        job_start_view = (StarView) findViewById(R.id.job_start_view);
        work_score_listview = (ListView) findViewById(R.id.work_score_listview);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        crid = intent.getStringExtra("crid");


//        job_start_view.setmStarItemClickListener(new StarView.OnStarItemClickListener() {
//            @Override
//            public void onItemClick(View view, int pos) {
//                ShowToast.showShort(WorkScore.this, pos + 1 + "个");
//                Intent intent = new Intent(WorkScore.this, JobPjDetail.class);
//                intent.putExtra("starnum", pos + 1);
//                startActivityForResult(intent, A);
//            }
//        });

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetClzgqk())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("crid", crid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(WorkScore.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, response + "---" + crid);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                mDatas = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    mDatas.add(new WorkScoreBean(jsonArray.optJSONObject(i).getString("zzrq"), jsonArray.optJSONObject(i).getString("remark")));
                                }
                                adapter = new WorkScoreListAdapter(WorkScore.this, mDatas);
                                work_score_listview.setAdapter(adapter);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e(TAG, data.getIntExtra("selectstarnum", 0) + "");
//            //需重置后才可变化数量
//            job_start_view.resetDefaultImage();
//            job_start_view.setCurrentChoose(data.getIntExtra("selectstarnum", 0));
//            job_start_view.invalidate();
        }
    }


    @Override
    protected void setOnClick() {
        work_score_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_score_back:
                finish();
                break;
        }
    }
}
