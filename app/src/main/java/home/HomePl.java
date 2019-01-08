package home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import utils.BaseActivity;
import utils.ShowToast;

public class HomePl extends BaseActivity {
    private ImageView home_pl_back;
    private TextView home_pl_submit;
    private EditText home_pl_etv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pl);
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
                    ShowToast.showShort(HomePl.this, "请您填写一点宝贵的评论");
                } else {
                    ShowToast.showShort(HomePl.this, "评论完成");
                    finish();
                }

                break;

        }
    }
}
