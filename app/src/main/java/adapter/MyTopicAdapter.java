package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.Circle;
import utils.ListViewHolder;

public class MyTopicAdapter extends BaseAdapter {
    private Context mContext;
    private List<Circle> mTopics;

    public MyTopicAdapter(Context context, List<Circle> topics) {
        mContext = context;
        mTopics = topics;
    }

    @Override
    public int getCount() {
        return mTopics.size();
    }

    @Override
    public Object getItem(int position) {
        return mTopics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.mytopic_list_item, null);
        }
        final Circle bean = mTopics.get(position);
        TextView title = ListViewHolder.get(convertView, R.id.title);
        TextView name = ListViewHolder.get(convertView, R.id.name);
        TextView time = ListViewHolder.get(convertView, R.id.timeTv);
        TextView zan = ListViewHolder.get(convertView, R.id.zan_num);
        TextView pl = ListViewHolder.get(convertView, R.id.comment_num);

        title.setText(bean.getTitle());
        name.setText(bean.getUsername());
        time.setText(bean.getSendTime());
        zan.setText(bean.getDzNum() + "");
        pl.setText(bean.getCommentNum() + "");

        return convertView;
    }
}
