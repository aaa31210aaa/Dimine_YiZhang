package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import bean.LawsBean;
import utils.ListViewHolder;

public class LawsAdapter extends BaseAdapter {
    private Context context;
    private List<LawsBean> mlist;

    public LawsAdapter(Context context, List<LawsBean> mlist) {
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
            convertView = View.inflate(context, R.layout.laws_item, null);
        }
        TextView laws_item_lrtitle = ListViewHolder.get(convertView, R.id.laws_item_lrtitle);
        TextView laws_item_industryname = ListViewHolder.get(convertView, R.id.laws_item_industryname);
        TextView laws_item_lrtypename = ListViewHolder.get(convertView, R.id.laws_item_lrtypename);

        LawsBean bean = mlist.get(position);
        laws_item_lrtitle.setText(bean.getLrtitle());
        laws_item_industryname.setText("所属行业：" + bean.getIndustry());
        laws_item_lrtypename.setText("所属具体类别："+bean.getLrtypename());

//        laws_item_title.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        laws_item_title.getPaint().setAntiAlias(true);//抗锯齿

//        if (bean.isTag()) {
//            laws_item_title.setTextColor(ContextCompat.getColor(context, R.color.black));
//        } else {
//            laws_item_title.setTextColor(ContextCompat.getColor(context, R.color.text_select_color));
//        }
//
//        laws_item_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowToast.showShort(context, "点击了" + bean.getLawsTitle());
//                laws_item_title.setTextColor(Color.BLACK);
//                bean.setTag(true);
//            }
//        });
//        laws_item_industry.setText(bean.getIndustry());
//        laws_item_type.setText(bean.getType());

        return convertView;
    }


    public void DataNotify(List<LawsBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }
}
