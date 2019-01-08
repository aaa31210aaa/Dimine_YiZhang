package community;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.CircularImage;
import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.CircleRecycleAdapter;
import adapter.HTDetailAdapter;
import bean.Circle;
import bean.CommentBean;
import bean.FavortBean;
import bean.User;
import mine.ExpandTextView;
import mine.MyFavorActivity;
import mine.MyPartActivity;
import mine.MyTopicActivity;
import okhttp3.Call;
import utils.BaseActivity;
import utils.ParseEmojiMsgUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class HTDetailActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {

    private Circle bean;
    private CommentBean ctd;
    private RecyclerView test_item_recycler;

    private View root;
    private View back;
    private TextView title;
    private View header;
    private ListView list;
    private BaseAdapter adapter;

    private LinearLayout communitypl_body;
    private EditText communitypl_etv;
    private TextView communitypl_send;
    private View foucousview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htdetail);
        initView();
        initData();
        setOnClick();
    }

    //添加长按删除
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                OkHttpUtils
                        .post()
                        .url(PortIpAddress.DeleteComment())
                        .addParams("access_token", user_token)
                        .addParams("trid", ctd.getId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                ShowToast.showShort(context, "删除评论失败，请稍后重试");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    Log.e(TAG, jo + "");
                                    String success = jo.getString("success");
                                    String error = jo.getString("errormessage");
                                    if (success.equals("true")) {
                                        bean.comments.remove(ctd);
                                        bean.setCommentNum(bean.comments.size());
                                        Message message = handler.obtainMessage();
                                        message.what = 2;
                                        handler.sendMessage(message);
                                    } else {
                                        ShowToast.showShort(context, error);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ShowToast.showShort(context, "删除评论失败，请稍后重试");
                                }
                            }
                        });
                break;
        }
        return true;
    }

    @Override
    protected void initView() {
        user_token = SharedPrefsUtil.getValue(context, "userInfo", "user_token", "");
        user_id = SharedPrefsUtil.getValue(this, "userInfo", "userid", "");
        root = getWindow().getDecorView();
        back = root.findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) root.findViewById(R.id.win_title);
        list = (ListView) root.findViewById(R.id.list);
        list.setOnCreateContextMenuListener(this);
        list.setOnItemLongClickListener(this);
        communitypl_body = (LinearLayout) findViewById(R.id.communitypl_body);
        communitypl_etv = (EditText) findViewById(R.id.communitypl_etv);
        communitypl_send = (TextView) findViewById(R.id.communitypl_send);
        communitypl_send.setEnabled(false);
    }

    @Override
    protected void initData() {
        /*ArrayList<PingLun> pls = new ArrayList<>(50);
        bean = new Circle();
        bean.setTitle("美国地产大亨特朗普：哥有87亿，哥要选总统，哥喜欢又讨厌中国");
        bean.setContent("哥有87亿，哥要选总统，哥不喜欢中国、墨西哥、奥巴马和布什。当地时间16日，身兼地产大亨、真人秀主持、“美国小姐”选美主席等多重身份的美国亿万富豪唐纳德·特朗普，在死忠粉丝、富豪好友、女儿等人的助威下，伴着摇滚歌曲的背景乐，正式宣布角逐2016年美国总统大选。在这场喧闹的新闻发布会上，他毫不含糊地传达了上述几个主要意思。 ");
        bean.setImageUrls(new String[]{"http://i.guancha.cn/news/2015/06/18/20150618120134417.jpg","http://i.guancha.cn/news/2015/06/18/20150618120628341.jpg","http://i.guancha.cn/news/2015/06/18/20150618120539165.png"});
        bean.setUsername("Donald Trump");
        bean.setSendTime("2017-04-19 20:30:50");
        bean.setCommentNum(34);
        bean.setDzNum(999);
        for (int i = 0; i < 50; i++) {
            PingLun pl = new PingLun();
            pl.content = "评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容" + i;
            pl.name = "红领巾" + (i+1);
            pl.time = "2001-03-23 24:00:00";
            pls.add(pl);
        }*/

        //bean = getIntent().getParcelableExtra("bean");
        String from = getIntent().getStringExtra("from");
        if (from == null) {
            bean = Yhbz.sCurItem;
        } else {
            if (from.equals("MyTopic"))
                bean = MyTopicActivity.sCurItem;
            else if (from.equals("MyPart"))
                bean = MyPartActivity.sCurItem;
            else
                bean = MyFavorActivity.sCurItem;
            String url = PortIpAddress.TopicImage();
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("access_token", PortIpAddress.getToken(this))
                    .addParams("tid", bean.getId())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            ShowToast.showShort(context, "获取话题图片失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                Log.e(TAG, jo + "");
                                String success = jo.getString("success");
                                String error = jo.getString("errormessage");
                                if (success.equals("true")) {
                                    JSONArray cells = jo.getJSONArray("cells");
                                    String[] imgs = new String[cells.length()];
                                    for (int i = 0; i < cells.length(); i++) {
                                        JSONObject cell = (JSONObject) cells.get(i);
                                        imgs[i] = PortIpAddress.host.replace("mobile/", "") + cell.getString("fileurl").replace("\\", "/");
                                    }
                                    bean.setImageUrls(imgs);
                                    Message message = handler.obtainMessage();
                                    message.what = 3;
                                    handler.sendMessage(message);
                                } else {
                                    ShowToast.showShort(context, error);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ShowToast.showShort(context, "获取话题图片失败");
                            }
                        }
                    });
        }

        title.setText("话题详情");
        header = View.inflate(context, R.layout.detail_item, null);
        CircularImage test_headimage = (CircularImage) header.findViewById(R.id.test_headimage);
        TextView nameTv = (TextView) header.findViewById(R.id.nameTv);
        TextView titleTv = (TextView) header.findViewById(R.id.titleTv);
        final ImageView favorIm = (ImageView) header.findViewById(R.id.care);
        final ExpandTextView contentTv = (ExpandTextView) header.findViewById(R.id.contentTv);
        test_item_recycler = (RecyclerView) header.findViewById(R.id.test_item_recycler);
        TextView timeTv = (TextView) header.findViewById(R.id.timeTv);
        ImageView comment = (ImageView) header.findViewById(R.id.comment);
        final ImageView zan = (ImageView) header.findViewById(R.id.zan);
        final TextView zan_num = (TextView) header.findViewById(R.id.zan_num);
        nameTv.setText(bean.getUsername());
        titleTv.setText(bean.getTitle());

        /**
         * 关注
         */
        if (bean.isFv_tag()) {
            favorIm.setImageResource(R.drawable.service_star_active);
        }

        //关注点击事件
        favorIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (bean.isFv_tag()) {
                    url = PortIpAddress.DelFollow();
                    bean.setFv_tag(false);
                    favorIm.setImageResource(R.drawable.service_star_normal);
                } else {
                    url = PortIpAddress.AddFollow();
                    bean.setFv_tag(true);
                    favorIm.setImageResource(R.drawable.service_star_active);
                }

                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("access_token", user_token)
                        .addParams("tid", bean.getId())
                        .addParams("followid", user_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                ShowToast.showShort(context, "关注操作失败，请稍后重试");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    Log.e(TAG, response);
                                    JSONObject jo = new JSONObject(response);
                                    String success = jo.getString("success");
                                    String error = jo.getString("errormessage");
                                    if (success.equals("true")) {
                                    } else {
                                        ShowToast.showShort(context, error);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ShowToast.showShort(context, "关注操作失败，请稍后重试");
                                }
                            }
                        });
            }
        });
        contentTv.setText(bean.getContent());
        timeTv.setText(bean.getSendTime());
        zan_num.setText(bean.getDzNum() + "");


        /**
         * 监听评论框
         */
        communitypl_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (communitypl_etv.getText().toString().trim().length() == 0) {
                    communitypl_send.setEnabled(false);
                } else {
                    communitypl_send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent pl = new Intent(HTDetailActivity.this, CommunityPl.class);
//                pl.putExtra("id", bean.getId());
//                startActivityForResult(pl, 0);

                //弹出评论框
//                communitypl_body.setVisibility(View.VISIBLE);
                communitypl_etv.setFocusable(true);
                communitypl_etv.setFocusableInTouchMode(true);
                communitypl_etv.requestFocus();
                openKeyboard();
            }
        });

        Glide
                .with(context)
                .load(bean.getHeadUrl())
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(test_headimage);
        //设置recycleView
        GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                //禁止recycleView滑动 防止滑动冲突
                return false;
            }
        };
        test_item_recycler.setLayoutManager(manager);
        test_item_recycler.setAdapter(new CircleRecycleAdapter(context, bean.getImageUrls()));
        list.addHeaderView(header);
        list.setEmptyView(root.findViewById(R.id.empty));
        adapter = new HTDetailAdapter(context, bean.comments);
        list.setAdapter(adapter);

//        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        OkHttpUtils
                .get()
                .url(PortIpAddress.LikeList())
                .addParams("access_token", user_token)
                .addParams("tid", bean.getId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.e(TAG, jo + "");
                            String success = jo.getString("success");
                            String error = jo.getString("errormessage");
                            if (success.equals("true")) {
                                bean.favorters.clear();
                                JSONArray cells = jo.getJSONArray("cells");
                                for (int i = 0; i < cells.length(); i++) {
                                    JSONObject cell = (JSONObject) cells.get(i);
                                    FavortBean f = new FavortBean();
                                    f.setUser(new User(cell.getString("fabulousid"),
                                            cell.getString("fabulousname"), cell.getString("headimageurl")));
                                    bean.favorters.add(f);
                                }
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(context, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        refreshComment();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            foucousview = getCurrentFocus();

            if (isHideInput(foucousview, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(foucousview.getWindowToken(), 0);
//                    communitypl_body.setVisibility(View.GONE);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


    //弹出评论框
    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) communitypl_etv.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                if (imm.hideSoftInputFromWindow(communitypl_etv.getWindowToken(), 0)) {
                    imm.showSoftInput(communitypl_etv, 0);
                    //软键盘已弹出
                } else {
                    //软键盘未弹出
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 100);
    }

    @Override
    protected void setOnClick() {
        communitypl_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.communitypl_send:
                String a = communitypl_etv.getText().toString();
                Log.e(TAG, a + "----");
                OkHttpUtils
                        .post()
                        .url(PortIpAddress.Comment())
                        .addParams("access_token", user_token)
                        .addParams("tid", bean.getId())
                        .addParams("reviewid", user_id)
                        .addParams("reviewcontent", ParseEmojiMsgUtil.convertToMsg(communitypl_etv.getText(), this))
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
                                        message.what = 4;
                                        handler.sendMessage(message);
                                        communitypl_etv.setText("");
//                                        communitypl_body.setVisibility(View.GONE);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (imm != null) {
                                            imm.hideSoftInputFromWindow(foucousview.getWindowToken(), 0);
                                        }
                                    } else {
                                        ShowToast.showShort(context, error);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ShowToast.showShort(context, "评论失败，请稍后重试");
                                }
                            }
                        });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            refreshComment();
    }


    /**
     * 更新评论
     */
    private void refreshComment() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.CommentList())
                .addParams("access_token", user_token)
                .addParams("tid", bean.getId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.e(TAG, jo + "");
                            String success = jo.getString("success");
                            String error = jo.getString("errormessage");
                            if (success.equals("true")) {
                                bean.comments.clear();
                                JSONArray cells = jo.getJSONArray("cells");
                                for (int i = 0; i < cells.length(); i++) {
                                    JSONObject cell = (JSONObject) cells.get(i);
                                    CommentBean comment = new CommentBean();
                                    comment.setId(cell.getString("trid"));
                                    comment.setContent(cell.getString("reviewcontent"));
                                    if (cell.getString("headimageurl").equals("")) {
                                        comment.setUser(new User(cell.getString("reviewid"), cell.getString("reviewman"), ""));
                                    } else {
                                        comment.setUser(new User(cell.getString("reviewid"), cell.getString("reviewman"), PortIpAddress.host.replace("mobile/", "") + cell.getString("headimageurl").replace("\\", "/")));
                                    }

                                    comment.setTime(cell.getString("reviewdate"));
                                    bean.comments.add(comment);
                                }
                                bean.setCommentNum(bean.comments.size());
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(context, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void refreshHeader() {
        String list = "";
        for (int i = 0; i < bean.favorters.size(); i++) {
            if (list.isEmpty())
                list = bean.favorters.get(i).getUser().getName();
            else
                list += ", " + bean.favorters.get(i).getUser().getName();
        }
        if (list.isEmpty())
            return;
        ((TextView) header.findViewById(R.id.zan_num)).setText(list);
    }

    private void refreshImage() {
        test_item_recycler.setAdapter(new CircleRecycleAdapter(context, bean.getImageUrls()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return true;
        ctd = bean.comments.get(position - 1);
        String uid = ctd.getUser().getId();
        if (!uid.isEmpty() && uid.equals(user_id))
            return false;
        return true;
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshHeader();
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    refreshImage();
                    break;
                case 4:
                    refreshComment();
                    break;
                default:
                    break;
            }
        }
    };
}
