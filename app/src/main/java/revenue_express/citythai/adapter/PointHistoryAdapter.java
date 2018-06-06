package revenue_express.citythai.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import revenue_express.citythai.R;
import revenue_express.citythai.dao.PointHistoryDataDao;

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.ViewHolder> {

    Context context;
    public String title;
    private MyClickListener mCallback;
    private List<PointHistoryDataDao> mDataset;

    @Override
    public PointHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.point_history_page_card_layout, parent, false);
        ViewHolder dataObjHolder = new ViewHolder(view);
        return dataObjHolder;
    }

    @Override
    public void onBindViewHolder(PointHistoryAdapter.ViewHolder holder, final int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        //Set type fonts
        holder.tv_point.setTypeface(tf);
        holder.tvPointDollar.setTypeface(tf);
        holder.tvStatus.setTypeface(tf);
        holder.tvDate.setTypeface(tf);

        holder.tv_point.setText("$ "+mDataset.get(position).getPointStar());
        holder.tvPointDollar.setText(mDataset.get(position).getPointDollar()+" Point");
        if(mDataset.get(position).getStatus().equals("receive")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_soft));
            holder.tvStatus.setText("+ "+mDataset.get(position).getStatus());
        }else{
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setText("- "+mDataset.get(position).getStatus());
        }
        holder.tvDate.setText(mDataset.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(MyClickListener mCallback){
        this.mCallback = mCallback;
    }

    public PointHistoryAdapter(Context context, List<PointHistoryDataDao> pointHistoryDataset){
        mDataset = pointHistoryDataset;
        this.context = context;
    }

    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_point,tvPointDollar,tvStatus,tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_point = (TextView) itemView.findViewById(R.id.tv_point);
            tvPointDollar = (TextView) itemView.findViewById(R.id.tvPointDollar);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }

        @Override
        public void onClick(View v) {
            mCallback.onItemClick(getAdapterPosition(),v);
        }
    }
}