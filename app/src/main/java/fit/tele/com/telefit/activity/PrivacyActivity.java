package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPrivacyBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PrivacyActivity extends BaseActivity {

    ActivityPrivacyBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_privacy;
    }

    @Override
    public void init() {
        binding = (ActivityPrivacyBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (preferences.getPrivacyPref().equalsIgnoreCase("0"))
        {
            binding.imgPrivate.setVisibility(View.GONE);
            binding.imgPublic.setVisibility(View.VISIBLE);
        }
        else {
            binding.imgPrivate.setVisibility(View.VISIBLE);
            binding.imgPublic.setVisibility(View.GONE);
        }

        binding.txtPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgPrivate.setVisibility(View.VISIBLE);
                binding.imgPublic.setVisibility(View.GONE);
                callSetPrivacyApi("1");
            }
        });

        binding.txtPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgPrivate.setVisibility(View.GONE);
                binding.imgPublic.setVisibility(View.VISIBLE);
                callSetPrivacyApi("0");
            }
        });
    }

    private void callSetPrivacyApi(String isPrivate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("hide_show", isPrivate);

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).setPrivacy(map);
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
                            Log.e("callSetPrivacyApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                preferences.savePrivacyData(isPrivate);
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
