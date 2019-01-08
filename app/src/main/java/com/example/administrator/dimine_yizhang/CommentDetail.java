package com.example.administrator.dimine_yizhang;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mine.ExpandTextView;
import mine.SnsPopupWindow;
import utils.BaseActivity;

public class CommentDetail extends BaseActivity {
    private TextView comment_detail_finish;
    //头像
    private CircularImage comment_detail_headimg;
    //名字
    private TextView comment_detail_username;
    //发布的内容
    private ExpandTextView comment_detail_content;
    //发布的图片
    private RecyclerView comment_detail_recycler;
    //发布的时间
    private TextView comment_detail_time;
    //点赞评论的按钮
    private ImageView comment_detail_snsBtn;
    private SnsPopupWindow snsPopupWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        comment_detail_finish = (TextView) findViewById(R.id.comment_detail_finish);
        comment_detail_headimg = (CircularImage) findViewById(R.id.comment_detail_headimg);
        comment_detail_username = (TextView) findViewById(R.id.comment_detail_username);
        comment_detail_content = (ExpandTextView) findViewById(R.id.comment_detail_content);
        comment_detail_recycler = (RecyclerView) findViewById(R.id.comment_detail_recycler);
        comment_detail_time = (TextView) findViewById(R.id.comment_detail_time);
        comment_detail_snsBtn = (ImageView) findViewById(R.id.comment_detail_snsBtn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        comment_detail_finish.setOnClickListener(this);
        comment_detail_snsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_detail_finish:
                finish();
                break;
            case R.id.comment_detail_snsBtn:

                break;
        }
    }
}
