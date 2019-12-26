package fit.tele.com.telefit.activity;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPrivacyPolicyBinding;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.PrivacyBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.MyTagHandler;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PrivacyPolicyActivity extends BaseActivity {

    ActivityPrivacyPolicyBinding binding;
    private String strFrom = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    public void init() {
        binding = (ActivityPrivacyPolicyBinding) getBindingObj();

        if (getIntent().hasExtra("from"))
            strFrom = getIntent().getStringExtra("from");

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (strFrom.equalsIgnoreCase("privacy"))
        {
            binding.txtHeaderName.setText("Privacy");
            callPrivacyApi();
        }
        else
        {
            binding.txtHeaderName.setText("Terms and Conditions");
            callTermsApi();
        }
    }

    private void callPrivacyApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<PrivacyBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getPrivacyApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<PrivacyBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callPrivacyAdi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<PrivacyBean> apiPrivacyBean) {
                            binding.progress.setVisibility(View.GONE);
                            binding.txtPrivacy.setText(Html.fromHtml(""+Html.fromHtml(apiPrivacyBean.getResult().getDetails(), null, new MyTagHandler()), null, new MyTagHandler()));
                            binding.txtPrivacy.setMovementMethod(new ScrollingMovementMethod());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callTermsApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<PrivacyBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getTermsApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<PrivacyBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callTermsApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<PrivacyBean> apiPrivacyBean) {
                            binding.progress.setVisibility(View.GONE);
                            binding.txtPrivacy.setText(Html.fromHtml(apiPrivacyBean.getResult().getDetails()));
                            binding.txtPrivacy.setMovementMethod(new ScrollingMovementMethod());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
