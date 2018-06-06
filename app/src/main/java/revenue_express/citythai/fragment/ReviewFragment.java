package revenue_express.citythai.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import revenue_express.citythai.activity.WriteReviewsActivity;
import revenue_express.citythai.adapter.ReviewsAdapter;
import revenue_express.citythai.dao.ReviewsDataDao;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.User;

import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ReviewFragment extends Fragment {
    LinearLayout layout_fragment;
    AppBarLayout app_bar_layout;
    private View myFragmentView;
    private TextView txt_toolbar;
    private Button btn_write_review;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static List<ReviewsDataDao> categoery;
    TextView tvOrder;
    String access_user_key,id,title,description,score,created_date,writer_id,writer_name,writer_photo;
    String status_reload = "0";
    ImageView imgSumOrder,imgLoad;
    RecyclerView reviews_recycler_view;
    public static Context context;
    Realm realm;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    String jsonData;
//    public static ReviewFragment newInstance() {
//        ReviewFragment fragment = new ReviewFragment();
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
//
//        return rootView;
//    }

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_review, container, false);

        sendNamePageGoogleAnalytics("ReviewFragment");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        Realm.init(getActivity());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        bindView();
        setFont(tf);
        setColor();
        setView();
        return myFragmentView;
    }

    private void bindView(){
        reviews_recycler_view = (RecyclerView) myFragmentView.findViewById(R.id.reviews_recycler_view);
        imgLoad =(ImageView)myFragmentView.findViewById(R.id.imgLoad);

        txt_toolbar = (TextView) myFragmentView.findViewById(R.id.txt_toolbar);
        app_bar_layout = (AppBarLayout)myFragmentView.findViewById(R.id.app_bar_layout);
        btn_write_review = (Button) myFragmentView.findViewById(R.id.tv_write_review);
        tvOrder =(TextView)myFragmentView.findViewById(R.id.tvOrder);
        imgSumOrder = (ImageView)myFragmentView.findViewById(R.id.imgSumOrder);
        swipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swipe_refresh_layout);
        layout_fragment = (LinearLayout)myFragmentView.findViewById(R.id.layout_fragment);
    }
    private void setFont(Typeface tf){
//Set type fonts
        txt_toolbar.setTypeface(tf);
        btn_write_review.setTypeface(tf);
    }
    private void setColor(){
        reviews_recycler_view.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
        btn_write_review.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
        txt_toolbar.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
        app_bar_layout.setBackgroundColor(Color.parseColor(ColorPrimary));
        btn_write_review.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
        layout_fragment.setBackgroundColor(Color.parseColor(ColorPrimary));
    }
    private void setView(){

        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        if(user_all.size() == 0 ){
            access_user_key = "";
        }else{
            access_user_key = user_all.get(0).getAccess_user_key();
        }
        realm.commitTransaction();

        categoery = new ArrayList<ReviewsDataDao>();
        Glide.with(this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);
        btn_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_all.size() == 0 ){
                    Toast.makeText(getActivity(),getResources().getString(R.string.write_review_login_before), Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), WriteReviewsActivity.class);
                    startActivity(intent);
                }
            }
        });
        callgetReview();
//            setrecycler();


        imgSumOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (tvOrder.getText().equals("0")){
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ReviewsActivity.this);
//                    builder.setCancelable(false);
//                    builder.setMessage(getResources().getString(R.string.text_null_order));
//                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //if user pressed "yes", then he is allowed to exit from application
//
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }else {
////                    Intent intent = new Intent(getActivity(), SumOrderActivity.class);
////                    startActivity(intent);
//                }
            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Get shop detail
                try {
                    categoery.clear();
                    status_reload = "1";
                    callgetReview();
                }catch (Exception e){
                    e.getMessage();
                }
            }
        });

        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();


        imgSumOrder = (ImageView)myFragmentView.findViewById(R.id.imgSumOrder);
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
        imgLoad.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(),categoery);
        reviews_recycler_view.setAdapter(reviewsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        reviews_recycler_view.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check Shop list
        realm.beginTransaction();
        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
        tvOrder.setText(String.valueOf(cartItemLists.size()));
        realm.commitTransaction();

        setView();

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

    private void callgetReview(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getReview(getResources().getString(R.string.access_token),getResources().getString(R.string.shop_id_ref),"desc").enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                    Log.d("Data review: ", jsonObject2.toString());
                                    JSONObject review = jsonObject2.getJSONObject("review");
                                    JSONObject writer = jsonObject2.getJSONObject("writer");

                                    //Get data review
                                    id = review.getString("id");
                                    title = review.getString("title");
                                    description = review.getString("description");
                                    score = review.getString("score");
                                    created_date = review.getString("created_date");

                                    //Get data writer
                                    writer_id = writer.getString("id");
                                    writer_name = writer.getString("name");
                                    writer_photo = writer.getString("photo_thumb");

                                    ArrayList<String> jsonArrayImage = new ArrayList<>();
                                    ArrayList<String> jsonArrayCaption = new ArrayList<>();

                                    JSONArray photo = jsonObject2.getJSONArray("photo");

                                    if (photo.length() != 0) {
                                        for (int j = 0; j < photo.length(); j++) {
                                            JSONObject urlImage = photo.getJSONObject(j);
                                            jsonArrayImage.add(urlImage.getString("img"));
                                        }

                                        System.out.println("Array Image: " + jsonArrayImage.toString());

                                        if (photo.length() != 0) {
                                            for (int j = 0; j < photo.length(); j++) {
                                                JSONObject caption = photo.getJSONObject(j);
                                                jsonArrayCaption.add(caption.getString("caption"));
                                            }
                                        }

                                        System.out.println("Array Image: " + jsonArrayCaption.toString());
                                    }

                                    categoery.add(new ReviewsDataDao(id, title, description, score, created_date, writer_id, writer_name, writer_photo, jsonArrayImage, jsonArrayCaption));
                                }

                                setrecycler();
                            } catch (JSONException e) {
                                e.getMessage();
                            }
                            imgLoad.setVisibility(View.INVISIBLE);

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
