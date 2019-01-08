package com.example.administrator.dimine_yizhang;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewPagerAdapter;
import tab.Community;
import tab.Home;
import tab.Job;
import tab.Mine;
import utils.BaseActivity;
import utils.ShowToast;

public class MainActivity extends BaseActivity {
    private NoScrollViewPager main_viewpager;
    private TabLayout main_tablayout;

//    /**
//     * 主界面的viewpager
//     **/
//    private ViewPagerCompat mPager;
//    /**
//     * 所有fragment的集合
//     **/
//    private List<Fragment> fragments = new ArrayList<Fragment>();
//    /**
//     * 适配器
//     **/
//    private MyPagerAdapter adapter;
//
//    /**
//     * 当前所选中的栏目 默认首页(0-首页  1-执法 2-议事厅 3-我的)
//     **/
//    private int current = 0;
//    private LinearLayout ll_home;
//    private LinearLayout ll_job;
//    private LinearLayout ll_community;
//    private LinearLayout ll_mine;

    /**
     * 需要进行检测的权限数组
     */
//    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.CAMERA
//    };

    /**
     * 判断是否需要检测，防止不停的弹框
     */
//    private boolean isNeedCheck = true;
//    private static final int PERMISSON_REQUESTCODE = 0;


    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
//        setPager();
        setOnClick();
    }

    @Override
    protected void initView() {
        main_viewpager = (NoScrollViewPager) findViewById(R.id.main_viewpager);
        main_tablayout = (TabLayout) findViewById(R.id.main_tablayout);

    }

    @Override
    protected void initData() {
        //设置tab
        SetTab();

    }

    private void UpdateDiaglog() {

    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (isNeedCheck) {
//            checkPermissions(needPermissions);
//        }
    }


    private void SetTab() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Home());
        fragments.add(new Job());
        fragments.add(new Community());
        fragments.add(new Mine());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"首页", "执法", "议事厅", "我的"});

        main_viewpager.setOffscreenPageLimit(3);

        main_viewpager.setAdapter(adapter);
        //关联图文
        main_tablayout.setupWithViewPager(main_viewpager);
        main_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_viewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < main_tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = main_tablayout.getTabAt(i);
            Drawable d = null;
            switch (i) {
                case 0:
                    d = ContextCompat.getDrawable(this, R.drawable.home_tab);
                    break;
                case 1:
                    d = ContextCompat.getDrawable(this, R.drawable.job_tab);
                    break;
                case 2:
                    d = ContextCompat.getDrawable(this, R.drawable.community_tab);
                    break;
                case 3:
                    d = ContextCompat.getDrawable(this, R.drawable.mine_tab);
                    break;
            }
            tab.setIcon(d);
        }
    }


    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
