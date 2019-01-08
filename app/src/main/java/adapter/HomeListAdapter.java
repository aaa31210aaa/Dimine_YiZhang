package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.CircularImage;
import com.example.administrator.dimine_yizhang.R;

import java.util.List;
import java.util.Map;

import bean.Circle;
import community.HTDetailActivity;
import mine.ExpandTextView;
import utils.ListViewHolder;
import utils.ShowToast;

public class HomeListAdapter extends BaseAdapter {
    private Context context;
    private List<Circle> mlist;
    private int[] image;
    private CircleRecycleAdapter adapter;
    private Map<Integer, String[]> imageMap;
    //点赞评论
//    private SnsPopupWindow snsPopupWindow;
//    public static FavortText favortText;
//  public static LinearLayout digCommentBody;


    public HomeListAdapter(Context context, List<Circle> mlist, Map<Integer, String[]> imageMap) {
        this.context = context;
        this.mlist = mlist;
        this.imageMap = imageMap;
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
            convertView = View.inflate(context, R.layout.test_item, null);
//            snsPopupWindow = new SnsPopupWindow(context);
        }
        final Circle bean = mlist.get(position);
        CircularImage test_headimage = ListViewHolder.get(convertView, R.id.test_headimage);
        TextView nameTv = ListViewHolder.get(convertView, R.id.nameTv);
        ExpandTextView contentTv = ListViewHolder.get(convertView, R.id.contentTv);
        RecyclerView test_item_recycler = ListViewHolder.get(convertView, R.id.test_item_recycler);
        TextView timeTv = ListViewHolder.get(convertView, R.id.timeTv);
//        ImageView snsBtn = ListViewHolder.get(convertView, R.id.snsBtn);

        ImageView comment = ListViewHolder.get(convertView,R.id.comment);
        final ImageView zan = ListViewHolder.get(convertView, R.id.zan);
        final TextView zan_num = ListViewHolder.get(convertView, R.id.zan_num);
        final TextView comment_num = ListViewHolder.get(convertView, R.id.comment_num);


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
        timeTv.setText(bean.getSendTime());
        contentTv.setText(bean.getContent());

        //加载图片
        Glide
                .with(context)
                .load(bean.getHeadimage())
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
                if (bean.isDz_tag()) {
                    ShowToast.showShort(context, "取消点赞");
                    bean.setDzNum(bean.getDzNum() - 1);
                    zan.setImageResource(R.drawable.nozan);
                    bean.setDz_tag(false);
                    notifyDataSetChanged();
                } else {
                    ShowToast.showShort(context, "点赞");
                    bean.setDzNum(bean.getDzNum() + 1);
                    zan.setImageResource(R.drawable.zan);
                    bean.setDz_tag(true);
                    notifyDataSetChanged();
                }
            }
        });

        //监听评论图标
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, HTDetailActivity.class);
                context.startActivity(intent);
            }
        });



        //设置recycleView
        GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                //禁止recycleView滑动 防止滑动冲突
                return false;
            }
        };

        test_item_recycler.setLayoutManager(manager);
        adapter = new CircleRecycleAdapter(context, imageMap.get(position));
        test_item_recycler.setAdapter(adapter);

        return convertView;
    }
}
