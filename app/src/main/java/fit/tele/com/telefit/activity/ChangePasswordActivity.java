package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityChangePasswordBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePasswordActivity extends BaseActivity {

    ActivityChangePasswordBinding binding;
    private String strEmail="";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void init() {
        binding = (ActivityChangePasswordBinding) getBindingObj();
        setListner();
    }

    private void setListner() {

        if(getIntent() != null && getIntent().hasExtra("email"))
        {
            strEmail = getIntent().getStringExtra("email");
            binding.inputEmail.setText(strEmail.trim());
        }

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    callChangePasswordApi();
                }
            }

            private boolean validation() {
                if (binding.inputEmail.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter Email!");
                    return false;
                } else if (!CommonUtils.isValidEmail(binding.inputEmail.getText().toString().trim())) {
                    CommonUtils.toast(context, "Please enter valid email");
                    return false;
                } else if (binding.inputOtp.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter OTP!");
                    return false;
                } else if (binding.inputPassword.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter New Password!");
                    return false;
                } else if (binding.inputConfPassword.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter Confirm Password!");
                    return false;
                } else if (!binding.inputPassword.getText().toString().equalsIgnoreCase(binding.inputConfPassword.getText().toString())) {
                    CommonUtils.toast(context,"Password not matched!");
                    return false;
                }  else
                    return true;
            }
        });
    }

    private void callChangePasswordApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("email", binding.inputEmail.getText().toString().trim());
            map.put("forgot_otp", binding.inputOtp.getText().toString().trim());
            map.put("new_password", binding.inputPassword.getText().toString().trim());
            map.put("confirm_password", binding.inputConfPassword.getText().toString().trim());

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherService(context).setNewPasswordAPI(map);
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
                            Log.e("callChangePasswordApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                CommonUtils.toast(context, ""+loginBean.getMessage());
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                                CommonUtils.toast(context, ""+loginBean.getMessage());

                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
