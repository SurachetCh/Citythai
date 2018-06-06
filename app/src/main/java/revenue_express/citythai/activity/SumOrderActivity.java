package revenue_express.citythai.activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.citythai.R;
import revenue_express.citythai.manager.Contextor;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.LastOrderEmail;
import revenue_express.citythai.model.OrderListDAO;
import revenue_express.citythai.model.User;

import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.fragment.HomeFragment.tax_rate;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class SumOrderActivity extends AppCompatActivity {
    Realm realm;
    ImageView imgBack,imgLoad;
    TextView tv_sub_total,tv_tax,tv_total,tvSubtotal,tvTax,tvTotal,tv_header_sumorder;
    RecyclerView sum_order_recycler_view;
    LinearLayout activity_sum_order;
    private ArrayList<OrderListDAO> OnlineOrderDataListDaoArrayList;
    SumOrderAdapter mAdapter;
    Button btnAdd;

    //Ok http3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData,OrderID;
    JSONObject jsonObject,jsonObjectData;

    String id_menu;
    String id_size;
    String url_confirm_order;
    static String access_token;
    static String access_user_key;
    static Double total = 0.00;
    static Double GrandTotal = 0.00;

    public static ArrayList<String> item_id = new ArrayList<String>();
    public static ArrayList<String> item_size = new ArrayList<String>();
    public static ArrayList<String> item_instruction = new ArrayList<String>();
    public static ArrayList<String> item_qty = new ArrayList<String>();
    public static ArrayList<String> item_option = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_order);

        sendNamePageGoogleAnalytics("SumOrderActivity");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(ColorPrimary));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface tf = Typeface.createFromAsset(SumOrderActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        item_id.clear();
        item_size.clear();
        item_instruction.clear();
        item_qty.clear();
        item_option.clear();

        url_confirm_order = getResources().getString(R.string.Base_URL) + getResources().getString(R.string.URL_confirm_order);
//        url_payment = getResources().getString(R.string.Base_URL) + getResources().getString(R.string.URL_payment);
        access_token = getResources().getString(R.string.access_token);

        bindView();
        setFont(tf);
        setColor();
        setView();
    }

    private void bindView(){
        tv_header_sumorder = (TextView)findViewById(R.id.tv_header_sumorder);
        activity_sum_order = (LinearLayout)findViewById(R.id.activity_sum_order);
        tvSubtotal = (TextView)findViewById(R.id.tvSubtotal);
        tvTax = (TextView)findViewById(R.id.tvTax);
        tvTotal = (TextView)findViewById(R.id.tvTotal);
        tv_sub_total = (TextView)findViewById(R.id.tv_sub_total);
        tv_tax = (TextView)findViewById(R.id.tv_tax);
        tv_total = (TextView)findViewById(R.id.tv_total);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        imgLoad = (ImageView)findViewById(R.id.imgLoad);
        sum_order_recycler_view = (RecyclerView)findViewById(R.id.sum_order_recycler_view);
        imgBack = (ImageView)findViewById(R.id.imgBack);
    }
    private void setFont(Typeface tf){
        tv_header_sumorder.setTypeface(tf);
        tvSubtotal.setTypeface(tf);
        tvTax.setTypeface(tf);
        tvTotal.setTypeface(tf);
        tv_sub_total.setTypeface(tf);
        tv_tax.setTypeface(tf);
        tv_total.setTypeface(tf);
    }
    private void setColor(){
        activity_sum_order.setBackgroundColor(Color.parseColor(ColorPrimary));
    }
    private void setView(){
        Glide.with(this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);

        OnlineOrderDataListDaoArrayList = new ArrayList<OrderListDAO>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        sum_order_recycler_view.setLayoutManager(layoutManager);
        sum_order_recycler_view.setItemAnimator(new DefaultItemAnimator());
        sum_order_recycler_view.setHasFixedSize(true);

        //Add menu order card list
        AddListMenuCard();

        set_text_total();

        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
        realm.commitTransaction();
        try {
            access_user_key = user.get(0).getAccess_user_key();
        }catch (Exception e){

            Log.e("login", "false");
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEditText();
            }
        });
    }
    private void popUpEditText() {
        final String lastEmail;
        // do something here on OK
        item_id.clear();
        item_size.clear();
        item_instruction.clear();
        item_qty.clear();
        item_option.clear();
        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        realm.commitTransaction();
        if(user_all.size() == 0 ){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.order_user_login_before), Toast.LENGTH_SHORT).show();
        }else {
            realm.beginTransaction();
            RealmResults<LastOrderEmail> lastOrderEmails = realm.where(LastOrderEmail.class).findAll();
            realm.commitTransaction();
            if (lastOrderEmails.size() == 0) {
                realm.beginTransaction();
                RealmResults<User> users = realm.where(User.class).findAll();
                realm.commitTransaction();
                lastEmail = String.valueOf(users.get(0).getEmail());
            } else {
                lastEmail = String.valueOf(lastOrderEmails.get(0).getEmail());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter or Check the E-mail  below For Recive Invoice");

            final EditText input = new EditText(this);
            input.setText(lastEmail);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


//                    access_user_key = user_all.get(0).getAccess_user_key();
                    realm.beginTransaction();
                    RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
                    realm.commitTransaction();

                    for (int i = 0; i < cartItemLists.size(); i++) {
                        item_id.add(cartItemLists.get(i).getId_menu());
                        item_size.add(cartItemLists.get(i).getSize_menu());
                        item_instruction.add(cartItemLists.get(i).getInstruction_menu());
                        item_qty.add(cartItemLists.get(i).getQty());
                        item_option.add(cartItemLists.get(i).getOption_menu());
                    }

                    if (isEmailValid(String.valueOf(input.getText())) == true) {
                        realm.beginTransaction();
                        RealmResults<LastOrderEmail> last = realm.where(LastOrderEmail.class).findAll();
                        last.deleteAllFromRealm();
                        realm.commitTransaction();
                        executeRealmUser(String.valueOf(input.getText()));
                        callSyncSendMenuOrder(url_confirm_order, String.valueOf(input.getText()));
                    } else {
                        Toast.makeText(getApplicationContext(), "invalid email format", Toast.LENGTH_LONG).show();
                    }


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private void set_text_total() {
        realm.beginTransaction();
        RealmResults<CartItemList> results = realm.where(CartItemList.class).findAll();
        realm.commitTransaction();
        List<CartItemList> copied = realm.copyFromRealm(results);
        Double sub_total = 0.00;
        Double tax = 0.00;
        total = 0.00;
        for (int i = 0; i < copied.size(); i++) {
            sub_total = sub_total+Double.parseDouble(copied.get(i).getPrice_total() );
        }
        tvSubtotal.setText( " $  " +String.format("%.2f",(sub_total)));
        tax = (sub_total*Double.parseDouble(tax_rate))/100;
        total = sub_total+tax;
        tvTax.setText(" $  " +String.format("%.2f",(tax)));
        tvTotal.setText(" $  " +String.format("%.2f",(total)));
    }

    @Override
    public void onResume() {
        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
        realm.commitTransaction();
        try {
            access_user_key = user.get(0).getAccess_user_key();
        }catch (Exception e){

            Log.e("login", "false");
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void AddListMenuCard(){

        realm.beginTransaction();

        RealmResults<CartItemList> results = realm.where(CartItemList.class).findAll();
        Log.e("Results", String.valueOf(results));

        if(results == null || results.size() == 0){
            Log.e("Results", String.valueOf(results));
        }else{

            OnlineOrderDataListDaoArrayList.clear();
            List<CartItemList> copied = realm.copyFromRealm(results);

            for (int i = 0; i < copied.size(); i++) {

                String id_menu = copied.get(i).getId_menu();
                String name_menu = copied.get(i).getName_menu();
                String price_menu = copied.get(i).getPrice();
                String size_menu = copied.get(i).getSize_menu();
                String size_name = copied.get(i).getSize_name();
                String option_menu = copied.get(i).getOption_name();
                String option_name = copied.get(i).getOption_name();
                String option_price = copied.get(i).getOption_price();
                String instruction_menu = copied.get(i).getInstruction_menu();
                String qty = copied.get(i).getQty();

//                Integer id_menu = copied.get(i).getId_menu();
//                Integer id = copied.get(i).getId();
//                String title = copied.get(i).getTitle();
//                Double price = copied.get(i).getPrice();
//                Double total = copied.get(i).getTotal();
//                Integer amount = copied.get(i).getAmount();
//                String addition = copied.get(i).getAddition();
//                String note = copied.get(i).getNote();
//
//                item_id.add(copied.get(i).getId_menu());
//                item_price.add(copied.get(i).getPrice());
//                item_name.add(copied.get(i).getTitle());
//                item_instruction.add(copied.get(i).getAddition());
//                item_qty.add(copied.get(i).getAmount());
//                item_total.add(copied.get(i).getTotal());

                OnlineOrderDataListDaoArrayList.add(new OrderListDAO(id_menu, name_menu, price_menu, size_menu,size_name,option_menu,option_name,option_price,instruction_menu, qty));
            }

//            RealmResults<IDShop> id_shop = realm.where(IDShop.class).findAll();
//            shop = String.valueOf(id_shop.get(0).getIdShop());
//
            mAdapter = new SumOrderAdapter(SumOrderActivity.this,OnlineOrderDataListDaoArrayList);
            sum_order_recycler_view.setAdapter(mAdapter);
//
//            Typeface tf = Typeface.createFromAsset(getAssets(),
//                    "fonts/SukhumvitSet-Medium.ttf");
//
//            sub_total = Double.valueOf(new DecimalFormat("0.00").format(realm.where(OrderList.class).sum("total")));
//            tvSubtotal.setTypeface(tf);
//            tvSubtotal.setText(" $"+sub_total.toString());
//
//            tax = Double.valueOf(new DecimalFormat("0.00").format(getTax_rate()));
//            tvTaxDisplay.setText(getResources().getString(R.string.tax)+" ("+tax.toString()+"%)");
//
//            Double tax_last = Double.valueOf(new DecimalFormat("0.00").format((sub_total*tax)/100));
//            tvTax.setTypeface(tf);
//            tvTax.setText(" $"+tax_last.toString());
//
//            total = Double.valueOf(new DecimalFormat("0.00").format(tax_last+sub_total));
//            tvTotal.setTypeface(tf);
//            tvTotal.setText(" $"+total.toString());
        }
        realm.commitTransaction();
    }

    private void callSyncSendMenuOrder(final String url,final String email) {
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgLoad.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

//                realm.beginTransaction();
//                RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
//                realm.commitTransaction();


                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("access_user_key",access_user_key);
                formBuilder.add("shop", getResources().getString(R.string.shop_id_ref));
                formBuilder.add("item_count", String.valueOf(item_id.size()));

                for (int i = 0; i < item_id.size(); i++) {
                    formBuilder.add("item_id["+i+"]", String.valueOf(item_id.get(i)));
                    formBuilder.add("item_size["+i+"]", String.valueOf(item_size.get(i)));
                    formBuilder.add("item_qty["+i+"]", String.valueOf(item_qty.get(i)));
                    formBuilder.add("item_instruction["+i+"]", String.valueOf(item_instruction.get(i)));
                    formBuilder.add("item_option["+i+"]", String.valueOf(item_option.get(i)));
                }
                formBuilder.add("email", email);

                MediaType.parse("application/json; charset=utf-8");
                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                client.newCall(request)
                        .enqueue(new Callback() {

                            @Override
                            public void onFailure(final Call call, IOException e) {
                                // Error

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected code " + response);
                                } else {
                                    Log.i("Response:",response.toString());
                                    jsonData = response.body().string();

//                                    try {
//                                        jsonObject = new JSONObject(jsonData);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    OrderID = jsonObject.getString("")
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("Result shop name", jsonData);
                                        if (jsonData.toLowerCase().contains("true")){
                                            try {
                                                jsonObject = new JSONObject(jsonData);
                                                jsonObjectData = jsonObject.getJSONObject("data");

                                                OrderID = jsonObjectData.getString("order_id");
                                                GrandTotal = jsonObjectData.getDouble("grand_total");


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            imgLoad.setVisibility(View.INVISIBLE);
//                                            payment();

                                            Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
//                                            intent.putExtra("OrderID", OrderID);
                                            startActivity(intent);
//                                            DeleteOrderListRealm();
//                                            showStatus("Order Online Status", String.valueOf(getResources().getString(R.string.order_success)),"success");
                                        }else {
                                            imgLoad.setVisibility(View.INVISIBLE);
                                            showStatus("Order Online Status", String.valueOf(getResources().getString(R.string.order_fail)),"fail");
                                        }

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Show Status Dialog-----//
    private void showStatus(String title , String msg, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SumOrderActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (status.equals("success")){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void executeRealmUser( final String email) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                LastOrderEmail lastOrderEmail = realm.createObject(LastOrderEmail.class);
                lastOrderEmail.setEmail(email);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(SumOrderActivity.this,"Create user error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public class SumOrderAdapter extends RecyclerView.Adapter<SumOrderAdapter.MyViewHolder> {

        ArrayList<OrderListDAO> list;
        Context context;
        Realm realm;

        public SumOrderAdapter(Context context, ArrayList<OrderListDAO> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public SumOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_detail_layout, parent, false);
            Realm.init(Contextor.getInstance().getContext());
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(Realm.DEFAULT_REALM_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SumOrderAdapter.MyViewHolder holder, final int position) {

            OrderListDAO m_expert = list.get(position);

            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/SukhumvitSet-Medium.ttf");


            Double total_price_option = 0.00;
            Double price_total = 0.00;
            String opName = m_expert.getOption_name();
            if (opName == null){
                holder.tv_option_menu.setVisibility(View.GONE);
//                holder.tv_price_option.setVisibility(View.GONE);
            }else {
                holder.tv_option_menu.setVisibility(View.VISIBLE);
//                holder.tv_price_option.setVisibility(View.VISIBLE);
                String[] op_name = opName.split(",");

                String opPrice = m_expert.getOption_price();
                String[] op_price = opPrice.split(",");

                for (int i = 0; i < op_name.length; i++){
                    op_name[i] = op_name[i].trim();
                    if (i == (op_name.length-1))  {
                        holder.tv_option_menu.append(op_name[i]);
                    }else  {
                        holder.tv_option_menu.append(op_name[i]+"\n");
//                    holder.tv_option_menu.append(op_name[i]);
                    }
//                txtTimeshop.append(timeshop.get(j) + "\n");
                }

                for (int i = 0; i < op_price.length; i++){
                    op_price[i] = op_price[i].trim();
                    total_price_option = total_price_option+Double.parseDouble(op_price[i]);
//                    if (i == (op_price.length-1))  {
//                        holder.tv_price_option.append(" $ "+op_price[i]);
//                    }else  {
//                        holder.tv_price_option.append(" $ "+op_price[i]+"\n");
////                    holder.tv_option_menu.append(op_name[i]);
//                    }
////                txtTimeshop.append(timeshop.get(j) + "\n");
                }
            }


            price_total =  (Double.parseDouble(m_expert.getPrice_menu())+total_price_option )* Integer.parseInt(m_expert.getQty());


            holder.tv_name_menu.setTypeface(tf);
            holder.tv_size_menu.setTypeface(tf);
            holder.tv_option_menu.setTypeface(tf);
            holder.tv_qty.setTypeface(tf);
            holder.tv_price_menu.setTypeface(tf);
//            holder.tv_price_size.setTypeface(tf);
//            holder.tv_price_option.setTypeface(tf);
//            holder.tv_amount_qty.setTypeface(tf);
            holder.tv_instruction.setTypeface(tf);

            holder.tv_name_menu.setText(m_expert.getName_menu());
            holder.tv_size_menu.setText(m_expert.getSize_name());
//            holder.tv_option_menu.setText(m_expert.getOption_name());
            holder.tv_price_menu.setText("$ "+String.format("%.2f",(price_total)));
//            holder.tv_price_size.setText("$ "+m_expert.getPrice_menu());
//            holder.tv_price_option.setText("$ "+m_expert.getOption_price());
//            holder.tv_amount_qty.setText(m_expert.getQty());
            holder.tv_qty.setText("QTY "+m_expert.getQty());
            holder.tv_instruction.setText(m_expert.getInstruction_menu());
            if (holder.tv_size_menu.getText().equals("Standard")){
                holder.tv_size_menu.setVisibility(View.GONE);
            }else {
                holder.tv_size_menu.setVisibility(View.VISIBLE);
            }

//            holder.add_note.setText(context.getResources().getString(R.string.instruction) + ":" + addition + " " + note);
//
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderListDAO m_expert = list.get(position);

                    id_menu = m_expert.getId_menu();
                    id_size = m_expert.getSize_menu();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    alertDialog.setTitle(context.getResources().getString(R.string.confirm_delete_item));

                    // Setting Dialog Message
                    //alertDialog.setMessage(context.getResources().getString(R.string.delete_menu_item));

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.delete_menu);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkDelete(id_menu,id_size,position);
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            mAdapter.notifyDataSetChanged();
                            sum_order_recycler_view.setAdapter(mAdapter);
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });

        }
        public void checkDelete(final String id_menu,final String id_size,final   Integer position){
            realm.beginTransaction();
//            RealmResults<CartItemList> result = realm.where(CartItemList.class).equalTo("id_menu", id_menu).equalTo("size_menu", id_size).findAll();
            RealmResults<CartItemList> result = realm.where(CartItemList.class).findAll();
//            result.deleteLastFromRealm();
            result.get(position).deleteFromRealm();
            realm.commitTransaction();
            set_text_total();
        }

        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_name_menu, tv_size_menu, tv_option_menu, tv_qty, tv_price_menu,tv_instruction;
            public ImageView img_delete;
            public MyViewHolder(View view) {
                super(view);

                tv_name_menu = (TextView) itemView.findViewById(R.id.tv_name_menu);
                tv_size_menu = (TextView) itemView.findViewById(R.id.tv_size_menu);
                tv_option_menu = (TextView) itemView.findViewById(R.id.tv_option_menu);
                tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
                tv_price_menu = (TextView) itemView.findViewById(R.id.tv_price_menu);
//                tv_price_size = (TextView) itemView.findViewById(R.id.tv_price_size);
//                tv_price_option = (TextView) itemView.findViewById(R.id.tv_price_option);
//                tv_amount_qty = (TextView) itemView.findViewById(R.id.tv_amount_qty);
                tv_instruction = (TextView)itemView.findViewById(R.id.tv_instruction);
                img_delete = (ImageView)itemView.findViewById(R.id.img_delete);

            }
        }
    }
}
