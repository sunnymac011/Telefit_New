package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityHelpBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HelpActivity extends BaseActivity {

    private ActivityHelpBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_help;
    }

    @Override
    public void init() {
        binding = (ActivityHelpBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.inputName.setText(""+preferences.getUserDataPref().getName()+" "+preferences.getUserDataPref().getlName());

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation())
                    callSubmitHelpApi();
            }

            private boolean validation() {
                if (binding.inputDesc.getText().toString().isEmpty()) {
                    binding.inputDesc.setError("Please enter Description!");
                    return false;
                } else
                    return true;
            }
        });
    }

    private void callSubmitHelpApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("msg", binding.inputDesc.getText().toString().trim());

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).setHelp(map);
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
                            Log.e("callSubmitHelpApi "," "+e);
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
