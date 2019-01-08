package tab;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.SimpleFragmentPagerAdapter;
import community.AddTopic;
import community.Yhbz;
import okhttp3.Call;
import utils.BaseFragment;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Community extends BaseFragment {
    private View view;
    private ImageView btn;
    private TabLayout community_tablayout;
    private ViewPager community_viewpager;

    //定义要装fragment的列
    private List<Fragment> list_fragment = new ArrayList(10);
    private List<String> list_id = new ArrayList(10);
    //tab名称列表
    private List<String> list_title = new ArrayList(10);

    //烟花爆竹社区
    private Yhbz yhbz;

    public Community() {
        // Required empty public constructor
    }


    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_community, null);
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void loadData() {
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        btn = (ImageView) view.findViewById(R.id.community_add);
        community_tablayout = (TabLayout) view.findViewById(R.id.community_tablayout);
        community_viewpager = (ViewPager) view.findViewById(R.id.community_viewpager);
        //SetTablayout();
    }

    @Override
    protected void initData() {
        url = PortIpAddress.IndustryCat();
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            String success = jsonObject.getString("success");
                            String error = jsonObject.getString("errormessage");
                            list_fragment.clear();
                            list_title.clear();
                            if (success.equals("true")) {
                                JSONArray cells = jsonObject.getJSONArray("cells");
                                for (int i = 0; i < cells.length(); i++) {
                                    JSONObject cell = (JSONObject) cells.get(i);
                                    list_title.add(cell.getString("industrytypename"));
                                    list_id.add(cell.getString("industrytype"));
                                    Yhbz f = new Yhbz();
                                    f.setId(cell.getString("industrytype"));
                                    list_fragment.add(f);
                                }
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(getActivity(), error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast.showShort(getActivity(), R.string.network_error);
                        }
                    }
                });
        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading_write);
    }


    //设置tablayout
    public void SetTablayout() {
        for (int i = 0; i < list_title.size(); i++) {
            community_tablayout.addTab(community_tablayout.newTab().setText(list_title.get(i)));
        }
        community_tablayout.setTabMode(TabLayout.MODE_FIXED);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getFragmentManager(), list_fragment, list_title);
        community_viewpager.setAdapter(adapter);
        community_tablayout.setupWithViewPager(community_viewpager);
    }

    @Override
    protected void setOnClick() {
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_add:
                int i = community_tablayout.getSelectedTabPosition();
                Yhbz f = (Yhbz) list_fragment.get(i);
                Intent in = new Intent(getActivity(), AddTopic.class);
                in.putExtra("id", f.id);
                //ShowToast.showShort(getActivity(), f.id);
                startActivityForResult(in, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Yhbz f = (Yhbz) list_fragment.get(community_tablayout.getSelectedTabPosition());
            f.refresh();
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    SetTablayout();
                    break;
                default:
                    break;
            }
        }
    };
}
