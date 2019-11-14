package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ConferenceAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityConferenceBinding;
import fit.tele.com.telefit.modelBean.ConferenceBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.PrivacyBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoConferenceActivity extends BaseActivity implements View.OnClickListener {

    ActivityConferenceBinding binding;
    private ConferenceAdapter conferenceAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_conference;
    }

    @Override
    public void init() {
        binding = (ActivityConferenceBinding) getBindingObj();

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

        binding.txtAdd.setOnClickListener(this);

        setListner();
    }

    private void setListner() {
        callGetConferenceApi();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvConference.setLayoutManager(linearLayoutManager);

        conferenceAdapter = new ConferenceAdapter(context, new ConferenceAdapter.ClickListener() {
            @Override
            public void onClick(String exe_id) {

            }
        });
        binding.rvConference.setAdapter(conferenceAdapter);
        conferenceAdapter.clearAll();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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

            case R.id.txt_add:
                intent = new Intent(context, SetConferenceActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void callGetConferenceApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<ConferenceBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getConferenceApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<ConferenceBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetConferenceApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<ConferenceBean>> apiPrivacyBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiPrivacyBean != null && apiPrivacyBean.getResult().size()>0)
                                conferenceAdapter.addAllList(apiPrivacyBean.getResult());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
