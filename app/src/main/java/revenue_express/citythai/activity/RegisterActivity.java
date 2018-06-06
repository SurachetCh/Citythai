package revenue_express.citythai.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class RegisterActivity extends AppCompatActivity {

    String URL_register,access_token,firstname,lastname,email,password,confirmpassword,msg;
    TextInputEditText edt_firstname,edt_lastname,edt_email,edt_password,edt_confirmpassword;
    CheckBox cb_accept;
    Button btn_Register;
    TextView tv_header_register,tv_display_register_firstname,tv_display_register_lastname,tv_display_register_email,tv_display_register_password,tv_display_register_confirm_password,tv_display_register_termandcondition;
    ImageView img_bg_register;
    JSONObject json = null;
    String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sendNamePageGoogleAnalytics("RegisterActivity");

        Typeface tf = Typeface.createFromAsset(RegisterActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        URL_register = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_register);
        access_token = getResources().getString(R.string.access_token);

        bindView();
        setFont(tf);
//        setColor();
        setView();
    }

    private void bindView(){
        img_bg_register = (ImageView)findViewById(R.id.img_bg_register) ;
        tv_header_register = (TextView)findViewById(R.id.tv_header_register);
        tv_display_register_firstname = (TextView)findViewById(R.id.tv_display_register_firstname);
        tv_display_register_lastname = (TextView)findViewById(R.id.tv_display_register_lastname);
        tv_display_register_email = (TextView)findViewById(R.id.tv_display_register_email);
        tv_display_register_password = (TextView)findViewById(R.id.tv_display_register_password);
        tv_display_register_confirm_password = (TextView)findViewById(R.id.tv_display_register_confirm_password);
        tv_display_register_termandcondition = (TextView)findViewById(R.id.tv_display_register_termandcondition);

        edt_firstname = (TextInputEditText)findViewById(R.id.edt_firstname);
        edt_lastname = (TextInputEditText)findViewById(R.id.edt_lastname);
        edt_email = (TextInputEditText)findViewById(R.id.edt_email);
        edt_password = (TextInputEditText)findViewById(R.id.edt_password);
        edt_confirmpassword = (TextInputEditText)findViewById(R.id.edt_confirmpassword);

        cb_accept = (CheckBox)findViewById(R.id.cb_accept);

        btn_Register = (Button)findViewById(R.id.btn_Register);
    }
    private void setFont(Typeface tf){
//Set type fonts
        tv_header_register.setTypeface(tf);
        tv_display_register_firstname.setTypeface(tf);
        tv_display_register_lastname.setTypeface(tf);
        tv_display_register_email.setTypeface(tf);
        tv_display_register_password.setTypeface(tf);
        tv_display_register_confirm_password.setTypeface(tf);
        tv_display_register_termandcondition.setTypeface(tf);
        edt_firstname.setTypeface(tf);
        edt_lastname.setTypeface(tf);
        edt_email.setTypeface(tf);
        edt_password.setTypeface(tf);
        edt_confirmpassword.setTypeface(tf);
        cb_accept.setTypeface(tf);
        btn_Register.setTypeface(tf);
    }
    private void setColor(){

    }
    private void setView(){
        if (SplashScreen.status_version = true){
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .into(img_bg_register);
        }else {
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_bg_register);
        }



        tv_display_register_termandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCondition("Term & Condition",getResources().getString(R.string.term_condition));
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firstname = edt_firstname.getText().toString();
                lastname = edt_lastname.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                confirmpassword = edt_confirmpassword.getText().toString();

                if (firstname.equals("")) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.firstname_must_be_filled), Toast.LENGTH_LONG).show();
                    return;
                }
                if(lastname.equals("")){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.lastname_must_be_filled), Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.email_must_be_filled), Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.equals("") || confirmpassword.equals("")){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.password_or_confirmpassword_must_be_filled), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmpassword)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.password_and_confirmpassword_must_be_different), Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i("ลงทะเบียน : ","ผู้ใช้ใหม่");

                if (cb_accept.isChecked()) {
                    callGetRegister(firstname, lastname, email, password ,confirmpassword , access_token);
                }else{
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.Please_click_accept_and_condition_before_register), Toast.LENGTH_LONG).show();
                    return;
                }
            }

        });
    }

    //-----Register User Email/Facebook/Google-----//
    private void callGetRegister(final String firstname, final String lastname, final String email, final String password , final String confirmpassword, final String access_token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit.create(APIService.class);
        api.getRegister(access_token,firstname,lastname,email,password,confirmpassword).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                                    showSuccess("Register",msg);
                                }else{
                                    showError("Register",msg);
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

    private void showCondition(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSuccess(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showError(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
