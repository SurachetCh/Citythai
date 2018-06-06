package revenue_express.citythai.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.adapter.PointHistoryAdapter;
import revenue_express.citythai.dao.PointHistoryDataDao;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.User;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class PointHistoryPageActivity extends AppCompatActivity {

    RecyclerView point_history_recycler_view;
    List<PointHistoryDataDao> pointHistoryList;
    PointHistoryAdapter mAdapter;
    String URL_get_point,URL_history_point,access_token,access_user_key,shop_id_ref,points;
    TextView tv_point,tv_noRecord;
    RelativeLayout layout_point_history;
    Realm realm;

    //Ok http3
    private final OkHttpClient client = new OkHttpClient();
    String jsonData,result;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no statudbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_point_history_page);

        sendNamePageGoogleAnalytics("PointHistoryPageActivity");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        if(user_all.size() == 0 ){
            access_user_key = "";
        }else{

            access_user_key = user_all.get(0).getAccess_user_key();
        }
        realm.commitTransaction();

        access_token = getResources().getString(R.string.access_token);
        shop_id_ref = getResources().getString(R.string.shop_id_ref);

        bindView();
//        setFont(tf);
        setColor();
        setView();




    }

    private void bindView(){
        layout_point_history = (RelativeLayout) findViewById(R.id.layout_point_history);
        tv_point = (TextView) findViewById(R.id.tv_point);
        tv_noRecord = (TextView) findViewById(R.id.tv_noRecord);
    }
//    private void setFont(Typeface tf){
//
//    }
    private void setColor(){
        layout_point_history.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
    }
    private void setView(){
        tv_noRecord.setVisibility(View.GONE);

        pointHistoryList = new ArrayList<PointHistoryDataDao>();

        point_history_recycler_view = (RecyclerView) findViewById(R.id.point_history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        point_history_recycler_view.setLayoutManager(layoutManager);
        point_history_recycler_view.setItemAnimator(new DefaultItemAnimator());
        point_history_recycler_view.setHasFixedSize(true);

        callGetPointMember();
    }

    private void callGetHistoryPointMember(){
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit1.create(APIService.class);
        api.getHistoryPointMember(getResources().getString(R.string.access_token),access_user_key,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                result = jsonObject.getString("result");

                                if (result.equals("true")) {
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    for (int j = 0; j < data.length(); j++) {
                                        JSONObject dataArray = data.getJSONObject(j);

                                        String status = dataArray.getString("action");
                                        String date = dataArray.getString("adate");
                                        String pointDollar = dataArray.getString("point");

                                        String pointStar = dataArray.getString("order_amount");

                                        pointHistoryList.add(new PointHistoryDataDao(pointStar, pointDollar, status, date));
                                    }

                                    mAdapter = new PointHistoryAdapter(PointHistoryPageActivity.this, pointHistoryList);
                                    point_history_recycler_view.setAdapter(mAdapter);

                                }else{
                                    //String message = jsonObject.getString("message");
                                    //showStatusSync("Status",message);
                                    tv_noRecord.setVisibility(View.VISIBLE);
                                }

                            }catch (JSONException e){
                                e.getMessage();                                        }

                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    private void callGetPointMember(){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit2.create(APIService.class);
        api.getPointMember(getResources().getString(R.string.access_token),access_user_key,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                points = jsonObject.getString("points");

                                tv_point.setText(points);

                                //Get Point Member
//                                callSyncGetPointMember(URL_history_point,access_token,access_user_key,shop_id_ref);
                                callGetHistoryPointMember();
                            }catch (JSONException e){
                                e.getMessage();                                        }

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
