package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.GirlAdapter;
import fit.tele.com.telefit.adapter.TrainerListAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityTrainersBinding;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.SubCatId;
import fit.tele.com.telefit.modelBean.TrainerBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrainersActivity extends BaseActivity implements View.OnClickListener {

    ActivityTrainersBinding binding;
    private TrainerListAdapter trainerListAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_trainers;
    }

    @Override
    public void init() {
        binding = (ActivityTrainersBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.llProfile.setOnClickListener(this);
        binding.llNutrition.setOnClickListener(this);
        binding.llGoals.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);

        if (trainerListAdapter == null)
        {
            trainerListAdapter = new TrainerListAdapter(context, binding.rvExercises, new TrainerListAdapter.ClickListener() {
                @Override
                public void onClick(TrainerBean trainerBean) {
                    Intent intent = new Intent(context, TrainerProfileActivity.class);
                    intent.putExtra("TrainerBean", trainerBean);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }
        binding.rvExercises.setAdapter(trainerListAdapter);
        trainerListAdapter.clearAll();

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                trainerListAdapter.filter(binding.edtSearch.getText().toString());
                binding.txtTotalTrainers.setText(trainerListAdapter.getItemCount()+" Trainers");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        callTrainerListApi();
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
        }
    }

    private void callTrainerListApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);

            Observable<ModelBean<ArrayList<TrainerBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getTrainerListApi();
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<TrainerBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callTrainerListApi"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<TrainerBean>> exercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (exercisesBean.getResult() != null && exercisesBean.getResult().size() > 0) {
                                binding.txtTotalTrainers.setText(exercisesBean.getResult().size()+" Trainers");
                                trainerListAdapter.addAllList(exercisesBean.getResult());
                            }
                            else
                                CommonUtils.toast(context, exercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(0, 0);
    }
}
