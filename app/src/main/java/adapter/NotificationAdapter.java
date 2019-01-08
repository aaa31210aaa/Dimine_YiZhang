package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.TzGg;
import utils.ListViewHolder;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<TzGg> mlist;

    public NotificationAdapter(Context context, List<TzGg> mlist) {
        this.context = context;
        this.mlist = mlist;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.notification_item, null);
        }
        TextView notification_item_title = ListViewHolder.get(convertView, R.id.notification_item_title);
        TextView notification_item_content = ListViewHolder.get(convertView, R.id.notification_item_content);
        TextView notification_item_date = ListViewHolder.get(convertView, R.id.notification_item_date);
        ImageView notification_item_unread = ListViewHolder.get(convertView, R.id.notification_item_unread);


        TzGg bean = mlist.get(position);
        notification_item_title.setText(bean.getTitle());
        notification_item_content.setText(bean.getContent());
        notification_item_date.setText(bean.getDate());

        if (bean.getUnread().equals("1")) {
            notification_item_unread.setVisibility(View.GONE);
        } else {
            notification_item_unread.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    public void DataNotify(List<TzGg> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }
}
