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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import revenue_express.citythai.adapter.OptionAdapter;
import revenue_express.citythai.dao.OptionDataDao;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.OptionList;

import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

/**
 * Created by NEO on 10/11/2560.
 */

public class DishDetailsActivity extends AppCompatActivity{

    public static TextView tvOrder;
    ImageView imgPlus,imgDelete,imgPhoto,imgSumOrder,imgBack;
    LinearLayout layout_Add;
    String title,menu_id;
    TextView tv_header_addmenu,tv_category,tv_name_menu,tv_price_default,tv_discription,tvAmount,tv_title_qty,tv_title_option,tv_title_special_instructions,tv_size;
    public static String price_default;
    public static TextView tv_price_total;
    EditText edt_special_instruction;
    RecyclerView option_recycler_view;
    public static OptionAdapter MAdapter;
    public static ArrayList<OptionDataDao> OptionDataDaoArrayList;
    Spinner spn_size;
    LinearLayout layout_option;
    public static Integer qty = 1;
    int id;
    public static Realm realm;
    public static Context context;
    private ArrayList<Integer> List;
    public static JSONObject jsonObject = null;
    public static JSONObject jsonObjectData = null;
    public static JSONArray jsonArraySize = null;
    public static JSONArray jsonArrayOption = null;

    public static JSONObject jsonObjectBySize = null;
    public static JSONObject jsonObjectDataBySize = null;
    public static JSONArray jsonArrayBySize = null;
    JSONObject jsonObject_sizes,jsonObject_by_sizes;
    static String jsonData,jsonDataBySize,category_name;
    private ArrayList<String> arrayList_size = new ArrayList<String>();
    String size_name,size_id;
    public static Double price_option = 0.00,priceA=0.00,priceB=0.00;
    LinearLayout layout_activity,layout_size;
    String NameMenu;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        sendNamePageGoogleAnalytics("DishDetailsActivity");

        menu_id = getIntent().getExtras().getString("menu_id");
        category_name = getIntent().getExtras().getString("category_name");
//        price_default = getIntent().getExtras().getString("price");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        Typeface tf = Typeface.createFromAsset(DishDetailsActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bindView();
        setFont(tf);
        setColor();
        setView();
    }

    private void bindView(){
        layout_activity = (LinearLayout)findViewById(R.id.layout_activity);
        layout_activity.setBackgroundColor(Color.parseColor(ColorPrimary));
        layout_size = (LinearLayout)findViewById(R.id.ll_size);
        layout_size.setBackgroundColor(Color.parseColor(ColorPrimary));
        tv_header_addmenu = (TextView)findViewById(R.id.tv_header_addmenu);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_name_menu = (TextView)findViewById(R.id.tv_name_menu);
        tv_price_default = (TextView)findViewById(R.id.tv_price_default);
        tv_discription = (TextView)findViewById(R.id.tv_discription);
        tvAmount = (TextView)findViewById(R.id.tvAmount);
        tv_price_total = (TextView)findViewById(R.id.tv_price_total);

        edt_special_instruction = (EditText)findViewById(R.id.edt_special_instruction);
        layout_Add = (LinearLayout)findViewById(R.id.layout_Add);

        imgDelete = (ImageView)findViewById(R.id.imgDelete);
        imgPlus = (ImageView)findViewById(R.id.imgPlus);
        imgPhoto = (ImageView)findViewById(R.id.imgPhoto);

        tv_title_qty = (TextView)findViewById(R.id.tv_title_qty);
        tv_title_option = (TextView)findViewById(R.id.tv_title_option);
        tv_title_special_instructions = (TextView)findViewById(R.id.tv_title_special_instructions);
        tv_size = (TextView)findViewById(R.id.tv_size);
        spn_size = (Spinner)findViewById(R.id.spn_size);
        layout_option = (LinearLayout)findViewById(R.id.layout_option);

        tvOrder = (TextView)findViewById(R.id.tvOrder);

        option_recycler_view = (RecyclerView)findViewById(R.id.option_recycler);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgSumOrder = (ImageView)findViewById(R.id.imgSumOrder);
    }
    private void setFont(Typeface tf){
        tv_header_addmenu.setTypeface(tf);
        tv_category.setTypeface(tf);
        tv_name_menu.setTypeface(tf);
        tv_price_default.setTypeface(tf);
        tv_discription.setTypeface(tf);
        tvAmount.setTypeface(tf);
        tv_price_total.setTypeface(tf);
        tv_title_qty.setTypeface(tf);
        tv_title_option.setTypeface(tf);
        tv_title_special_instructions.setTypeface(tf);
        tv_size.setTypeface(tf);
        tvOrder.setTypeface(tf);
        edt_special_instruction.setTypeface(tf);
    }
    private void setColor(){

    }
    private void setView(){
        DeleteOptionListRealm();

        OptionDataDaoArrayList = new ArrayList<OptionDataDao>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        option_recycler_view.setLayoutManager(layoutManager);
        option_recycler_view.setItemAnimator(new DefaultItemAnimator());
        option_recycler_view.setHasFixedSize(true);

        tv_category.setText(category_name);

        callGetMenu();
//        callGetMenuBySize("103");

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
            public void onClick(View v) {if (tvOrder.getText().equals("0")){

                AlertDialog.Builder builder = new AlertDialog.Builder(DishDetailsActivity.this);
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
        qty = 1;
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = qty+1;
                tvAmount.setText(qty.toString());
                setTv_price_total();

            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty<=1){
                    qty = 1;
                    tvAmount.setText(qty.toString());
                    setTv_price_total();
                }else {
                    qty = qty-1;
                    tvAmount.setText(qty.toString());
                    setTv_price_total();
                }
            }
        });


//        spn_size.setOnItemSelectedListener(this);

        layout_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String List_option_id = null;
                String List_option_name = null;
                String List_option_price = null;
                Double price_total = 0.00;
                Double price_op = 0.00;

                RealmResults<OptionList> optionLists = realm.where(OptionList.class).findAll();
                for (int i = 0; i < optionLists.size(); i++){
                    priceA = priceA+Double.parseDouble(optionLists.get(i).getPrice());
                    price_op = price_op+Double.parseDouble(optionLists.get(i).getPrice());
                    if (i == 0){
                        List_option_id = (optionLists.get(i).getItem_id());
                        List_option_name = (optionLists.get(i).getItem_name());
                        List_option_price = (optionLists.get(i).getPrice());
                    }else {
                        List_option_id = List_option_id+","+(optionLists.get(i).getItem_id());
                        List_option_name= List_option_name+","+(optionLists.get(i).getItem_name());
                        List_option_price = List_option_price+","+(optionLists.get(i).getPrice());
                    }

//                    executeRealmAddCartItemData(menu_id,size_id,size_name,price_default,qty.toString() ,String.valueOf(edt_special_instruction.getText()) ,optionLists.get(i).getItem_id(),optionLists.get(i).getItem_name(),optionLists.get(i).getPrice());
                }
                price_total = (price_op+Double.parseDouble(price_default))*qty;
                executeRealmAddCartItemList(menu_id,NameMenu,size_id,size_name,price_default,qty.toString(),String.valueOf(edt_special_instruction.getText()),List_option_id,List_option_name,List_option_price,String.format("%.2f",(price_total)));

                Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
                startActivity(intent);
//                setTv_price_total();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        DeleteOptionListRealm();
        super.onBackPressed();
    }

    //Doing the same with this method as we did with getName()
    private String getSizeName(int position){
        String sname="";
        try {
            for (int i = 0;i<jsonArraySize.length();i++){
                JSONObject job_sizes = jsonArraySize.getJSONObject(i);

                if (i==position){
                    sname = job_sizes.getString("size");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sname;
    }

    private String getSizePrice(int position){
        String sname="";
        try {
            for (int i = 0;i<jsonArraySize.length();i++){
                JSONObject job_sizes = jsonArraySize.getJSONObject(i);

                if (i==position){
                    sname = job_sizes.getString("price");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sname;
    }

    //Doing the same with this method as we did with getState()
    private String getSizeID(int position){
        String state="";
        try {
            for (int i = 0;i<jsonArraySize.length();i++){
                JSONObject job_sizes = jsonArraySize.getJSONObject(i);

                if (i==position){
                    state = job_sizes.getString("id");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check shop list
        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

    }

    private void callGetMenu(){
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit1.create(APIService.class);
        api.getMenu(getResources().getString(R.string.access_token),menu_id,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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

                                arrayList_size = new ArrayList<String>();
                                jsonObject = new JSONObject(jsonData);
                                jsonObjectData = jsonObject.getJSONObject("data");
                                NameMenu = jsonObjectData.getString("name");
                                String detail = jsonObjectData.getString("detail");
                                String photo = jsonObjectData.getString("img_thumb");

                                String option_default = jsonObjectData.getString("default");
                                size_id = option_default;
                                int position_default = 0;
                                jsonArraySize = jsonObjectData.getJSONArray("sizes");
                                jsonArrayOption = jsonObjectData.getJSONArray("options");
                                for (int i = 0;i<jsonArraySize.length();i++){
                                    jsonObject_sizes = jsonArraySize.getJSONObject(i);
                                    String mID = jsonObject_sizes.getString("id");
                                    if (mID.equals(option_default)){
                                        position_default = i;
                                    }
                                                price_default = jsonObject_sizes.getString("price");
                                                tv_price_default.setText(price_default);
                                    arrayList_size.add(jsonObject_sizes.getString("size")+" + $"+jsonObject_sizes.getString("price"));
                                }
                                setTv_price_total();

//                                            option_recycler_view.jsonArrayOption.length();
                                //TODO setOption
//                                JSONObject OptionjsonOpject = jsonArrayOption.getJSONObject(0);
                                OptionDataDaoArrayList.clear();
                                for (int i = 0;i<jsonArrayOption.length();i++){
                                    jsonObject_by_sizes = jsonArrayOption.getJSONObject(i);

                                    try{
                                        JSONArray jsonArrayData = jsonObject_by_sizes.getJSONArray("data");
                                        for (int j = 0;j<jsonArrayData.length(); j ++){
                                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(j);


                                            String option_id = jsonObjectData.getString("id");
                                            String option_detail;
                                            try {
                                                option_detail = jsonObjectData.getString("detail");
                                            }catch (Exception e){
                                                option_detail = "";
                                            }

                                            String option_name = jsonObjectData.getString("name");
                                            String option_type = jsonObjectData.getString("type");
                                            String option_counter = jsonObjectData.getString("counter");
                                            String optiondefault = jsonObjectData.getString("default");
                                            JSONArray jsonArray = jsonObjectData.getJSONArray("datas");

                                            OptionDataDaoArrayList.add(new OptionDataDao(option_id, option_name,option_detail, option_type,option_counter, optiondefault,jsonArray));
                                        }
                                    }catch (Exception e){

                                    }
                                }

//                                            price_option = 0.00;
                                MAdapter = new OptionAdapter(DishDetailsActivity.this,OptionDataDaoArrayList);
                                option_recycler_view.setAdapter(MAdapter);

                                //close TODO setOption

                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arrayList_size);
                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(R.layout.spinner_size_item);
                                if (jsonArraySize.length()==1){
                                    layout_size.setVisibility(View.GONE);
                                }else {
                                    layout_size.setVisibility(View.VISIBLE);
                                }
                                // attaching data adapter to spinner
                                spn_size.setAdapter(dataAdapter);
                                spn_size.setSelection(position_default);

                                tv_name_menu.setText(NameMenu);
                                tv_discription.setText(detail);
                                if (tv_discription.getText() == "null"){
                                    tv_discription.setText("");
                                }

                                Glide.with(getApplicationContext())
                                        .load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .skipMemoryCache(true)
                                        .placeholder(R.drawable.load_image)
                                        .into(imgPhoto);

                                spn_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        size_name = (getSizeName(position).toString());
                                        size_id = (getSizeID(position).toString());
                                        price_option = 0.00;
                                        price_default = (getSizePrice(position).toString());
                                        tv_price_default.setText("$ "+price_default);
                                        DeleteOptionListRealmInActivity(size_id);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
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

    private void callGetMenuBySize(final String size_id){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit2.create(APIService.class);
        api.getMenuBySize(getResources().getString(R.string.access_token),menu_id,size_id,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                        jsonDataBySize = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                jsonObjectBySize = new JSONObject(jsonDataBySize);
                                jsonObjectDataBySize = jsonObjectBySize.getJSONObject("data");
//                                            String name = jsonObjectDataBySize.getString("name");
//                                            String detail = jsonObjectDataBySize.getString("detail");
//                                            String photo = jsonObjectDataBySize.getString("img_thumb");

                                jsonArrayBySize = jsonObjectDataBySize.getJSONArray("options");
                                OptionDataDaoArrayList.clear();
                                for (int i = 0;i<jsonArrayBySize.length();i++){
                                    jsonObject_by_sizes = jsonArrayBySize.getJSONObject(i);
                                    String option_id = jsonObject_by_sizes.getString("id");
                                    String option_detail = jsonObject_by_sizes.getString("detail");
                                    String option_name = jsonObject_by_sizes.getString("name");
                                    String option_type = jsonObject_by_sizes.getString("type");
                                    String option_counter = jsonObject_by_sizes.getString("counter");
                                    String option_default = jsonObject_by_sizes.getString("default");
                                    JSONArray jsonArray = jsonObject_by_sizes.getJSONArray("datas");

                                    OptionDataDaoArrayList.add(new OptionDataDao(option_id, option_name,option_detail, option_type,option_counter, option_default,jsonArray));
                                }

//                                            price_option = 0.00;
                                MAdapter = new OptionAdapter(DishDetailsActivity.this,OptionDataDaoArrayList);
                                option_recycler_view.setAdapter(MAdapter);


                            } catch (JSONException e) {
                                e.getMessage();
                            }
                            setTv_price_total();
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private  void DeleteOptionListRealm(){
        realm.beginTransaction();
        RealmResults<OptionList> result = realm.where(OptionList.class).findAll();
        result.deleteAllFromRealm();
//        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
//        cartItemLists.deleteAllFromRealm();
//        RealmResults<CartItemData> cartItemDatas = realm.where(CartItemData.class).findAll();
//        cartItemDatas.deleteAllFromRealm();
        realm.commitTransaction();
    }
    private  void DeleteOptionListRealmInActivity(final String size_id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                realm.beginTransaction();
                RealmResults<OptionList> result = realm.where(OptionList.class).findAll();
                result.deleteAllFromRealm();
                realm.commitTransaction();
                callGetMenuBySize(size_id);
            }
        });
    }

    public static void setTv_price_total(){
        priceA = 0.0;
//        priceB=0.00;
        RealmResults<OptionList> optionLists = realm.where(OptionList.class).findAll();
        for (int i = 0; i < optionLists.size(); i++){
            priceA = priceA+Double.parseDouble(optionLists.get(i).getPrice());
        }
//        RealmResults<OptionAddExtraList> optionAddExtraLists = realm.where(OptionAddExtraList.class).findAll();
//        for (int i = 0; i < optionAddExtraLists.size(); i++){
//            priceB = priceB+Double.parseDouble(optionAddExtraLists.get(i).getPrice());
//        }
        //TODO
        tv_price_total.setText(String.format("%.2f",(priceA+Double.parseDouble(price_default))*qty));
    }

    private void executeRealmAddCartItemList(final String id_menu,final String name_menu, final String size_menu, final String size_name, final String price,final String qty , final String instruction_menu , final String option_menu, final String option_name, final String option_price,final String price_total) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                CartItemList cartItemList = realm.createObject(CartItemList.class);
                cartItemList.setId_menu(id_menu);
                cartItemList.setName_menu(name_menu);
                cartItemList.setSize_menu(size_menu);
                cartItemList.setSize_name(size_name);
                cartItemList.setPrice(price);
                cartItemList.setQty(qty);
                if (instruction_menu.equals(null)){
                    cartItemList.setInstruction_menu("null");
                }else {
                    cartItemList.setInstruction_menu(instruction_menu);
                }
                cartItemList.setOption_menu(option_menu);
                cartItemList.setOption_name(option_name);
                cartItemList.setOption_price(option_price);
                cartItemList.setPrice_total(price_total);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Create Success"+id_menu+","+size_menu+","+qty+","+instruction_menu+","+option_menu, Toast.LENGTH_SHORT).show();
//                setTv_price_total();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(),"Create Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
