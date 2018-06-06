package revenue_express.citythai.activity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.model.APIService;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ForgetPasswordActivity extends AppCompatActivity {

    String m_user,msg;
    EditText edit_email;
    TextView tv_header_forget_password;
    Button btnAccept;

    JSONObject json = null;
    String jsonData;

    ImageView img_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Typeface tf = Typeface.createFromAsset(ForgetPasswordActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        sendNamePageGoogleAnalytics("ForgetPasswordActivity");

        bindView();
        setFont(tf);
        setColor();
        setView();



    }

    private void bindView(){
        tv_header_forget_password = (TextView)findViewById(R.id.tv_header_forget_password);

        edit_email = (EditText)findViewById(R.id.editemail);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        img_forget_password = (ImageView)findViewById(R.id.img_forget_password);
    }
    private void setFont(Typeface tf){
//Set type fonts
        edit_email.setTypeface(tf);
        btnAccept.setTypeface(tf);
        tv_header_forget_password.setTypeface(tf);
    }
    private void setColor(){

    }
    private void setView(){

        if (SplashScreen.status_version = true){
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .into(img_forget_password);
        }else {
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_forget_password);
        }



        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                m_user = edit_email.getText().toString();
                m_user.trim();

                if(m_user.equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.username_or_password_not_null), Toast.LENGTH_LONG).show();
                    return;
                }
                callGetForgetPassword();
            }

        });
    }

    private void callGetForgetPassword(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();

        APIService api = retrofit.create(APIService.class);
        api.getForgetPassword(getResources().getString(R.string.access_token),m_user).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                json = new JSONObject(jsonData);
                                msg = json.getString("message");

                                if(jsonData.toLowerCase().contains("success")){
                                    showLocationDialog("Forget Password",msg);
                                    return;
                                }else{
                                    showLocationDialog("Forget Password",msg);
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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

    //-----Show dialog box-----//
    private void showLocationDialog(String title , String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
