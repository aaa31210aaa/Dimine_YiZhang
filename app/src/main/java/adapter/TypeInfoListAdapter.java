package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.XcJcBean;
import utils.ListViewHolder;

public class TypeInfoListAdapter extends BaseAdapter {
    private Context context;
    private List<XcJcBean> lists;

    public TypeInfoListAdapter(Context context, List<XcJcBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.typeinfolist_item, null);
        }
        ImageView typeinfolist_item_image = ListViewHolder.get(convertView, R.id.typeinfolist_item_image);
        TextView typeinfolist_item_qyname = ListViewHolder.get(convertView, R.id.typeinfolist_item_qyname);
        TextView typeinfolist_item_content = ListViewHolder.get(convertView, R.id.typeinfolist_item_content);
        TextView typeinfolist_item_zgdate = ListViewHolder.get(convertView, R.id.typeinfolist_item_zgdate);

        XcJcBean bean = lists.get(position);

        typeinfolist_item_image.setImageResource(bean.getImage());
        typeinfolist_item_qyname.setText(bean.getBjcdw());
        typeinfolist_item_content.setText(bean.getRemark());
        typeinfolist_item_zgdate.setText(bean.getStarttime());

        return convertView;
    }

    public void DataNotify(List<XcJcBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

}
