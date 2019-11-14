package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityNotificationSettingsBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationSettingsActiviry extends BaseActivity {

    ActivityNotificationSettingsBinding binding;
    private LoginBean loginBean;
    private String alert = "1",requestEmail = "1", requestApproved = "1", encorage = "1";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_notification_settings;
    }

    @Override
    public void init() {
        binding = (ActivityNotificationSettingsBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginBean = preferences.getUserDataPref();
        setListener();
    }

    private void setListener() {
        if (loginBean.getNotificationAlert().equalsIgnoreCase("1"))
            binding.switchAlert.setChecked(true);
        else
            binding.switchAlert.setChecked(false);
        if (loginBean.getRequestEmail().equalsIgnoreCase("1"))
            binding.switchFrEmail.setChecked(true);
        else
            binding.switchFrEmail.setChecked(false);
        if (loginBean.getRequestApproved().equalsIgnoreCase("1"))
            binding.switchFraEmail.setChecked(true);
        else
            binding.switchFraEmail.setChecked(false);
        if (loginBean.getEncourageNotification().equalsIgnoreCase("1"))
            binding.switchEncouragement.setChecked(true);
        else
            binding.switchEncouragement.setChecked(false);

        binding.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchAlert.isChecked())
                {
                    loginBean.setNotificationAlert("1");
                    alert = "1";
                }
                else {
                    loginBean.setNotificationAlert("0");
                    alert = "0";
                }
                if (binding.switchFrEmail.isChecked())
                {
                    loginBean.setRequestEmail("1");
                    requestEmail = "1";
                }
                else {
                    loginBean.setRequestEmail("0");
                    requestEmail = "0";
                }
                if (binding.switchFraEmail.isChecked())
                {
                    loginBean.setRequestApproved("1");
                    requestApproved = "1";
                }
                else {
                    loginBean.setRequestApproved("0");
                    requestApproved = "0";
                }
                if (binding.switchEncouragement.isChecked())
                {
                    loginBean.setEncourageNotification("1");
                    encorage = "1";
                }
                else {
                    loginBean.setEncourageNotification("0");
                    encorage = "0";
                }

                preferences.saveUserData(loginBean);
                callSubmitSettingsApi();
            }
        });
    }

    private void callSubmitSettingsApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("notification_alert", alert);
            map.put("request_email", requestEmail);
            map.put("request_approved", requestApproved);
            map.put("encourage_notification", encorage);

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).setNotificationSettings(map);
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
                            Log.e("callSubmitSettingsApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                Intent intent = new Intent(context, ProfileActivity.class);
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
