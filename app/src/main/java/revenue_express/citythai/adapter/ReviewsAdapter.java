package revenue_express.citythai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import revenue_express.citythai.R;
import revenue_express.citythai.activity.ReviewDetailActivity;
import revenue_express.citythai.dao.ReviewsDataDao;


public class ReviewsAdapter extends RecyclerView.Adapter {
    private List<ReviewsDataDao> mDataset;
    private Context context;

    public ReviewsAdapter(FragmentActivity activity, List<ReviewsDataDao> categoery) {
        mDataset = categoery;
        context = activity;
    }

//    public ReviewsAdapter(Context context,List<ReviewsDataDao> categoery) {
//        mDataset = categoery;
//        this.context = context;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_card_layout,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
//        return OrderData.title.length;
        return mDataset.size();
    }
    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        private TextView mtext;
        public TextView tvTitle, tvDetail,tvUser,tvReadmore,tvPostdate;
        public AvatarImageView imgUser;
        public LinearLayout ll_review_card,ll_star_review;
        public ImageView img_star1,img_star2,img_star3,img_star4,img_star5;

        public ListViewHolder(View itemView){
            super(itemView);
//            mtext = (TextView)itemView.findViewById(R.id.mText);
            ll_review_card = (LinearLayout) itemView.findViewById(R.id.ll_review_card);
//            ll_star_review = (LinearLayout) itemView.findViewById(R.id.ll_star_review);

            imgUser = (AvatarImageView) itemView.findViewById(R.id.imgUser);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvPostdate = (TextView) itemView.findViewById(R.id.tvPostdate);
            img_star1 = (ImageView)itemView.findViewById(R.id.img_star1);
            img_star2 = (ImageView)itemView.findViewById(R.id.img_star2);
            img_star3 = (ImageView)itemView.findViewById(R.id.img_star3);
            img_star4 = (ImageView)itemView.findViewById(R.id.img_star4);
            img_star5 = (ImageView)itemView.findViewById(R.id.img_star5);
//            tvReadmore = (TextView) itemView.findViewById(R.id.tvReadmore);
            itemView.setOnClickListener(this);
        }

        public void bindView(final int position){
//            tvTitle.setText(mDataset.get(position).getTitle());
//            mtext.setText(OrderData.title[position]);
            tvTitle.setText(mDataset.get(position).getTitle());
            tvDetail.setText(mDataset.get(position).getDescription());
            tvPostdate.setText(mDataset.get(position).getCreated_date());
            tvUser.setText(mDataset.get(position).getWriter_name());
//
//            Typeface tf = Typeface.createFromAsset(context.getAssets(),
//                    context.getResources().getString(R.string.SukhumvitSet_Medium));
//        //Set type fonts
//            tvTitle.setTypeface(tf);
//            tvDetail.setTypeface(tf);
//            tvPostdate.setTypeface(tf);
//            tvUser.setTypeface(tf);
//
            try {
                Glide.with(context)
                        .load(mDataset.get(position).getWriter_photo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.load_image)
                        .into(imgUser);
            } catch (Exception e) {
            }
            Double star = Double.valueOf(mDataset.get(position).getScore());
            if (star==0.0){
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==0.5){
                Glide.with(context)
                        .load(R.drawable.star_half)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==1.0){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==1.5){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_half)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==2.0){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==2.5){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_half)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }else if (star==3.0){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);

            }else if (star==3.5){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_half)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);

            }else if (star==4.0){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);

            }else if (star==4.5){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_half)
                        .into(img_star5);

            }else if (star==5){
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_review)
                        .into(img_star5);

            }else {
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star1);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star2);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star3);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star4);
                Glide.with(context)
                        .load(R.drawable.star_empty)
                        .into(img_star5);
            }

//            Integer star = Integer.valueOf(mDataset.get(position).getScore());
//
//            ll_star_review.removeAllViews();
//
//            int resultStar = 5-star;
//
//            for(int i= 0;i < star; i++)
//            {
//                ImageView ll_star_shop= new ImageView(context);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
//                ll_star_shop.setLayoutParams(params);
//                ll_star_shop.setBackgroundResource(R.drawable.star_review);
//                ll_star_review.addView(ll_star_shop);
//            }
//
//            for(int i= 0;i < resultStar; i++)
//            {
//                ImageView ll_star_shop= new ImageView(context);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
//                ll_star_shop.setLayoutParams(params);
//                ll_star_shop.setBackgroundResource(R.drawable.star_empty);
//                ll_star_review.addView(ll_star_shop);
//            }
//
            ll_review_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ReviewsDataDao m_expert = mDataset.get(position);

                    String review_id = m_expert.getId();

                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra("review_id", review_id);
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

//    Context context;
//    public String title;
//    private MyClickListener mCallback;
//    private List<ReviewsDataDao> mDataset;
//
//    @Override
//    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.reviews_card_layout, parent, false);
//        ViewHolder dataObjHolder = new ViewHolder(view);
//        return dataObjHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, final int position) {
//
//        Typeface tf = Typeface.createFromAsset(context.getAssets(),
//                context.getResources().getString(R.string.SukhumvitSet_Medium));
//
//
//        holder.tvTitle.setText(mDataset.get(position).getTitle());
//        holder.tvDetail.setText(mDataset.get(position).getDescription());
//        holder.tvPostdate.setText(mDataset.get(position).getCreated_date());
//        holder.tvUser.setText(mDataset.get(position).getWriter_name());
//
//        //Set type fonts
//        holder.tvTitle.setTypeface(tf);
//        holder.tvDetail.setTypeface(tf);
//        holder.tvPostdate.setTypeface(tf);
//        holder.tvUser.setTypeface(tf);
//
//        try {
//            Glide.with(context)
//                    .load(mDataset.get(position).getWriter_photo())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .skipMemoryCache(true)
//                    .placeholder(R.drawable.load_image)
//                    .into(holder.imgUser);
//        } catch (Exception e) {
//        }
//
//        Integer star = Integer.valueOf(mDataset.get(position).getScore());
//
//        holder.ll_star_review.removeAllViews();
//
//        int resultStar = 5-star;
//
//        for(int i= 0;i < star; i++)
//        {
//            ImageView ll_star_shop= new ImageView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
//            ll_star_shop.setLayoutParams(params);
//            ll_star_shop.setBackgroundResource(R.drawable.star_review);
//            holder.ll_star_review.addView(ll_star_shop);
//        }
//
//        for(int i= 0;i < resultStar; i++)
//        {
//            ImageView ll_star_shop= new ImageView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
//            ll_star_shop.setLayoutParams(params);
//            ll_star_shop.setBackgroundResource(R.drawable.star_empty);
//            holder.ll_star_review.addView(ll_star_shop);
//        }
//
//        holder.ll_review_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ReviewsDataDao m_expert = mDataset.get(position);
//
//                String review_id = m_expert.getId();
//
//                Intent intent = new Intent(context, ReviewDetailActivity.class);
//                intent.putExtra("review_id", review_id);
//                v.getContext().startActivity(intent);
//            }
//        });
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//
//    public void setOnItemClickListener(MyClickListener mCallback){
//        this.mCallback = mCallback;
//    }
//
//    public ReviewsAdapter(Context context,List<ReviewsDataDao> myDataset){
//        mDataset = myDataset;
//        this.context = context;
//    }
//
//    public interface MyClickListener{
//        public void onItemClick(int position, View v);
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public TextView tvTitle, tvDetail,tvUser,tvReadmore,tvPostdate;
//        public AvatarImageView imgUser;
//        public LinearLayout ll_review_card,ll_star_review;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            ll_review_card = (LinearLayout) itemView.findViewById(R.id.ll_review_card);
//            ll_star_review = (LinearLayout) itemView.findViewById(R.id.ll_star_review);
//
//            imgUser = (AvatarImageView) itemView.findViewById(R.id.imgUser);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
//            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
//            tvPostdate = (TextView) itemView.findViewById(R.id.tvPostdate);
////            tvReadmore = (TextView) itemView.findViewById(R.id.tvReadmore);
//        }
//
//        @Override
//        public void onClick(View v) {
//            mCallback.onItemClick(getAdapterPosition(),v);
//        }
//    }
}