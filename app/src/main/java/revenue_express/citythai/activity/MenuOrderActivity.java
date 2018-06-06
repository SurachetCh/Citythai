package revenue_express.citythai.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.adapter.MenuOrderAdapter;
import revenue_express.citythai.dao.MenuOrderDataDao;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.OrderList;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class MenuOrderActivity extends AppCompatActivity {
    ImageView imgBack,imgSumOrder;
    RecyclerView order_recycler_view;
    java.util.List<Integer> list = new ArrayList<>();
    Realm realm;
    public static TextView tvOrder,tv_header_menu;
    Integer ranks = 0;

    public static Context context;

    public static MenuOrderAdapter MAdapter;
    public static ArrayList<MenuOrderDataDao> MenuOrderDataListDaoArrayList;
    private ArrayList<Integer> List;
    public static JSONObject jsonObject = null;
    public static JSONArray jsonArray = null;

    static String jsonData,category_id,category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);

        sendNamePageGoogleAnalytics("MenuOrderActivity");

        category_id = getIntent().getExtras().getString("category_id");
        category_name = getIntent().getExtras().getString("category_name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Typeface tf = Typeface.createFromAsset(MenuOrderActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        bindView();
        setFont(tf);
        setColor();
        setView();
    }

    private void bindView(){
        tv_header_menu = (TextView)findViewById(R.id.tv_header_menu);
        tvOrder = (TextView)findViewById(R.id.tvOrder);
        order_recycler_view = (RecyclerView) findViewById(R.id.order_recycler_view);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgSumOrder = (ImageView)findViewById(R.id.imgSumOrder);
    }
    private void setFont(Typeface tf){
//Set type fonts
        tv_header_menu.setTypeface(tf);
        tvOrder.setTypeface(tf);
    }
    private void setColor(){
        tv_header_menu.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
        order_recycler_view.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
    }
    private void setView(){
        realm.beginTransaction();
        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
        for (int i = 0; i < result.size(); i++){
            ranks = ranks+result.get(i).getAmount();
        }
        realm.commitTransaction();

        tv_header_menu.setText(category_name);
        tvOrder.setText(String.valueOf(ranks));

        MenuOrderDataListDaoArrayList = new ArrayList<MenuOrderDataDao>();

        callGetMenuData();

        LinearLayoutManager layoutManager_new_shop = new LinearLayoutManager(this);
        layoutManager_new_shop.setOrientation(LinearLayoutManager.VERTICAL);
        order_recycler_view.setLayoutManager(layoutManager_new_shop);
        order_recycler_view.setItemAnimator(new DefaultItemAnimator());
        order_recycler_view.setHasFixedSize(true);

        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgSumOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvOrder.getText().equals("0")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuOrderActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(getResources().getString(R.string.text_null_order));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if user pressed "yes", then he is allowed to exit from application

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check Shop list
        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

    }

//    public static void SumOnlineOrder(int total){
//        String total_last = String.valueOf(total);
//        tvOrder.setText(total_last);
//    }


    private void callGetMenuData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getMenuData(getResources().getString(R.string.access_token),category_id,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (!response.isSuccessful()) {
                    try {
                        throw new IOException("Unexpected code " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("Response:",response.toString());
                    try {
                        jsonData = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                jsonObject = new JSONObject(jsonData);
                                jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    JSONObject jsonObject_list_id = jsonArray.getJSONObject(i);

                                    Integer id_menu = jsonObject_list_id.getInt("id");
                                    String name_menu = jsonObject_list_id.getString("name");
                                    String description = jsonObject_list_id.getString("detail");
                                    Integer multisize = jsonObject_list_id.getInt("multisize");
                                    Integer online = jsonObject_list_id.getInt("online");
                                    Double price = jsonObject_list_id.getDouble("price");
                                    String img_thumb = jsonObject_list_id.getString("img_thumb");
                                    String img_full = jsonObject_list_id.getString("img_full");

                                    MenuOrderDataListDaoArrayList.add(new MenuOrderDataDao(id_menu, name_menu, description, multisize, online, price,img_thumb,img_full));
                                }

                                MAdapter = new MenuOrderAdapter(MenuOrderActivity.this,MenuOrderDataListDaoArrayList);
                                order_recycler_view.setAdapter(MAdapter);

                            } catch (JSONException e) {
                                e.getMessage();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
