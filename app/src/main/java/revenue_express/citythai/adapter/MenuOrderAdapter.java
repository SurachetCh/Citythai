package revenue_express.citythai.adapter;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import revenue_express.citythai.activity.DishDetailsActivity;
import revenue_express.citythai.R;
import revenue_express.citythai.dao.MenuOrderDataDao;

import static revenue_express.citythai.activity.MenuOrderActivity.tv_header_menu;
public class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.MyViewHolder> {

    ArrayList<MenuOrderDataDao> list;
    Context context;
    Realm realm;


    public MenuOrderAdapter(Context context, ArrayList<MenuOrderDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MenuOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_order_card_layout, parent, false);

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        return new MenuOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MenuOrderAdapter.MyViewHolder holder, int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        holder.title.setText(list.get(position).getName());
        holder.desc.setText(list.get(position).getDetail());
        if (holder.desc.getText()== "null"){
            holder.desc.setText("");
        }
        holder.price.setText("$ "+String.format("%.2f",(list.get(position).getPrice())));

        //Set type fonts
        holder.title.setTypeface(tf);
        holder.desc.setTypeface(tf);
        holder.price.setTypeface(tf);

        try {
            Log.d("Image Menu: :",list.get(position).getImg_thumb());
            Glide.with(context)
                    .load(list.get(position).getImg_thumb())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.load_image)
                    .into(holder.iv_menu);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,price;
        public ImageView iv_menu;
        public RelativeLayout online_order_list_layout_card;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.txtTitle);
            desc = (TextView) view.findViewById(R.id.txtDesc);
            price = (TextView) view.findViewById(R.id.txtPrice);
            iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
            online_order_list_layout_card = (RelativeLayout)view.findViewById(R.id.online_order_list_layout_card);

            online_order_list_layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                        int position = getAdapterPosition();
                        String id = String.valueOf(list.get(position).getId());
                        String price = String.valueOf(list.get(position).getPrice());

                            Intent intent = new Intent(context, DishDetailsActivity.class);
                            intent.putExtra("menu_id", id);
                            intent.putExtra("category_name",tv_header_menu.getText());
                            intent.putExtra("price",price);
                            v.getContext().startActivity(intent);
                }
            });
        }
    }

    //-----Shop status dialog-----//
    private void showStatusOpenDialog(String title , String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = "OK";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
