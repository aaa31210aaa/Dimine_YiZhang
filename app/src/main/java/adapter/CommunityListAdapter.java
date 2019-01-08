package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.CircularImage;
import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bean.Circle;
import community.HTDetailActivity;
import community.Yhbz;
import mine.ExpandTextView;
import okhttp3.Call;
import utils.ListViewHolder;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class CommunityListAdapter extends BaseAdapter {
    public String TAG = getClass().getSimpleName();
    private Context context;
    private Fragment delegate;
    private List<Circle> mlist;
    private int[] image;
    private CircleRecycleAdapter adapter;
    //点赞评论
//    private SnsPopupWindow snsPopupWindow;
//    public static FavortText favortText;
//  public static LinearLayout digCommentBody;


    public CommunityListAdapter(Context context, List<Circle> mlist, Fragment delegate) {
        this.context = context;
        this.mlist = mlist;
        this.delegate = delegate;
    }



    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.community_item, null);
//            snsPopupWindow = new SnsPopupWindow(context);
        }
        final Circle bean = mlist.get(position);

        CircularImage test_headimage = ListViewHolder.get(convertView, R.id.test_headimage);
        TextView nameTv = ListViewHolder.get(convertView, R.id.nameTv);
        ImageView delIm = ListViewHolder.get(convertView, R.id.del);
        TextView titleTv = ListViewHolder.get(convertView, R.id.titleTv);
        final ExpandTextView contentTv = ListViewHolder.get(convertView, R.id.contentTv);
        RecyclerView test_item_recycler = ListViewHolder.get(convertView, R.id.test_item_recycler);
        TextView timeTv = ListViewHolder.get(convertView, R.id.timeTv);
//        ImageView snsBtn = ListViewHolder.get(convertView, R.id.snsBtn);

        ImageView comment = ListViewHolder.get(convertView, R.id.comment);
        final ImageView zan = ListViewHolder.get(convertView, R.id.zan);
        final TextView zan_num = ListViewHolder.get(convertView, R.id.zan_num);
        final TextView comment_num = ListViewHolder.get(convertView, R.id.comment_num);
        String uid = bean.getUid();
        zan_num.setText(bean.getDzNum() + "");
        comment_num.setText(bean.getCommentNum() + "");

        //点赞列表
//        favortText = ListViewHolder.get(convertView, R.id.favort_tv);

        //点赞评论布局
//        digCommentBody = ListViewHolder.get(convertView, R.id.digCommentBody);


        //监听点赞评论点击事件
//        snsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snsPopupWindow.showPopupWindow(v);
//            }
//        });

        nameTv.setText(bean.getUsername());
        delIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示")
                        .setMessage("是否确认删除这个话题？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String user_token = SharedPrefsUtil.getValue(context, "userInfo", "user_token", "");
                                OkHttpUtils
                                        .post()
                                        .url(PortIpAddress.DeleteTopic())
                                        .addParams("access_token", user_token)
                                        .addParams("tid", bean.getId())
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                e.printStackTrace();
                                                ShowToast.showShort(context, "删除话题失败，请稍后重试");
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                try {
                                                    JSONObject jo = new JSONObject(response);
                                                    Log.e(TAG, jo + "");
                                                    String success = jo.getString("success");
                                                    String error = jo.getString("errormessage");
                                                    if (success.equals("true")) {
                                                        mlist.remove(bean);
                                                        Message message = handler.obtainMessage();
                                                        message.what = 1;
                                                        handler.sendMessage(message);
                                                    } else {
                                                        ShowToast.showShort(context, error);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    ShowToast.showShort(context, "删除话题失败，请稍后重试");
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        delIm.setVisibility(View.INVISIBLE);
        if (bean.getUid() != null && !bean.getUid().isEmpty() && bean.getUid().equals(SharedPrefsUtil.getValue(context, "userInfo", "userid", ""))) {
            delIm.setVisibility(View.VISIBLE);
        }
        titleTv.setText(bean.getTitle());
        timeTv.setText(bean.getSendTime());
        contentTv.setText(bean.getContent());

        //加载图片
        Glide
                .with(context)
                .load(bean.getHeadUrl())
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(test_headimage);

        if (bean.isDz_tag()) {
            zan.setImageResource(R.drawable.zan);
        } else {
            zan.setImageResource(R.drawable.nozan);
        }

        //点赞图标点击监听
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (bean.isDz_tag()) {
                    url = PortIpAddress.Dislike();
                    //ShowToast.showShort(context, "取消点赞");
                    bean.setDzNum(bean.getDzNum() - 1);
                    zan.setImageResource(R.drawable.nozan);
                    bean.setDz_tag(false);
                    notifyDataSetChanged();
                } else {
                    url = PortIpAddress.Like();
                    //ShowToast.showShort(context, "点赞");
                    bean.setDzNum(bean.getDzNum() + 1);
                    zan.setImageResource(R.drawable.zan);
                    bean.setDz_tag(true);
                    notifyDataSetChanged();
                }
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("access_token", PortIpAddress.getToken(context))
                        .addParams("tid", bean.getId())
                        .addParams("fabulousid", SharedPrefsUtil.getValue(context, "userInfo", "userid", ""))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                ShowToast.showShort(context, "赞操作失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    Log.e(TAG, "" + jo);
                                    String success = jo.getString("success");
                                    String error = jo.getString("errormessage");
                                    if (success.equals("true")) {
                                    } else {
                                        ShowToast.showShort(context, "赞操作失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

        //监听评论图标
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HTDetailActivity.class);
                Yhbz.sCurItem = bean;
                //intent.putExtra("bean", bean);
                delegate.startActivityForResult(intent, 0);
            }
        });

        if (bean.getImageUrls() != null) {
            //设置recycleView
            GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    //禁止recycleView滑动 防止滑动冲突
                    return false;
                }
            };

            test_item_recycler.setLayoutManager(manager);
            adapter = new CircleRecycleAdapter(context, bean.getImageUrls());
            test_item_recycler.setAdapter(adapter);
        }


        return convertView;
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
