package job;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;
import com.example.administrator.dimine_yizhang.StarView;

import utils.BaseActivity;
import utils.ShowToast;

public class JobPjDetail extends BaseActivity {
    private ImageView job_pj_detail_back;
    private TextView job_pj_detail_submit;
    private StarView job_pj_detail_starview;
    private EditText job_pj_detail_pl_opinion;
    private int starnum;
    private int endSelectStarnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_pj_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        job_pj_detail_back = (ImageView) findViewById(R.id.job_pj_detail_back);
        job_pj_detail_submit = (TextView) findViewById(R.id.job_pj_detail_submit);
        job_pj_detail_starview = (StarView) findViewById(R.id.job_pj_detail_starview);
        job_pj_detail_pl_opinion = (EditText) findViewById(R.id.job_pj_detail_pl_opinion);
    }

    @Override
    protected void initData() {
        Intent intent2 = getIntent();
        starnum = intent2.getIntExtra("starnum", starnum);
        endSelectStarnum = starnum;
        job_pj_detail_starview.setCurrentChoose(starnum);

        job_pj_detail_starview.setmStarItemClickListener(new StarView.OnStarItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                endSelectStarnum = pos + 1;
            }
        });
    }


    @Override
    protected void setOnClick() {
        job_pj_detail_back.setOnClickListener(this);
        job_pj_detail_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.job_pj_detail_back:
                finish();
                break;
            case R.id.job_pj_detail_submit:
                Intent intent = new Intent();
                intent.putExtra("selectstarnum", endSelectStarnum);
                setResult(RESULT_OK, intent);
                ShowToast.showShort(JobPjDetail.this, "提交成功");
                finish();
                break;
        }
    }
}
