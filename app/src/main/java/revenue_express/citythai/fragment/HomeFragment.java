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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.activity.SumOrderActivity;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;

import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.activity.SplashScreen.TextColorPrimary;
import static revenue_express.citythai.manager.ClereCache.deleteCache;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class HomeFragment extends Fragment {
    private View myFragmentView;
    ImageView iv_image_shop_name, img_star1, img_star2, img_star3, img_star4, img_star5, img_review;
    //    RatingBar ratingBar;
    ImageView bt_status_shop,imgSumOrder;
    TextView time, txtAddress, txtTel, txtDetail, txtTimeshop, txt_toolbar,titleAddress,titleTel,titleTimeshop;
    LinearLayout layout_fragment;
    ViewFlipper viewFlipper;
    TextView tvOrder;
    AppBarLayout app_bar_layout;
    // Time open and close shop

    GoogleMap mGoogleMap;
    MapView mMapView;

    String img_cover, title, detail, map_position, address, phone;
    String img_cover_thumb;
    private static String now_opening,upcoming_open;
    public static String tax_rate;
    Double star;
    Realm realm;

    protected Context context;

    private static ArrayList<String> timeshop = new ArrayList<String>();
    public static ArrayList<String> day_of_week_now = new ArrayList<String>();

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;

    public static String force_online;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        sendNamePageGoogleAnalytics("HomeFragment");
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        deleteCache(getContext());

        bindView();
        setFont(tf);
        setColor();
        callgetShopData();
        mMapView.onCreate(savedInstanceState);

        return myFragmentView;
    }

    private void bindView(){
        layout_fragment = (LinearLayout)myFragmentView.findViewById(R.id.layout_fragment);
        app_bar_layout = (AppBarLayout)myFragmentView.findViewById(R.id.app_bar_layout);
        txt_toolbar = (TextView) myFragmentView.findViewById(R.id.txt_toolbar);
        iv_image_shop_name = (ImageView) myFragmentView.findViewById(R.id.iv_image_shop_name);
        txtDetail = (TextView) myFragmentView.findViewById(R.id.txtDetail);
        txtAddress = (TextView) myFragmentView.findViewById(R.id.txtAddress);
        txtTel = (TextView) myFragmentView.findViewById(R.id.txtTel);
        time = (TextView) myFragmentView.findViewById(R.id.tvTime);
        txtTimeshop = (TextView) myFragmentView.findViewById(R.id.txtTimeshop);
        titleAddress = (TextView) myFragmentView.findViewById(R.id.titleAddress);
        titleTel = (TextView) myFragmentView.findViewById(R.id.titleTel);
        titleTimeshop = (TextView) myFragmentView.findViewById(R.id.titleTimeshop);
        txtTimeshop = (TextView) myFragmentView.findViewById(R.id.txtTimeshop);
//        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        img_star1 = (ImageView) myFragmentView.findViewById(R.id.img_star1);
        img_star2 = (ImageView) myFragmentView.findViewById(R.id.img_star2);
        img_star3 = (ImageView) myFragmentView.findViewById(R.id.img_star3);
        img_star4 = (ImageView) myFragmentView.findViewById(R.id.img_star4);
        img_star5 = (ImageView) myFragmentView.findViewById(R.id.img_star5);
        viewFlipper = (ViewFlipper)myFragmentView.findViewById(R.id.viewFlipper);

        bt_status_shop = (ImageView) myFragmentView.findViewById(R.id.bt_status_shop);
        tvOrder =(TextView)myFragmentView.findViewById(R.id.tvOrder);
        imgSumOrder = (ImageView)myFragmentView.findViewById(R.id.imgSumOrder);

        mMapView = (MapView) myFragmentView.findViewById(R.id.map);

    }

    private void setFont(Typeface tf){
        txt_toolbar.setTypeface(tf);
        txtAddress.setTypeface(tf);
        txtDetail.setTypeface(tf);
        txtTel.setTypeface(tf);
        titleAddress.setTypeface(tf);
        titleTel.setTypeface(tf);
        time.setTypeface(tf);
        titleTimeshop.setTypeface(tf);
        txtTimeshop.setTypeface(tf);
    }

    private void setColor(){
        layout_fragment.setBackgroundColor(Color.parseColor(ColorPrimary));
        app_bar_layout.setBackgroundColor(Color.parseColor(ColorPrimary));
        txt_toolbar.setTextColor(Color.parseColor(TextColorPrimary));
        txtAddress.setTextColor(Color.parseColor(TextColorPrimary));
        txtDetail.setTextColor(Color.parseColor(TextColorPrimary));
        txtTel.setTextColor(Color.parseColor(TextColorPrimary));
        titleAddress.setTextColor(Color.parseColor(TextColorPrimary));
        titleTel.setTextColor(Color.parseColor(TextColorPrimary));
        time.setTextColor(Color.parseColor(TextColorPrimary));
        titleTimeshop.setTextColor(Color.parseColor(TextColorPrimary));
        txtTimeshop.setTextColor(Color.parseColor(TextColorPrimary));

    }

    private void setView(){
        setmap();

        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

        txtAddress.setText(address);
        txtDetail.setText(detail);
        txtTel.setText(phone);
        time.setText(upcoming_open);
//        timeshop = getTimeShop();

        for (int j = 0; j < timeshop.size(); j++){

//            if(j==Integer.valueOf(day_of_week_now.get(0))) {
//                txtTimeshop.append(timeshop.get(j)+" (OPEN)" + "\n");
//            }else{
            txtTimeshop.append(timeshop.get(j) + "\n");
//            }
        }

        Glide.with(getContext())
                .load(img_cover_thumb)
                .placeholder(R.drawable.load_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(iv_image_shop_name);
        if (now_opening == "true"){
            Glide.with(getContext())
                    .load(R.drawable.status_open)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(bt_status_shop);
        }else {
            Glide.with(getContext())
                    .load(R.drawable.status_close)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(bt_status_shop);
        }

        if (star==0.0){
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==0.5){
            Glide.with(getContext())
                    .load(R.drawable.star_half)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==1.0){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==1.5){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_half)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==2.0){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==2.5){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_half)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }else if (star==3.0){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);

        }else if (star==3.5){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_half)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);

        }else if (star==4.0){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);

        }else if (star==4.5){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_half)
                    .into(img_star5);

        }else if (star==5){
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_review)
                    .into(img_star5);

        }else {
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star1);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star2);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star3);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star4);
            Glide.with(getContext())
                    .load(R.drawable.star_empty)
                    .into(img_star5);
        }

//        imgSumOrder = (ImageView)myFragmentView.findViewById(R.id.imgSumOrder);
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

    //Check time opening shop
    public static String checkTimeOpen(){
        return now_opening;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    public void setmap(){
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                MapsInitializer.initialize(getContext());
                mGoogleMap = mMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                try {
                    String CurrentString = map_position;
                    String[] separated = CurrentString.split(",");

                    separated[0] = separated[0].trim();
                    separated[1] = separated[1].trim();

                    String lat = separated[0];
                    String lng = separated[1];

                    double valueLat = Double.parseDouble(lat);
                    double valueLng = Double.parseDouble(lng);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(valueLat,valueLng)).title(title).snippet(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));
                    CameraPosition Liberty = CameraPosition.builder().target(new LatLng(valueLat,valueLng)).zoom(16).bearing(0).tilt(45).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
                }catch (Exception e){

                }
            }
        });
    }

    private void callgetShopData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getShopData(getResources().getString(R.string.access_token),getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                JSONObject jsonObject = null;
//                                    JSONArray jsonArray = null;

                                jsonObject = new JSONObject(jsonData);

                                JSONObject jsonData = jsonObject.getJSONObject("data");
                                JSONObject json_info = jsonData.getJSONObject("info");
                                JSONArray shop_slider = jsonData.getJSONArray("shop_slider");

                                //setViewFlipper
                                for(int i=0; i<shop_slider.length(); i++){
                                    Log.i("Set Flipper Called", shop_slider.get(i).toString()+"");
                                    ImageView imageviewlide = new ImageView(getActivity());
                                    Glide.with(getActivity()).load(shop_slider.get(i).toString()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageviewlide);
                                    viewFlipper.addView(imageviewlide);
                                }
                                img_cover = json_info.getString("img_cover");
                                img_cover_thumb = json_info.getString("img_cover_thumb");
                                title = json_info.getString("title");
                                address = json_info.getString("address");
                                phone = json_info.getString("phone");
                                detail = json_info.getString("detail");
                                map_position = json_info.getString("map_position");
                                tax_rate = json_info.getString("tax_rate");

                                force_online = json_info.getString("force_online");


                                //get star rating point
                                JSONObject jsonratstar = jsonData.getJSONObject("rating_now");
                                Double rating = jsonratstar.getDouble("review_score");
//
                                star=rating;


                                JSONArray json_shop_open = jsonData.getJSONArray("shop_open");
                                timeshop.clear();
                                for (int j = 0; j < json_shop_open.length(); j++){
                                    timeshop.add(json_shop_open.get(j).toString());
                                }
//
                                now_opening = jsonData.getString("shop_status");
                                upcoming_open = jsonData.getString("shop_message");

                                setView();

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


    public String formatTime(String time){
        String lastTime= "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("HH:mm");
            DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            lastTime = outputFormat.format(inputFormat.parse(time));

        }catch (java.text.ParseException e){
            e.getMessage();
        }
        return lastTime;
    }

    public static ArrayList<String> getDay_of_week_now(){
        return day_of_week_now;
    }

    public  static ArrayList<String> getTimeShop(){
        return timeshop;
    }

    public static String checkForceOnline(){
        return force_online;
    }


}
