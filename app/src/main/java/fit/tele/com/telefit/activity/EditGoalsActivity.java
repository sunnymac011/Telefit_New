package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityEditGoalBinding;
import fit.tele.com.telefit.databinding.ActivityGoalsBinding;
import fit.tele.com.telefit.dialog.AddGoalsDialog;
import fit.tele.com.telefit.dialog.AddGoalsWeightDialog;
import fit.tele.com.telefit.dialog.SetNumbersDialog;
import fit.tele.com.telefit.modelBean.GoalBarBean;
import fit.tele.com.telefit.modelBean.GoalBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditGoalsActivity extends BaseActivity implements View.OnClickListener {

    ActivityEditGoalBinding binding;
    private SetNumbersDialog setNumbersDialog;
    private AddGoalsDialog addGoalsDialog;
    private AddGoalsWeightDialog addGoalsWeightDialog;
    private Map<String, String> map = new HashMap<>();
    private Calendar calendar = Calendar.getInstance();
    private DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    private String strDate = "";
    GoalBean goalBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_goal;
    }

    @Override
    public void init() {
        binding = (ActivityEditGoalBinding) getBindingObj();
        setListner();
    }

    private void setListner() {
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

        binding.txtProteinCount.setOnClickListener(this);
        binding.txtCarbsCount.setOnClickListener(this);
        binding.txtFatCount.setOnClickListener(this);
        binding.txtBfatCount.setOnClickListener(this);
        binding.txtCholesterolCount.setOnClickListener(this);
        binding.txtFiberCount.setOnClickListener(this);
        binding.txtWaterCount.setOnClickListener(this);
        binding.txtWeightCount.setOnClickListener(this);

        if (preferences.getGoalDatePref()!= null && !TextUtils.isEmpty(preferences.getGoalDatePref()))
            strDate = preferences.getGoalDatePref();
        else
            strDate = format1.format(calendar.getTime());

        callGetGoalsApi(strDate);
        binding.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation())
                    callSetGoalsApi(strDate);
            }

            private boolean validation() {
                if (binding.txtProteinCount.getText().toString().isEmpty() && binding.txtProteinCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Protein!");
                    return false;
                }
                else if (binding.txtCarbsCount.getText().toString().isEmpty() && binding.txtCarbsCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Carbs!");
                    return false;
                }
                else if (binding.txtFatCount.getText().toString().isEmpty() && binding.txtFatCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Fat!");
                    return false;
                }
                else if (binding.txtBfatCount.getText().toString().isEmpty() && binding.txtBfatCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Body Fat%!");
                    return false;
                }
                else if (binding.txtCholesterolCount.getText().toString().isEmpty() && binding.txtCholesterolCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Cholesterol!");
                    return false;
                }
                else if (binding.txtFiberCount.getText().toString().isEmpty() && binding.txtFiberCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Fiber!");
                    return false;
                }
                else if (binding.txtWaterCount.getText().toString().isEmpty() && binding.txtWaterCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Water!");
                    return false;
                }
                else if (binding.txtWeightCount.getText().toString().isEmpty() && binding.txtWeightCount.getText().toString().equalsIgnoreCase("Set Goal")) {
                    CommonUtils.toast(context,"Please set Weight!");
                    return false;
                }
                else
                    return true;
            }
        });
    }

    private void setData() {
        binding.txtProteinCount.setText(goalBean.getProtein()+"g");
        binding.txtCarbsCount.setText(goalBean.getCarbs()+"g");
        binding.txtFatCount.setText(goalBean.getFat()+"g");
        binding.txtBfatCount.setText(goalBean.getGoalBodyFat()+"%, "+goalBean.getBodyFat()+"%");
        binding.txtCholesterolCount.setText(goalBean.getCholesterol()+"mg");
        binding.txtFiberCount.setText(goalBean.getFiber()+"g");
        binding.txtWaterCount.setText(goalBean.getGoalWater()+"floz, "+goalBean.getWater()+"floz");
        binding.txtWeightCount.setText(goalBean.getGoalWeight()+goalBean.getWeightType()+", "+goalBean.getWeight()+goalBean.getWeightType());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String strBody = "",strCurrentWeight = "";
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

            case R.id.txt_protein_count:
                setNumbersDialog = new SetNumbersDialog(context, "Set Value", false, false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtProteinCount.setText(strNumbers+"g");
                        map.put("protein", strNumbers);
                    }
                });
                setNumbersDialog.show();
                break;

            case R.id.txt_carbs_count:
                setNumbersDialog = new SetNumbersDialog(context, "Set Value", false, false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtCarbsCount.setText(strNumbers+"g");
                        map.put("carbs", strNumbers);
                    }
                });
                setNumbersDialog.show();
                break;

            case R.id.txt_fat_count:
                setNumbersDialog = new SetNumbersDialog(context, "Set Value", false, false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtFatCount.setText(strNumbers+"g");
                        map.put("fat", strNumbers);
                    }
                });
                setNumbersDialog.show();
                break;

            case R.id.txt_bfat_count:
                if (goalBean != null && goalBean.getGoalBodyFat() != null)
                    strBody = goalBean.getGoalBodyFat();
                else
                    strBody = "";
                addGoalsDialog = new AddGoalsDialog(context, "Current Body Fat%",strBody, new AddGoalsDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String goal, String consumed) {
                        binding.txtBfatCount.setText(goal+"%, "+consumed+"%");
                        map.put("body_fat", consumed);
                        map.put("goal_body_fat", goal);
                    }
                });
                addGoalsDialog.show();
                break;

            case R.id.txt_cholesterol_count:
                setNumbersDialog = new SetNumbersDialog(context, "Set Value", false, false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtCholesterolCount.setText(strNumbers+"mg");
                        map.put("cholesterol", strNumbers);
                    }
                });
                setNumbersDialog.show();
                break;

            case R.id.txt_fiber_count:
                setNumbersDialog = new SetNumbersDialog(context, "Set Value", false, false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtFiberCount.setText(strNumbers+"g");
                        map.put("fiber", strNumbers);
                    }
                });
                setNumbersDialog.show();
                break;

            case R.id.txt_water_count:
                if (goalBean != null && goalBean.getGoalBodyFat() != null)
                    strBody = goalBean.getGoalWater();
                else
                    strBody = "";
                addGoalsDialog = new AddGoalsDialog(context, "Consumed", strBody, new AddGoalsDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String goal, String consumed) {
                        binding.txtWaterCount.setText(goal+"fl, "+consumed+"oz");
                        map.put("water", consumed);
                        map.put("goal_water", goal);
                    }
                });
                addGoalsDialog.show();
                break;

            case R.id.txt_weight_count:
                String strType = preferences.getUserDataPref().getWeightType();
                if (goalBean != null && goalBean.getGoalBodyFat() != null)
                    strBody = goalBean.getGoalWeight();
                else
                    strBody = "";
                if (goalBean != null && goalBean.getWeight() != null)
                    strCurrentWeight = goalBean.getWeight();
                else
                    strCurrentWeight = "";
                addGoalsWeightDialog = new AddGoalsWeightDialog(context, "Set Value in "+strType, strBody, strCurrentWeight, new AddGoalsWeightDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String goal, String consumed) {
                        binding.txtWeightCount.setText(goal+strType+", "+consumed+strType);
                        map.put("weight", consumed);
                        map.put("goal_weight", goal);
                        map.put("weight_type", strType);
                    }
                });
                addGoalsWeightDialog.show();
                break;
        }
    }

    private void callSetGoalsApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("goal_date", strDate);

            Observable<ModelBean<GoalBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).setGoals(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<GoalBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callSetGoalsApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<GoalBean> goalBarBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (goalBarBean.getStatus() == 1) {
                                if (strDate.equalsIgnoreCase(format1.format(calendar.getTime()))) {
                                    LoginBean loginBean = preferences.getUserDataPref();
                                    loginBean.setWeight(goalBarBean.getResult().getWeight());
                                    preferences.saveUserData(loginBean);
                                }

                                Intent intent = new Intent(context, GoalsActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else
                                CommonUtils.toast(context,goalBarBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private void callGetGoalsApi(String strDate) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            map.put("goal_day_date", strDate);

            Observable<ModelBean<GoalBean>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getGoals(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<GoalBean>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override

                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callGetGoalsApi "," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<GoalBean> goal_bean) {
                            binding.progress.setVisibility(View.GONE);
                            if (goal_bean.getStatus() == 1) {
                                goalBean = goal_bean.getResult();
                                setData();
                            } else
                                CommonUtils.toast(context,goal_bean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
