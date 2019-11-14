package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.view.View;

import com.suke.widget.SwitchButton;

import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySocialSettingBinding;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.Preferences;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocialSetting extends BaseActivity implements View.OnClickListener{

    ActivitySocialSettingBinding binding;
    Preferences preferences;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_social_setting;
    }

    @Override
    public void init() {
        binding = (ActivitySocialSettingBinding)getBindingObj();

        preferences = new Preferences(this);
        if(preferences.getUserDataPref().getIs_snapchat_share().equals("1")){
            binding.switchSnapchat.setChecked(true);
        }else
            binding.switchSnapchat.setChecked(false);

        if(preferences.getUserDataPref().getIs_facebook_share().equals("1")){
            binding.switchFb.setChecked(true);
        }else
            binding.switchFb.setChecked(false);

        if(preferences.getUserDataPref().getIs_instagram_share().equals("1")){
            binding.switchInstagram.setChecked(true);
        }else
            binding.switchInstagram.setChecked(false);

        if(preferences.getUserDataPref().getIs_twiter_share().equals("1")){
            binding.switchTwitter.setChecked(true);
        }else
            binding.switchTwitter.setChecked(false);

        if(preferences.getUserDataPref().getIs_friend_share().equals("1")){
            binding.switchFriends.setChecked(true);
        }else
            binding.switchFriends.setChecked(false);

        if(preferences.getUserDataPref().getIs_trainer_share().equals("1")){
            binding.switchMytrainer.setChecked(true);
        }else
            binding.switchMytrainer.setChecked(false);

        setListner();


        binding.switchFriends.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
        binding.switchMytrainer.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
        binding.switchFb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
        binding.switchTwitter.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
        binding.switchInstagram.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
        binding.switchSnapchat.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
              //                                                                                                                          Log.w("CheckStatus",""+binding.switchFriends.isChecked());
                setSocialSetting(false);
            }
        });
    }

    public void setListner() {
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

    private void setSocialSetting(Boolean isFirstTime) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String,String> map = new HashMap<>();
            if(isFirstTime) {

            }else {
                if (binding.switchFb.isChecked())
                    map.put("Is_facebook_share", "1");
                else
                    map.put("Is_facebook_share", "0");
                if (binding.switchTwitter.isChecked())
                    map.put("Is_twiter_share", "1");
                else
                    map.put("Is_twiter_share", "0");
                if (binding.switchInstagram.isChecked())
                    map.put("Is_instagram_share", "1");
                else
                    map.put("Is_instagram_share", "0");
                if (binding.switchSnapchat.isChecked())
                    map.put("Is_snapchat_share", "1");
                else
                    map.put("Is_snapchat_share", "0");

                if (binding.switchFriends.isChecked())
                    map.put("Is_friend_share", "1");
                else
                    map.put("Is_friend_share", "0");

                if (binding.switchMytrainer.isChecked())
                    map.put("Is_trainer_share", "1");
                else
                    map.put("Is_trainer_share", "0");
            }
            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context)
                    .getSocialSetting(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<LoginBean>>() {
                        @Override
                        public void onCompleted() {
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            binding.progress.setVisibility(View.GONE);
                            CommonUtils.toast(getApplicationContext(), e.getMessage());
                        }
                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {

                                preferences.saveUserData(loginBean.getResult());

                            } else {
                                CommonUtils.toast(context, loginBean.getMessage());
                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }



}
