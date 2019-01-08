package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.SearchQyBean;
import utils.ListViewHolder;

public class SearchQyAdapter extends BaseAdapter {
    private Context context;
    private List<SearchQyBean> mDatas;

    public SearchQyAdapter(Context context, List<SearchQyBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.searchqy_item, null);
        }
        TextView searchqy_name = ListViewHolder.get(convertView, R.id.searchqy_name);
        SearchQyBean bean = mDatas.get(position);
        searchqy_name.setText(bean.getQyname());
        return convertView;
    }

    public void DataNotify(List<SearchQyBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
}
