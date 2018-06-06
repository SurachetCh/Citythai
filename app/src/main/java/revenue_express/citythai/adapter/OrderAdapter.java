package revenue_express.citythai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import revenue_express.citythai.R;
import revenue_express.citythai.model.OrderData;

/**
 * Created by NEO on 8/11/2560.
 */

public class OrderAdapter extends RecyclerView.Adapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return OrderData.title.length;
    }
    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mtext;
        public ListViewHolder(View itemView){
            super(itemView);
            mtext = (TextView)itemView.findViewById(R.id.mText);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position){
            mtext.setText(OrderData.title[position]);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
