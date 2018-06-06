package revenue_express.citythai.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import revenue_express.citythai.R;
import revenue_express.citythai.model.APIService;
import revenue_express.citythai.model.CartItemList;
import revenue_express.citythai.model.User;

import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    TextInputEditText editName,editPassword;
    String URL_email, URL_facebook_token,URL_google_token ,access_token,access_user_key,shop_id,
            Name,Password, name_user,firstname_user,lastname_user,email_user,level_user,reward_points,
            gender,photo_thumb,email;
    Integer id_user;
    ImageView imgSkip,img_bg_login,img_logo;
    TextView tv_display_sign_email,tv_display_sign_member_yet,tv_display_sign_forget_password,tv_display_sign_password;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    public static GoogleApiClient mGoogleApiClient;

    //login facebook or google
    Button fb,btnSignIn,btnSignInGoogle;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    String msg,status_login;
    JSONObject json = null;
    Realm realm;
    static String jsonData1,jsonData2,jsonData3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);

        SplashScreen.check_signin = false;
        //GoogleAnalytics
        sendNamePageGoogleAnalytics("SignInActivity");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Typeface tf = Typeface.createFromAsset(SignInActivity.this.getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        access_token = getResources().getString(R.string.access_token);
        shop_id = getResources().getString(R.string.shop_id_ref);
        URL_email = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_signin);
        URL_facebook_token = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_facebook_login_token);
        URL_google_token = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_google_login_token);

        status_login = "null";

        bindView();
        setFont(tf);
        setColor();
        setView();
//        if (getIntent().getExtras().getString("CheckLogIn").equals("true")){
//
////            FacebookSdk.sdkInitialize(getActivity());
////                            // Logout facebook
////            LoginManager.getInstance().logOut();
//            LoginManager.getInstance().logOut();
//            //Logout google
//            logoutUser();
//        }
    }

    private void bindView(){
        img_bg_login = (ImageView)findViewById(R.id.img_bg_login);
        img_logo = (ImageView)findViewById(R.id.img_logo);
        tv_display_sign_email = (TextView) findViewById(R.id.tv_display_sign_email);
        tv_display_sign_member_yet = (TextView) findViewById(R.id.tv_display_sign_member_yet);
        tv_display_sign_forget_password = (TextView) findViewById(R.id.tv_display_sign_forget_password);
        tv_display_sign_password = (TextView) findViewById(R.id.tv_display_sign_password);

        editPassword = (TextInputEditText) findViewById(R.id.editPassword);
        editName = (TextInputEditText) findViewById(R.id.editName);

        imgSkip = (ImageView) findViewById(R.id.imgskip);

        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignInGoogle = (Button) findViewById(R.id.btnSignInGoogle);
    }
    private void setFont(Typeface tf){
//Set type fonts
        tv_display_sign_email.setTypeface(tf);
        tv_display_sign_member_yet.setTypeface(tf);
        tv_display_sign_forget_password.setTypeface(tf);
        tv_display_sign_password.setTypeface(tf);

        editPassword.setTypeface(tf);
        editName.setTypeface(tf);

        fb.setTypeface(tf);
        loginButton.setTypeface(tf);
        btnSignIn.setTypeface(tf);
        btnSignInGoogle.setTypeface(tf);
    }
    private void setColor(){

    }
    private void setView(){
        if (SplashScreen.status_version = true){
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .into(img_bg_login);

            Glide.with(this)
                    .load(SplashScreen.logo_url)
                    .into(img_logo);
        }else {
            //set theme
            Glide.with(this)
                    .load(SplashScreen.login_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_bg_login);

            Glide.with(this)
                    .load(SplashScreen.logo_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_logo);
        }





        tv_display_sign_member_yet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        tv_display_sign_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        imgSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                onBackPressed();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Name = editName.getText().toString();
                Password = editPassword.getText().toString();
                if (Name.equals("") || Password.equals("")) {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.username_or_password_not_null), Toast.LENGTH_LONG).show();
                    return;
                }
                if (Name.length() <= 1 || Password.length() <= 1) {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.username_password_length), Toast.LENGTH_LONG).show();
                    return;
                }
                callLoginUserGeneral(Name, Password, access_token,shop_id);
            }

        });

        status_login = getIntent().getExtras().getString("Cat_login");

        //logout facebook
        if (status_login.equals("FB")){

//            FacebookSdk.sdkInitialize(getActivity());
//                            // Logout facebook
//            DeleteUserRealm();
            LoginManager.getInstance().logOut();
            Log.i("Logout Facebook","true");
            DeleteUserRealm();
            realm.beginTransaction();
            RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
            cartItemLists.deleteAllFromRealm();
            realm.commitTransaction();
            onBackPressed();
        }
        //logout general
        if (status_login.equals("Gen")){
            DeleteUserRealm();
            realm.beginTransaction();
            RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
            cartItemLists.deleteAllFromRealm();
            realm.commitTransaction();
            onBackPressed();
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("Content", "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());

                String m_fb_token = loginResult.getAccessToken().getToken();

                callgetloginUserFB(access_token, m_fb_token,"FB");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                /**
                 * AccessTokenTracker to manage logout facebook
                 */
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.putExtra("name", "false");
//                            intent.putExtra("pic", "false");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            DeleteUserRealm();
//                            onBackPressed();
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //-----Login user general-----//
//    void loginUserGeneral(String url, String Name, String Password, String access_token, String shop_id) throws IOException {
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        formBuilder.add("access_token",access_token);
//        formBuilder.add("m_user", Name);
//        formBuilder.add("m_pass", Password);
//        formBuilder.add("shop", shop_id);
//
//        MediaType.parse("application/json; charset=utf-8");
//        RequestBody formBody = formBuilder.build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//
//        client.newCall(request)
//                .enqueue(new Callback() {
//
//                    @Override
//                    public void onFailure(final Call call, IOException e) {
//                        // Error
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//                        jsonData = response.body().string();
//                        Log.i("Result login user", jsonData);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    json = new JSONObject(jsonData);
//                                    Log.i("Data login user: ",json.toString());
//                                    msg = json.getString("message");
//                                    Log.i("Message: ",msg.toString());
//
//                                    if (jsonData.toLowerCase().contains("success")) {
//                                        access_user_key = json.getString("access_user_key");
//
//                                        JSONObject parent = (JSONObject) json.get("access_user");
//                                        id_user = parent.getInt("id");
//                                        name_user = parent.getString("name");
//                                        firstname_user = parent.getString("firstname");
//                                        lastname_user = parent.getString("lastname");
//                                        email_user = parent.getString("email");
//                                        level_user = parent.getString("level");
//                                        gender = parent.getString("gender");
//                                        photo_thumb = parent.getString("photo_thumb");
//                                        reward_points = parent.getString("reward_points");
//
//                                        executeRealmUser(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,"Gen");
//
////                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(intent);
//                                        onBackPressed();
//                                    } else {
//                                        showMessageStatus("Login City thai", msg);
//                                    }
//                                } catch (JSONException e) {
//                                    e.getMessage();
//                                }
//                            }
//                        });
//
//                    }
//                });
//    }

    private void callLoginUserGeneral( String Name, String Password, String access_token, String shop_id){
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit1.create(APIService.class);
        api.getloginUserGeneral(access_token,Name,Password,shop_id).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                        jsonData1 = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = new JSONObject(jsonData1);
                                Log.i("Data login user: ",json.toString());
                                msg = json.getString("message");
                                Log.i("Message: ",msg.toString());

                                if (jsonData1.toLowerCase().contains("success")) {
                                    access_user_key = json.getString("access_user_key");

                                    JSONObject parent = (JSONObject) json.get("access_user");
                                    id_user = parent.getInt("id");
                                    name_user = parent.getString("name");
                                    firstname_user = parent.getString("firstname");
                                    lastname_user = parent.getString("lastname");
                                    email_user = parent.getString("email");
                                    level_user = parent.getString("level");
                                    gender = parent.getString("gender");
                                    photo_thumb = parent.getString("photo_thumb");
                                    reward_points = parent.getString("reward_points");

                                    executeRealmUser(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,"Gen");

//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
                                    onBackPressed();
                                } else {
                                    showMessageStatus("Login City thai", msg);
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


    //-----Login Social facebook/google-----//
//    void doGetRequestSocial(String url, String access_token, final String m_fb_token, final String cat_login) throws IOException {
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        formBuilder.add("access_token", access_token);
//        formBuilder.add("m_oauth_token", m_fb_token);
//        System.out.println("Facebook/Google Value: " + m_fb_token);
//
//        MediaType.parse("application/json; charset=utf-8");
//        RequestBody formBody = formBuilder.build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//
//        client.newCall(request)
//                .enqueue(new Callback() {
//
//                    @Override
//                    public void onFailure(final Call call, IOException e) {
//                        // Error
//
//                        Log.i(TAG, "สถานะการเข้าสู่ระบบ: error");
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//                        jsonData = response.body().string();
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    json = new JSONObject(jsonData);
//                                    Log.i("ผลลัพธ์ FB|GG:", jsonData.toString());
//
//                                    access_user_key = json.getString("access_user_key");
//
//                                    if (jsonData.toLowerCase().contains("success")) {
//
//                                        JSONObject parent = (JSONObject) json.get("access_user");
//                                        id_user = parent.getInt("id");
//                                        name_user = parent.getString("name");
//                                        firstname_user = parent.getString("firstname");
//                                        lastname_user = parent.getString("lastname");
//                                        email_user = parent.getString("email");
//                                        level_user = parent.getString("level");
//                                        gender = parent.getString("gender");
//                                        photo_thumb = parent.getString("photo_thumb");
//                                        Log.d("Image User: ",photo_thumb);
//
////                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(intent);
//                                        if (getIntent().getExtras().getString("Cat_login").equals("GG")){
////                                            if (mGoogleApiClient.isConnected()) {
//                                                // do your stuff with Google Api Client
//                                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                                                        new ResultCallback<Status>() {
//                                                            @Override
//                                                            public void onResult(Status status) {
//
//                                                                Log.d("Logout google : ","true");
//                                                                DeleteUserRealm();
//                                                                realm.beginTransaction();
//                                                                RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
//                                                                cartItemLists.deleteAllFromRealm();
//                                                                realm.commitTransaction();
//                                                                onBackPressed();
//                                                            }
//                                                        });
////                                            }
//                                        }else {
//                                            executeRealmUser(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,cat_login);
//                                            onBackPressed();
//                                        }
//
//
//                                    } else {
//                                        showMessageStatus("Login CityThai", msg);
//                                        return;
//                                    }
//                                } catch (JSONException e) {
//                                    e.getMessage();
//                                }
//                            }
//                        });
//
//                    }
//                });
//    }

    private void callgetloginUserFB(final String access_token, final String m_fb_token, final String cat_login){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit2.create(APIService.class);
        api.getloginUserFB(getResources().getString(R.string.shop_id_ref),access_token,m_fb_token).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                        jsonData2 = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = new JSONObject(jsonData2);
                                Log.i("ผลลัพธ์ FB|GG:", jsonData2.toString());

                                access_user_key = json.getString("access_user_key");

                                if (jsonData2.toLowerCase().contains("success")) {

                                    JSONObject parent = (JSONObject) json.get("access_user");
                                    id_user = parent.getInt("id");
                                    name_user = parent.getString("name");
                                    firstname_user = parent.getString("firstname");
                                    lastname_user = parent.getString("lastname");
                                    email_user = parent.getString("email");
                                    level_user = parent.getString("level");
                                    gender = parent.getString("gender");
                                    photo_thumb = parent.getString("photo_thumb");
                                    Log.d("Image User: ",photo_thumb);

//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
                                    if (getIntent().getExtras().getString("Cat_login").equals("GG")){
//                                            if (mGoogleApiClient.isConnected()) {
                                        // do your stuff with Google Api Client
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                                new ResultCallback<Status>() {
                                                    @Override
                                                    public void onResult(Status status) {

                                                        Log.d("Logout google : ","true");
                                                        DeleteUserRealm();
                                                        realm.beginTransaction();
                                                        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
                                                        cartItemLists.deleteAllFromRealm();
                                                        realm.commitTransaction();
                                                        onBackPressed();
                                                    }
                                                });
//                                            }
                                    }else {
                                        executeRealmUser(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,cat_login);
                                        onBackPressed();
                                    }


                                } else {
                                    showMessageStatus("Login CityThai", msg);
                                    return;
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

    private void callgetloginUserGG(String access_token, final String m_fb_token, final String cat_login){
        Retrofit retrofit3 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .build();
        APIService api = retrofit3.create(APIService.class);
        api.getloginUserGG(getResources().getString(R.string.shop_id_ref),access_token,m_fb_token).enqueue(new retrofit2.Callback<ResponseBody>() {
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
                        jsonData3 = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = new JSONObject(jsonData3);
                                Log.i("ผลลัพธ์ FB|GG:", jsonData3.toString());

                                access_user_key = json.getString("access_user_key");

                                if (jsonData3.toLowerCase().contains("success")) {

                                    JSONObject parent = (JSONObject) json.get("access_user");
                                    id_user = parent.getInt("id");
                                    name_user = parent.getString("name");
                                    firstname_user = parent.getString("firstname");
                                    lastname_user = parent.getString("lastname");
                                    email_user = parent.getString("email");
                                    level_user = parent.getString("level");
                                    gender = parent.getString("gender");
                                    photo_thumb = parent.getString("photo_thumb");
                                    Log.d("Image User: ",photo_thumb);

//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
                                    if (getIntent().getExtras().getString("Cat_login").equals("GG")){
//                                            if (mGoogleApiClient.isConnected()) {
                                        // do your stuff with Google Api Client
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                                new ResultCallback<Status>() {
                                                    @Override
                                                    public void onResult(Status status) {

                                                        Log.d("Logout google : ","true");
                                                        DeleteUserRealm();
                                                        realm.beginTransaction();
                                                        RealmResults<CartItemList> cartItemLists = realm.where(CartItemList.class).findAll();
                                                        cartItemLists.deleteAllFromRealm();
                                                        realm.commitTransaction();
                                                        onBackPressed();
                                                    }
                                                });
//                                            }
                                    }else {
                                        executeRealmUser(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,cat_login);
                                        onBackPressed();
                                    }


                                } else {
                                    showMessageStatus("Login CityThai", msg);
                                    return;
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

    @Override
    public void onClick(View v) {

        if (v == fb) {
            loginButton.performClick();
        }

        int id = v.getId();
        switch (id) {
            case R.id.btnSignInGoogle:

                if (getIntent().getExtras().getString("Cat_login").equals("GG")){
                    if (mGoogleApiClient.isConnected()) {
                        // do your stuff with Google Api Client
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {

                                        Log.d("Logout google : ","true");
                                    }
                                });
                    }
                    DeleteUserRealm();
                    onBackPressed();
                }else {
                    signIn();
                }
                break;
        }
    }
    //-----Show Message Status-----//
    private void showMessageStatus(String title , String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
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
    //-----End Show Message Status-----//

    public static void logoutUser() {
        // Logout google
        signOut();
    }

    //-----Login user Google+-----//
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //-----Check access token-----//
    private class CheckAccessToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String token = null;
            String Scopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";

            try {

                token = GoogleAuthUtil.getToken(SignInActivity.this, email, Scopes);

            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e(TAG, transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e(TAG, e.toString());
            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e(TAG, authEx.toString());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            Log.i(TAG, "Access token :" + token);
            callgetloginUserGG(access_token, token,"GG");
        }
    }

    public static void signOut() {

//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                }
        );

//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//
//                    }
//                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.i(TAG, "สถานะการเข้าสู่ระบบกูเกิล: Success");

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();

            Log.i(TAG, "Name: " + personName + ", email: " + email + ", image user url: ");

            new CheckAccessToken().execute();

//            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
//            intent.putExtra("text","Log Out");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }else{
            Log.i(TAG, "สถานะการเข้าสู่ระบบกูเกิล: False");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void executeRealmUser(final int id , final String access_user_key, final String name , final String firstname, final String lastname, final String email, final String level, final String gender, final String photo_thumb, final String status_connect) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                User user = realm.createObject(User.class);
                user.setId(id);
                user.setAccess_user_key(access_user_key);
                user.setName(name);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setLevel(level);
                user.setGender(gender);
                user.setPhoto_thumb(photo_thumb);
                user.setStatus_connect(status_connect);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(SignInActivity.this,"Create user error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void DeleteUserRealm(){
        realm.beginTransaction();
        RealmResults<User> result = realm.where(User.class).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }
}

