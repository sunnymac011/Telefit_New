package fit.tele.com.telefit.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySignUpBinding;
import fit.tele.com.telefit.utils.CommonUtils;


public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;

    private void setListner() {

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.inputConfPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.btnCreateAccount.performClick();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    return true;
                }
                return false;
            }
        });

        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (validation()) {
                        Intent intent = new Intent(SignUpActivity.this, SignUpTwoActivity.class);
                        intent.putExtra("fname",binding.inputFname.getText().toString().trim());
                        intent.putExtra("lname",binding.inputLname.getText().toString().trim());
                        intent.putExtra("email",binding.inputEmail.getText().toString().trim());
                        intent.putExtra("password",binding.inputPassword.getText().toString().trim());
                        startActivity(intent);
                    }
            }

            private boolean validation() {
                if (binding.inputFname.getText().toString().isEmpty()) {
                    binding.inputFname.setError("Please enter First name");
                    return false;
                } else if (binding.inputLname.getText().toString().isEmpty()) {
                    binding.inputLname.setError("Please enter Last name");
                    return false;
                } else if (binding.inputEmail.getText().toString().isEmpty()) {
                    binding.inputEmail.setError("Please enter Email");
                    return false;
                } else if (!CommonUtils.isValidEmail(binding.inputEmail.getText().toString().trim())) {
                    binding.inputEmail.setError("Please enter valid email");
                    return false;
                } else if (binding.inputPassword.getText().toString().isEmpty()) {
                    binding.inputPassword.setError("Please enter Password");
                    return false;
                } else if (binding.inputPassword.length() < 6) {
                    binding.inputEmail.setError("Passwords must be at least 6 characters in length!");
                    return false;
                } else if (binding.inputConfPassword.getText().toString().isEmpty()) {
                    binding.inputConfPassword.setError("Please enter Confirm password");
                    return false;
                } else if (!binding.inputConfPassword.getText().toString().trim().equalsIgnoreCase(binding.inputPassword.getText().toString().trim())) {
                    binding.inputConfPassword.setError("Both Password not match");
                    return false;
                } else
                    return true;
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void init() {

        binding = (ActivitySignUpBinding) getBindingObj();
        setListner();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

