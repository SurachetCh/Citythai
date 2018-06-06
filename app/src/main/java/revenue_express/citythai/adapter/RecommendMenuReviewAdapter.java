package revenue_express.citythai.adapter;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import revenue_express.citythai.R;
import revenue_express.citythai.dao.RecommendMenuReviewDataDao;


public class RecommendMenuReviewAdapter extends RecyclerView.Adapter<RecommendMenuReviewAdapter.MyViewHolder> {

    ArrayList<RecommendMenuReviewDataDao> list;
    Context context;

    public RecommendMenuReviewAdapter(Context context, ArrayList<RecommendMenuReviewDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommend_menu_review_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        holder.title.setText(list.get(position).getRecommend_menu_name());

        //Set type fonts
        holder.title.setTypeface(tf);

        try {
            Log.d("Image Menu: :",list.get(position).getRecommend_menu_photo());
            Glide.with(context)
                    .load(list.get(position).getRecommend_menu_photo())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.load_image)
                    .into(holder.iv_menu);
        } catch (Exception e) {
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView iv_menu;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.txtTitle);
            iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        }
    }
}
