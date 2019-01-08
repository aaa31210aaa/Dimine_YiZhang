package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.CircularImage;
import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.CommentBean;
import utils.ListViewHolder;

/**
 * Created by dengliang on 4/27/17.
 */
public class HTDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentBean> mPingLun;

    @Override
    public boolean isEmpty() {
        return false;
    }

    public HTDetailAdapter(Context context, List<CommentBean> pl) {
        mContext = context;
        mPingLun = pl;
    }

    @Override
    public int getCount() {
        return mPingLun==null?0:mPingLun.size();
    }

    @Override
    public Object getItem(int position) {
        return mPingLun.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.pinglun_list_item, null);
        }
        final CommentBean pl = mPingLun.get(position);
        CircularImage head = ListViewHolder.get(convertView, R.id.test_headimage);
        TextView name = ListViewHolder.get(convertView, R.id.nameTv);
        TextView comment = ListViewHolder.get(convertView, R.id.commentTv);
        TextView time = ListViewHolder.get(convertView, R.id.timeTv);
        Glide
                .with(mContext)
                .load(pl.getUser().getHeadUrl())
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(head);
        name.setText(pl.getUser().getName());
        comment.setText(pl.getContent());
        time.setText(pl.getTime());
        return convertView;
    }
}
