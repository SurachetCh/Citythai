package revenue_express.citythai.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.adapter.RecommendMenuReviewAdapter;
import revenue_express.citythai.adapter.ReviewDetailAdapter;
import revenue_express.citythai.dao.RecommendMenuReviewDataDao;
import revenue_express.citythai.dao.ReviewDetailDataDao;
import revenue_express.citythai.model.APIService;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class ReviewDetailActivity extends AppCompatActivity {

    TextView txt_toolbar,tvTitle,tvDesc,tvUser,tv_recommend_menu,tv_share_photos,tvPostdate;
    AvatarImageView imgUser;
    String access_token,reviewID;
    String review_title,review_desc,review_score,review_create_date,writer_id,writer_name,writer_photo;
    RecyclerView review_detail_recycler_view,recommend_menu_review_recycler_view;
    ImageView btnBack;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout ll_review_share_photo,ll_review_recommend_menu,ll_star_review_detail;
    private List<ReviewDetailDataDao> reviewShow;
    private ArrayList<RecommendMenuReviewDataDao> RecommendMenuReview;
    private ReviewDetailAdapter mAdapter;
    private RecommendMenuReviewAdapter rAdapter;

    //Ok http3
    private final OkHttpClient client = new OkHttpClient();
    String jsonData;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        sendNamePageGoogleAnalytics("ReviewDetailActivity");

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

        Typeface tf = Typeface.createFromAsset(ReviewDetailActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        access_token = getResources().getString(R.string.access_token);

        bindView();
        setFont(tf);
        setColor();
        setView();
    }

    private void bindView(){
        txt_toolbar = (TextView)findViewById(R.id.txt_toolbar);
        tvUser = (TextView)findViewById(R.id.tvUser);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        tv_recommend_menu = (TextView)findViewById(R.id.tv_recommend_menu);
        tv_share_photos = (TextView)findViewById(R.id.tv_share_photos);

        ll_review_recommend_menu = (LinearLayout)findViewById(R.id.ll_review_recommend_menu);
        ll_review_share_photo = (LinearLayout)findViewById(R.id.ll_review_share_photo);
        ll_star_review_detail = (LinearLayout)findViewById(R.id.ll_star_review_detail);

        tvPostdate = (TextView)findViewById(R.id.tvPostdate);
        imgUser = (AvatarImageView)findViewById(R.id.imgUser);
        btnBack =(ImageView) findViewById(R.id.imgBack);
        review_detail_recycler_view = (RecyclerView) findViewById(R.id.review_detail_recycler_view);
        recommend_menu_review_recycler_view = (RecyclerView) findViewById(R.id.recommend_menu_review_recycler_view);
    }
    private void setFont(Typeface tf){
//Set type fonts
        txt_toolbar.setTypeface(tf);
        tvUser.setTypeface(tf);
        tv_share_photos.setTypeface(tf);
        tvTitle.setTypeface(tf);
        tvDesc.setTypeface(tf);
        tvPostdate.setTypeface(tf);
        tv_recommend_menu.setTypeface(tf);
    }
    private void setColor(){
        txt_toolbar.setTextColor(Color.parseColor(SplashScreen.TextColorPrimary));
    }
    private void setView(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reviewShow = new ArrayList<ReviewDetailDataDao>();
        RecommendMenuReview = new ArrayList<RecommendMenuReviewDataDao>();

        reviewID = getIntent().getStringExtra("review_id");



//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        } else {
//            layoutManager = new GridLayoutManager(getApplicationContext(), 1);
//        }

        layoutManager = new GridLayoutManager(getApplicationContext(), 1);

        review_detail_recycler_view.setLayoutManager(layoutManager);
        review_detail_recycler_view.setItemAnimator(new DefaultItemAnimator());
        review_detail_recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommend_menu_review_recycler_view.setLayoutManager(layoutManager1);
        recommend_menu_review_recycler_view.setItemAnimator(new DefaultItemAnimator());
        recommend_menu_review_recycler_view.setHasFixedSize(true);
        recommend_menu_review_recycler_view.setNestedScrollingEnabled(false);

        callGetReviewShow(access_token , reviewID);

    }

    @Override
    public void onResume(){
        super.onResume();
        //        realm.beginTransaction();
        //        RealmResults<OrderList> shop_list = realm.where(OrderList.class).findAll();
        //
        //        ranks = 0;
        //
        //        for (int i = 0; i < shop_list.size(); i++){
        //            ranks = ranks + shop_list.get(i).getAmount();
        //            Log.d("Ranks : ", String.valueOf(ranks));
        //        }
        //        realm.commitTransaction();
        //
        //        if (shop_list.size() == 0 ){
        //            tvOrder.setText("0");
        //        }else {
        //            tvOrder.setText(String.valueOf(ranks));
        //        }

    }

    private void callGetReviewShow(final String access_token, final String reviewID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit.create(APIService.class);
        api.getReviewShow(access_token,reviewID).enqueue(new retrofit2.Callback<ResponseBody>() {
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

                                JSONObject jsonObject = null;
                                JSONObject jsonArray = null;

                                Log.d("Data Review: ",jsonData.toString());

                                jsonObject = new JSONObject(jsonData);
                                jsonArray = jsonObject.getJSONObject("data");

                                JSONObject review = jsonArray.getJSONObject("review");

                                JSONObject writer = jsonArray.getJSONObject("writer");

                                review_title = review.getString("title");
                                review_desc = review.getString("description");
                                review_score = review.getString("score");
                                review_create_date = review.getString("created_date");

                                writer_id = writer.getString("id");
                                writer_name = writer.getString("name");
                                writer_photo = writer.getString("photo_thumb");

                                tvUser.setText(writer_name);
                                tvTitle.setText(review_title);
                                tvDesc.setText(review_desc);
                                tvPostdate.setText(review.getString("created_timeago"));

                                Glide.with(ReviewDetailActivity.this).load(writer.getString("photo")).centerCrop().placeholder(R.drawable.male).into(imgUser);

                                Integer star = review.getInt("score");

                                ll_star_review_detail.removeAllViews();

                                int resultStar = 5-star;

                                for(int i= 0;i < star; i++)
                                {
                                    ImageView ll_star_shop= new ImageView(ReviewDetailActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
                                    ll_star_shop.setLayoutParams(params);
                                    ll_star_shop.setBackgroundResource(R.drawable.star_review);
                                    ll_star_review_detail.addView(ll_star_shop);
                                }

                                for(int i= 0;i < resultStar; i++)
                                {
                                    ImageView ll_star_shop= new ImageView(ReviewDetailActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
                                    ll_star_shop.setLayoutParams(params);
                                    ll_star_shop.setBackgroundResource(R.drawable.star_empty);
                                    ll_star_review_detail.addView(ll_star_shop);
                                }
//TODO set image


                                if(jsonArray.isNull("photo")) {
                                    ll_review_share_photo.setVisibility(View.INVISIBLE);
                                    Log.i("Photo", "Photo Is Null");
                                }else {
                                    JSONArray photo = jsonArray.getJSONArray("photo");
                                    if (photo.isNull(0)){
                                        ll_review_share_photo.setVisibility(View.INVISIBLE);
                                    Log.i("Photo","Photo Is Null");
                                    }else {
                                        for (int j = 0; j < photo.length(); j++) {
                                            JSONObject urlImage = null;
                                            try {
                                                urlImage = photo.getJSONObject(j);
                                                String imgFoodReview = urlImage.getString("img");
                                                String imgCaptionReview = urlImage.getString("caption");
                                                Log.d("Image | Caption: ", imgFoodReview + "|" + imgCaptionReview);
                                                reviewShow.add(new ReviewDetailDataDao(imgFoodReview, imgCaptionReview));
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }

                                        }
                                        mAdapter = new ReviewDetailAdapter(ReviewDetailActivity.this,reviewShow);
                                        review_detail_recycler_view.setAdapter(mAdapter);
                                    }
                                }

// TODO menu_recommend
                                if(jsonArray.isNull("recommend_menu")) {
                                    ll_review_recommend_menu.setVisibility(View.INVISIBLE);
                                    Log.i("Recommend Menu","Recommend Menu Is Null");
                                }else {
                                    JSONArray recommend_menu = jsonArray.getJSONArray("recommend_menu");
                                    if (recommend_menu.isNull(0)){
                                        ll_review_recommend_menu.setVisibility(View.INVISIBLE);
                                        Log.i("Recommend Menu","Recommend Menu Is Null");
                                    }else {
                                        if (recommend_menu.length() != 0) {
                                            for (int j = 0; j < recommend_menu.length(); j++) {
                                                JSONObject recommend_menu_object = recommend_menu.getJSONObject(j);
                                                String recommend_menu_name = recommend_menu_object.getString("name");
                                                String recommend_menu_photo = recommend_menu_object.getString("photo");

                                                Log.i("Recommend Menu Name: ",recommend_menu_name.toString());
                                                Log.i("Recommend Menu photo: ",recommend_menu_photo.toString());


                                                RecommendMenuReview.add(new RecommendMenuReviewDataDao(recommend_menu_name,recommend_menu_photo));
                                            }

                                            rAdapter = new RecommendMenuReviewAdapter(ReviewDetailActivity.this,RecommendMenuReview);
                                            recommend_menu_review_recycler_view.setAdapter(rAdapter);
                                        }
                                    }
                                }
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
