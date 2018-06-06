package revenue_express.citythai.adapter;
/**
 * Created by chaleamsuk on 12/26/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import revenue_express.citythai.R;
import revenue_express.citythai.activity.WriteReviewsActivity;
import revenue_express.citythai.dao.WriteReviewsMenuDataDao;

public class WriteReviewsMenuAdapter extends RecyclerView.Adapter<WriteReviewsMenuAdapter.MyViewHolder> {

    List<WriteReviewsMenuDataDao> list;
    Context context;
    WriteReviewsMenuDataDao m_expert;
    String check;

    public WriteReviewsMenuAdapter(Context context, List<WriteReviewsMenuDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.write_reviews_menu_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        m_expert = list.get(position);

        //Set type fonts
        holder.name_menu.setTypeface(tf);

        try {
            holder.name_menu.setText(m_expert.getMenu_name());

            String url_image = m_expert.getUrl_menu_image();
            if(!url_image.equals("")) {
                Glide.with(context)
                        .load(url_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.load_image)
                        .into(holder.image_recommend_menu);
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,list.size());

                    if(list.size() == 0){
                        check = "false";
                    }else{
                        check = "true";
                    }

                    WriteReviewsActivity.removeIdMenu(m_expert.getId(),check);

                }
            });

        } catch (Exception e) {
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_menu;
        public ImageView image_recommend_menu,delete;

        public MyViewHolder(View view) {
            super(view);

            name_menu = (TextView) view.findViewById(R.id.tv_name_menu);
            image_recommend_menu = (ImageView) view.findViewById(R.id.iv_image_recommend_menu);
            delete = (ImageView) view.findViewById(R.id.iv_delete);

        }
    }
}
