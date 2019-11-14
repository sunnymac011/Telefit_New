package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.CountryAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityDatabaseCountryBinding;
import fit.tele.com.telefit.modelBean.CountryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DatabaseCountryActivity extends BaseActivity implements View.OnClickListener {

    ActivityDatabaseCountryBinding binding;
    private CountryAdapter countryAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_database_country;
    }

    @Override
    public void init() {
        binding = (ActivityDatabaseCountryBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvCountry.setLayoutManager(linearLayoutManager);

        countryAdapter = new CountryAdapter(context,preferences);
        binding.rvCountry.setAdapter(countryAdapter);
        countryAdapter.clearAll();

        callCountryApi();
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

    private void callCountryApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<CountryBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getCountryApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<CountryBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callCountryApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<CountryBean>> apiFoodBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiFoodBean.getResult() != null && apiFoodBean.getResult().size() > 0) {
                                countryAdapter.addAllList(apiFoodBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, apiFoodBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
