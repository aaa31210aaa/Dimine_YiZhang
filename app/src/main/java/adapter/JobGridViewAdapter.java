package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_yizhang.R;

import java.util.List;

import utils.ListViewHolder;

public class JobGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> mImages;
    private List<String> mlists;

    public JobGridViewAdapter(Context context, List<String> mlists) {
        this.context = context;
        this.mlists = mlists;
    }

    @Override
    public int getCount() {
        return mlists.size();
    }

    @Override
    public Object getItem(int position) {
        return mlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.job_gridview_item, null);
        }
        ImageView job_gridview_item_img = ListViewHolder.get(convertView, R.id.job_gridview_item_img);
        TextView job_gridview_item_tv = ListViewHolder.get(convertView, R.id.job_gridview_item_tv);

        job_gridview_item_img.setImageResource(R.mipmap.ic_launcher);
        job_gridview_item_tv.setText(mlists.get(position));


        return convertView;
    }
}
