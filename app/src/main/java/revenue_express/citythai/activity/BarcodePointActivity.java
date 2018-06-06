package revenue_express.citythai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.User;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class BarcodePointActivity extends AppCompatActivity {

    String URL_idn_barcode,access_token,access_user_key,shop_id_ref;
    ImageView iv_barcode;
    Button btn_history;
    TextView tv_point;
    LinearLayout layout_barcode_point;
    Realm realm;

    String jsonData,points;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no statudbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barcode_point);

        sendNamePageGoogleAnalytics("BarcodePointActivity");

        URL_idn_barcode = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_idn_barcode);
        access_token = getResources().getString(R.string.access_token);
        shop_id_ref = getResources().getString(R.string.shop_id_ref);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        bindView();
//        setFont(tf);
        setColor();
        setView();
    }

    private void bindView(){
        iv_barcode = (ImageView) findViewById(R.id.iv_barcode);
        btn_history = (Button) findViewById(R.id.btn_history);
        tv_point = (TextView) findViewById(R.id.tv_point);
        layout_barcode_point = (LinearLayout)findViewById(R.id.layout_barcode_point);
    }
//    private void setFont(Typeface tf){
//
//    }
    private void setColor(){
        layout_barcode_point.setBackgroundColor(Color.parseColor(SplashScreen.ColorPrimary));
    }
    private void setView(){
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointHistoryPageActivity.class);
                startActivity(intent);
            }
        });

        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        if(user_all.size() == 0 ){
            access_user_key = "";
        }else{

            access_user_key = user_all.get(0).getAccess_user_key();
        }
        realm.commitTransaction();

        Log.i("Barcode Point: ",URL_idn_barcode+"?access_token="+access_token+"&access_user_key="+access_user_key);

        Glide.with(this)
                .load(URL_idn_barcode+"?access_token="+access_token+"&access_user_key="+access_user_key)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .placeholder(R.drawable.load_image)
                .into(iv_barcode);

//        callSyncGetPointMember(URL_get_point,access_user_key,shop_id_ref);
        callGetPointMember(access_user_key);
    }

    private void callGetPointMember(final String access_user_key){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getPion(getResources().getString(R.string.access_token),access_user_key,getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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
