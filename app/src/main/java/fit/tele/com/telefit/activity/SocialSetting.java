package fit.tele.com.telefit.activity;

import android.content.Intent;
import android.view.View;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivitySocialSettingBinding;

public class SocialSetting extends BaseActivity {

    ActivitySocialSettingBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_social_setting;
    }

    @Override
    public void init() {
        binding = (ActivitySocialSettingBinding)getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
