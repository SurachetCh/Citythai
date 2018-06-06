package revenue_express.citythai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import revenue_express.citythai.activity.MenuOrderActivity;
import revenue_express.citythai.R;
import revenue_express.citythai.activity.SplashScreen;
import revenue_express.citythai.dao.CategoryDataDao;


public class CategoryAdapter extends RecyclerView.Adapter {
    private List<CategoryDataDao> mDataset;
    private Context context;

    public CategoryAdapter(FragmentActivity activity, List<CategoryDataDao> categoery) {
        mDataset = categoery;
        context = activity;
    }

//    public ReviewsAdapter(Context context,List<ReviewsDataDao> categoery) {
//        mDataset = categoery;
//        this.context = context;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout,parent,false);
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
        public TextView tv_name_category,tv_count;
        public LinearLayout ll_category_card;

        public ListViewHolder(View itemView){
            super(itemView);
//            mtext = (TextView)itemView.findViewById(R.id.mText);
            ll_category_card = (LinearLayout) itemView.findViewById(R.id.card_view);
            tv_name_category = (TextView) itemView.findViewById(R.id.tv_name_category);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            itemView.setOnClickListener(this);
        }

        public void bindView(final int position){
//            tvTitle.setText(mDataset.get(position).getTitle());
//            mtext.setText(OrderData.title[position]);
            tv_name_category.setText(mDataset.get(position).getName());
            tv_count.setText(mDataset.get(position).getCount());

            tv_name_category.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
            tv_count.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));

//            Typeface tf = Typeface.createFromAsset(context.getAssets(),
//                    context.getResources().getString(R.string.SukhumvitSet_Medium));
//        //Set type fonts
//            tvTitle.setTypeface(tf);
//            tvDetail.setTypeface(tf);
//            tvPostdate.setTypeface(tf);
//            tvUser.setTypeface(tf);
//

            ll_category_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CategoryDataDao m_expert = mDataset.get(position);

                    String category_id = m_expert.getId();
                    String category_name = m_expert.getName();

                    Intent intent = new Intent(context, MenuOrderActivity.class);
                    intent.putExtra("category_id", category_id);
                    intent.putExtra("category_name", category_name);
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}