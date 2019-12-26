package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityTrainerProfileBinding;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.modelBean.TrainerBean;
import fit.tele.com.telefit.utils.CircleTransform;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrainerProfileActivity extends BaseActivity implements View.OnClickListener {

    ActivityTrainerProfileBinding binding;
    private TrainerBean trainerBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_trainer_profile;
    }

    @Override
    public void init() {
        binding = (ActivityTrainerProfileBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llFitness.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        binding.txtRequestTrainer.setOnClickListener(this);

        if(getIntent() != null && getIntent().hasExtra("TrainerBean"))
        {
            trainerBean = getIntent().getParcelableExtra("TrainerBean");
            setData(trainerBean);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvPackages.setLayoutManager(linearLayoutManager);
    }

    private void setData(TrainerBean data) {
        if (data != null) {
            if(data.getProfilePic() != null && !TextUtils.isEmpty(data.getProfilePic())) {
                Picasso.with(context)
                        .load(data.getProfilePic())
                        .error(R.drawable.user_placeholder)
                        .placeholder(R.drawable.user_placeholder)
                        .transform(new CircleTransform())
                        .into(binding.imgUser);
            }

            if(data.getName() != null && !TextUtils.isEmpty(data.getName()))
                binding.txtHeaderName.setText(data.getName()+" "+data.getlName());
            if(data.getCity() != null && !TextUtils.isEmpty(data.getCity()))
                binding.txtAddress.setText(data.getCity()+", "+data.getState());
            if(data.getDescription() != null && !TextUtils.isEmpty(data.getDescription()))
                binding.txtTrainerDetails.setText(data.getDescription());
            if(data.getIsSubscribe() != null && data.getIsSubscribe().equalsIgnoreCase("1"))
                binding.txtRequestTrainer.setText("Schedule Conference >");
            else {
                if(data.getIsAccept() != null && data.getIsAccept().equalsIgnoreCase("Accept"))
                    binding.txtRequestTrainer.setText("Buy Package >");
                if(data.getIsAccept() != null && data.getIsAccept().equalsIgnoreCase("Pending"))
                    binding.txtRequestTrainer.setText("Request Pending");
                else
                    binding.txtRequestTrainer.setText("Request Trainer >");
            }

        }
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

            case R.id.ll_fitness :
                intent = new Intent(context, FitnessActivity.class);
                startActivity(intent);
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txt_request_trainer :
                if(trainerBean.getIsSubscribe() != null && trainerBean.getIsSubscribe().equalsIgnoreCase("1"))
                {
                    intent = new Intent(context, VideoConferenceActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(0, 0);
                }
                else {
                    if(trainerBean.getIsAccept() != null && trainerBean.getIsAccept().equalsIgnoreCase("Accept"))
                        binding.txtRequestTrainer.setText("Buy Package >");
                    if(trainerBean.getIsAccept() != null && trainerBean.getIsAccept().equalsIgnoreCase("Pending"))
                        binding.txtRequestTrainer.setText("Request Pending");
                    else
                        sendRequest(trainerBean.getId());
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(0, 0);
    }

    private void sendRequest(String trainer_id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("trainer_id",trainer_id);

            Observable<ModelBean<TrainerBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).sendRequest(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<TrainerBean>>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.e("deleteRoutine"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }
                        @Override
                        public void onNext(ModelBean<TrainerBean> arrayListModelBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (arrayListModelBean.getStatus().toString().equalsIgnoreCase("1") )
                            {
                                Intent intent = new Intent(context,TrainersActivity.class);
                                startActivity(intent);
                            }
                            else
                                CommonUtils.toast(context, ""+arrayListModelBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
