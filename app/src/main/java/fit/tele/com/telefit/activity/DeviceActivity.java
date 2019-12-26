package fit.tele.com.telefit.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.DeviceAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityDeviceBinding;
import fit.tele.com.telefit.modelBean.CaloriesBarBean;
import fit.tele.com.telefit.modelBean.DeviceBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeviceActivity extends BaseActivity implements View.OnClickListener {

    ActivityDeviceBinding binding;
    private ArrayList<DeviceBean> deviceBeans = new ArrayList<>();
    private DeviceBean deviceBean;
    private DeviceAdapter deviceAdapter;
    String TAG = "google fit", strFitbit = "";
    private GoogleApiClient mClient;
    private boolean authInProgress = false;
    private static final int REQUEST_OAUTH = 1;
    private CustomTabsServiceConnection connection;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_device;
    }

    @Override
    public void init() {
        binding = (ActivityDeviceBinding) getBindingObj();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onNewIntent(getIntent());

        if (!TextUtils.isEmpty(strFitbit)) {
            String accessToken = strFitbit.substring(strFitbit.indexOf("&access_token") + 48, strFitbit.indexOf("&user_id"));
            String userId = strFitbit.substring(strFitbit.indexOf("&user_id")+9, strFitbit.indexOf("&scope"));
            String tokenType = strFitbit.substring(strFitbit.indexOf("&token_type")+12,strFitbit.indexOf("&expires_in"));

            preferences.saveFitbitTokenData(accessToken);
            preferences.saveFitbitIDData(userId);
            preferences.saveFitbitTypeData(tokenType);

            callFitbitApi(accessToken);
        }

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        deviceBean = new DeviceBean();
        deviceBean.setDeviceImage(R.drawable.googlefit_icon);
        deviceBean.setDeviceName("Google Fit");
        deviceBean.setLastSync("");
        deviceBeans.add(deviceBean);
        deviceBean = new DeviceBean();
        deviceBean.setDeviceImage(R.drawable.fitbit_icon);
        deviceBean.setDeviceName("Fitbit");
        deviceBean.setLastSync("");
        deviceBeans.add(deviceBean);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvDevice.setLayoutManager(linearLayoutManager);

        if (deviceAdapter == null) {
            deviceAdapter = new DeviceAdapter(context, new DeviceAdapter.ClickListener() {
                @Override
                public void onClick(String name) {
                    if (name.equalsIgnoreCase("Google Fit")) {
                        syncGoogleFit();
                    }
                    else if (name.equalsIgnoreCase("Fitbit")) {
                        syncFitbit();
                    }
                }
            });
        }
        binding.rvDevice.setAdapter(deviceAdapter);
        deviceAdapter.clearAll();
        deviceAdapter.addAllList(deviceBeans);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_nutrition:
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_fitness:
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_goals:
                intent = new Intent(context, GoalsActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.ll_social:
                intent = new Intent(context, SocialActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;
        }
    }

    private void syncFitbit() {
        binding.progress.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(preferences.getFitbitTokenPref()) || preferences.getFitbitTokenPref().equalsIgnoreCase("0")) {
            connection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient client) {
                    final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    final CustomTabsIntent intent = builder.build();
                    client.warmup(0L); // This prevents backgrounding after redirection
                    Uri uri = Uri.parse("https://www.fitbit.com/oauth2/authorize?response_type=token" +
                            "&client_id=22BDPG" +
                            "&redirect_uri=anything://auth_callback_anything" +
                            "&scope=activity&expires_in=604800");
                    intent.launchUrl(context, uri);
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {}
            };
            CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection);
            binding.progress.setVisibility(View.GONE);
        }
        else
        {
            binding.progress.setVisibility(View.GONE);
            callFitbitApi(preferences.getFitbitTypePref()+" "+preferences.getFitbitTokenPref());
        }

    }

    private void callFitbitApi(String token) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Observable<ResponseBody> signupusers;

            signupusers = FetchServiceBase.getFitbitFetcherService(context).getFitbitCalories(token);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callFitbitApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ResponseBody fitbitJSON) {
                            binding.progress.setVisibility(View.GONE);
                            try {
//                                {"success":false,"errors":[{"errorType":"invalid_token","message":"Access token invalid: eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMkJEUEciLCJzdWIiOiI3Vzc3N0QiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyYWN0IiwiZXhwIjoxNTc3ODgwNzY1LCJpYXQiOjE1NzcyNzU5ODJ9.Uke31QCg7U9bFr69Qf7zbfjKPn_e1drZApC8JeLSUGE. Visit https://dev.fitbit.com/docs/oauth2 for more information on the Fitbit Web API authorization process."}]}
                                JSONObject jsonObject = new JSONObject(fitbitJSON.string());
                                if (!jsonObject.getString("success").equalsIgnoreCase("false")) {
                                    if (jsonObject.has("summary")) {
                                        JSONObject summaryObject = jsonObject.getJSONObject("summary");
                                        String burnedCalories = summaryObject.getString("caloriesOut");
                                        Log.e("burnedCalories",""+burnedCalories);
                                        CommonUtils.toast(context,"Burned Calories synced from Fitbit!");
                                        preferences.saveBurnedCaloriesData(""+burnedCalories);
                                    }
                                }
                                else {
                                    callFitbitRefreshApi(token);
                                }

                            }catch (Exception e) {
                                Log.e("callFitbitApi exc",""+e.getCause());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callFitbitRefreshApi(String token) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Observable<ResponseBody> signupusers;

            signupusers = FetchServiceBase.getFitbitFetcherServiceWithToken(context).getRefreshToken("refresh_token",token);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callFitbitApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ResponseBody fitbitJSON) {
                            binding.progress.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(fitbitJSON.string());
                                JSONObject summaryObject = jsonObject.getJSONObject("summary");
                                String burnedCalories = summaryObject.getString("caloriesOut");
                                Log.e("burnedCalories",""+burnedCalories);
                                CommonUtils.toast(context,"Burned Calories synced from Fitbit!");
                                preferences.saveBurnedCaloriesData(""+burnedCalories);
                            }catch (Exception e) {
                                Log.e("callFitbitApi exc",""+e.getCause());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void syncGoogleFit() {
        binding.progress.setVisibility(View.VISIBLE);
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .useDefaultAccount()
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {


                                //Async To fetch steps
                                new FetchCalorieAsync().execute();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.e(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.e(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                ).addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                binding.progress.setVisibility(View.GONE);
                                Log.e(TAG, "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                            DeviceActivity.this, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        Log.e(TAG, "Attempting to resolve failed connection");
//                                        authInProgress = true;
                                        result.startResolutionForResult(DeviceActivity.this, REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG,
                                                "Exception while starting resolution activity", e);
                                    }
                                }
                            }
                        }
                ).build();
        mClient.connect();
    }

    private class FetchCalorieAsync extends AsyncTask<Object, Object, Long> {
        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType. TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(com.google.android.gms.fitness.data.Field.FIELD_CALORIES).asInt();
                }
            } else {
                Log.e(TAG, "There was a problem getting the calories.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            binding.progress.setVisibility(View.GONE);
            CommonUtils.toast(context,"Burned Calories synced from Google Fit!");
            preferences.saveBurnedCaloriesData(""+aLong);
            //Total calories burned for that day
            Log.e(TAG, "Total calories: " + aLong);

        }
    }

    /*private class FetchCalorieAsync extends AsyncTask<Object, Object, List<DataSet>> {
        protected List<DataSet> doInBackground(Object... params) {
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();
            List<DataSet> totalSet = null;
            PendingResult<DataReadResult> result = Fitness.HistoryApi.readData(mClient, new DataReadRequest.Builder().setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA).build());
            DataReadResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                totalSet = totalResult.getDataSets();
            } else {
                Log.e(TAG, "There was a problem getting the calories.");
            }
            return totalSet;
        }


        @Override
        protected void onPostExecute(List<DataSet> aLong) {
            super.onPostExecute(aLong);
            binding.progress.setVisibility(View.GONE);
            CommonUtils.toast(context,"Burned Calories synced from Google Fit!");
//            preferences.saveBurnedCaloriesData(""+aLong);
            //Total calories burned for that day

            Log.e(TAG, "Total calories: " + aLong.toString());
            for (int i=0;i<aLong.size();i++) {

                Log.e(TAG, "Total calories: " + aLong.get(i).getDataPoints());
            }

        }
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        strFitbit = intent.getDataString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null)
            this.unbindService(connection);
        connection = null;
    }
}
