package revenue_express.citythai.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.model.APIService;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;
    Realm realm;
    public static String ColorPrimary = null,TextColorPrimary = null,logo_url = null,splash_url = null,login_url = null,version,old_version = null;
    public static Boolean status_version = true;
    public static Boolean check_signin = true;
    ImageView img_splash_screen;
    String access_token;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    String jsonData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no statudbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        access_token = getResources().getString(R.string.access_token);
        img_splash_screen = (ImageView)findViewById(R.id.img_splash_screen);
        // call service
        callGetTheme();

        //GoogleAnalytics
        sendNamePageGoogleAnalytics("SplashScreenActivity");

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        };
    }
    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    private void callGetTheme(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getTheme(getResources().getString(R.string.access_token),getResources().getString(R.string.shop_id_ref)).enqueue(new retrofit2.Callback<ResponseBody>() {
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

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    Log.d("Data Theme: ", jsonObject2.toString());

                                    logo_url = jsonObject2.getString("logo_url");
                                    splash_url = jsonObject2.getString("splash_url");
                                    login_url = jsonObject2.getString("login_url");
                                    ColorPrimary = jsonObject2.getString("back_color");
                                    TextColorPrimary = jsonObject2.getString("font_color");
                                    version = jsonObject2.getString("version");

//                                                ColorPrimary = "#785447";

                                }

                                //set image splash screen
                                Glide.with(SplashScreen.this)
                                        .load(splash_url)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(img_splash_screen);



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

    public void connectInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            Log.d("สถานะการเชื่อมต่อ", "ต่อ internet");

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            Log.d("สถานะการเชื่อมต่อ", "ไม่ต่อ internet");
            checkInternet(getResources().getString(R.string.status_connect), getResources().getString(R.string.disconnection));
        }
    }

    //Check internet
    private void checkInternet(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getResources().getString(R.string.setting);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                });

        String negativeText = getResources().getString(R.string.close);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
