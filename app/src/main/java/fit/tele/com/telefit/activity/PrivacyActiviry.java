package fit.tele.com.telefit.activity;

import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPrivacyBinding;

public class PrivacyActiviry extends BaseActivity {

    ActivityPrivacyBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_privacy;
    }

    @Override
    public void init() {
        binding = (ActivityPrivacyBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
