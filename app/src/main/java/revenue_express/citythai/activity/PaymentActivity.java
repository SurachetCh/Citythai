package revenue_express.citythai.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.OrderList;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


/**
 * Created by surachet on 4/9/2560.
 */

public class PaymentActivity extends AppCompatActivity {
    Realm realm;
    ImageView imgCreditCard,imgCash,imgBack;
    ImageView imgLoad;
    String jsonData;
    JSONObject jsonObject;
    String payment_name,card_number,cvv,card_month,card_year;
    TextView tv_total;
    TextInputEditText edt_payment_name,edt_card_number,edt_cvv;
    Button btn_payment;
    Spinner spn_month,spn_year;
    String url_payment;
    LinearLayout layout_credit_card;
    FrameLayout layout_total;
    String statuspayment = "credit";
    String[] array_month = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        sendNamePageGoogleAnalytics("PaymentActivity");

        bindView();
//        setFont(tf);
//        setColor();
        setView();




    }

    private void bindView(){
        layout_credit_card = (LinearLayout)findViewById(R.id.layout_credit_card);
        layout_total = (FrameLayout) findViewById(R.id.layout_total);
        imgCreditCard = (ImageView)findViewById(R.id.imgCreditCard);
        imgCash = (ImageView)findViewById(R.id.imgCash);
        edt_payment_name = (TextInputEditText)findViewById(R.id.edt_payment_name);
        edt_card_number = (TextInputEditText)findViewById(R.id.edt_card_number);
//        edt_expired_date = (EditText)findViewById(R.id.edt_expired_date);
        tv_total = (TextView)findViewById(R.id.tv_total);
        edt_cvv = (TextInputEditText)findViewById(R.id.edt_cvv);
        spn_month = (Spinner)findViewById(R.id.spn_month);
        spn_year = (Spinner)findViewById(R.id.spn_year);
        btn_payment = (Button)findViewById(R.id.btn_payment);
        imgLoad = (ImageView)findViewById(R.id.imgLoad);
        imgBack = (ImageView)findViewById(R.id.imgBack);
    }
//    private void setFont(Typeface tf){
//
//    }
//    private void setColor(){
//
//    }
    private void setView(){
        tv_total.setText(" $  " +String.format("%.2f",(SumOrderActivity.GrandTotal)));
        Glide.with(this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);
        imgLoad.setVisibility(View.INVISIBLE);
        url_payment = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_payment);
        ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_month);
        spn_month.setAdapter(adapter_month);
        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                card_month = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        int year_current = (Calendar.getInstance().get(Calendar.YEAR))-2000;
        String[] array_year = new String[]{String.valueOf(year_current), String.valueOf(year_current+1), String.valueOf(year_current+2), String.valueOf(year_current+3), String.valueOf(year_current+4), String.valueOf(year_current+5), String.valueOf(year_current+6)};
        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_year);
        spn_year.setAdapter(adapter_year);
        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                card_year = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
//        imgCreditCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Glide.with(getApplicationContext())
//                        .load(R.drawable.bgtab1)
//                        .into(imgCreditCard);
//                Glide.with(getApplicationContext())
//                        .load(R.drawable.bgtab2)
//                        .into(imgCash);
//                layout_total.setVisibility(View.GONE);
//                layout_credit_card.setVisibility(View.VISIBLE);
//                statuspayment = "credit";
////                payment();
//            }
//        });
//        imgCash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Glide.with(getApplicationContext())
//                        .load(R.drawable.bgtab2)
//                        .into(imgCreditCard);
//                Glide.with(getApplicationContext())
//                        .load(R.drawable.bgtab1)
//                        .into(imgCash);
//                layout_total.setVisibility(View.VISIBLE);
//                layout_credit_card.setVisibility(View.GONE);
//                statuspayment = "cash";
//            }
//        });
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (statuspayment == "credit"){
                card_number = String.valueOf(edt_card_number.getText());
                payment_name = String.valueOf(edt_payment_name.getText());
//                expired_date = String.valueOf(edt_expired_date.getText());
                cvv = String.valueOf(edt_cvv.getText());
//                imgLoad.setVisibility(View.VISIBLE);
                if (card_number.length() != 16){
                    showDialog("", getResources().getString(R.string.invalid_data));
                }else if (cvv.length() != 3){
                    showDialog("", getResources().getString(R.string.invalid_data));
                } else{
                    callGetSendPayment();
                }


//                    showStatus("","Please,Payment at restauran","success");
//                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
//        DeleteOrderListRealm();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        super.onBackPressed();
    }

    private void callGetSendPayment(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit.create(APIService.class);
        imgLoad.setVisibility(View.VISIBLE);
        api.getPayment(getResources().getString(R.string.access_token),SumOrderActivity.access_user_key,card_number,payment_name,card_month,card_year,cvv,SumOrderActivity.OrderID,String.format("%.2f",(SumOrderActivity.total))).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                            Log.i("Result shop name", jsonData);
                            if (jsonData.toLowerCase().contains("true")){
                                imgLoad.setVisibility(View.INVISIBLE);
                                showStatus("Payment","success full","success");
//                                            payment();
//                                            DeleteOrderListRealm();
                                showStatus("Order Online Status", String.valueOf(getResources().getString(R.string.order_success)),"success");
                            }else {
                                imgLoad.setVisibility(View.INVISIBLE);
//                                            showStatus("Order Online Status", String.valueOf(getResources().getString(R.string.order_fail)),"fail");
                                try {
                                    jsonObject = new JSONObject(jsonData);
                                    JSONArray mesg_array = jsonObject.getJSONArray("mesg");
                                    String ResultData = (String) jsonObject.get("ResultData");
                                    String mesg="";
                                    if (mesg_array.length() == 0){
                                        mesg = ResultData;
                                    }else {
                                        for (int i = 0; i < mesg_array.length(); i++) {
                                            mesg = mesg+mesg_array.getString(i);
                                        }
                                    }
                                    showStatus("Payment",mesg,"false");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                imgLoad.setVisibility(View.INVISIBLE);
            }
        });
    }
    //-----Show Status Dialog-----//
    private void showStatus(String title, String msg, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        statuspayment = "";
                        if (status.equals("success")){
                            DeleteOrderListRealm();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void QueryRealm() {
        RealmQuery<OrderList> query = realm.where(OrderList.class);
        String count = String.valueOf(query.findAll());
        Log.e("Results", String.valueOf(count));
    }
    private void DeleteOrderListRealm(){
        realm.beginTransaction();
        RealmResults<CartItemList> result = realm.where(CartItemList.class).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
