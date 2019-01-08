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

public class JobListAdapter extends BaseAdapter {
    private Context context;
    private List<XcJcBean> mlist;

    public JobListAdapter(Context context, List<XcJcBean> mlist) {
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
            convertView = View.inflate(context, R.layout.job_list_item, null);
        }
        ImageView job_list_item_image = ListViewHolder.get(convertView, R.id.job_list_item_image);
        TextView job_list_item_qyname = ListViewHolder.get(convertView, R.id.job_list_item_qyname);
        TextView job_list_item_content = ListViewHolder.get(convertView, R.id.job_list_item_content);
        TextView job_list_item_zgdate = ListViewHolder.get(convertView, R.id.job_list_item_zgdate);
//        TextView job_list_item_zgtag = ListViewHolder.get(convertView, R.id.job_list_item_zgtag);

        XcJcBean bean = mlist.get(position);
        job_list_item_image.setImageResource(bean.getImage());
        job_list_item_qyname.setText(bean.getBjcdw());
        job_list_item_content.setText(bean.getRemark());
        job_list_item_zgdate.setText(bean.getStarttime());

//        if (bean.getZgtag().toString().equals("yzg")) {
//            job_list_item_zgtag.setText("已整改");
//            job_list_item_zgtag.setTextColor(Color.WHITE);
//            job_list_item_zgtag.setBackgroundResource(R.drawable.jobyzg);
//        } else if (bean.getZgtag().toString().equals("wzg")) {
//            job_list_item_zgtag.setText("未整改");
//            job_list_item_zgtag.setTextColor(Color.WHITE);
//            job_list_item_zgtag.setBackgroundResource(R.drawable.jobwzg);
//        } else {
//            job_list_item_zgtag.setText("整改中");
//            job_list_item_zgtag.setTextColor(Color.WHITE);
//            job_list_item_zgtag.setBackgroundResource(R.drawable.jobzgz);
//        }

        return convertView;
    }

    public void DataNotify(List<XcJcBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }

}
