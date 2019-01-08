package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.R;

import mine.PhotoView;

public class CircleRecycleAdapter extends RecyclerView.Adapter<CircleRecycleAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private String[] mImages;
    private MyViewHolder myViewHolder;
    private OnItemClickLitener mOnItemClickLitener;

    public CircleRecycleAdapter(Context context, String[] mImages) {
        this.context = context;
        this.mImages = mImages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CircleRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.circle_recycle_item, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    /**
     * 绑定viewholder里面的数据
     * 加载图片显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final CircleRecycleAdapter.MyViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(mImages[position])
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(holder.circle_recycle_item_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoView.class);
                intent.putExtra("position", position);
                intent.putExtra("urls", mImages);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.length;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView circle_recycle_item_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            circle_recycle_item_image = (ImageView) itemView.findViewById(R.id.circle_recycle_item_image);
        }
    }
}
