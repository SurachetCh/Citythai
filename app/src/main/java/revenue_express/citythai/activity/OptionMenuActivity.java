package revenue_express.citythai.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import revenue_express.citythai.R;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class OptionMenuActivity extends AppCompatActivity {
    Realm realm;
    public static Context context;
//
//    public static MenuOrderAdapter MAdapter;
//    public static ArrayList<MenuOrderDataDao> MenuOrderDataListDaoArrayList;
//    private ArrayList<Integer> List;
    public static JSONObject jsonObject = null;
    public static JSONArray jsonArray = null;
    TextView tv_test;

    //Ok http3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData,category_id,category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);

        sendNamePageGoogleAnalytics("MenuOrderActivity");

        try {
            jsonArray = new JSONArray(getIntent().getExtras().getString("jsonArrayOption"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Typeface tf = Typeface.createFromAsset(OptionMenuActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        bindView();
        setFont(tf);
        setColor();
        setView();





    }

    private void bindView(){
        tv_test = (TextView)findViewById(R.id.tv_test);
    }
    private void setFont(Typeface tf){

    }
    private void setColor(){

    }
    private void setView(){
        tv_test.setText(jsonArray.toString());
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check Shop list
    }

//    public static void SumOnlineOrder(int total){
//        String total_last = String.valueOf(total);
//        tvOrder.setText(total_last);
//    }
//
//    private void callSyncGet(final String url) {
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
////                imgLoad.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                System.out.println("Url Value:"+url.toString());
//
//                FormBody.Builder formBuilder = new FormBody.Builder();
//                formBuilder.add("access_token", access_token);
//                formBuilder.add("category_id", category_id);
//                formBuilder.add("shop", getResources().getString(R.string.shop_id_ref));
//
//                MediaType.parse("application/json; charset=utf-8");
//                RequestBody formBody = formBuilder.build();
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(formBody)
//                        .build();
//
//                client.newCall(request)
//                        .enqueue(new Callback() {
//
//                            @Override
//                            public void onFailure(final Call call, IOException e) {
//                                // Error
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                                if (!response.isSuccessful()) {
//                                    throw new IOException("Unexpected code " + response);
//                                } else {
//                                    Log.i("Response:",response.toString());
//                                    jsonData = response.body().string();
//                                }
//
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//
//                                            jsonObject = new JSONObject(jsonData);
//                                            jsonArray = jsonObject.getJSONArray("data");
//
//                                            for (int i = 0; i < jsonArray.length(); ++i) {
//                                                JSONObject jsonObject_list_id = jsonArray.getJSONObject(i);
//
//                                                        Integer id_menu = jsonObject_list_id.getInt("id");
//                                                        String name_menu = jsonObject_list_id.getString("name");
//                                                        String description = jsonObject_list_id.getString("detail");
//                                                        Integer multisize = jsonObject_list_id.getInt("multisize");
//                                                        Integer online = jsonObject_list_id.getInt("online");
//                                                        Double price = jsonObject_list_id.getDouble("price");
//                                                        String img_thumb = jsonObject_list_id.getString("img_thumb");
//                                                        String img_full = jsonObject_list_id.getString("img_full");
//
//                                                        MenuOrderDataListDaoArrayList.add(new MenuOrderDataDao(id_menu, name_menu, description, multisize, online, price,img_thumb,img_full));
//                                            }
//
//                                            MAdapter = new MenuOrderAdapter(OptionMenuActivity.this,MenuOrderDataListDaoArrayList);
//                                            order_recycler_view.setAdapter(MAdapter);
//
//                                        } catch (JSONException e) {
//                                            e.getMessage();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                return null;
//            }
//
//        }.execute();
//    }
}
