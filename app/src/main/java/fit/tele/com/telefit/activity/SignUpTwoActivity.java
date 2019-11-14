package fit.tele.com.telefit.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySignUpTwoBinding;
import fit.tele.com.telefit.dialog.SetHeightDialog;
import fit.tele.com.telefit.dialog.SetNumbersDialog;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpTwoActivity extends BaseActivity {

    ActivitySignUpTwoBinding binding;
    String strfName="",strlName="",strEmail="",strPassword="";
    private SetNumbersDialog setNumbersDialog;
    private SetHeightDialog setHeightDialog;

    private void setListner() {

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        list.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list);
        binding.spiGender.setAdapter(adapter);

        List<String> list1 = new ArrayList<>();
        list1.add("Sedentary");
        list1.add("Lightly active");
        list1.add("Moderately active");
        list1.add("Very active");
        list1.add("Extra active");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list1);
        binding.spiActivity.setAdapter(adapter1);

        List<String> list2 = new ArrayList<>();
        list2.add("Lose 1 pound per week");
        list2.add("Lose 1.5 pound per week");
        list2.add("Lose 2 pounds per week");
        list2.add("Gain 0.5 pound per week");
        list2.add("Gain 1 pound per week");
        list2.add("Gain 1.5 pounds per week");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.item_custom_spinner, list2);
        binding.spiMaintain.setAdapter(adapter2);

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    callSignUpApi();
                }
            }

            private boolean validation() {
                if (binding.edtMm.getText().toString().isEmpty() || binding.edtDd.getText().toString().isEmpty() || binding.edtYyyy.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter Valid Birth Date!");
                    return false;
                }  else if (binding.spiGender.getSelectedItem().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please select Gender!");
                    return false;
                } else if (binding.txtHeight.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter Height!");
                    return false;
                } else if (binding.txtWeight.getText().toString().isEmpty()) {
                    CommonUtils.toast(context,"Please enter Weight!");
                    return false;
                } else
                    return true;
            }
        });

        binding.txtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeightDialog = new SetHeightDialog(context, "Set Height", new SetHeightDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        if (strWeightType.equalsIgnoreCase("cm"))
                        {
                            binding.txtHeight.setText(strNumbers);
                            binding.txtWeightType.setText("kg");
                        }
                        else
                        {
                            binding.txtHeight.setText(strNumbers+"'"+strNumbersTwo);
                            binding.txtWeightType.setText("lbs");
                        }
                        binding.txtHeightType.setText(strWeightType);
                    }
                });
                setHeightDialog.show();
            }
        });

        binding.txtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNumbersDialog = new SetNumbersDialog(context, "Set Weight", false,false, new SetNumbersDialog.SetDataListener() {
                    @Override
                    public void onContinueClick(String strNumbers, String strNumbersTwo, String strWeightType) {
                        binding.txtWeight.setText(strNumbers);
                    }
                });
                setNumbersDialog.show();
            }
        });

        binding.edtMm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.edtMm.getText().toString().length() > 1)
                    binding.edtDd.requestFocus();
            }
        });

        binding.edtDd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.edtDd.getText().toString().length() > 1)
                    binding.edtYyyy.requestFocus();
                else if (binding.edtDd.getText().toString().length() == 0)
                    binding.edtMm.requestFocus();
            }
        });

        binding.edtYyyy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.edtYyyy.getText().toString().length() == 0)
                    binding.edtDd.requestFocus();
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sign_up_two;
    }

    @Override
    public void init() {
        binding = (ActivitySignUpTwoBinding) getBindingObj();
        setListner();
        Intent intent = getIntent();
        strfName = intent.getStringExtra("fname");
        strlName = intent.getStringExtra("lname");
        strEmail = intent.getStringExtra("email");
        strPassword = intent.getStringExtra("password");
        if (!strfName.toString().isEmpty())
            binding.txtTitle.setText("Hey, "+strfName);
    }

    private void callSignUpApi() {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("name", strfName);
            map.put("l_name", strlName);
            map.put("email", strEmail);
            map.put("password", strPassword.trim());
            map.put("activity", binding.spiActivity.getSelectedItem().toString());
            map.put("maintain_weight", binding.spiMaintain.getSelectedItem().toString());
            map.put("gender", binding.spiGender.getSelectedItem().toString().trim());
            map.put("height", binding.txtHeight.getText().toString().trim());
            map.put("height_type", binding.txtHeightType.getText().toString());
            map.put("weight", binding.txtWeight.getText().toString().trim());
            map.put("weight_type", binding.txtWeightType.getText().toString().trim());
            map.put("bdate", binding.edtMm.getText().toString().trim()+"/"+binding.edtDd.getText().toString().trim()+"/"+binding.edtYyyy.getText().toString().trim());
            map.put("device_type", "android");
            map.put("login_by", "manual");

            if (preferences.getPushToken() != null) {
                map.put("device_token", preferences.getPushToken());
            } else {
                map.put("device_token", "123456789");
            }

            Observable<ModelBean<LoginBean>> signupusers = FetchServiceBase.getFetcherService(context).signUpApi(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<LoginBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<LoginBean> loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus() == 1) {
                                CommonUtils.toast(context,"Registered Successfully, Please verify your Email!");
                                startActivity(new Intent(context, LoginActivity.class));
                            } else
                                CommonUtils.toast(context,loginBean.getMessage());

                        }
                    });
        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }
}
