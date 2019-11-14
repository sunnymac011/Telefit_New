package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.ExercisesAdapter;
import fit.tele.com.telefit.adapter.SetsRepsAdapter;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySetsRepsBinding;
import fit.tele.com.telefit.dialog.SetNumbersDialog;
import fit.tele.com.telefit.dialog.SetTimeDialog;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.SetsRepsBean;
import fit.tele.com.telefit.utils.CommonUtils;
import fit.tele.com.telefit.utils.OnLoadMoreListener;

public class SetsRepsDetailsActivity extends BaseActivity {

    ActivitySetsRepsBinding binding;
    private SetsRepsBean setsRepsBean;
    private SetsRepsAdapter setsRepsAdapter;
    private ArrayList<SetsRepsBean> setsRepsBeans = new ArrayList<>();
    private SetNumbersDialog setNumbersDialog;
    private SetTimeDialog setTimeDialog;
    private ExercisesListBean exercisesListBean;
    private ArrayList<ExercisesListBean> exercisesListBeans;
    private RoutinePlanDetailsBean routinePlanDetailsBean;
    private String strFrom="";
    private ExxDetial exxDetial = new ExxDetial();
    private boolean isEmpty = true;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sets_reps;
    }

    @Override
    public void init() {
        binding = (ActivitySetsRepsBinding) getBindingObj();
        setsRepsBean = new SetsRepsBean();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent() != null && getIntent().hasExtra("From"))
            strFrom = getIntent().getStringExtra("From");
        if(getIntent() != null && getIntent().hasExtra("ExercisesListBean"))
            exercisesListBean = getIntent().getParcelableExtra("ExercisesListBean");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvDetails.setLayoutManager(linearLayoutManager);

        if (setsRepsAdapter == null) {
            setsRepsAdapter = new SetsRepsAdapter(context, new SetsRepsAdapter.ClickListener() {
                @Override
                public void onRepsClick(int pos) {
                    setNumbersDialog = new SetNumbersDialog(context, "Set Reps", false, false, new SetNumbersDialog.SetDataListener() {
                        @Override
                        public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                            setsRepsBeans.get(pos).setReps(strNumbers);
                            setsRepsAdapter.clearAll();
                            setsRepsAdapter.addAllList(setsRepsBeans);
                        }
                    });
                    setNumbersDialog.show();
                }

                @Override
                public void onWeightClick(int pos) {
                    setNumbersDialog = new SetNumbersDialog(context, "Set Weight", false,true, new SetNumbersDialog.SetDataListener() {
                        @Override
                        public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                            if (strNumbers.equalsIgnoreCase("00") && strNumbersTwo.equalsIgnoreCase("00")
                                    && strWeightType.equalsIgnoreCase("00"))
                                CommonUtils.toast(context,"Please add time!");
                            else {
                                setsRepsBeans.get(pos).setWeight(strNumbers);
                                setsRepsBeans.get(pos).setWeightType(strWeightType);
                                setsRepsAdapter.clearAll();
                                setsRepsAdapter.addAllList(setsRepsBeans);
                            }
                        }
                    });
                    setNumbersDialog.show();
                }

                @Override
                public void onTimeClick(int pos) {
//                    int intHour=0,intMin=0,intSec=0;
//                    if (exercisesListBean.getExeHours() != null)
//                        intHour = Integer.parseInt(exercisesListBean.getExeHours());
//                    if (exercisesListBean.getExeMin() != null)
//                        intMin = Integer.parseInt(exercisesListBean.getExeMin());
//                    if (exercisesListBean.getExeSec() != null)
//                        intSec = Integer.parseInt(exercisesListBean.getExeSec());
                    setTimeDialog = new SetTimeDialog(context, 0, 0, 0, new SetTimeDialog.SetDataListener() {
                        @Override
                        public void onContinueClick(String strHour, String strMin, String strSec) {
                            setsRepsBeans.get(pos).setSrHours(strHour);
                            setsRepsBeans.get(pos).setSrMin(strMin);
                            setsRepsBeans.get(pos).setSrSec(strSec);
                            setsRepsAdapter.clearAll();
                            setsRepsAdapter.addAllList(setsRepsBeans);
                        }
                    });
                    setTimeDialog.show();
                }
            });
        }
        binding.rvDetails.setAdapter(setsRepsAdapter);
        setsRepsAdapter.clearAll();

        setsRepsBean.setSets("1");
        setsRepsBean.setReps("1");
        setsRepsBean.setWeight("1");
        setsRepsBean.setWeightType("lbs");
        setsRepsBean.setSrHours("00");
        setsRepsBean.setSrMin("00");
        setsRepsBean.setSrSec("00");

        if (exercisesListBean != null && exercisesListBean.getSetsRepsBeans() != null)
        {
            setsRepsBeans = exercisesListBean.getSetsRepsBeans();
            setsRepsAdapter.addAllList(setsRepsBeans);
        }
        else {
            setsRepsBeans.add(setsRepsBean);
            setsRepsAdapter.addAllList(setsRepsBeans);
        }

        binding.txtAddSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setsRepsBeans != null && setsRepsBeans.size() > 0) {
                    setsRepsBean = new SetsRepsBean();
                    setsRepsBean.setSets(""+(setsRepsBeans.size()+1));
                    setsRepsBean.setReps("1");
                    setsRepsBean.setWeight("1");
                    setsRepsBean.setWeightType("lbs");
                    setsRepsBean.setSrHours("00");
                    setsRepsBean.setSrMin("00");
                    setsRepsBean.setSrSec("00");
                    setsRepsBeans.add(setsRepsBean);
                    setsRepsAdapter.clearAll();
                    setsRepsAdapter.addAllList(setsRepsBeans);
                }
            }
        });

        binding.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i=0;i<setsRepsBeans.size();i++) {
//                    if (setsRepsBeans.get(i).getSrHours().equalsIgnoreCase("00") &&
//                            setsRepsBeans.get(i).getSrMin().equalsIgnoreCase("00") &&
//                            setsRepsBeans.get(i).getSrSec().equalsIgnoreCase("00")) {
//                        CommonUtils.toast(context,"Please add Rest Time in set "+setsRepsBeans.get(i).getSets()+"!");
//                        isEmpty = false;
//                        return;
//                    }
//                    else
//                        isEmpty = true;
//                }
//
//                if (isEmpty) {
                    exercisesListBean.setSetsRepsBeans(setsRepsBeans);

                    if (preferences.getIsUpdatePlan() != null && !TextUtils.isEmpty(preferences.getIsUpdatePlan()))
                    {
                        if (preferences.getUpdateRoutineDataPref() != null) {
                            Gson gson = new Gson();
                            routinePlanDetailsBean = gson.fromJson(preferences.getUpdateRoutineDataPref(), new TypeToken<RoutinePlanDetailsBean>(){}.getType());
                        }
                        else
                            routinePlanDetailsBean = new RoutinePlanDetailsBean();

                        exercisesListBean.setExeHours("");
                        exercisesListBean.setExeMin("");
                        exercisesListBean.setExeSec("");

                        if (strFrom != null && strFrom.equalsIgnoreCase("ExercisesActivity")) {
                            exxDetial.setExeHours("");
                            exxDetial.setExeMin("");
                            exxDetial.setExeSec("");
                            exxDetial.setSetsReps(setsRepsBeans);
                            exxDetial.setUserId(preferences.getUserDataPref().getId().toString().trim());
                            exxDetial.setId(preferences.getIsUpdatePlan().trim());
                            exxDetial.setExes(exercisesListBean);
                            routinePlanDetailsBean.getExxDetial().add(exxDetial);
                            preferences.saveUpdateRoutineData(routinePlanDetailsBean);
                        }
                        else {
                            for (int i=0;i<routinePlanDetailsBean.getExxDetial().size();i++) {
                                if (exercisesListBean.getId().equalsIgnoreCase(routinePlanDetailsBean.getExxDetial().get(i).getExes().getId())) {
                                    routinePlanDetailsBean.getExxDetial().get(i).setUserId(preferences.getUserDataPref().getId().toString().trim());
                                    routinePlanDetailsBean.getExxDetial().get(i).setId(preferences.getIsUpdatePlan().trim());
                                    routinePlanDetailsBean.getExxDetial().get(i).setExeHours("");
                                    routinePlanDetailsBean.getExxDetial().get(i).setExeMin("");
                                    routinePlanDetailsBean.getExxDetial().get(i).setExeSec("");
                                    routinePlanDetailsBean.getExxDetial().get(i).setSetsReps(setsRepsBeans);
                                    routinePlanDetailsBean.getExxDetial().get(i).setExes(exercisesListBean);
                                    preferences.saveUpdateRoutineData(routinePlanDetailsBean);
                                }
                            }
                        }

                        Intent intent = new Intent(context, UpdateRoutineActivity.class);
                        startActivity(intent);
                    }
                    else {
                        if (preferences.getRoutineDataPref() != null) {
                            Gson gson = new Gson();
                            exercisesListBeans = gson.fromJson(preferences.getRoutineDataPref(), new TypeToken<List<ExercisesListBean>>(){}.getType());
                        }
                        else
                            exercisesListBeans = new ArrayList<>();

                        if (strFrom != null && strFrom.equalsIgnoreCase("ExercisesActivity")) {
                            exercisesListBean.setExeHours("");
                            exercisesListBean.setExeMin("");
                            exercisesListBean.setExeSec("");
                            exercisesListBean.setSetsRepsBeans(setsRepsBeans);
                            exercisesListBeans.add(exercisesListBean);
                            preferences.saveRoutineData(exercisesListBeans);
                        }
                        else {
                            for (int i=0;i<exercisesListBeans.size();i++) {
                                if (exercisesListBean.getId().equalsIgnoreCase(exercisesListBeans.get(i).getId())) {
                                    exercisesListBeans.get(i).setExeHours("");
                                    exercisesListBeans.get(i).setExeMin("");
                                    exercisesListBeans.get(i).setExeSec("");
                                    exercisesListBeans.get(i).setSetsRepsBeans(setsRepsBeans);
                                    preferences.saveRoutineData(exercisesListBeans);
                                }
                            }
                        }

                        Intent intent = new Intent(context, CreateRoutineActivity.class);
                        startActivity(intent);
                    }
                }
//            }
        });
    }
}
