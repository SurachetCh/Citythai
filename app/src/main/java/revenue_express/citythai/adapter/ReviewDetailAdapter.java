package revenue_express.citythai.adapter;

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
import revenue_express.citythai.dao.ReviewDetailDataDao;

public class ReviewDetailAdapter extends RecyclerView.Adapter<ReviewDetailAdapter.ViewHolder> {

    Context context;
    public String title;
    private MyClickListener mCallback;
    private List<ReviewDetailDataDao> mDataset;

    @Override
    public ReviewDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_detail_card_layout, parent, false);
        ViewHolder dataObjHolder = new ViewHolder(view);
        return dataObjHolder;
    }

    @Override
    public void onBindViewHolder(ReviewDetailAdapter.ViewHolder holder, final int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        try {

            holder.caption.setText(mDataset.get(position).getImgCaptionReview());

            //Set type fonts
            holder.caption.setTypeface(tf);

            String url_image = mDataset.get(position).getImgFoodReview();
            if(!url_image.equals("")) {
                Glide.with(context)
                        .load(mDataset.get(position).getImgFoodReview())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.load_image)
                        .into(holder.review_detail_image);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(MyClickListener mCallback){
        this.mCallback = mCallback;
    }

    public ReviewDetailAdapter(Context context, List<ReviewDetailDataDao> myDataset){
        mDataset = myDataset;
        this.context = context;
    }

    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView review_detail_image;
        public TextView caption;

        public ViewHolder(View itemView) {
            super(itemView);

            review_detail_image = (ImageView) itemView.findViewById(R.id.iv_review_detail_image);
            caption = (TextView) itemView.findViewById(R.id.tv_caption);
        }

        @Override
        public void onClick(View v) {
            mCallback.onItemClick(getAdapterPosition(),v);
        }
    }
}


