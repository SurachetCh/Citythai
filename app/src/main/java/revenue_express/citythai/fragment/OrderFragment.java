package revenue_express.citythai.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.activity.SplashScreen;
import revenue_express.citythai.activity.SumOrderActivity;
import revenue_express.citythai.adapter.CategoryAdapter;
import revenue_express.citythai.dao.CategoryDataDao;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;

import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class OrderFragment extends Fragment {
    LinearLayout layout_fragment;
    private View myFragmentView;
    private TextView txt_toolbar;
    public static List<CategoryDataDao> categoery;
    CategoryAdapter mAdapter;
    RecyclerView category_recycler_view;
    public static Context context;
    Integer ranks = 0;
    TextView tvOrder;
    ImageView imgSumOrder;
    String id,menu_id,name,detail,seq_num,online,active,count;
    Realm realm;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    String jsonData;
    AppBarLayout app_bar_layout;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_order, container, false);

        sendNamePageGoogleAnalytics("OrderFragment");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        categoery = new ArrayList<CategoryDataDao>();
        bindView();
        setFont(tf);
        setColor();
        setView();
        return myFragmentView;
    }

    private void bindView(){
        category_recycler_view = (RecyclerView) myFragmentView.findViewById(R.id.category_recycler_view);
        txt_toolbar = (TextView) myFragmentView.findViewById(R.id.txt_toolbar);
        app_bar_layout = (AppBarLayout)myFragmentView.findViewById(R.id.app_bar_layout);
        tvOrder =(TextView)myFragmentView.findViewById(R.id.tvOrder);
        imgSumOrder = (ImageView)myFragmentView.findViewById(R.id.imgSumOrder);
    }
    private void setFont(Typeface tf){
        txt_toolbar.setTypeface(tf);
    }
    private void setColor(){
        txt_toolbar.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
        app_bar_layout.setBackgroundColor(Color.parseColor(ColorPrimary));
        layout_fragment = (LinearLayout)myFragmentView.findViewById(R.id.layout_fragment);
        layout_fragment.setBackgroundColor(Color.parseColor(ColorPrimary));
        category_recycler_view.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
    }

    private void setView(){

        callgetCategoery();
//            setrecycler();

        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

        imgSumOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvOrder.getText().equals("0")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(getResources().getString(R.string.text_null_order));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Intent intent = new Intent(getActivity(), SumOrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setrecycler(){
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),categoery);
        category_recycler_view.setAdapter(categoryAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        category_recycler_view.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check Shop list
        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    private void callgetCategoery(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getCategoery(getResources().getString(R.string.access_token),getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                jsonObject = new JSONObject(jsonData);

                                jsonArray = jsonObject.getJSONArray("data");
                                categoery.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    Log.d("Data category: ", jsonObject2.toString());

                                    id = jsonObject2.getString("id");
                                    menu_id = jsonObject2.getString("menu_id");
                                    name = jsonObject2.getString("name");
                                    detail = jsonObject2.getString("detail");
                                    seq_num = jsonObject2.getString("seq_num");
                                    online = jsonObject2.getString("online");
                                    active = jsonObject2.getString("active");
                                    count = jsonObject2.getString("count");

                                    categoery.add(new CategoryDataDao(id, menu_id, name, detail, seq_num, online, active, count));
                                }

                                setrecycler();
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
