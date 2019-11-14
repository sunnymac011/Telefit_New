package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityWelcomeBinding;
import fit.tele.com.telefit.interfaces.FacebookInterface;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.socialSignup.FaceBookLogin;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.MyApplication;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomeActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, FacebookInterface, View.OnClickListener {

    ActivityWelcomeBinding binding;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private FaceBookLogin faceBookLogin;
    private GoogleApiClient mGoogleApiClient;

    private void setListner() {
        binding.rlFb.setOnClickListener(this);
        binding.rlGoogle.setOnClickListener(this);
        binding.btnCreate.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    private void setEnableView(boolean isEnable) {
        binding.rlFb.setEnabled(isEnable);
        binding.rlGoogle.setEnabled(isEnable);
        binding.btnCreate.setEnabled(isEnable);
        binding.btnLogin.setEnabled(isEnable);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void init() {

        mAuth = ((MyApplication) getApplication()).mAuth;
        faceBookLogin = new FaceBookLogin(context, this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fbKey();

        binding = (ActivityWelcomeBinding) getBindingObj();
        setListner();

    }

    private void clickGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void clickFb() {
        if (CommonUtils.isInternetOn(context)) {
            if(faceBookLogin != null)
                faceBookLogin.faceBookManager(context);
        } else
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FACEBOOK_REQUEST_CODE:

                    if(faceBookLogin != null)
                        faceBookLogin.onResult(requestCode, resultCode, data);
                    else
                        setEnableView(true);
                    break;

                case RC_SIGN_IN:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    if (result.isSuccess()) {
                        GoogleSignInAccount account = result.getSignInAccount();
                        firebaseAuthWithGoogle(account);
                    } else
                        setEnableView(true);
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setEnableView(true);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if(mAuth != null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setApiValueForGoogleSignup(mAuth.getCurrentUser());
                            } else if (task.getException() != null) {
                                setEnableView(true);
                                CommonUtils.toast(context, task.getException().getMessage());
                            }
                        }
                    });
        } else
            setEnableView(true);
    }

    private void setApiValueForGoogleSignup(FirebaseUser user) {
        Map<String, String> map = new HashMap<>();
        if (user.getEmail() != null)
            map.put("email", user.getEmail());
        map.put("social_id", user.getUid());
        map.put("is_social", "2");
        map.put("device_type", "android");
        map.put("login_by", "google");
        if(user.getDisplayName() != null) {
            StringTokenizer tokens = new StringTokenizer(user.getDisplayName().toString().trim(), " ");
            String first = tokens.nextToken();
            String second = tokens.nextToken();

            if (!first.isEmpty())
                map.put("name", first.trim());
            if (!second.isEmpty())
                map.put("l_name", second.trim());
        }

        if (preferences.getPushToken() != null) {
            map.put("device_token", preferences.getPushToken());
        } else {
            map.put("device_token", "123456789");
        }

        Log.e("Google account details "," "+map.toString());
        callSocialSignUp(map);
    }

    @Override
    public void success(Map<String, String> map1) {
        Map<String, String> map = new HashMap<>();
        map.put("is_social", "1");
        map.put("device_type", "android");
        map.put("login_by", "facebook");

        if (map1.containsKey("firstName") && map1.get("firstName") != null)
            map.put("name", map1.get("firstName"));
        if (map1.containsKey("lastName") && map1.get("lastName") != null)
            map.put("l_name", map1.get("lastName"));
        if (map1.containsKey("email") && map1.get("email") != null)
            map.put("email", map1.get("email"));
        if (map1.containsKey("profileImageURL") && map1.get("profileImageURL") != null)
            map.put("profile_pic", map1.get("profileImageURL"));

        map.put("social_id", map1.get("socialId"));

        if (preferences.getPushToken() != null) {
                map.put("device_token", preferences.getPushToken());
        } else {
            map.put("device_token", "123456789");
        }

        Log.e("FB account details "," "+map.toString());
        callSocialSignUp(map);
    }

    @Nullable
    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_fb:
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                setEnableView(false);
                clickFb();
                break;
            case R.id.rl_google:
                setEnableView(false);
                clickGoogle();
                break;
            case R.id.btn_create:
                setEnableView(false);
                intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                setEnableView(false);
                intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEnableView(true);
    }

    @Override
    protected void onDestroy() {
        if (subscription != null)
            subscription.unsubscribe();
        super.onDestroy();
    }

    private void fbKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
        }
    }

    private void callSocialSignUp(Map<String, String> map) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherService(context).signUpApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<LoginBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callSocialSignUp "," "+e);
                            binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                if(loginBean.getResult() != null) {
                                    preferences.saveUserData(loginBean.getResult());
                                    if(loginBean.getResult().getSessionToken() != null)
                                        preferences.setSessionToken(loginBean.getResult().getSessionToken());
                                }
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finishAffinity();
                            } else
                                CommonUtils.toast(context,loginBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
