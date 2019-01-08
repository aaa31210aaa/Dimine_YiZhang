package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.R;

import java.util.ArrayList;

public class AddContentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listUrls;
    private LayoutInflater inflater;

    public AddContentAdapter(Context context, ArrayList<String> listUrls) {
        this.context = context;
        this.listUrls = listUrls;
        inflater = LayoutInflater.from(context);
        //        if (listUrls.size() == 7) {
//            listUrls.remove(listUrls.size() - 1);
//        }
    }

    @Override
    public int getCount() {
        return listUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.add_content_img_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.img_item_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String path = listUrls.get(position);
        if (path.equals("000000")) {
            Glide.with(context)
                    .load(R.drawable.icon_addpic_unfocused)
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView image;
    }
}
