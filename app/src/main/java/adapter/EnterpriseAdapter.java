package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.QyXx;
import utils.ListViewHolder;

public class EnterpriseAdapter extends BaseAdapter {
    private Context context;
    private List<QyXx> mlist;

    public EnterpriseAdapter(Context context, List<QyXx> mlist) {
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
            convertView = View.inflate(context, R.layout.enterprise_item, null);
        }
        TextView enterprise_item_qyname = ListViewHolder.get(convertView, R.id.enterprise_item_qyname);
        TextView enterprise_item_jgbm = ListViewHolder.get(convertView, R.id.enterprise_item_jgbm);
        TextView enterprise_qyaddress = ListViewHolder.get(convertView, R.id.enterprise_qyaddress);

        QyXx bean = mlist.get(position);
        enterprise_item_qyname.setText("企业名称：" + bean.getQyName());
        enterprise_item_jgbm.setText("监管部门：" + bean.getJgBm());
        enterprise_qyaddress.setText("企业地址：" + bean.getQyAddress());

        return convertView;
    }

    public void DataNotify(List<QyXx> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }
}
