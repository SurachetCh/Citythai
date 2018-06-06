package revenue_express.citythai.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import revenue_express.citythai.activity.BarcodePointActivity;
import revenue_express.citythai.R;
import revenue_express.citythai.activity.SignInActivity;
import revenue_express.citythai.activity.UserProfileActivity;
import revenue_express.citythai.model.User;

import static com.facebook.FacebookSdk.getApplicationContext;
import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class MoreFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MoreFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    public static CallbackManager callbackManager;

    private View myFragmentView;
    public static GoogleApiClient mGoogleApiClient;
    public static Realm realm;
    public static Context context;
    ImageView iv_image_shop_name;
    AvatarImageView iv_user;
    TextView tv_user_name,tv_user_email,tv_login,tv_user_profile,tv_reward;
    LinearLayout layout_fragment;
    String login_status = "null";
    AppBarLayout app_bar_layout;


//    public static MoreFragment newInstance() {
//        MoreFragment fragment = new MoreFragment();
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_more, container, false);
//
//        return rootView;
//    }

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_more, container, false);

        sendNamePageGoogleAnalytics("MoreFragment");

        //start detail user of menu bar



        //Connect database realm
        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getResources().getString(R.string.SukhumvitSet_Medium));

        bindView();
        setFont(tf);
        setColor();
        setView();




//        //Google Sign in option
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(getActivity(), (GoogleApiClient.OnConnectionFailedListener) getActivity())
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

//        tv_login.setText(text);
//        tv_user_name.setText(display_name);
//        tv_user_email.setText(email);
        return myFragmentView;
    }

    private void bindView(){
        iv_image_shop_name = (ImageView) myFragmentView.findViewById(R.id.iv_image_shop_name);
        iv_user = (AvatarImageView)myFragmentView.findViewById(R.id.iv_user);
        tv_user_name = (TextView)myFragmentView.findViewById(R.id.tv_user_name);
        tv_user_email = (TextView)myFragmentView.findViewById(R.id.tv_user_email);
        tv_login = (TextView)myFragmentView.findViewById(R.id.tv_login);
        tv_user_profile = (TextView)myFragmentView.findViewById(R.id.tv_user_profile);
        tv_reward = (TextView)myFragmentView.findViewById(R.id.tv_reward);

        layout_fragment = (LinearLayout)myFragmentView.findViewById(R.id.layout_fragment);
        app_bar_layout = (AppBarLayout)myFragmentView.findViewById(R.id.app_bar_layout);
    }
    private void setFont(Typeface tf){
//Set type fonts
        tv_user_name.setTypeface(tf);
        tv_user_email.setTypeface(tf);
        tv_login.setTypeface(tf);
    }
    private void setColor(){
        layout_fragment.setBackgroundColor(Color.parseColor(ColorPrimary));
        app_bar_layout.setBackgroundColor(Color.parseColor(ColorPrimary));
    }
    private void setView(){
//Check status login user
        realm.beginTransaction();
        RealmResults<User> result_user = realm.where(User.class).findAll();
        int checklogin = result_user.size();
        realm.commitTransaction();

        if (checklogin != 0){
            login_status.isEmpty();
            login_status = result_user.get(0).getStatus_connect();
            tv_user_name.setText(result_user.get(0).getName());
            tv_user_email.setText(result_user.get(0).getEmail());
            Glide.with(this)
                    .load(result_user.get(0).getPhoto_thumb())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);
            tv_login.setText(getResources().getString(R.string.logout));

            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
//
//                    // Setting Dialog Title
//                    alertDialog.setTitle(getResources().getString(R.string.confirm_logout));
//
//                    // Setting Dialog Message
//                    alertDialog.setMessage(getResources().getString(R.string.delete_order_logout));
//
//                    // Setting Icon to Dialog
////                    alertDialog.setIcon(R.drawable.delete_menu);
//
//                    // Setting Positive "NO" Button
//                    alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            dialog.cancel();
//
//                        }
//                    });
//
//                    // Setting Negative "YES" Button
//                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
////                            FacebookSdk.sdkInitialize(getApplicationContext());
////                            // Logout facebook
////                            LoginManager.getInstance().logOut();
////                            //Logout google
////                            logoutUser();
//
//                            //realm.beginTransaction();
//                            //RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
//
////                            DeleteOrderListRealm();
//                            tv_login.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                                    intent.putExtra("CheckLogIn", "true");
//                                    startActivity(intent);
//                                }
//                            });
//
//                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.putExtra("CheckLogIn", "true");
                    intent.putExtra("Cat_login", login_status);
                    startActivity(intent);
                }
            });

        }else {
            tv_user_name.setText(getResources().getString(R.string.username));
            tv_user_email.setText(getResources().getString(R.string.email));
            tv_login.setText(getResources().getString(R.string.login));
            Glide.with(this)
                    .load(R.drawable.male)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);

            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.putExtra("CheckLogIn", "false");
                    intent.putExtra("Cat_login", login_status);
                    startActivity(intent);
                }
            });
        }


        tv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<User> user = realm.where(User.class).findAll();
                if(user.size() != 0 ) {
                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.user_profile_login_before),Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<User> user = realm.where(User.class).findAll();
                if(user.size() != 0 ) {
                    Intent intent = new Intent(getApplicationContext(), BarcodePointActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.barcode_user_login_before),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        //Check status login user
        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
        int checklogin = user.size();
        realm.commitTransaction();

        if (checklogin != 0){
            login_status.isEmpty();
            login_status = user.get(0).getStatus_connect();
            tv_user_name.setText(user.get(0).getName());
            tv_user_email.setText(user.get(0).getEmail());
            Glide.with(this)
                    .load(user.get(0).getPhoto_thumb())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);
            tv_login.setText(getResources().getString(R.string.logout));

            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//
//                    // Setting Dialog Title
//                    alertDialog.setTitle(getResources().getString(R.string.confirm_logout));
//
//                    // Setting Dialog Message
//                    alertDialog.setMessage(getResources().getString(R.string.delete_order_logout));
//
//                    // Setting Icon to Dialog
////                    alertDialog.setIcon(R.drawable.delete_menu);
//
//                    // Setting Positive "NO" Button
//                    alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            dialog.cancel();
//
//                        }
//                    });
//
//                    // Setting Negative "YES" Button
//                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
////                            FacebookSdk.sdkInitialize(getActivity());
////                            // Logout facebook
////                            LoginManager.getInstance().logOut();
////                            //Logout google
////                            logoutUser();
//
//                            //realm.beginTransaction();
//                            //RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
//
////                            DeleteOrderListRealm();
//
//                            tv_login.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                                    intent.putExtra("CheckLogIn", "true");
//                                    startActivity(intent);
//                                }
//                            });
//
//                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    intent.putExtra("CheckLogIn", "true");
                                    intent.putExtra("Cat_login", login_status);
                                    startActivity(intent);
                }
            });

        }else {
            tv_user_name.setText(getResources().getString(R.string.username));
            tv_user_email.setText(getResources().getString(R.string.email));
            tv_login.setText(getResources().getString(R.string.login));
            Glide.with(this)
                    .load(R.drawable.male)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);

            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_status = "null";
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.putExtra("CheckLogIn", "false");
                    intent.putExtra("Cat_login", login_status);
                    startActivity(intent);
                }
            });
        }
        super.onResume();
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



    //Logout google
    public void logoutUser() {
        realm.beginTransaction();
        RealmResults<User> result = realm.where(User.class).findAll();
        result.deleteLastFromRealm();
        realm.commitTransaction();
        signOut();
    }


    //Login user Google+
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public  void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public
                    void onResult(Status status) {

                        if (mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.disconnect();
                            mGoogleApiClient.connect();

                            Log.i("ออกจากระบบ Google:","Success");

                            Intent intent = new Intent(getApplicationContext() , SignInActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.i("เข้าสู่ระบบ Google:","Success");
//
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
//            String email = acct.getEmail();
//
//            Log.i("เข้าสู่ระบบ :", "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);
//
////            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
////            intent.putExtra("display_name", personName);
////            intent.putExtra("email",email);
////            intent.putExtra("picture",personPhotoUrl);
//        }else{
//            Log.i("เข้าสู่ระบบ Google:","False");
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
